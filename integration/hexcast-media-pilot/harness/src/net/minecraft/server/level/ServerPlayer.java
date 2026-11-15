package net.minecraft.server.level;
import java.util.HashSet;
import java.util.Set;
public class ServerPlayer {
    private final Set<String> tags = new HashSet<>();
    public Set<String> getTags() { return tags; }
}
