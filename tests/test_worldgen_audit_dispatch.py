"""Offline tests for owner-only worldgen bridge; no network or dispatch."""
import datetime as dt
import sys
import unittest
from pathlib import Path
from unittest.mock import patch
sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))
import worldgen_audit_dispatch as bridge

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
