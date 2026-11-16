// Standalone source launcher check; use Java 21 with the installed GeckoLib
// and Minecraft/NeoForge runtime libraries on the classpath. No game is started.
import java.nio.file.Files;
import java.nio.file.Path;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.loading.json.typeadapter.KeyFramesAdapter;
import software.bernie.geckolib.loading.json.raw.Model;
import software.bernie.geckolib.loading.object.GeometryTree;
import software.bernie.geckolib.loading.object.BakedModelFactory;

public class CheckDracolichGeo {
    private static int bones, cubes;

    private static void count(GeoBone bone) {
        bones++;
        cubes += bone.getCubes().size();
        bone.getChildBones().forEach(CheckDracolichGeo::count);
    }

    public static void main(String[] args) throws Exception {
        var model = KeyFramesAdapter.GEO_GSON.fromJson(
                Files.readString(Path.of(args[0])), Model.class);
        var tree = GeometryTree.fromModel(model);
        var baked = BakedModelFactory.DEFAULT_FACTORY.constructGeoModel(tree);
        baked.topLevelBones().forEach(CheckDracolichGeo::count);
        if (bones != Integer.parseInt(args[1]) || cubes != Integer.parseInt(args[2])) {
            throw new AssertionError("Baked bone/cube count mismatch: " + bones + "/" + cubes);
        }
        System.out.println("PASS: GeckoLib deserialize, hierarchy and cube bake; "
                + bones + " bones, " + cubes + " cubes, " + baked.topLevelBones().size() + " roots");
    }
}
