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

Defines:

* Model + MCP tools
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
uv run server.py
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

## Security Extension (Phase 5)

The project includes a custom MCP tool:

```
scan_maven_vulnerabilities(pom_path)
```

Capabilities:

* Extract Maven dependencies
* Query OSV vulnerability database
* Return CVEs and severity
* Allow agent to avoid insecure libraries

Demonstrates measurable value beyond testing.

---

## Demonstration Scenario

1. Initial coverage baseline
2. Agent generates new tests
3. Coverage increases
4. Bug discovered in service/controller
5. Agent fixes code
6. Tests pass
7. Commit + PR recorded

Git history shows iterative improvement.

---

## Results

The system achieves:

* Automated test generation
* Coverage improvement across iterations
* Autonomous bug detection and repair
* Reproducible MCP‑driven workflow
* Git‑auditable evolution

---

## Reflection

This project demonstrates how MCP enables:

* Tool‑augmented LLM development
* Feedback‑driven testing loops
* Autonomous software and security maintenance
* Integration of testing + DevOps

Key insight: **coverage feedback is sufficient to guide iterative test generation without human prompts.**

---

## Author

Daniel Carbajal
SE333 — Software Testing & Quality Assurance
DePaul University

---

## License

Academic project — educational use only.
