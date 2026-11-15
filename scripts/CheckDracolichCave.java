// Java 21 source launcher. Pass installed Dragon Survival JAR, this repository,
// and a private output path OUTSIDE the repository. Requires the built module,
// GeckoLib and the existing Minecraft/NeoForge libraries on the classpath.
import com.google.gson.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipFile;
import java.io.*;
import java.lang.reflect.Proxy;
import io.github.heatwaveqc.tmm.transformations.client.DracolichModelLoader;
import io.github.heatwaveqc.tmm.transformations.client.DracolichArmorRenderer;
import io.github.heatwaveqc.tmm.transformations.dracolich.DracolichGeometryAssembler;
import software.bernie.geckolib.loading.json.typeadapter.KeyFramesAdapter;
import software.bernie.geckolib.loading.json.raw.Model;
import software.bernie.geckolib.loading.object.GeometryTree;
import software.bernie.geckolib.loading.object.BakedModelFactory;
import software.bernie.geckolib.cache.object.GeoBone;

public class CheckDracolichCave {
    private static int bones, cubes;
    private static void count(GeoBone bone) {
        bones++; cubes += bone.getCubes().size(); bone.getChildBones().forEach(CheckDracolichCave::count);
    }
    private static JsonArray bones(JsonObject model) {
        return model.getAsJsonArray("minecraft:geometry").get(0).getAsJsonObject().getAsJsonArray("bones");
    }
    public static void main(String[] args) throws Exception {
        Path root=Path.of(args[1]).toAbsolutePath().normalize();
        Path output=Path.of(args[2]).toAbsolutePath().normalize();
        if(output.startsWith(root))throw new IllegalArgumentException("Keep native rig output private/outside the repository");
        Path assets=root.resolve("compat/transformations/src/main/resources/assets/tmm_transformations");
        Map<String,byte[]> files=new HashMap<>();
        try(var z=new ZipFile(args[0])) {
            String path="assets/dragonsurvival/geo/dragon_model.geo.json";
            files.put("dragonsurvival:geo/dragon_model.geo.json", z.getInputStream(z.getEntry(path)).readAllBytes());
        }
        for(String path:List.of("dracolich/cave_center.mesh.json","dracolich/cave_center.rig.json","geo/dracolich_cave_center.geo.json")) {
            files.put("tmm_transformations:"+path,Files.readAllBytes(assets.resolve(path)));
        }
        JsonObject source=JsonParser.parseString(new String(files.get("dragonsurvival:geo/dragon_model.geo.json"),java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject mesh=JsonParser.parseString(Files.readString(assets.resolve("dracolich/cave_center.mesh.json"))).getAsJsonObject();
        String hash=JsonParser.parseString(Files.readString(assets.resolve("dracolich/cave_center.rig.json"))).getAsJsonObject().get("rig_sha256_normalized").getAsString();
        JsonObject assembled=DracolichGeometryAssembler.assemble(source,mesh,hash);
        if(!DracolichGeometryAssembler.fingerprint(bones(source)).equals(DracolichGeometryAssembler.fingerprint(bones(assembled))))throw new AssertionError("Rig fields changed");
        // Exercise the actual runtime ResourceManager loader, not just its pure helper.
        ClassLoader cl=CheckDracolichCave.class.getClassLoader();
        Class<?> packClass=Class.forName("net.minecraft.server.packs.PackResources");
        Object pack=Proxy.newProxyInstance(cl,new Class<?>[]{packClass},(o,m,a)->m.getName().equals("packId")?"dracolich-test":null);
        Class<?> supplierClass=Class.forName("net.minecraft.server.packs.resources.IoSupplier");
        Class<?> resourceClass=Class.forName("net.minecraft.server.packs.resources.Resource");
        Class<?> managerClass=Class.forName("net.minecraft.server.packs.resources.ResourceManager");
        Object manager=Proxy.newProxyInstance(cl,new Class<?>[]{managerClass},(o,m,a)->{
            if(m.getName().equals("getResourceOrThrow") || m.getName().equals("getResource")) {
                String id=a[0].toString();
                byte[] bytes=files.get(id);
                if(bytes==null) {
                    if(m.getName().equals("getResource"))return Optional.empty();
                    throw new FileNotFoundException(id);
                }
                Object supplier=Proxy.newProxyInstance(cl,new Class<?>[]{supplierClass},(so,sm,sa)->new ByteArrayInputStream(bytes));
                Object resource=resourceClass.getConstructor(packClass,supplierClass).newInstance(pack,supplier);
                return m.getName().equals("getResource")?Optional.of(resource):resource;
            }
            throw new UnsupportedOperationException(m.toString());
        });
        Model loaded=(Model)DracolichModelLoader.class.getMethod("load",managerClass).invoke(null,manager);
        var baked=BakedModelFactory.DEFAULT_FACTORY.constructGeoModel(GeometryTree.fromModel(loaded));
        baked.topLevelBones().forEach(CheckDracolichCave::count);
        if(bones!=272 || cubes!=407 || baked.getBone("BreathSource").isEmpty() || baked.getBone("MountingBone").isEmpty())throw new AssertionError("Invalid assembled rig: "+bones+"/"+cubes);
        var nativeArmor=BakedModelFactory.DEFAULT_FACTORY.constructGeoModel(GeometryTree.fromModel(
                KeyFramesAdapter.GEO_GSON.fromJson(source,Model.class)));
        GeoBone head=baked.getBone("Head").orElseThrow();
        head.updateRotation(.41f,.22f,-.3f);head.updatePosition(1.2f,-.7f,2.3f);
        head.updateScale(.8f,.9f,1.1f);head.setHidden(true);head.setChildrenHidden(true);
        DracolichArmorRenderer.copyModelPose(baked,nativeArmor);
        GeoBone armorHead=nativeArmor.getBone("Head").orElseThrow();
        if(armorHead.getRotX()!=head.getRotX() || armorHead.getPosZ()!=head.getPosZ()
                || armorHead.getScaleY()!=head.getScaleY() || !armorHead.isHidden() || !armorHead.isHidingChildren())throw new AssertionError("Armor pose copy lost state");
        // Changed native rigs must yield the harmless marker that selectors reject.
        byte[] original=files.get("dragonsurvival:geo/dragon_model.geo.json");
        bones(source).get(0).getAsJsonObject().addProperty("name","UnverifiedRig");
        files.put("dragonsurvival:geo/dragon_model.geo.json",source.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
        Model fallback=(Model)DracolichModelLoader.class.getMethod("load",managerClass).invoke(null,manager);
        var marker=BakedModelFactory.DEFAULT_FACTORY.constructGeoModel(GeometryTree.fromModel(fallback));
        if(marker.getBone("Torso").isPresent() || marker.getBone("tmm_unassembled_marker").isEmpty())throw new AssertionError("Unsafe failed-rig fallback");
        Files.createDirectories(output.getParent());Files.writeString(output,assembled.toString());
        System.out.println("PASS: official-JAR fingerprint, actual runtime resource loader, GeckoLib bake: 272 bones/407 cubes; native armor pose copy; changed-rig fallback; native rig preserved");
    }
}
