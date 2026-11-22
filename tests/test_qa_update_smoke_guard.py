#!/usr/bin/env python3
"""Source-only regression for owner-only update smoke command authorization."""
import datetime as dt
import os
import pathlib
import re
import sys
import tempfile
import unittest
from unittest.mock import patch
sys.path.insert(0, str(pathlib.Path(__file__).resolve().parents[1] / "scripts"))
import qa_update_smoke_guard as m

NOW = dt.datetime(2026, 11, 21, 20, 0, tzinfo=dt.timezone.utc)
SHA = "a"*40
NONCE = "b"*32
BRANCH = "experiment/four-mod-update-smoke-20261121"
OWNER = {"id": m.OWNER_ID, "login":"heatwaveqc", "type":"User"}
COMMAND = f"/tmm-update smoke {BRANCH} {SHA} {NONCE}"

def event():
    posted=(NOW-dt.timedelta(minutes=1)).isoformat().replace("+00:00", "Z")
    return {
        "action":"created", "repository":{"full_name":m.REPO,"owner":{"id":m.OWNER_ID}},
        "issue":{"number":291,"state":"open"}, "sender":dict(OWNER),
        "comment":{"id":123,"body":COMMAND,"author_association":"OWNER","user":dict(OWNER),
                   "created_at":posted,"updated_at":posted,
                   "issue_url":f"https://api.github.com/repos/{m.REPO}/issues/291"},
    }

class Fake:
    def __init__(self, ev):
        self.ev = ev
        self.green = True
        self.active = False
        self.head = SHA
        self.edited = False
        self.writes = []
    def __call__(self, path, token, method="GET", data=None):
        if method == "POST":
            self.writes.append((path, data))
            return {}
        if path == "issues/comments/123":
            c = dict(self.ev["comment"])
            if self.edited: c["updated_at"] = NOW.isoformat().replace("+00:00","Z")
            return c
        if path.startswith("git/ref/heads/"):
            return {"object":{"sha":self.head}}
        if path.startswith("pulls?"):
            return [{"head":{"sha":SHA,"repo":{"full_name":m.REPO}},
                     "base":{"ref":"test/full-integration"}}]
        if path.startswith("actions/runs?") and "head_sha=" in path:
            return {"workflow_runs":[{"id":1, "path":".github/workflows/verify.yml",
                                     "head_sha":SHA, "head_branch":BRANCH,
                                     "run_number":2, "status":"completed",
                                     "conclusion":"success" if self.green else "failure"}]}
        if path.startswith("actions/runs?"):
            # Unrelated 100+ push/PR runs must not block an authorized game
            # smoke or make the current owner-comment run reject itself.
            assert "event=workflow_dispatch" in path
            active=[{"id":5,"path":".github/workflows/full-integration-launcher.yml","name":"QA Smoke"}] if self.active else []
            return {"total_count":len(active),"workflow_runs":active}
        raise AssertionError(path)

class GuardTests(unittest.TestCase):
    def test_exact_command_only(self):
        self.assertEqual(m.parse(COMMAND), ("smoke",BRANCH,SHA,NONCE))
        self.assertEqual(m.parse(COMMAND.replace(" smoke "," prism ")), ("prism",BRANCH,SHA,NONCE))
        for value in [COMMAND+"\nexit 0", COMMAND+" extra",
                      COMMAND.replace(" smoke "," quick "), COMMAND.replace(" smoke "," promote "),
                      COMMAND.replace(SHA,SHA[:-1]), COMMAND.upper(),
                      "/tmm-update smoke refs/heads/main "+SHA+" "+NONCE,
                      "/tmm-update smoke experiment/x "+SHA+" "+NONCE]:
            with self.subTest(value=value), self.assertRaises(m.Refused):
                m.parse(value)
    def test_valid_source_only_preflight_no_mutations(self):
        e = event()
        f = Fake(e)
        with patch.object(m, "get", side_effect=f):
            self.assertEqual(m.validate(e,"token",NOW),("smoke",BRANCH,SHA,NONCE))
            e["comment"]["body"] = COMMAND.replace(" smoke "," prism ")
            self.assertEqual(m.validate(e,"token",NOW),("prism",BRANCH,SHA,NONCE))
        self.assertEqual(f.writes, [])
    def test_fail_closed(self):
        modifiers=[
            lambda e,f:e["comment"]["user"].update({"id":4}),
            lambda e,f:e["sender"].update({"id":4}),
            lambda e,f:e["issue"].update({"number":1}),
            lambda e,f:e["comment"].update({"author_association":"MEMBER"}),
            lambda e,f:e.update({"action":"edited"}),
            lambda e,f:setattr(f,"edited",True),
            lambda e,f:setattr(f,"head","c"*40),
            lambda e,f:setattr(f,"green",False),
            lambda e,f:setattr(f,"active",True),
            lambda e,f:e["comment"].update({"body":COMMAND+"\nsecond command"}),
        ]
        for change in modifiers:
            e=event();f=Fake(e);change(e,f)
            with self.subTest(change=change),patch.object(m,"get",side_effect=f):
                with self.assertRaises(m.Refused):
                    m.validate(e,"token",NOW)
            self.assertEqual(f.writes,[])
    def test_expired(self):
        e=event();f=Fake(e)
        with patch.object(m,"get",side_effect=f):
            with self.assertRaises(m.Refused):
                m.validate(e,"token",NOW+dt.timedelta(hours=1))

if __name__=="__main__":
    unittest.main()
