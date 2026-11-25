"""Check failure extraction and avoid publishing unrelated private log lines."""
import importlib.util
from pathlib import Path
import unittest

SPEC = importlib.util.spec_from_file_location('qa_log', Path(__file__).resolve().parents[1] / 'scripts/audit_qa_client_log.py')
MODULE = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(MODULE)


class ClientLogTests(unittest.TestCase):
    def test_partial_script_load_and_recipe_failure_are_not_a_pass(self):
        report = MODULE.summarize(b'[KubeJS Server/]: Loaded 12/13 KubeJS server scripts in 1 s with 1 errors\n'
            b'[Render thread/ERROR] [KubeJS Server/]: test.js#4: TypeError: redeclaration\n'
            b'Parsing error loading recipe tmm:ritual/test: Unknown registry key\n'
            b'Parsing error loading recipe tmm:ritual/test: Unknown registry key\n')
        self.assertEqual(report['server_script_loads'][0]['errors'], 1)
        self.assertEqual(report['script_failures'][0]['script'], 'test.js')
        self.assertEqual(report['recipe_failure_namespaces'], {'tmm': 1})
        self.assertEqual(report['recipe_failures'][0]['occurrences'], 2)

    def test_plugin_cause_and_private_login_are_separated(self):
        report = MODULE.summarize(b'PrivateName[local:private] logged in with entity id 99 at (1, 2, 3)\n'
            b'[EMI/]: Exception loading plugin provided by test_plugin\n'
            b'java.lang.NoClassDefFoundError: example/missing/Class\n')
        self.assertTrue(report['integrated_world_login_observed'])
        self.assertEqual(report['emi_plugin_failures'][0]['cause']['class'], 'example/missing/Class')
        self.assertNotIn('PrivateName', str(report))
        self.assertNotIn('(1, 2, 3)', str(report))


if __name__ == '__main__':
    unittest.main()
