import os
import sys
import re
from datetime import datetime
from openai import OpenAI

# Groq API configuration
client = OpenAI(
    api_key=os.environ.get("GROQ_API_KEY"),
    base_url="https://api.groq.com/openai/v1"
)

def read_file(filepath):
    with open(filepath, 'r') as f:
        return f.read()

def generate_documentation(filename, content):
    prompt = f"""You are a technical documentation writer. Analyze this Java file and generate comprehensive documentation in the Service Spec format.

File: {filename}

Code:
```java
{content}
```

Generate documentation using this EXACT format (fill in all sections based on code analysis):

# Service Spec — {filename}

**Service name:** {filename}  
**Last updated:** [current date]

---

# 1. Ownership

| Role | Name | Responsibility |
|------|------|----------------|
| **Lead** | TBD | Tech direction, ownership, incidents |
| **Service owner** | TBD | Roadmap, SLAs, stakeholders |
| **Team members** | TBD | Development and maintenance |

---

# 2. Logic & Purpose

**What does this service do?** *(One sentence describing the main purpose)*

**Scope**
- **In scope:** *(List what this file/service is responsible for)*

**Features / Capabilities**

| Feature | What it does | Exposed via |
|---------|--------------|-------------|
| *(list features from code)* | *(description)* | API / event / UI |

---

# 3. Architecture — Resources, Framework, Data

**Core Framework**

| What | Version | Purpose |
|------|---------|---------|
| Language | Java | Primary language |
| Framework | Spring Boot | REST API framework |
| *(add if detected)* | | |

**API Endpoints** *(if this is a controller)*

| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| *(list all endpoints)* | | | |

**Dependencies**

| Dependency | Purpose | How it's used |
|------------|---------|---------------|
| *(list autowired dependencies)* | | |

---

# 4. Key Components

**Methods / Functions**

| Method | Purpose | Parameters | Returns |
|--------|---------|------------|---------|
| *(list all public methods)* | | | |

**Configuration** *(if applicable)*

| Property | Default | Description |
|----------|---------|-------------|
| *(list config properties)* | | |

---

# 5. Key Flows

*(Describe the main execution flows in this file)*

- **Flow 1:** *(description of how data flows through the methods)*
- **Flow 2:** *(if applicable)*

---

Output clean markdown only. Fill ALL sections based on actual code analysis. Use "N/A" if a section doesn't apply.
"""
    
    response = client.chat.completions.create(
        model="openai/gpt-oss-120b",
        messages=[{"role": "user", "content": prompt}],
        temperature=0.3
    )
    
    return response.choices[0].message.content

def update_docs_file(filename, new_docs):
    docs_path = "DOCS.md"
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M")
    
    # Read existing docs or create new
    if os.path.exists(docs_path):
        with open(docs_path, 'r') as f:
            content = f.read()
    else:
        content = f"""# Technical Documentation

*Auto-generated Service Specs for this repository.*

**Last updated:** {timestamp}

---

"""
    
    # Section markers
    section_start = f"<!-- START:{filename} -->"
    section_end = f"<!-- END:{filename} -->"
    
    new_section = f"""{section_start}
{new_docs}

*Last updated: {timestamp}*
{section_end}"""
    
    # Replace existing section or add new one
    if section_start in content:
        pattern = f"{re.escape(section_start)}[\\s\\S]*?{re.escape(section_end)}"
        content = re.sub(pattern, new_section, content)
    else:
        content = content.rstrip() + f"\n\n---\n\n{new_section}\n"
    
    # Update header timestamp
    content = re.sub(
        r'\*\*Last updated:\*\* .*',
        f'**Last updated:** {timestamp}',
        content
    )
    
    with open(docs_path, 'w') as f:
        f.write(content)

def main():
    if len(sys.argv) < 2 or not sys.argv[1].strip():
        print("No files to process")
        return
    
    files = sys.argv[1].split()
    
    for filepath in files:
        filepath = filepath.strip()
        if not filepath or not os.path.exists(filepath):
            print(f"Skipping: {filepath}")
            continue
        
        filename = os.path.basename(filepath)
        print(f"Processing: {filename}")
        
        content = read_file(filepath)
        docs = generate_documentation(filename, content)
        update_docs_file(filename, docs)
        
        print(f"✓ Updated documentation for: {filename}")

if __name__ == "__main__":
    main()
