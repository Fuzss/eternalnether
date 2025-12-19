package fuzs.eternalnether.init;

import fuzs.eternalnether.EternalNether;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public final class ModTags {
    public static final TagKey<Item> SHEAR_TOOLS_ITEM_TAG_KEY = TagFactory.COMMON.registerItemTag("tools/shear");
    static final TagFactory TAGS = TagFactory.make(EternalNether.MOD_ID);
    public static final TagKey<Biome> HAS_PIGLIN_MANOR_BIOME_TAG_KEY = TAGS.registerBiomeTag(
            "has_structure/piglin_manor");
    public static final TagKey<Biome> HAS_CITADEL_BIOME_TAG_KEY = TAGS.registerBiomeTag("has_structure/citadel");
    public static final TagKey<Biome> HAS_CATACOMB_BIOME_TAG_KEY = TAGS.registerBiomeTag("has_structure/catacomb");
    public static final TagKey<DamageType> BYPASSES_CUTLASS_DAMAGE_TYPE_TAG_KEY = TAGS.registerTagKey(Registries.DAMAGE_TYPE,
            "bypasses_cutlass");
    public static final TagKey<TrimMaterial> PIGLIN_SAFE_TRIM_MATERIAL_TAG_KEY = TAGS.registerTagKey(Registries.TRIM_MATERIAL,
            "piglin_safe");
    public static final TagKey<Item> PIGLIN_BRUTE_SAFE_ARMOR_ITEM_TAG_KEY = TAGS.registerItemTag(
            "piglin_brute_safe_armor");
    public static final TagKey<Block> WITHERED_BLOCK_TAG_KEY = TAGS.registerBlockTag("withered");
}
