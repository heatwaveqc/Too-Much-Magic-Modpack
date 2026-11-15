package io.github.heatwaveqc.tmm.forms;

import com.tyzsskills.api.events.TyzsSkillsCommonSetupEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

/** Independent Tyz/JAWR module, no Vampirism/Werewolves/Monk/Hunter classes. */
@Mod(FormSkillsMod.MOD_ID)
public final class FormSkillsMod {
    public static final String MOD_ID = "tmm_form_skills";

    public FormSkillsMod(IEventBus modBus) {
        modBus.addListener(FormSkillRegistration::onSetup);
        NeoForge.EVENT_BUS.register(FormSkillRegistration.class);
        NeoForge.EVENT_BUS.register(FormSkillRuntime.class);
        // No mutation to JAWR, and no runtime-active form skill on a clean install.
    }
}
