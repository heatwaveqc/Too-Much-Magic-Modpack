package at.petrak.hexcasting.api.casting.eval;
import net.minecraft.server.level.ServerPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
public abstract class CastingEnvironment {
    private static final List<Consumer<CastingEnvironment>> listeners = new ArrayList<>();
    private final List<CastingEnvironmentComponent.ExtractMedia> mediaExtensions = new ArrayList<>();
    public static void addCreateEventListener(Consumer<CastingEnvironment> listener) { listeners.add(listener); }
    public void triggerCreateEvent() { listeners.forEach(x -> x.accept(this)); }
    public abstract ServerPlayer getCaster();
    public <T extends CastingEnvironmentComponent> void addExtension(T extension) {
        if (extension instanceof CastingEnvironmentComponent.ExtractMedia media) mediaExtensions.add(media);
    }
    public long testExtractCost(long cost) {
        for (var ext : mediaExtensions) cost = ext.onExtractMedia(cost);
        return cost;
    }
    public int testExtensionCount() { return mediaExtensions.size(); }
}
