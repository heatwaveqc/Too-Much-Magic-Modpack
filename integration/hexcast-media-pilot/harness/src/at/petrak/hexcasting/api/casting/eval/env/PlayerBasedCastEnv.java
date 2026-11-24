package at.petrak.hexcasting.api.casting.eval.env;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import net.minecraft.server.level.ServerPlayer;
public class PlayerBasedCastEnv extends CastingEnvironment {
    private final ServerPlayer caster;
    public PlayerBasedCastEnv(ServerPlayer caster) { this.caster = caster; }
    @Override public ServerPlayer getCaster() { return caster; }
}
