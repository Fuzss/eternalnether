package fuzs.eternalnether.init;

import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

import java.util.List;
import java.util.Optional;

public final class ModStructureSets {
    public static final ResourceKey<StructureSet> CATACOMB_STRUCTURE_SET = ModRegistry.REGISTRIES.makeResourceKey(
            Registries.STRUCTURE_SET,
            "catacomb");
    public static final ResourceKey<StructureSet> CITADEL_STRUCTURE_SET = ModRegistry.REGISTRIES.makeResourceKey(
            Registries.STRUCTURE_SET,
            "citadel");
    public static final ResourceKey<StructureSet> PIGLIN_MANOR_STRUCTURE_SET = ModRegistry.REGISTRIES.makeResourceKey(
            Registries.STRUCTURE_SET,
            "piglin_manor");

    public static void bootstrapStructureSets(BootstrapContext<StructureSet> context) {
        context.register(CATACOMB_STRUCTURE_SET,
                new StructureSet(List.of(StructureSet.entry(context.lookup(Registries.STRUCTURE)
                        .getOrThrow(ModStructures.CATACOMB_STRUCTURE))),
                        new RandomSpreadStructurePlacement(Vec3i.ZERO,
                                StructurePlacement.FrequencyReductionMethod.DEFAULT,
                                1.0F,
                                1163018812,
                                Optional.of(new StructurePlacement.ExclusionZone(context.lookup(Registries.STRUCTURE_SET)
                                        .getOrThrow(BuiltinStructureSets.NETHER_COMPLEXES), 8)),
                                12,
                                4,
                                RandomSpreadType.LINEAR)));
        context.register(CITADEL_STRUCTURE_SET,
                new StructureSet(List.of(StructureSet.entry(context.lookup(Registries.STRUCTURE)
                        .getOrThrow(ModStructures.CITADEL_STRUCTURE))),
                        new RandomSpreadStructurePlacement(Vec3i.ZERO,
                                StructurePlacement.FrequencyReductionMethod.DEFAULT,
                                1.0F,
                                1621815507,
                                Optional.of(new StructurePlacement.ExclusionZone(context.lookup(Registries.STRUCTURE_SET)
                                        .getOrThrow(BuiltinStructureSets.NETHER_COMPLEXES), 4)),
                                12,
                                4,
                                RandomSpreadType.LINEAR)));
        context.register(PIGLIN_MANOR_STRUCTURE_SET,
                new StructureSet(List.of(StructureSet.entry(context.lookup(Registries.STRUCTURE)
                        .getOrThrow(ModStructures.PIGLIN_MANOR_STRUCTURE))),
                        new RandomSpreadStructurePlacement(Vec3i.ZERO,
                                StructurePlacement.FrequencyReductionMethod.DEFAULT,
                                1.0F,
                                292421824,
                                Optional.of(new StructurePlacement.ExclusionZone(context.lookup(Registries.STRUCTURE_SET)
                                        .getOrThrow(BuiltinStructureSets.NETHER_COMPLEXES), 6)),
                                12,
                                4,
                                RandomSpreadType.LINEAR)));
    }
}
