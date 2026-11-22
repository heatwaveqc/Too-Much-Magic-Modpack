#!/usr/bin/env python3
"""Source-only tests: assembling clean baseline then overlaying exactly four pinned mods."""
import hashlib
import json
import os
import sys
import tempfile
import tomllib
import unittest
from pathlib import Path
from unittest.mock import patch

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))
import apply_qa_update_overlay as target

SHA = "a"*40
BASE = "b"*40


class OverlayTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.trial = Path(self.temp.name) / "trial"
        self.assembled = Path(self.temp.name) / "assembly"
        self.original_pins = {}
        self.original_manifest = {"mods": []}
        self.updated_manifest = {"mods": []}
        index = ['hash-format = "sha256"', ""]
        for slug, filename in target.SLUG_FILES.items():
            initial_pin = (f'name = "{slug}"\nfilename = "{slug}-old.jar"\nside = "both"\n\n'
                           f'[download]\nurl = "https://example.invalid/old.jar"\n'
                           f'hash-format = "sha512"\nhash = "{"1"*128}"\n')
            new_pin = (f'name = "{slug}"\nfilename = "{filename}"\nside = "both"\n\n'
                       f'[download]\nurl = "https://cdn.modrinth.com/official.jar"\n'
                       f'hash-format = "sha512"\nhash = "{"2"*128}"\n')
            self.original_pins[f"mods/{slug}.pw.toml"] = initial_pin
            self.write(self.assembled / f"mods/{slug}.pw.toml", initial_pin)
            self.write(self.trial / f"mods/{slug}.pw.toml", new_pin)
            self.original_manifest["mods"].append({"id": slug, "version": "original", "enabled_in_pack": True})
            self.updated_manifest["mods"].append({"id": slug, "version": "new", "enabled_in_pack": True})
            index.extend(["[[files]]", f'file = "mods/{slug}.pw.toml"',
                          f'hash = "{hashlib.sha256(initial_pin.encode()).hexdigest()}"',
                          'metafile = true', ""])
        self.write(self.assembled / "manifest/mods.json", json.dumps(self.original_manifest))
        self.write(self.trial / "manifest/mods.json", json.dumps(self.updated_manifest))
        self.write(self.assembled / "index.toml", "\n".join(index))
        self.write(self.assembled / "pack.toml", ('name = "QA"\n\n[index]\nfile = "index.toml"\n'
                                                'hash-format = "sha256"\nhash = "' + "a"*64 + '"\n'))
        self.write(self.assembled / "manifest/integration-lock.json",
                   json.dumps({"channel":"INTEGRATION-NOT-STABLE", "pack_sha256":"x"}))

    def write(self, path, content):
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(content, encoding="utf-8")

    def git(self, root, *args):
        if args == ("rev-parse","HEAD"):
            return SHA if root == self.trial else BASE
        if args == ("rev-parse","refs/remotes/origin/test/full-integration"):
            return BASE
        if args == ("branch","--show-current"):
            return "" if root == self.trial else "test/full-integration"
        raise AssertionError((root,args))

    def head(self, root, path):
        if path == "manifest/mods.json":
            return json.dumps(self.original_manifest)
        return self.original_pins[path]

    def prepare(self):
        return patch.dict(os.environ, {"TMM_UPDATE_CANDIDATE_SHA":SHA,
                                     "GITHUB_ACTIONS":"true", "GITHUB_EVENT_NAME":"issue_comment"})

    def test_complete_four_update_overlay_and_valid_pack_hash(self):
        with self.prepare(), patch.object(target, "git", side_effect=self.git), patch.object(
            target, "content_at_head", side_effect=self.head):
            plan = target.build_plan(self.trial,self.assembled,SHA)
        self.assertEqual(set(plan), {"mods/"+k+".pw.toml" for k in target.SLUG_FILES} |
                         {"index.toml","pack.toml","manifest/mods.json","manifest/integration-lock.json"})
        index = tomllib.loads(plan["index.toml"])
        for slug in target.SLUG_FILES:
            row=next(x for x in index["files"] if x["file"]==f"mods/{slug}.pw.toml")
            self.assertEqual(row["hash"],hashlib.sha256(plan[row["file"]].encode()).hexdigest())
        pack=tomllib.loads(plan["pack.toml"])
        self.assertEqual(pack["index"]["hash"],hashlib.sha256(plan["index.toml"].encode()).hexdigest())
        self.assertEqual(json.loads(plan["manifest/integration-lock.json"])["update_trial"]["candidate_sha"],SHA)

    def test_refuse_changed_integration_pin(self):
        self.write(self.assembled / "mods/neovitae.pw.toml","manually changed pin")
        with self.prepare(), patch.object(target,"git",side_effect=self.git),patch.object(
            target,"content_at_head",side_effect=self.head):
            with self.assertRaisesRegex(ValueError, "Baseline changed neovitae"):
                target.build_plan(self.trial,self.assembled,SHA)

    def test_refuse_wrong_ci_identity(self):
        with patch.dict(os.environ, {"TMM_UPDATE_CANDIDATE_SHA":"c"*40,
                                     "GITHUB_ACTIONS":"true", "GITHUB_EVENT_NAME":"issue_comment"}):
            with self.assertRaisesRegex(ValueError,"candidate SHA"):
                target.build_plan(self.trial,self.assembled,SHA)


if __name__ == "__main__":
    unittest.main()
