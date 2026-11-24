package io.github.heatwaveqc.tyr.progression.runtime;

import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Map;

/** Frozen approved #545 source outputs, exact 18/18 two-run identical
 * compressed SHA-256s from Actions job 137384529984. Untrusted cache data
 * cannot substitute another blueprint. Reconcile with new reviewed builds. */
final class ApprovedHomeAssets {
    private ApprovedHomeAssets() {}
    private static final Map<String,String> SHA256 = Map.ofEntries(
        Map.entry("adventurers_inn", "1971852c41b470a82ec4c8d570c82312d7e901355473f4b6dce0e24218f25c40"),
        Map.entry("arcane_forge", "bde75e464d8877dbdddca51ca5894b305f69966092f373cd4d297a2331b46a5b"),
        Map.entry("arcane_sanctum", "994d6e0482b7074320110ed644dce18bfebd17a1372525a999330ed441ef25c9"),
        Map.entry("castaways_retreat", "40f440b65bf3956488f3366c49e84b1b413e5d33bc3b122fee0d7e0c3c95ffe6"),
        Map.entry("celestial_observatory", "901dd2d55e80c950f3d3a9ce311dfb4b3370656641a9b8f2d9414931d4091d40"),
        Map.entry("chromatic_manor", "44fd6f5464ca2520baf3420793e2d5da8d5bf8fa45808aab9f52b095be7fc860"),
        Map.entry("dark_citadel", "c61cf423bc5b48b73c5cbc7b03ca5d8b3c8dc2dea5c582b8e9350f6b4bbec5eb"),
        Map.entry("dwarven_foundry", "65468d330c1d4f4f7c6cdb5570d5ec3f3394e377531b8ad057508ae76bb35e01"),
        Map.entry("elven_garden", "2ed1f5a501f6d04f3188a4b18e0345031450f0025cfa8e836b2ce39e97c1a7f3"),
        Map.entry("farmers_homestead", "9e3623e9dc42e8226eb17c21bca7dee22b0d08d7d3a2fd9dbe77d0a303e0d2f1"),
        Map.entry("hearthkeeper", "0a34f974184aabe13e7ba4df8c29089a64a859bb938c08ec6650a9e9d7e0d15d"),
        Map.entry("mages_academy", "93e3cf3e7bae093c7c3e88384fb3f07a5484c0250f54dd1ad91f31063312f7eb"),
        Map.entry("summoners_lodge", "2a985b431ba1c3792725e33ff21b007b359e57ab27c31743c5cb4a22531b7587"),
        Map.entry("sanguine_temple", "5c2a26215744e8f2298d5557ad6cbc9fe735f4f3b8fd226bee080829817852ea"),
        Map.entry("spirit_shrine", "6f687559e138dd993a08d46b81b5f8c127bed10212364ef974863309682d25a5"),
        Map.entry("witch_cottage", "d424ec0c1894af27d4479582a2c2d6877f83d2102f4e8394e46e2a12269dfc47"),
        Map.entry("witchs_manor", "815cc21a92cbb9822181d033e66a27f692ffadc12c9b1ed99bd4a7bf1141e456"),
        Map.entry("wizards_tower", "b81c740c6557d7ca58239aac1907acb9e3b629a7494ff0cded57d102d75da719")
    );
    static boolean matches(String name, byte[] bytes) throws java.security.NoSuchAlgorithmException {
        if (bytes.length < 1 || bytes.length > 8 * 1024 * 1024) return false;
        String expected=SHA256.get(name);
        return expected!=null && expected.equals(HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes)));
    }
}
