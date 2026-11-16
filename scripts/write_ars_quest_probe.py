#!/usr/bin/env python3
"""Generate a smoke-only KubeJS live item/advancement registry check."""
from __future__ import annotations

import json
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1]
pilot = json.loads((ROOT / "manifest/ars_quest_pilot.json").read_text())
items = set()
advancements = set()
for quest in pilot["quests"]:
    items.add(quest["icon"])
    if "item" in quest["reward"]:
        items.add(quest["reward"]["item"])
    task = quest["criterion"]
    if task["type"] == "item":
        items.add(task["id"])
    elif task["type"] == "advancement":
        advancements.add(task["id"])
output = Path(sys.argv[1])
output.parent.mkdir(parents=True, exist_ok=True)
output.write_text("""// Disposable server QA only; never shipped in the player's instance.
ServerEvents.loaded(event => {
  const Registries = Java.loadClass('net.minecraft.core.registries.BuiltInRegistries');
  const ResourceLocation = Java.loadClass('net.minecraft.resources.ResourceLocation');
  const items = ITEM_LIST;
  const advances = ADV_LIST;
  let missing = [];
  items.forEach(id => {
    if (!Registries.ITEM.containsKey(ResourceLocation.parse(id))) missing.push('item ' + id);
  });
  advances.forEach(id => {
    if (event.server.getAdvancements().get(ResourceLocation.parse(id)) == null) missing.push('advancement ' + id);
  });
  if (missing.length) {
    console.error('[TMM ARS PILOT] FAIL: ' + missing.join(', '));
  } else {
    console.info('[TMM ARS PILOT] PASS: ' + items.length + ' items and ' + advances.length + ' advancements resolved');
  }
});
""".replace("ITEM_LIST", json.dumps(sorted(items))).replace("ADV_LIST", json.dumps(sorted(advancements))), encoding="utf-8")
print(f"Wrote disposable registry probe for {len(items)} items and {len(advancements)} advancements")
