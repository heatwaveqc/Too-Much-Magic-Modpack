package io.github.heatwaveqc.tyr.progression.mixin;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Projectile.class)
public interface ProjectileOwnerAccess {
    @Accessor("leftOwner") void tmm$leftOwner(boolean value);
}
