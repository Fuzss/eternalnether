package fuzs.eternalnether.world.level.levelgen.structure;

import com.mojang.serialization.MapCodec;
import fuzs.eternalnether.init.ModRegistry;
import fuzs.eternalnether.util.ModStructureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class PiglinManorStructure extends JigsawStructure {
    public static final MapCodec<PiglinManorStructure> CODEC = JigsawStructure.CODEC.xmap(PiglinManorStructure::new,
            Function.identity());

    private PiglinManorStructure(JigsawStructure structure) {
        super(new StructureSettings(structure.biomes(),
                        structure.spawnOverrides(),
                        structure.step(),
                        structure.terrainAdaptation()),
                structure.startPool,
                structure.startJigsawName,
                structure.maxDepth,
                structure.startHeight,
                structure.useExpansionHack,
                structure.projectStartToHeightmap,
                structure.maxDistanceFromCenter,
                structure.poolAliases,
                structure.dimensionPadding,
                structure.liquidSettings);
    }

    public PiglinManorStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int maxDepth, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, JigsawStructure.MaxDistance maxDistanceFromCenter) {
        super(settings,
                startPool,
                startJigsawName,
                maxDepth,
                startHeight,
                false,
                projectStartToHeightmap,
                maxDistanceFromCenter,
                List.of(),
                JigsawStructure.DEFAULT_DIMENSION_PADDING,
                JigsawStructure.DEFAULT_LIQUID_SETTINGS);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        if (this.isLocationValid(context)) {
            BlockPos blockPos = ModStructureUtils.getElevation(context,
                    34,
                    ModStructureUtils.getScaledNetherHeight(context, 72));
            return JigsawPlacement.addPieces(context,
                    this.startPool,
                    this.startJigsawName,
                    this.maxDepth,
                    blockPos,
                    this.useExpansionHack,
                    this.projectStartToHeightmap,
                    this.maxDistanceFromCenter,
                    PoolAliasLookup.create(this.poolAliases, blockPos, context.seed()),
                    this.dimensionPadding,
                    this.liquidSettings);
        } else {
            return Optional.empty();
        }
    }

    private boolean isLocationValid(Structure.GenerationContext context) {
        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);
        NoiseColumn blockReader = context.chunkGenerator()
                .getBaseColumn(blockpos.getX(), blockpos.getZ(), context.heightAccessor(), context.randomState());
        return this.isChunkValid(context) && !ModStructureUtils.isLavaLake(blockReader)
                && ModStructureUtils.verticalSpace(blockReader,
                34,
                ModStructureUtils.getScaledNetherHeight(context, 72),
                24);
    }

    private boolean isChunkValid(Structure.GenerationContext context) {
        return context.validBiome()
                .test(context.chunkGenerator()
                        .getBiomeSource()
                        .getNoiseBiome(QuartPos.fromBlock(context.chunkPos().getMiddleBlockX()),
                                QuartPos.fromBlock(64),
                                QuartPos.fromBlock(context.chunkPos().getMiddleBlockZ()),
                                context.randomState().sampler()));
    }

    @Override
    public StructureType<?> type() {
        return ModRegistry.PIGLIN_MANOR_STRUCTURE_TYPE.value();
    }
}
