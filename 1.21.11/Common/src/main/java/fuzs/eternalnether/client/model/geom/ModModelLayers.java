package fuzs.eternalnether.client.model.geom;

import fuzs.eternalnether.EternalNether;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ArmorModelSet;

public class ModModelLayers {
    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(EternalNether.MOD_ID);
    public static final ModelLayerLocation NETHERITE_BELL = MODEL_LAYERS.registerModelLayer("netherite_bell");
    public static final ModelLayerLocation GILDED_NETHERITE_SHIELD = MODEL_LAYERS.registerModelLayer(
            "gilded_netherite_shield");
    public static final ModelLayerLocation WEX = MODEL_LAYERS.registerModelLayer("wex");
    public static final ModelLayerLocation WARPED_ENDERMAN = MODEL_LAYERS.registerModelLayer("warped_enderman");
    public static final ModelLayerLocation CORPOR = MODEL_LAYERS.registerModelLayer("corpor");
    public static final ArmorModelSet<ModelLayerLocation> CORPOR_ARMOR = MODEL_LAYERS.registerArmorSet("corpor");
    public static final ModelLayerLocation WRAITHER = MODEL_LAYERS.registerModelLayer("wraither");
    public static final ArmorModelSet<ModelLayerLocation> WRAITHER_ARMOR = MODEL_LAYERS.registerArmorSet("wraither");
    public static final ModelLayerLocation WITHER_SKELETON_KNIGHT = MODEL_LAYERS.registerModelLayer(
            "wither_skeleton_knight");
    public static final ModelLayerLocation WITHER_SKELETON_KNIGHT_OUTER_LAYER = MODEL_LAYERS.registerModelLayer(
            "wither_skeleton_knight",
            "outer");
    public static final ArmorModelSet<ModelLayerLocation> WITHER_SKELETON_KNIGHT_ARMOR = MODEL_LAYERS.registerArmorSet(
            "wither_skeleton_knight");
    public static final ModelLayerLocation PIGLIN_PRISONER = MODEL_LAYERS.registerModelLayer("piglin_prisoner");
    public static final ArmorModelSet<ModelLayerLocation> PIGLIN_PRISONER_ARMOR = MODEL_LAYERS.registerArmorSet(
            "piglin_prisoner");
    public static final ModelLayerLocation PIGLIN_PRISONER_BABY = MODEL_LAYERS.registerModelLayer("piglin_prisoner_baby");
    public static final ArmorModelSet<ModelLayerLocation> PIGLIN_PRISONER_BABY_ARMOR = MODEL_LAYERS.registerArmorSet(
            "piglin_prisoner_baby");
    public static final ModelLayerLocation PIGLIN_HUNTER = MODEL_LAYERS.registerModelLayer("piglin_hunter");
    public static final ModelLayerLocation PIGLIN_HUNTER_SKULL = MODEL_LAYERS.registerModelLayer("piglin_hunter",
            "skull");
    public static final ArmorModelSet<ModelLayerLocation> PIGLIN_HUNTER_ARMOR = MODEL_LAYERS.registerArmorSet(
            "piglin_hunter");
    public static final ModelLayerLocation PIGLIN_HUNTER_BABY = MODEL_LAYERS.registerModelLayer("piglin_hunter_baby");
    public static final ArmorModelSet<ModelLayerLocation> PIGLIN_HUNTER_BABY_ARMOR = MODEL_LAYERS.registerArmorSet(
            "piglin_hunter_baby");
    public static final ModelLayerLocation WITHER_SKELETON_HORSE = MODEL_LAYERS.registerModelLayer(
            "wither_skeleton_horse");
    public static final ModelLayerLocation WITHER_SKELETON_HORSE_BABY = MODEL_LAYERS.registerModelLayer(
            "wither_skeleton_horse_baby");
    public static final ModelLayerLocation WITHER_SKELETON_HORSE_SADDLE = MODEL_LAYERS.registerModelLayer(
            "wither_skeleton_horse",
            "saddle");
    public static final ModelLayerLocation WITHER_SKELETON_HORSE_BABY_SADDLE = MODEL_LAYERS.registerModelLayer(
            "wither_skeleton_horse_baby",
            "saddle");
}
