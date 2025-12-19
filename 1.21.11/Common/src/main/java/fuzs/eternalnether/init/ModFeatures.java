package fuzs.eternalnether.init;

import com.google.common.collect.ImmutableList;
import fuzs.eternalnether.world.level.levelgen.feature.MobFeature;
import fuzs.eternalnether.world.level.levelgen.feature.MobPassengerFeature;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.BasaltColumnsFeature;
import net.minecraft.world.level.levelgen.feature.DeltaFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

@SuppressWarnings("unchecked")
public final class ModFeatures {
    public static final WeightedList<Holder<? extends EntityType<? extends Mob>>> PIGLIN_MANOR_MOBS = WeightedList.of(
            new Weighted<>(ModEntityTypes.PIGLIN_HUNTER, 1),
            new Weighted<>(getBuiltInRegistryHolder(EntityType.PIGLIN), 3));
    public static final WeightedList<Holder<? extends EntityType<? extends Mob>>> CATACOMB_MOBS = WeightedList.of(new Weighted<>(
                    ModEntityTypes.CORPOR,
                    1),
            new Weighted<>(ModEntityTypes.WITHER_SKELETON_KNIGHT, 2),
            new Weighted<>(ModEntityTypes.WRAITHER, 3),
            new Weighted<>(getBuiltInRegistryHolder(EntityType.WITHER_SKELETON), 1));
    public static final WeightedList<Holder<? extends EntityType<? extends Mob>>> PIGLIN_PRISONER_CONVERSIONS = WeightedList.of(
            new Weighted<>(getBuiltInRegistryHolder(EntityType.PIGLIN), 4),
            new Weighted<>(ModEntityTypes.PIGLIN_HUNTER, 3),
            new Weighted<>(getBuiltInRegistryHolder(EntityType.PIGLIN_BRUTE), 1));

    public static final Holder.Reference<Feature<NoneFeatureConfiguration>> MOB_FEATURE_PIGLIN_PRISONER = ModRegistry.REGISTRIES.register(
            Registries.FEATURE,
            "mob_feature_piglin_prisoner",
            () -> new MobFeature(ModEntityTypes.PIGLIN_PRISONER));
    public static final Holder.Reference<Feature<NoneFeatureConfiguration>> MOB_FEATURE_PIGLIN_MANOR_INSIDE = ModRegistry.REGISTRIES.register(
            Registries.FEATURE,
            "mob_feature_piglin_manor_inside",
            () -> new MobFeature(PIGLIN_MANOR_MOBS));
    public static final Holder.Reference<Feature<NoneFeatureConfiguration>> MOB_FEATURE_PIGLIN_MANOR_OUTSIDE = ModRegistry.REGISTRIES.register(
            Registries.FEATURE,
            "mob_feature_piglin_manor_outside",
            () -> new MobPassengerFeature(ModEntityTypes.PIGLIN_HUNTER, ModEntityTypes.WITHER_SKELETON_HORSE));
    public static final Holder.Reference<Feature<NoneFeatureConfiguration>> MOB_FEATURE_STRIDER = ModRegistry.REGISTRIES.register(
            Registries.FEATURE,
            "mob_feature_strider",
            () -> new MobFeature(getBuiltInRegistryHolder(EntityType.STRIDER)));
    public static final Holder.Reference<Feature<NoneFeatureConfiguration>> MOB_FEATURE_WITHER_SKELETON = ModRegistry.REGISTRIES.register(
            Registries.FEATURE,
            "mob_feature_wither_skeleton",
            () -> new MobFeature(getBuiltInRegistryHolder(EntityType.WITHER_SKELETON)));
    public static final Holder.Reference<Feature<NoneFeatureConfiguration>> MOB_FEATURE_CATACOMB = ModRegistry.REGISTRIES.register(
            Registries.FEATURE,
            "mob_feature_catacomb",
            () -> new MobFeature(CATACOMB_MOBS));
    public static final Holder.Reference<Feature<NoneFeatureConfiguration>> MOB_FEATURE_WARPED_ENDERMAN = ModRegistry.REGISTRIES.register(
            Registries.FEATURE,
            "mob_feature_warped_enderman",
            () -> new MobFeature(ModEntityTypes.WARPED_ENDERMAN));

    public static void boostrap() {
        // NO-OP
    }

    private static <T extends Mob> Holder.Reference<EntityType<T>> getBuiltInRegistryHolder(EntityType<T> entityType) {
        return (Holder.Reference<EntityType<T>>) (Holder.Reference<?>) entityType.builtInRegistryHolder();
    }

    public static void setBasaltFeatureRestrictions() {
        BasaltColumnsFeature.CANNOT_PLACE_ON = ImmutableList.<Block>builder()
                .addAll(BasaltColumnsFeature.CANNOT_PLACE_ON)
                .add(
                        // New Fortresses
                        Blocks.NETHER_BRICK_SLAB,
                        Blocks.CRACKED_NETHER_BRICKS,
                        Blocks.CHISELED_NETHER_BRICKS,
                        Blocks.RED_NETHER_BRICKS,
                        Blocks.RED_NETHER_BRICK_STAIRS,
                        Blocks.RED_NETHER_BRICK_SLAB,
                        Blocks.CRIMSON_TRAPDOOR,
                        // Wither Forts
                        ModBlocks.COBBLED_BLACKSTONE.value(),
                        ModBlocks.WITHERED_BLACKSTONE.value(),
                        ModBlocks.CHISELED_WITHERED_BLACKSTONE.value(),
                        ModBlocks.CRACKED_WITHERED_BLACKSTONE.value(),
                        ModBlocks.WITHERED_DEBRIS.value(),
                        Blocks.IRON_BARS,
                        Blocks.COAL_BLOCK)
                .build();
        DeltaFeature.CANNOT_REPLACE = ImmutableList.<Block>builder().addAll(DeltaFeature.CANNOT_REPLACE).add(
                // New Fortresses
                Blocks.NETHER_BRICK_SLAB,
                Blocks.CRACKED_NETHER_BRICKS,
                Blocks.CHISELED_NETHER_BRICKS,
                Blocks.RED_NETHER_BRICKS,
                Blocks.RED_NETHER_BRICK_STAIRS,
                Blocks.RED_NETHER_BRICK_SLAB,
                Blocks.CRIMSON_TRAPDOOR,
                // Wither Forts
                ModBlocks.COBBLED_BLACKSTONE.value(),
                ModBlocks.WITHERED_BLACKSTONE.value(),
                ModBlocks.CHISELED_WITHERED_BLACKSTONE.value(),
                ModBlocks.CRACKED_WITHERED_BLACKSTONE.value(),
                ModBlocks.WITHERED_DEBRIS.value(),
                Blocks.IRON_BARS,
                Blocks.COAL_BLOCK).build();
    }
}
