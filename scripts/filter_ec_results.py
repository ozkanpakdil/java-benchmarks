import os
import re
from collections import defaultdict

def version_key(v_str):
    # Handle 1.8.0_362 -> 1.8.0.362
    v_str = v_str.replace('_', '.')
    return [int(x) for x in re.split(r'\.', v_str) if x.isdigit()]

def parse_filename(filename):
    # jmh-result-1.8.0_362.json
    m1 = re.match(r"jmh-result-([\d\._]+)\.json", filename)
    if m1:
        ver_str = m1.group(1)
        # Group by major.minor or major
        parts = ver_str.replace('_', '.').split('.')
        if parts[0] == '1' and len(parts) > 1:
            group_id = f"jmh-jdk-{parts[0]}.{parts[1]}"
        else:
            group_id = f"jmh-jdk-{parts[0]}"
        return ver_str, group_id

    # VERSION-VMNAME.json
    m2 = re.match(r"([\d\.]+)-(.*)\.json", filename)
    if m2:
        ver_str = m2.group(1)
        vm_name = m2.group(2)
        major = ver_str.split('.')[0]
        group_id = f"{vm_name}-jdk-{major}"
        return ver_str, group_id

    return None, None

def main():
    directory = "eclipse-collections/results"
    if not os.path.exists(directory):
        print(f"Directory {directory} not found")
        return

    files = [f for f in os.listdir(directory) if f.endswith(".json")]
    
    groups = defaultdict(list)
    
    for f in files:
        ver_str, group_id = parse_filename(f)
        if ver_str:
            groups[group_id].append((version_key(ver_str), f))
        else:
            print(f"Skipping unparseable or unrecognized format: {f}")

    to_keep = set()
    for group_id, items in groups.items():
        # Sort by version key descending
        items.sort(key=lambda x: x[0], reverse=True)
        latest_file = items[0][1]
        to_keep.add(latest_file)
        print(f"Group {group_id}: Keeping {latest_file}")

    # Files to remove
    to_remove = [f for f in files if f not in to_keep]
    for f in to_remove:
        path = os.path.join(directory, f)
        print(f"Removing {path}")
        os.remove(path)

if __name__ == "__main__":
    main()
