# Third-party notices — Trader's Vengence

The combined module is licensed under GPL-3.0-only. First-party rights are granted only to the extent the Too Much Magic contributors hold them. Other modules and independently distributed dependencies retain their own licenses. Earlier MIT grants are not withdrawn.

## Original first-party MIT notice (preserved)
MIT License

Copyright (c) 2026 Too Much Magic contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


## Planned donors (not yet imported)
- PneumaticCraft: Repressurized, MineMaarten, desht, Team Pneumatic and contributors; GPLv3 mod, separate LGPLv3 API, independently licensed sounds. Exact candidate: https://github.com/TeamPneumatic/pnc-repressurized/tree/7bcbcbc744a851d158d924cfa3ce6e88fb8ccb28. Before import record per-file upstream paths, notices and modifications; inspect sound provenance separately.
- Caliber Arms Team, https://www.curseforge.com/minecraft/mc-mods/caliber-arms. Project page identifies models, textures and code as MIT. Verify exact artifact and preserve full MIT notice before import. Do not infer sound licensing.
- Iron's Spells 'n Spellbooks remains separately distributed under its own terms; existing integration remains until replaced.

Release gate: provide corresponding source, license text and notices for incorporated files, with build instructions and modified files identified. Do not distribute donor binaries or unrelated assets.

## Staged PneumaticCraft minigun source and art
Unmodified source references (outside Gradle source sets), adapted GolemMinigunModel and identical upstream drone minigun texture recorded in `donor-reference/pneumaticcraft/provenance.json`. Upstream GPLv3, GPL-3.0-or-later in relevant Java headers; original headers preserved. Model attribution: original Techne header and Team Pneumatic contributor credits. No sound imported: specific mapping of freesound licenses remains unverified. The model is not registered/rendered yet.

MinigunSpin adapts the base spin-up/spin-down limits and angle update from the pinned PneumaticCraft Minigun.java; no PNC air, upgrades, player, projectile or ammo mechanisms copied into live runtime. Original source is retained in donor-reference.

Base weapon balance and standard damage type semantics adapted from pinned PneumaticCraft CommonConfig.java, Minigun.java, AbstractGunAmmoItem.java, and generated data/pneumaticcraft/damage_type/minigun.json; local damage ID and NPC attribution differ. Standard damage does not bypass armor or the normal damage cooldown.

## Verified Caliber Arms M1911A1 import
Original code/art by Copyright (c) 2026 Caliber Arms Team, MIT. Exact supplied Forge 1.21.1 / 1.0.0 JAR SHA-256 88068f10a62af5af35b2e8256137cb5160ea5eb2cc8f42644a0fa42722931f90. Original MIT notice and full donor audio credits are bundled in META-INF/traders_vengence/caliberarms/LICENSE.txt and CREDITS.md; exact per-file SHA-256 provenance manifest alongside. Model texture references and sounds registry were adapted to the local namespace; PNG and ten OGG files are original bytes. Gunshots are credited to The Free Firearm Sound Library (CC0); pistol handling sounds credited in the original CREDITS.md (CC0). No compiled donor classes or complete donor JAR redistributed. Values and damage semantics inspected from exact class bytecode, with independent NPC adapter. No external Caliber Arms dependency.
