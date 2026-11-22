"""Static regression checks for the #510 JEI exception to Default Options."""
from __future__ import annotations

import sys
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
from generate_defaultoptions_keybindings import JEI_NATIVE_DEFAULTS, MIN_VANILLA, build
from validate_defaultoptions_keybindings import validate


class DefaultOptionsJeiTests(unittest.TestCase):
    def test_generator_exempts_jei_navigation_but_not_cheats(self):
        vanilla = {key: "native" for key in MIN_VANILLA}
        mod_keys = {
            "key.jei.showRecipe", "key.jei.showUses", "key.jei.focusSearch",
            "key.jei.bookmark", "key.jei.recipeBack", "key.jei.nextPage",
            "key.jei.cheatOneItem", "key.jei.showRecipe2", "key.some_mod.ability",
        }
        output = build({**vanilla, **{key: "native" for key in mod_keys}}, vanilla)
        self.assertNotIn("key_key.jei.showRecipe:", output)
        self.assertNotIn("key_key.jei.showUses:", output)
        self.assertNotIn("key_key.jei.bookmark:", output)
        self.assertNotIn("key_key.jei.nextPage:", output)
        self.assertIn("key_key.jei.cheatOneItem:key.keyboard.unknown:", output)
        self.assertIn("key_key.jei.showRecipe2:key.keyboard.unknown:", output)
        self.assertIn("key_key.some_mod.ability:key.keyboard.unknown:", output)
        self.assertNotIn("key_key.attack:", output)

    def test_exception_scope_is_exact(self):
        self.assertIn("key.jei.showRecipe", JEI_NATIVE_DEFAULTS)
        self.assertIn("key.jei.showUses", JEI_NATIVE_DEFAULTS)
        self.assertNotIn("key.jei.showRecipe2", JEI_NATIVE_DEFAULTS)
        self.assertNotIn("key.jei.showUses2", JEI_NATIVE_DEFAULTS)
        self.assertNotIn("key.jei.cheatItemStack", JEI_NATIVE_DEFAULTS)
        self.assertNotIn("key.jei.toggleEditMode", JEI_NATIVE_DEFAULTS)

    def test_checked_in_pack_defaults(self):
        validate()


if __name__ == "__main__":
    unittest.main()
