package at.petrak.hexcasting.api.casting.eval;
public interface CastingEnvironmentComponent {
    Key<?> getKey();
    interface Key<C extends CastingEnvironmentComponent> {}
    interface ExtractMedia extends CastingEnvironmentComponent {
        long onExtractMedia(long cost);
    }
}
