# Local art tooling
Schema 2 workflow: `acquire_sources.py` obtains only frozen art-inspection archives and verifies provider digests; `collect_inventory.py` rechecks them, scans nested resources, overlays and renderer JSON, and projects pinned first-party source assets without building a JAR. `catalog_registry.py` stores compact deterministic gzip JSON facts and preserves decision rows. These are registry data, not archived upstream artwork. `validate.py` checks every shard hash/count before validating output assets. See registry/INVENTORY-STATUS.md for the exact baseline and limits.

`prepare_pilot.py` and `review_pilot.py` require Pillow and NumPy for local original-art processing; generation inputs remain in scratch. Their outputs are the selected PNGs and small original-art review board, not a distributable ZIP. Inventory and static CI otherwise use the Python standard library.

Python 3.10+ standard library only. Read ../PRODUCTION-PIPELINE.md first.
Example from repo root: python art/tools/inventory.py --source vanilla=/local/1.21.1.jar --source botania=/local/botania.jar --out /tmp/art-inventory.json
Verify archives against integration locks first. Output inventories top-level asset PNG paths; exact duplicate paths and nested/overlay/non-PNG assets flagged. Merge into reviewed registry without overwriting prior decisions. Never store upstream files in Git.
python art/tools/validate.py --inventory art/registry/inventory.json --pack art/too-much-magic-hd
Empty registry should warn and report unknown coverage. Never call this complete. Structural validation only; no in-game or visual pass. 
