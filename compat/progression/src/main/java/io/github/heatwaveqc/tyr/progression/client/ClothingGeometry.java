package io.github.heatwaveqc.tyr.progression.client;

import io.github.heatwaveqc.tyr.progression.foundation.MartialGarmentStyle;
import io.github.heatwaveqc.tyr.progression.runtime.clothing.ClothingItems;
import java.util.EnumMap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/** Static, pose-following clothing meshes. One cached model per shape/slot;
 * no cloth physics, texture uploads, or per-entity model construction.
 * Native armor hook copies the original humanoid's current pose and visibility. */
final class ClothingGeometry {
    private ClothingGeometry() {}
    private enum Shape { KILT, LOINCLOTH, CAP, KARATE_GI, TAEKWONDO_DOBOK, SHAOLIN, KUNG_FU, KIMONO_HAKAMA, WUXIA }
    private static final EnumMap<Shape, HumanoidModel<LivingEntity>> SPECIAL = new EnumMap<>(Shape.class);
    private static final EnumMap<Shape, HumanoidModel<LivingEntity>> UPPER = new EnumMap<>(Shape.class);
    private static final EnumMap<Shape, HumanoidModel<LivingEntity>> LOWER = new EnumMap<>(Shape.class);

    static HumanoidModel<?> select(ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
        if (stack.is(ClothingItems.TARTAN_KILT.get()) && slot == EquipmentSlot.LEGS)
            return SPECIAL.computeIfAbsent(Shape.KILT, ClothingGeometry::special);
        if (stack.is(ClothingItems.LOINCLOTH.get()) && slot == EquipmentSlot.LEGS)
            return SPECIAL.computeIfAbsent(Shape.LOINCLOTH, ClothingGeometry::special);
        if (stack.is(ClothingItems.WHITE_CAP.get()) && slot == EquipmentSlot.HEAD)
            return SPECIAL.computeIfAbsent(Shape.CAP, ClothingGeometry::special);
        var style = ClothingItems.martialStyle(stack).orElse(null);
        if (style == null) return original;
        var shape = Shape.valueOf(style.name());
        if (slot == EquipmentSlot.CHEST) return UPPER.computeIfAbsent(shape, s -> martial(s, false));
        if (slot == EquipmentSlot.LEGS) return LOWER.computeIfAbsent(shape, s -> martial(s, true));
        return original;
    }

    private static CubeListBuilder box(int u, int v, float x, float y, float z, int w, int height, int depth) {
        return CubeListBuilder.create().texOffs(u,v).addBox(x,y,z,w,height,depth);
    }

    private static HumanoidModel<LivingEntity> special(Shape shape) {
        var mesh = new MeshDefinition();
        var root = mesh.getRoot();
        // HumanoidModel requires these named pose roots even when their geometry is empty.
        for (String name : new String[]{"head","hat","body","right_arm","left_arm","right_leg","left_leg"})
            root.addOrReplaceChild(name, CubeListBuilder.create(), PartPose.ZERO);
        var body = root.getChild("body");
        var head = root.getChild("head");
        switch (shape) {
            case KILT -> {
                // Three joined waist/pleat sections, wider at the hem; a single
                // connected garment anchored to torso, not two independent leggings.
                body.addOrReplaceChild("waist",box(0,0,-4.5F,10,-2.5F,9,2,5),PartPose.ZERO);
                body.addOrReplaceChild("upper_pleats",box(0,8,-5,12,-3,10,3,6),PartPose.ZERO);
                body.addOrReplaceChild("hem",box(0,20,-6,15,-3.5F,12,3,7),PartPose.ZERO);
            }
            case LOINCLOTH -> {
                body.addOrReplaceChild("cord",box(0,0,-4.5F,11,-2.5F,9,1,5),PartPose.ZERO);
                body.addOrReplaceChild("front_panel",box(0,8,-2.5F,0,-0.5F,5,6,1),
                        PartPose.offsetAndRotation(0,12,-2.7F,-0.10F,0,0));
                body.addOrReplaceChild("rear_panel",box(0,8,-2.5F,0,-0.5F,5,6,1),
                        PartPose.offsetAndRotation(0,12,2.7F,0.10F,0,0));
            }
            case CAP -> {
                head.addOrReplaceChild("cuff",box(0,0,-4.5F,-8.5F,-4.5F,9,3,9),PartPose.ZERO);
                head.addOrReplaceChild("crown",box(0,13,-3.5F,-10.5F,-3.5F,7,3,7),PartPose.ZERO);
                // Bent stocking cap with a fixed long tail, attached to the head.
                var tail = head.addOrReplaceChild("tail",box(0,24,-2,0,-2,4,6,4),
                        PartPose.offsetAndRotation(2,-9,2,0.95F,0,-0.65F));
                var fold = tail.addOrReplaceChild("fold",box(0,36,-1.5F,0,-1.5F,3,5,3),
                        PartPose.offsetAndRotation(0,5,0,-0.45F,0,0.3F));
                fold.addOrReplaceChild("tip",box(0,45,-1,0,-1,2,3,2),
                        PartPose.offsetAndRotation(0,4,0,-0.25F,0,0.15F));
            }
            default -> throw new IllegalArgumentException("Not a special garment");
        }
        return new HumanoidModel<>(LayerDefinition.create(mesh,64,64).bakeRoot());
    }

    private static HumanoidModel<LivingEntity> martial(Shape shape, boolean lower) {
        var mesh = HumanoidModel.createMesh(new CubeDeformation(0.25F),0);
        var root = mesh.getRoot();
        // Retain vanilla UV islands (64x32) used by the previous 64x64
        // vertically expanded texture masks. Enlarge only across the fabric,
        // not along limbs, to avoid moving the cuffs.
        if (lower) {
            float width = switch(shape) {
                case SHAOLIN -> 0.65F;
                case KIMONO_HAKAMA -> 0.95F;
                case WUXIA -> 0.8F;
                case KUNG_FU -> 0.45F;
                default -> 0.30F;
            };
            var grow = new CubeDeformation(width,0.12F,width);
            root.addOrReplaceChild("right_leg",CubeListBuilder.create().texOffs(0,16)
                    .addBox(-2,0,-2,4,12,4,grow),PartPose.offset(-1.9F,12,0));
            root.addOrReplaceChild("left_leg",CubeListBuilder.create().texOffs(0,16).mirror()
                    .addBox(-2,0,-2,4,12,4,grow),PartPose.offset(1.9F,12,0));
        } else {
            float width = switch(shape) {
                case KIMONO_HAKAMA -> 1.15F;
                case WUXIA -> 0.95F;
                case KARATE_GI -> 0.55F;
                case SHAOLIN -> 0.45F;
                default -> 0.25F;
            };
            var grow = new CubeDeformation(width,0.10F,width);
            root.addOrReplaceChild("right_arm",CubeListBuilder.create().texOffs(40,16)
                    .addBox(-3,-2,-2,4,12,4,grow),PartPose.offset(-5,2,0));
            root.addOrReplaceChild("left_arm",CubeListBuilder.create().texOffs(40,16).mirror()
                    .addBox(-1,-2,-2,4,12,4,grow),PartPose.offset(5,2,0));
        }
        return new HumanoidModel<>(LayerDefinition.create(mesh,64,32).bakeRoot());
    }
}
