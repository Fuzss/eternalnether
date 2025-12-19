package fuzs.eternalnether.init;

import fuzs.eternalnether.world.level.levelgen.structure.CatacombStructure;
import fuzs.eternalnether.world.level.levelgen.structure.CitadelStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Map;
import java.util.Optional;

public final class ModStructures {
    public static final ResourceKey<Structure> CATACOMB_STRUCTURE = ModRegistry.REGISTRIES.makeResourceKey(Registries.STRUCTURE,
            "catacomb");
    public static final ResourceKey<Structure> CITADEL_STRUCTURE = ModRegistry.REGISTRIES.makeResourceKey(Registries.STRUCTURE,
            "citadel");
    public static final ResourceKey<Structure> PIGLIN_MANOR_STRUCTURE = ModRegistry.REGISTRIES.makeResourceKey(
            Registries.STRUCTURE,
            "piglin_manor");
    public static final ResourceKey<StructureTemplatePool> CATACOMB_START_POOL = ModRegistry.REGISTRIES.makeResourceKey(
            Registries.TEMPLATE_POOL,
            "catacomb/start_pool");
    public static final ResourceKey<StructureTemplatePool> CITADEL_START_POOL = ModRegistry.REGISTRIES.makeResourceKey(
            Registries.TEMPLATE_POOL,
            "citadel/start_pool");
    public static final ResourceKey<StructureTemplatePool> PIGLIN_MANOR_START_POOL = ModRegistry.REGISTRIES.makeResourceKey(
            Registries.TEMPLATE_POOL,
            "piglin_manor/start_pool");

    public static void bootstrapStructures(BootstrapContext<Structure> context) {
        context.register(CATACOMB_STRUCTURE,
                new CatacombStructure(new Structure.StructureSettings.Builder(context.lookup(Registries.BIOME)
                        .getOrThrow(ModTags.HAS_CATACOMB_BIOME_TAG_KEY)).spawnOverrides(Map.of(MobCategory.MONSTER,
                                new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE,
                                        WeightedList.of(new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 1, 1)))))
                        .generationStep(GenerationStep.Decoration.UNDERGROUND_DECORATION)
                        .build(),
                        context.lookup(Registries.TEMPLATE_POOL).getOrThrow(CATACOMB_START_POOL),
                        Optional.empty(),
                        3,
                        UniformHeight.of(VerticalAnchor.absolute(56), VerticalAnchor.absolute(84)),
                        Optional.empty(),
                        new JigsawStructure.MaxDistance(128)));
        context.register(CITADEL_STRUCTURE,
                new CitadelStructure(new Structure.StructureSettings.Builder(context.lookup(Registries.BIOME)
                        .getOrThrow(ModTags.HAS_CITADEL_BIOME_TAG_KEY)).spawnOverrides(Map.of(MobCategory.MONSTER,
                                new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE,
                                        WeightedList.of(new Weighted<>(new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN,
                                                        1,
                                                        1), 1),
                                                new Weighted<>(new MobSpawnSettings.SpawnerData(ModEntityTypes.WARPED_ENDERMAN.value(),
                                                        1,
                                                        1), 1)))))
                        .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                        .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                        .build(),
                        context.lookup(Registries.TEMPLATE_POOL).getOrThrow(CITADEL_START_POOL),
                        Optional.empty(),
                        4,
                        UniformHeight.of(VerticalAnchor.absolute(48), VerticalAnchor.absolute(70)),
                        Optional.empty(),
                        new JigsawStructure.MaxDistance(116)));
        context.register(PIGLIN_MANOR_STRUCTURE,
                new CitadelStructure(new Structure.StructureSettings.Builder(context.lookup(Registries.BIOME)
                        .getOrThrow(ModTags.HAS_PIGLIN_MANOR_BIOME_TAG_KEY)).spawnOverrides(Map.of(MobCategory.MONSTER,
                                new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE,
                                        WeightedList.of(new Weighted<>(new MobSpawnSettings.SpawnerData(EntityType.PIGLIN,
                                                        1,
                                                        1), 2),
                                                new Weighted<>(new MobSpawnSettings.SpawnerData(ModEntityTypes.PIGLIN_HUNTER.value(),
                                                        1,
                                                        1), 1)))))
                        .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                        .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                        .build(),
                        context.lookup(Registries.TEMPLATE_POOL).getOrThrow(PIGLIN_MANOR_START_POOL),
                        Optional.empty(),
                        1,
                        UniformHeight.of(VerticalAnchor.absolute(34), VerticalAnchor.absolute(72)),
                        Optional.empty(),
                        new JigsawStructure.MaxDistance(116)));
    }
}
