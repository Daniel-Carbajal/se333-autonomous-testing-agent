from fastmcp import FastMCP
from typing import Union
import xml.etree.ElementTree as ET
import requests

mcp = FastMCP("Demo 🚀")

@mcp.tool
def add(a: int, b: int) -> int:
    """Add two numbers"""
    return a + b

@mcp.tool
def jacoco_parser(xml_path: str) -> dict:
    """Parse JaCoCo XML report and return coverage data"""
    tree = ET.parse(xml_path)
    root = tree.getroot()

    coverage_data = {}
    for package in root.findall(".//package"):
        package_name = package.get("name")
        coverage_data[package_name] = {}

        for class_ in package.findall("class"):
            class_name = class_.get("name")

            line_counter = class_.find("counter[@type='LINE']")
            instruction_counter = class_.find("counter[@type='INSTRUCTION']")
            if line_counter is None and instruction_counter is None:
                continue

            line_cov = int(line_counter.get("covered"))
            line_miss = int(line_counter.get("missed"))
            instr_cov = int(instruction_counter.get("covered"))
            instr_miss = int(instruction_counter.get("missed"))

            line_den = line_cov + line_miss
            instr_den = instr_cov + instr_miss

            coverage_data[package_name][class_name] = {
                "lines_covered": line_cov,
                "lines_missed": line_miss,
                "line_total": line_den,
                "coverage_percentage": (line_cov / line_den * 100) if line_den > 0 else 0.0,
                "instruction_coverage_percentage": (instr_cov / instr_den * 100) if instr_den > 0 else 0.0,
                "methods_needing_coverage" : []
                }

            for method in class_.findall("method"):
                method_name = method.get("name")
                method_desc = method.get("desc")
                method_line_counter = method.find("counter[@type='LINE']")

                if method_line_counter is not None:
                    method_line_cov = int(method_line_counter.get("covered"))
                    method_line_miss = int(method_line_counter.get("missed"))
                    method_line_total = method_line_cov + method_line_miss
                    method_line_percentage = (method_line_cov / method_line_total * 100) if method_line_total > 0 else 0.0

                    if method_line_percentage < 100.0 and method_line_total > 0:
                        coverage_data[package_name][class_name]["methods_needing_coverage"].append({
                            "name": method_name,
                            "desc": method_desc,
                            "line_total": method_line_total,
                            "lines_missed": method_line_miss,
                            "line_coverage_percentage": method_line_percentage
                        })

    return coverage_data

def scan_maven_vulnerabilities(pom_path: Union[str, list]) -> list[dict]:
    """
    Scan pom.xml direct dependencies for known vulnerabilities using OSV.
    Returns a list of findings.
    """
    findings: list[dict] = []

    # Accept either a single path or an array of paths (some callers provide an array)
    if pom_path is None:
        findings.append({"error": "pom_path is None"})
        return findings

    pom_paths = pom_path if isinstance(pom_path, list) else [pom_path]

    dependencies = []
    for p in pom_paths:
        try:
            dependencies.extend(parse_pom_dependancies(p))
        except Exception as e:
            findings.append({"pom_path": p, "error": str(e)})
            continue

    for dep in dependencies:
        try:
            osv_response = query_osv(dep["groupId"], dep["artifactId"], dep["version"])
        except Exception as e:
            findings.append({
                "dependency": dep,
                "error": str(e)
            })
            continue

        vulnerabilities = osv_response.get("vulns", []) or []
        for vuln in vulnerabilities:
            vuln_id = vuln.get("id")
            summary = vuln.get("summary", "")
            details = vuln.get("details", "")
            first_fixed_version = None

            for affected in vuln.get("affected", []):
                for range_ in affected.get("ranges", []):
                    if range_.get("type") == "SEMVER":
                        for event in range_.get("events", []):
                            if "introduced" in event and event["introduced"] == dep["version"]:
                                first_fixed_version = event.get("fixed")
                                break
                if first_fixed_version:
                    break

            findings.append({
                "dependency": dep,
                "vulnerability_id": vuln_id,
                "summary": summary,
                "details": details,
                "fixed_version": first_fixed_version
            })

    return findings

# Register the callable function as an MCP tool under a different name
scan_maven_vulnerabilities_tool = mcp.tool(scan_maven_vulnerabilities)

def query_osv(group_id: str, artifact_id: str, version: str):
    payload = {
        "package": {"ecosystem": "Maven", "name": f"{group_id}:{artifact_id}"},
        "version": version
    }
    response = requests.post("https://api.osv.dev/v1/query", json=payload, timeout=20)
    response.raise_for_status()
    return response.json()

def parse_pom_dependancies(pom_path: str) -> list[dict]:
    tree = ET.parse(pom_path)
    root = tree.getroot()

    #Handle namespaces
    ns = {'m': root.tag.split('}')[0].strip('{') if '}' in root.tag else ''}
    dep_xpath = ".//m:dependencies/m:dependency" if ns['m'] else ".//dependencies/dependency"
    gid_xpath = "m:groupId" if ns['m'] else "groupId"
    aid_xpath = "m:artifactId" if ns['m'] else "artifactId"
    ver_xpath = "m:version" if ns['m'] else "version"

    dependencies = []
    for dep in root.findall(dep_xpath, ns):
        group_id = dep.find(gid_xpath, ns).text if dep.find(gid_xpath, ns) is not None else ""
        artifact_id = dep.find(aid_xpath, ns).text if dep.find(aid_xpath, ns) is not None else ""
        version = dep.find(ver_xpath, ns).text if dep.find(ver_xpath, ns) is not None else ""

        if group_id and artifact_id and version and not version.startswith("${"):
            dependencies.append({
                "groupId": group_id,
                "artifactId": artifact_id,
                "version": version
            })

    return dependencies

if __name__ == "__main__":
    mcp.run(transport="sse")
    