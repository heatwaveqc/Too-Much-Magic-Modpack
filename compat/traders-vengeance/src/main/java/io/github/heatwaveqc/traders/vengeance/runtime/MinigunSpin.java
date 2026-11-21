/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Spin rates and ramp logic adapted 2026-11-18 from PneumaticCraft: Repressurized,
 * Team Pneumatic, MineMaarten, desht and contributors, Minigun.java
 * upstream 7bcbcbc744a851d158d924cfa3ce6e88fb8ccb28.
 * NPC-only adaptation: no upgrades, air, item/ammo capability, or player.
 */
package io.github.heatwaveqc.traders.vengeance.runtime;

public final class MinigunSpin {
    public static final float MAX_SPEED = 0.4F;
    private float speed;
    private float angle;
    public void tick(boolean trigger) {
        speed = trigger ? Math.min(speed + 0.01F, MAX_SPEED)
                        : Math.max(0F, speed - 0.003F);
        // Avoid float precision accumulation in indefinitely loaded entities.
        angle = (angle + speed) % ((float)(2 * Math.PI));
    }
    public boolean ready() { return speed >= MAX_SPEED; }
    public float speed() { return speed; }
    public float angle() { return angle; }
}
