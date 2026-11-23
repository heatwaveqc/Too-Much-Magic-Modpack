"""Type-A static regression for the TMM informational starter hub (no game boot)."""
from __future__ import annotations

import hashlib
import json
import re
import tomllib
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
STARTER_ID = "544D502001500000"
QUESTDIR = ROOT / "config/ftbquests/quests"
KUBE = ROOT / "kubejs/data/tmm"


def load(path: str):
    return json.loads((ROOT / path).read_text(encoding="utf-8"))


class TipsStarterA(unittest.TestCase):
    def test_all_tips_are_browsable_and_guide_nodes_are_personal(self):
        authored = [q for q in load("manifest/quest_content.json")["quests"]
                    if q["chapter"] == "Tips & Tricks"]
        resolved = [q for q in load("manifest/quest_content_resolved.json")["quests"]
                    if q["chapter"] == "Tips & Tricks"]
        self.assertEqual(len(authored), 34)
        self.assertEqual(len(resolved), 34)
        self.assertEqual(len({q["title"] for q in authored}), 34)
        self.assertTrue(all(q["criterion"]["type"] == "checkmark" and q["optional"]
                            and not q.get("depends") for q in authored))
        self.assertEqual(len({q["quest_id"] for q in resolved}), 34)
        kit = next(q for q in resolved if q["quest_id"] == STARTER_ID)
        self.assertEqual(kit["title"], "Claim Your Starter Supplies")
        self.assertEqual(kit["reward"]["command"],
                         "execute as {p} run function tmm:rewards/starter_kit")
        self.assertFalse(any(q["reward"].get("command") for q in resolved
                             if q["quest_id"] != STARTER_ID))

    def test_single_claim_marker_and_no_automatic_item_grants(self):
        old = load("kubejs/data/tmm/advancement/starter_kit.json")
        self.assertEqual(old["criteria"]["first_tick"]["trigger"], "minecraft:impossible")
        self.assertNotIn("rewards", old)
        login = load("kubejs/data/tmm/advancement/starter_first_login.json")
        self.assertEqual(login["criteria"]["first_tick"]["trigger"], "minecraft:tick")
        self.assertEqual(login["rewards"]["function"], "tmm:rewards/starter_first_login")
        login_cmd = (KUBE / "function/rewards/starter_first_login.mcfunction").read_text()
        self.assertIn("improvableskills book unlock @s true", login_cmd)
        self.assertNotIn("give @s", login_cmd)
        claimed = load("kubejs/data/tmm/advancement/starter_kit_claimed.json")
        self.assertEqual(claimed["criteria"]["claimed"]["trigger"], "minecraft:impossible")
        cmd = (KUBE / "function/rewards/starter_kit.mcfunction").read_text()
        meaningful = [line.strip() for line in cmd.splitlines()
                      if line.strip() and not line.lstrip().startswith("#")]
        self.assertEqual(len(meaningful), 4)
        for line in meaningful[:3]:
            self.assertIn("unless entity @s[advancements={tmm:starter_kit_claimed=true}]", line)
        for line in meaningful[:2]:
            self.assertIn("unless entity @s[advancements={tmm:starter_kit=true}]", line)
        self.assertIn("run give @s akashictome:tome 1", meaningful[0])
        self.assertIn("run give @s minecraft:cooked_beef 2", meaningful[1])
        self.assertIn("run give @s comforts:sleeping_bag_white 1", meaningful[2])
        self.assertEqual(meaningful[3], "advancement grant @s only tmm:starter_kit_claimed")

    def test_ftb_live_ids_positions_and_reward_contract(self):
        chapter = (QUESTDIR / "chapters/tips_tricks.snbt").read_text()
        ids = re.findall(r'^\t\t\tid: "([0-9A-F]{16})"', chapter, re.M)
        self.assertEqual(len(ids), 34)
        self.assertEqual(len(set(ids)), 34)
        self.assertIn('command: "execute as {p} run function tmm:rewards/starter_kit", permission_level: 2, team_reward: false', chapter)
        self.assertIn('image: "minecraft:textures/item/book.png"', chapter)
        self.assertEqual(chapter.count('image: "minecraft:textures/item/'), 5)
        loc = (QUESTDIR / "lang/en_us.snbt").read_text()
        for q in load("manifest/quest_content_resolved.json")["quests"]:
            if q["chapter"] == "Tips & Tricks":
                self.assertIn("quest." + q["quest_id"] + ".title:", loc)
        layout = load("manifest/quest_layouts.json")
        self.assertEqual(layout["quest_count"], 273)
        self.assertEqual(len([q for q in layout["positions"] if q["chapter"] == "Tips & Tricks"]), 34)
        self.assertEqual(len({tuple(x["position"]) for x in layout["positions"]
                              if x["chapter"] == "Tips & Tricks"}), 34)

    def test_packwiz_references_starter_files_and_hash_chain(self):
        pack = tomllib.loads((ROOT / "pack.toml").read_text())
        index_bytes = (ROOT / "index.toml").read_bytes()
        self.assertEqual(pack["index"]["hash"], hashlib.sha256(index_bytes).hexdigest())
        files = tomllib.loads(index_bytes.decode("utf-8"))["files"]
        selected = {r["file"]: r["hash"] for r in files}
        for rel in ("kubejs/data/tmm/advancement/starter_kit.json",
                    "kubejs/data/tmm/advancement/starter_first_login.json",
                    "kubejs/data/tmm/advancement/starter_kit_claimed.json",
                    "kubejs/data/tmm/function/rewards/starter_kit.mcfunction",
                    "kubejs/data/tmm/function/rewards/starter_first_login.mcfunction",
                    "config/ftbquests/quests/chapters/tips_tricks.snbt",
                    "config/ftbquests/quests/lang/en_us.snbt"):
            self.assertIn(rel, selected)
            self.assertEqual(selected[rel], hashlib.sha256((ROOT / rel).read_bytes()).hexdigest())


if __name__ == "__main__":
    unittest.main()
