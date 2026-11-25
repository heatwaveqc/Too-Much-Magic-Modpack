package io.github.heatwaveqc.tyr.progression.runtime;

/**
 * Pure-Java geometry for bedrock sleeping. No Minecraft types, so the core
 * test suite can verify this invariant without launching a server.
 */
public final class BedrockRestGeometry {
    private BedrockRestGeometry() {}

    @FunctionalInterface
    public interface BlockTest {
        boolean test(int x, int y, int z);
    }

    /**
     * Needs two horizontally adjacent bedrock floor blocks and two blocks of
     * vertical clearance over BOTH. No diagonals, 1-wide pads or lofted slabs.
     */
    public static boolean hasExposedPair(int x, int y, int z,
                                         BlockTest isBedrock, BlockTest isAir) {
        if (!isBedrock.test(x,y,z) || !isAir.test(x,y+1,z)
                || !isAir.test(x,y+2,z)) return false;
        return clearNeighbor(x+1,y,z,isBedrock,isAir)
            || clearNeighbor(x-1,y,z,isBedrock,isAir)
            || clearNeighbor(x,y,z+1,isBedrock,isAir)
            || clearNeighbor(x,y,z-1,isBedrock,isAir);
    }

    private static boolean clearNeighbor(int x,int y,int z,
                                         BlockTest isBedrock, BlockTest isAir) {
        return isBedrock.test(x,y,z) && isAir.test(x,y+1,z) && isAir.test(x,y+2,z);
    }
}
