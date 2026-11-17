package at.petrak.hexcasting.api.casting.eval.env;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import net.minecraft.server.level.ServerPlayer;
public class CircleCastEnv extends CastingEnvironment {
    private final ServerPlayer caster;
    public CircleCastEnv(ServerPlayer caster) { this.caster = caster; }
    @Override public ServerPlayer getCaster() { return caster; }
}
