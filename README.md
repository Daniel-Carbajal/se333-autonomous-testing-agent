# SE333 Final Project — Autonomous Testing Agent with MCP

## Overview

This project implements an **autonomous, coverage‑driven testing agent** using the **Model Context Protocol (MCP)**. The agent iteratively generates and improves unit tests for a Java application, measures code coverage with JaCoCo, fixes discovered defects, and commits validated changes to GitHub using a trunk‑based workflow.

The system demonstrates an end‑to‑end automated testing loop:

```
Generate tests → Run tests → Measure coverage → Identify gaps → Improve tests → Fix bugs → Commit → Repeat
```

---

## Architecture

### 1. MCP Server (Python, FastMCP)

Provides tools callable by the agent via SSE transport:

* **jacoco_parser(xml_path)** — Parses JaCoCo XML and returns per‑package/class/method coverage and targets needing tests
* **scan_maven_vulnerabilities(pom_path)** — Queries OSV for Maven dependency vulnerabilities (security pre‑check)
* **add(a, b)** — Example Phase‑1 tool (connectivity validation)

Transport: `SSE` (VS Code MCP compatible)

---

### 2. Agent Workspace (Java + Prompt Config)

Contains:

* Spark Java mini web application under test
* JUnit test suite generated/improved by agent
* JaCoCo build integration
* MCP prompt configuration

Key file:

```
.github/prompts/tester.prompt.md
```
This prompt defines the autonomous behavior of the testing agent including tool:
* Model + MCP tools and usage
* Iterative testing workflow
* Git automation rules
* Coverage‑driven decision logic

---

### 3. Iterative Testing Pipeline

Each agent iteration performs:

0. Security pre-check on dependencies
1. Run Maven tests
2. Locate JaCoCo XML report
3. Parse coverage via MCP tool
4. Select low‑coverage methods/classes
5. Generate or improve tests
6. Re‑run tests
7. Fix failing code if needed
8. Commit changes on feature branch
9. Open PR → merge to main

---

## Repository Structure

```
MCP Server/                # MCP server workspace
  main.py -> mcp server
  pyproject.toml
  uv.lock

Agent Workspace (se333-Demo)/              # Agent + Java app workspace
  src/main/java/
  src/test/java/
  pom.xml
  .github/prompts/tester.prompt.md
```

---

## Setup Instructions

### Prerequisites

* Python 3.11+
* Node.js (for VS Code MCP extension runtime)
* Java 17+
* Maven 3.9+
* VS Code with MCP extension
* Git + GitHub account

---

## MCP Server Setup

```bash
cd MCP Server
uv sync
uv run main.py
```

Server starts on:

```
http://127.0.0.1:8000/sse
```

Configure VS Code MCP:

```
{
  "servers": {
    "se333-mcp-server": {
      "type": "sse",
      "url": "http://127.0.0.1:8000/sse"
    }
  }
}
```

Enable **YOLO / Auto‑Approve** mode for tool execution.

---

## Java Project Setup

```bash
cd Agent Workspace (se333-Demo)
mvn clean test
```

JaCoCo reports generated at:

```
target/site/jacoco/jacoco.xml
```

---

## Running the Autonomous Agent

Open the project in VS Code and run the MCP prompt:

```
.github/prompts/tester.prompt.md
```

The agent will:

* Run tests
* Parse coverage
* Generate new tests
* Improve coverage
* Fix bugs
* Commit changes

---

## Git Workflow

The agent uses trunk‑based development:

* Create branch per iteration
* Commit test/code improvements
* Open pull request
* Merge after validation

Example history progression:

```
test: add AuthenticationController edge cases
coverage: increase HTMLRenderer branch coverage
fix: resolve null handling in DashboardController
```

---

## Coverage‑Driven Decisions

The agent prioritizes:

* Uncovered methods
* Low branch coverage
* Controllers and services
* Error/edge paths

Selection source: `jacoco_parser` MCP tool output.

---

# Technical Documentation
## MCP Tool API Reference
### jacoco_parser(xml_path)
Parses the JaCoCo XML coverage report and extracts coverage statistics

<strong>Input:</strong>
```
xml_path: string
```
Path to the JaCoCo XML file (typically in target/site/jacoco/jacoco.xml)

<strong>Output:</strong>
Returns structured coverage data including:
```
{
  packages: [...],
  classes: [...],
  methods: [...],
  methods_needing_coverage: [...]
}
```
Used For identifying uncovered methods, targeting test generation, and prioritizing coverage improvements.

### scan_maven_vulnerabilities(pom_path)
Performs dependency vulnerabilities scanning using the OSV database.

<strong>Input:</strong>
```
pom_path: string
```
Path to the Maven **pom.xml** file.

<strong>Output:</strong>
Returns detected vulnerabilities including:
```
{
  dependency: "...",
  version: "...",
  cve: "...",
  severity: "...",
  description: "..."
}
```
Used for security pre-check before test generation, identifying risky dependencies, and demonstrating security-aware autonomous tooling.

### add(a, b)
Example MCP connectivity tool.

<strong>Input:</strong>
```
a: number
b: number
```

<strong>Output:</strong>
```
sum = a + b
```
Used for Phase-1 MCP connectivity test and ensured that the MCP server and agent could communicate before other MCP tools were available.

---

## Agent Decision Logic
The testing agent follows a coverage-guided reasoning process.

Decision priorities include:
1. Methods with 0% coverage
2. Methods with low branch coverage
3. Controller endpoints
4. Service logic
5. Edge cases and null handling
    The agent uses the following feedback signals:
    - Low Coverage            -> Generate new tests
    - Failing Tests           -> Inspect code and apply fix
    - New Branches discovered -> Generate additional tests
    - Security Issue          -> Flag and fix dependency

---

## Reproducing an Iteration
To reproduce the results shown in the project:
1. Start the MCP server
```
cd MCP Server
uv sync
uv run main.py
```
2. Build Java Project
```
cd Agent Workspace (se333-Demo)
mvn clean test
```
3. Run the Agent
Execute the prompt from VS Code chat window:
```
.github/prompts/tester.prompt.md
```
Observe the following process:
1. Test execution
2. Coverage parsing
3. Target selection
4. Test generation
5. Coverage improvement
6. Bug detection and repair
7. Git commits created

---

## Results

The system achieves:

* Automated test generation
* Coverage improvement across iterations
* Autonomous bug detection and repair
* Reproducible MCP‑driven workflow
* Git‑auditable evolution
* Maven dependency vulnerability pre-checks

See coverage improvement <a href="https://github.com/Daniel-Carbajal/se333-autonomous-testing-agent/blob/main/Agent%20Workspace%20(se333%20Demo)/coverage_log.md">here!</a>

---

## Troubleshooting Common Issues
### MCP tools not appearing in VS Code
Check that:
- MCP server is still running
- URL matches config
- SSE endpoint is reachable
'''
http://127.0.0.1:8000/sse
'''
### JaCoCo XML not found
Ensure tests were run successfully:
'''
mvn clean test
'''
Expected file:
'''
target/site/jacoco/jacoco.xml
'''
### MCP server fails to start
Check Python environment:
'''
python --version
'''
Then reinstall dependencies:
'''
uv sync
'''

---

## Reflection

This project demonstrates how MCP enables:

* Tool‑augmented LLM development
* Feedback‑driven testing loops
* Autonomous software and security maintenance
* Integration of testing + DevOps

Key insight: **coverage feedback is sufficient to guide iterative test generation without human prompts.**

In depth reflection in <a href="https://github.com/Daniel-Carbajal/se333-autonomous-testing-agent/blob/main/report/SE333%20Final%20Reflection.pdf">'report/SE333 Final Reflection.pdf'</a>

### Limitations
Currently I think the LLM-generated tests still require human review occasionally. Also not all coverage metrics will always reflect behavioral correctness for tests.

### Future Work
Some future improvements that I think would be practical:
- Static Analysis Security Vulnerability check in codebase using CodeQL (SAST)
- Integration with mutation testing
- CI/CD automation with GitHub Actions

---

## Author

Daniel Carbajal
SE333 — Software Testing & Quality Assurance
DePaul University

---

## License

Academic project — educational use only.
