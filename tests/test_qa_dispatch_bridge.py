#!/usr/bin/env python3
import copy
import datetime as dt
import sys
import unittest
from pathlib import Path
sys.path.insert(0,str(Path(__file__).resolve().parents[1]/"scripts"))
import qa_dispatch_bridge as b
NOW=dt.datetime(2026,11,17,4,0,tzinfo=dt.timezone.utc)
SHA="a"*40
NONCE="b"*32
OWNER={"login":b.OWNER,"id":b.OWNER_ID,"type":"User"}
def event(mode="smoke"):
    body=f"/tmm {mode} {SHA}"+("" if mode=="check" else " "+NONCE)
    return {"action":"created","repository":{"full_name":b.REPO,"owner":{"id":b.OWNER_ID}},
            "issue":{"number":b.ISSUE,"state":"open"},"sender":OWNER.copy(),
            "comment":{"id":100,"author_association":"OWNER","user":OWNER.copy(),"body":body,
                       "created_at":"2026-11-17T03:59:00Z","updated_at":"2026-11-17T03:59:00Z",
                       "issue_url":f"https://api.github.com/repos/{b.REPO}/issues/{b.ISSUE}"}}
class Fake:
    def __init__(self,ev):
        self.ev=ev;self.writes=[];self.sha=SHA;self.green=True;self.active=False;self.claimed=False;self.edited=False
    def __call__(self,path,method="GET",body=None):
        if method=="POST":
            self.writes.append((path,body))
            if path=="git/refs":
                if self.claimed: raise b.Refused("duplicate nonce")
                self.claimed=True
            return {}
        if path=="issues/comments/100":
            c=self.ev["comment"].copy()
            if self.edited:c["body"]+=" altered"
            return c
        if path.startswith("issues/"):return self.ev["issue"]
        if path.startswith("git/ref/heads/"):return {"object":{"sha":self.sha}}
        def run(path,id):
            return {"id":id,"path":path,"event":"push","head_branch":b.BRANCH,"head_sha":SHA,"run_number":1,"status":"completed","conclusion":"success" if self.green else "failure"}
        if path.startswith("actions/runs?"):
            if "head_sha=" in path:return {"workflow_runs":[run(b.BOOTSTRAP,1),run(b.INTEGRATION,2)]}
            rows=[{"id":3,"path":b.LAUNCHER}] if self.active else []
            return {"total_count":len(rows),"workflow_runs":rows}
        if path.startswith("actions/runs/1/jobs"):return {"jobs":[{"name":"metadata","conclusion":"success"}]}
        if path.startswith("actions/runs/2/jobs"):
            passed=["Integration Preflight — frozen source refs, no writes or mod downloads","Compile and unit test all explicitly selected first-party modules","Offline regression for manifest-driven Prism packaging"]
            skipped=["Integration Assembly — explicitly authorized, never on automatic push","Full dedicated-server smoke with selected first-party runtime modules","Produce commit-locked all-workstreams Prism client ZIP"]
            return {"jobs":[{"name":"integration-preflight-and-test","conclusion":"success","steps":[{"name":x,"conclusion":"success"} for x in passed]+[{"name":x,"conclusion":"skipped"} for x in skipped]}]}
        raise AssertionError(path)
class Tests(unittest.TestCase):
    def test_modes(self):
        for mode,package in (("smoke","false"),("prism","true")):
            e=event(mode);api=Fake(e);res=b.execute(api,e,NOW)
            self.assertTrue(res["dispatched"]);self.assertEqual(len(api.writes),2)
            self.assertEqual(api.writes[0][1],{"ref":"refs/tags/qa-request/"+NONCE,"sha":SHA})
            self.assertEqual(api.writes[1][1],{"ref":"main","inputs":{"full_smoke":"true","package_prism":package,"expected_integration_sha":SHA,"qa_request_id":NONCE}})
    def test_check_no_writes(self):
        e=event("check");api=Fake(e);self.assertFalse(b.execute(api,e,NOW)["dispatched"]);self.assertEqual(api.writes,[])
    def test_untrusted_variants(self):
        for modify in (lambda e:e["comment"]["user"].update({"id":1}),lambda e:e["comment"].update({"author_association":"MEMBER"}),
                       lambda e:e["sender"].update({"id":1}),lambda e:e["issue"].update({"number":1}),
                       lambda e:e["issue"].update({"pull_request":{}}),lambda e:e.update({"action":"edited"}),
                       lambda e:e["comment"].update({"body":"/tmm smoke "+SHA+" "+NONCE+"\n; arbitrary command"})):
            e=event();modify(e);api=Fake(e)
            with self.assertRaises(b.Refused):b.execute(api,e,NOW)
            self.assertEqual(api.writes,[])
    def test_guards(self):
        for key,val in (("sha","c"*40),("green",False),("active",True),("edited",True)):
            e=event();api=Fake(e);setattr(api,key,val)
            with self.assertRaises(b.Refused):b.execute(api,e,NOW)
            self.assertEqual(api.writes,[])
    def test_stale(self):
        e=event();api=Fake(e)
        with self.assertRaises(b.Refused):b.execute(api,e,NOW+dt.timedelta(hours=1))
        self.assertEqual(api.writes,[])
    def test_duplicate_nonce(self):
        e=event();api=Fake(e);b.execute(api,e,NOW)
        with self.assertRaises(b.Refused):b.execute(api,e,NOW)
        self.assertEqual(len([x for x in api.writes if x[0].endswith("/dispatches")]),1)
if __name__=="__main__":unittest.main()
