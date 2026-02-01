import os
import sys
import re
from datetime import datetime
from openai import OpenAI

# Langflow API configuration
client = OpenAI(
    api_key=os.environ.get("LANGFLOW_API_KEY"),
    base_url="https://dev-langflowfe.salescode.ai/api/v1"
)

def read_file(filepath):
    with open(filepath, 'r') as f:
        return f.read()

def generate_documentation(filename, content):
    prompt = f"""You are a technical documentation writer. Generate markdown documentation for this Java file.

File: {filename}

Code:
```java
{content}
```

Generate documentation with these sections:

## {filename}

### Overview
Brief description of what this file does.

### Key Components
List main methods/functions with:
- **Method name**
- Purpose
- Parameters  
- Returns

### API Endpoints (if applicable)
List any REST endpoints with method, path, description.

### Usage Examples
Code examples showing how to use this.

Output in clean markdown format only. No extra explanation.
"""
    
    response = client.chat.completions.create(
        model="gpt-4o-mini",
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

*Auto-generated documentation for this repository.*

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
