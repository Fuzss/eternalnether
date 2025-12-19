package fuzs.eternalnether;

import fuzs.eternalnether.init.ModEntityTypes;
import fuzs.eternalnether.init.ModFeatures;
import fuzs.eternalnether.init.ModRegistry;
import fuzs.eternalnether.world.entity.animal.horse.WitherSkeletonHorse;
import fuzs.eternalnether.world.entity.monster.*;
import fuzs.eternalnether.world.entity.monster.piglin.GoalBasedPiglin;
import fuzs.eternalnether.world.entity.monster.piglin.PiglinPrisoner;
import fuzs.eternalnether.world.entity.projectile.ThrownWarpedEnderpearl;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingContext;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingPhase;
import fuzs.puzzleslib.api.biome.v1.BiomeModificationContext;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.BiomeModificationsContext;
import fuzs.puzzleslib.api.core.v1.context.EntityAttributesContext;
import fuzs.puzzleslib.api.core.v1.context.SpawnPlacementsContext;
import fuzs.puzzleslib.api.event.v1.entity.EnderPearlTeleportCallback;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EternalNether implements ModConstructor {
    public static final String MOD_ID = "eternalnether";
    public static final String MOD_NAME = "Eternal Nether";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ModRegistry.boostrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        EnderPearlTeleportCallback.EVENT.register(ThrownWarpedEnderpearl::onEnderPearlTeleport);
    }

    @Override
    public void onCommonSetup() {
        ModFeatures.setBasaltFeatureRestrictions();
        ModEntityTypes.setPiglinBruteSensorsAndMemories();
    }

    @Override
    public void onRegisterBiomeModifications(BiomeModificationsContext context) {
        context.registerBiomeModification(BiomeLoadingPhase.ADDITIONS,
                (BiomeLoadingContext biomeLoadingContext) -> biomeLoadingContext.is(Biomes.SOUL_SAND_VALLEY),
                (BiomeModificationContext biomeModificationContext) -> {
                    biomeModificationContext.generationSettings()
                            .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION,
                                    ModRegistry.SOUL_STONE_BLOBS_PLACED_FEATURE);
                });
    }

    @Override
    public void onRegisterEntityAttributes(EntityAttributesContext context) {
        context.registerAttributes(ModEntityTypes.PIGLIN_PRISONER.value(), PiglinPrisoner.createAttributes());
        context.registerAttributes(ModEntityTypes.PIGLIN_HUNTER.value(), PiglinBrute.createAttributes());
        context.registerAttributes(ModEntityTypes.WEX.value(), Wex.createAttributes());
        context.registerAttributes(ModEntityTypes.WARPED_ENDERMAN.value(), WarpedEnderman.createAttributes());
        context.registerAttributes(ModEntityTypes.WRAITHER.value(), Wraither.createAttributes());
        context.registerAttributes(ModEntityTypes.WITHER_SKELETON_KNIGHT.value(),
                WitherSkeletonKnight.createAttributes());
        context.registerAttributes(ModEntityTypes.CORPOR.value(), Corpor.createAttributes());
        context.registerAttributes(ModEntityTypes.WITHER_SKELETON_HORSE.value(),
                WitherSkeletonHorse.createAttributes());
    }

    @Override
    public void onRegisterSpawnPlacements(SpawnPlacementsContext context) {
        context.registerSpawnPlacement(ModEntityTypes.PIGLIN_PRISONER.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                GoalBasedPiglin::checkPiglinSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.PIGLIN_HUNTER.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                GoalBasedPiglin::checkPiglinSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.WEX.value(),
                SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.WARPED_ENDERMAN.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.WRAITHER.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.WITHER_SKELETON_KNIGHT.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.CORPOR.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.WITHER_SKELETON_HORSE.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SkeletonHorse::checkSkeletonHorseSpawnRules);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
