# Local art tooling
Python 3.10+ standard library only. Read ../PRODUCTION-PIPELINE.md first.
Example from repo root: python art/tools/inventory.py --source vanilla=/local/1.21.1.jar --source botania=/local/botania.jar --out /tmp/art-inventory.json
Verify archives against integration locks first. Output inventories top-level asset PNG paths; exact duplicate paths and nested/overlay/non-PNG assets flagged. Merge into reviewed registry without overwriting prior decisions. Never store upstream files in Git.
python art/tools/validate.py --inventory art/registry/inventory.json --pack art/too-much-magic-hd
Empty registry should warn and report unknown coverage. Never call this complete. Structural validation only; no in-game or visual pass. 
