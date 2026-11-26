"""Offline tests for owner-only worldgen bridge; no network or dispatch."""
import datetime as dt
import json
import os
import shlex
import subprocess
import tempfile
import sys
import unittest
from pathlib import Path
from unittest.mock import patch
sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))
import worldgen_audit_dispatch as bridge
from worldgen_dispatch_diagnostics import classify, registration
from run_worldgen_vanilla_control import occupancy

OWNER = {"login":"heatwaveqc","id":22598785,"type":"User"}
SHA = "a"*40
NONCE = "b"*32
def event():
    return {"action":"created","repository":{"full_name":bridge.qa.REPO,"owner":{"id":22598785}},
            "issue":{"number":746,"state":"open"},
            "sender":dict(OWNER),"comment":{"id":123,"author_association":"OWNER","user":dict(OWNER),
            "body":f"/tmm worldgen {bridge.TOOLS} {NONCE}"}}

class DispatchTests(unittest.TestCase):
    def test_only_exact_request(self):
        self.assertEqual(bridge.validate(event()), NONCE)
        for mutate in (
            lambda e: e["comment"].update(body=f"/tmm worldgen {'c'*40} {NONCE}"),
            lambda e: e["comment"].update(body=e["comment"]["body"]+"\nextra"),
            lambda e: e["sender"].update(id=999),
            lambda e: e["issue"].update(number=291),
            lambda e: e.update(action="edited"),
        ):
            e=event()
            mutate(e)
            with self.assertRaises(bridge.qa.Refused):
                bridge.validate(e)
    def test_exact_one_shot_dispatch(self):
        calls=[]
        def api(path,method="GET",body=None):
            calls.append((path,method,body))
            return {}
        with patch.object(bridge,"live_comment"), patch.object(bridge,"main_head",return_value=SHA), patch.object(bridge,"check_ci"), patch.object(bridge,"check_active"):
            self.assertEqual(bridge.execute(api,event(),dt.datetime.now(dt.timezone.utc),SHA),NONCE)
        self.assertEqual(calls[0],("git/refs","POST",{"ref":"refs/tags/worldgen-request/"+NONCE,"sha":SHA}))
        self.assertEqual(calls[1][0:2],("actions/workflows/worldgen-audit-pilot.yml/dispatches","POST"))
        self.assertEqual(calls[1][2]["inputs"]["expected_launcher_sha"],SHA)
        self.assertEqual(len(calls),2)
    def test_vanilla_mode_and_receipt(self):
        e=event()
        e["comment"]["body"]=f"/tmm worldgen vanilla {bridge.TOOLS} {NONCE}"
        calls=[]
        def api(path,method="GET",body=None):
            calls.append((path,method,body))
            return {}
        with patch.object(bridge,"live_comment"), patch.object(bridge,"main_head",return_value=SHA), patch.object(bridge,"check_ci"), patch.object(bridge,"check_active"):
            self.assertEqual(bridge.execute(api,e,dt.datetime.now(dt.timezone.utc),SHA),NONCE)
        self.assertEqual(calls[0][2]["ref"],"refs/tags/worldgen-vanilla-request/"+NONCE)
        self.assertEqual(calls[1][2]["inputs"],{"authorized_build":"true","expected_launcher_sha":SHA,"request_nonce":NONCE})
        e["comment"]["body"]=f"/tmm worldgen tectonic {bridge.TOOLS} {NONCE}"
        with self.assertRaises(bridge.qa.Refused): bridge.validate(e)
    def test_receipt_mode_resolution(self):
        workflow=(Path(__file__).resolve().parents[1]/".github/workflows/worldgen-audit-pilot.yml").read_text()
        line=next(s.strip() for s in workflow.splitlines() if s.strip().startswith("MODE=$(python3 -c "))
        cmd=shlex.split(line[len("MODE=$("):-1])
        with tempfile.TemporaryDirectory() as temp:
            p=Path(temp)
            env=dict(os.environ,RUNNER_TEMP=temp,NONCE=NONCE,APPROVED_SHA=SHA)
            def ref(prefix, sha=SHA):
                return {"ref":f"refs/tags/{prefix}/{NONCE}","object":{"sha":sha,"type":"commit"}}
            for tmm,vanilla,expected in [
                ([ref("worldgen-request")],[],"tmm"),
                ([],[ref("worldgen-vanilla-request")],"vanilla"),
                ([],[],None),
                ([ref("worldgen-request")],[ref("worldgen-vanilla-request")],None),
                ([],[ref("worldgen-vanilla-request","d"*40)],None)
            ]:
                (p/"worldgen-tmm-receipt.json").write_text(json.dumps(tmm))
                (p/"worldgen-vanilla-receipt.json").write_text(json.dumps(vanilla))
                result=subprocess.run(cmd,env=env,capture_output=True,text=True)
                if expected is None: self.assertNotEqual(result.returncode,0)
                else:
                    self.assertEqual(result.returncode,0,result.stderr)
                    self.assertEqual(result.stdout.strip(),expected)
    def test_diagnostic_redaction(self):
        raw=json.dumps({"message":"Unexpected inputs provided: SECRET_VALUE","errors":[{"field":"inputs","code":"invalid"},{"field":"SECRET_VALUE","code":"SECRET_VALUE"}]})
        self.assertEqual(classify(raw),"unexpected inputs; field=inputs; code=invalid")
        self.assertEqual(classify('{"message":"SECRET_VALUE"}'),"unrecognized response")
        self.assertEqual(classify('not json'),"unrecognized response")
        def api(path,method="GET",body=None):
            self.assertEqual(method,"GET")
            if path.startswith("actions/workflows/"):
                return {"id":430805133,"path":".github/workflows/worldgen-audit-pilot.yml","state":"active","secret":"SECRET_VALUE"}
            return {"object":{"sha":SHA}}
        result=registration(api)
        self.assertTrue(result["numeric"]["id_matches"])
        self.assertEqual(result["filename"]["state"],"active")
        self.assertNotIn("SECRET_VALUE",json.dumps(result))
    def test_occupancy(self):
        # Row fields: id, origin x/z, target x/z, accepted, unique, non-air, above, rise, cols, cross.
        rows=["minecraft:test\t0\t0\t0\t0\t2\t2\t2\t1\t1\t1\t0",
              "minecraft:test\t1\t1\t0\t0\t2\t2\t2\t1\t1\t1\t0",
              "minecraft:test\t0\t0\t1\t1\t2\t2\t2\t0\t0\t1\t0"]
        a=occupancy(rows)
        self.assertEqual(a["occupied"],1)
        self.assertEqual(a["empty"],4095)
        self.assertEqual(a["nearby"]["4"],49)
    def test_stale_ci_and_duplicate_stop(self):
        def forbidden(*a,**k):
            self.fail("Unexpected write")
        with patch.object(bridge,"live_comment"),patch.object(bridge,"main_head",return_value="d"*40):
            with self.assertRaises(bridge.qa.Refused):
                bridge.execute(forbidden,event(),dt.datetime.now(dt.timezone.utc),SHA)
        with patch.object(bridge,"live_comment"),patch.object(bridge,"main_head",return_value=SHA),patch.object(bridge,"check_ci",side_effect=bridge.qa.Refused("red")):
            with self.assertRaises(bridge.qa.Refused):
                bridge.execute(forbidden,event(),dt.datetime.now(dt.timezone.utc),SHA)
        calls=[]
        def duplicate(path,method="GET",body=None):
            calls.append(path)
            raise bridge.qa.Refused("existing reservation")
        with patch.object(bridge,"live_comment"),patch.object(bridge,"main_head",return_value=SHA),patch.object(bridge,"check_ci"),patch.object(bridge,"check_active"):
            with self.assertRaises(bridge.qa.Refused):
                bridge.execute(duplicate,event(),dt.datetime.now(dt.timezone.utc),SHA)
        self.assertEqual(calls,["git/refs"])
if __name__=="__main__":
    unittest.main()
