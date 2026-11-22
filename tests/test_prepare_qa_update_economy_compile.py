#!/usr/bin/env python3
import sys
import unittest
from pathlib import Path
sys.path.insert(0,str(Path(__file__).resolve().parents[1]/"scripts"))
from prepare_qa_update_economy_compile import checked_patch, UPGRADES

BASE = """plugins {
    id 'java-library'
}
def modsDir = providers.gradleProperty('tmmCompileModsDir')
def exactJars = ['ProjectE.jar', 'evilcraft-1.21.1-neoforge-1.2.100.jar', 'neovitae-1.21.1-1.1.53.jar', 'goety-3.2.2.jar']
if (modsDir.isPresent()) {
    exactJars.each { name ->
        if (!file("${modsDir.get()}/${name}").isFile()) throw new GradleException("Missing pinned compile dependency: ${name}")
    }
}
"""
class CompilePinsTests(unittest.TestCase):
    def test_exact_three_replacements_only(self):
        changed = checked_patch(BASE,UPGRADES)
        self.assertIn("'ProjectE.jar'",changed)
        for name, (old,new) in UPGRADES.items():
            self.assertNotIn("'"+old+"'",changed)
            self.assertEqual(changed.count("'"+new+"'"),1)
        self.assertIn('exactJars.each { name ->',changed)
        self.assertEqual(changed.count('if (modsDir.isPresent())'),1)
    def test_no_missing_old_pin(self):
        for name,(old,new) in UPGRADES.items():
            with self.subTest(name=name):
                with self.assertRaisesRegex(ValueError,"not the expected baseline"):
                    checked_patch(BASE.replace("'"+old+"'","'"+new+"'"),UPGRADES)
    def test_no_new_unreviewed_pin(self):
        changed = dict(UPGRADES)
        changed['evilcraft'] = ('evilcraft-1.21.1-neoforge-1.2.100.jar','evilcraft-1.21.1-neoforge-1.2.103.jar')
        with self.assertRaisesRegex(ValueError,"Unreviewed"):
            checked_patch(BASE,changed)
    def test_missing_contract_is_rejected(self):
        for changed in (BASE.replace('def exactJars = ', 'def runtimeJars = '),
                        BASE.replace('if (modsDir.isPresent())', 'if (true)'),
                        BASE + '\ndef exactJars = []\n'):
            with self.assertRaises(ValueError):
                checked_patch(changed,UPGRADES)
    def test_unknown_or_missing_slug_rejected(self):
        changed = dict(UPGRADES)
        changed['new'] = ('a','b')
        with self.assertRaises(ValueError):
            checked_patch(BASE, changed)
        changed.pop('new')
        changed.pop('goety')
        with self.assertRaises(ValueError):
            checked_patch(BASE,changed)

if __name__ == "__main__":
    unittest.main()
