package io.github.heatwaveqc.tmm.economy;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.PortableAnvilInstallPolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class PortableAnvilInstallPolicyTest {
    @Test void onlyValidFirstInstallSucceeds() {
        assertTrue(PortableAnvilInstallPolicy.canInstall(true,true,true,1,true));
        assertTrue(PortableAnvilInstallPolicy.canInstall(true,true,true,3,true));
        assertFalse(PortableAnvilInstallPolicy.canInstall(false,true,true,1,true));
        assertFalse(PortableAnvilInstallPolicy.canInstall(true,false,true,1,true));
        assertFalse(PortableAnvilInstallPolicy.canInstall(true,true,false,1,true));
        assertFalse(PortableAnvilInstallPolicy.canInstall(true,true,true,0,true));
        assertFalse(PortableAnvilInstallPolicy.canInstall(true,true,true,1,false));
    }
}
