# Machine-readable mod register

`mods.json` records every known approved or prohibited decision. `enabled_in_pack: false` is intentional: bootstrap installs no third-party mods yet. Version strings in this register are *release research*, not a download hash or an integration certification.

Do not treat links in this list as authoritative pinned artifact URLs. Add dependencies and actual .pw.toml entries with packwiz before changing enabled state. `scripts/validate.py` enforces basic invariants.
