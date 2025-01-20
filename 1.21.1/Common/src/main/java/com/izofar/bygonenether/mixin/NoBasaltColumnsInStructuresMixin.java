package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.init.ModRegistry;
import fuzs.eternalnether.EternalNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.BasaltColumnsFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BasaltColumnsFeature.class)
public class NoBasaltColumnsInStructuresMixin {

    @Inject(
            method = "canPlaceAt(Lnet/minecraft/world/level/LevelAccessor;ILnet/minecraft/core/BlockPos$MutableBlockPos;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void bygonenether_noBasaltColumnsInStructures(LevelAccessor levelAccessor, int seaLevel, BlockPos.MutableBlockPos mutableBlockPos, CallbackInfoReturnable<Boolean> cir) {
        if (!(levelAccessor instanceof WorldGenRegion)) {
            return;
        }

        SectionPos sectionPos = SectionPos.of(mutableBlockPos);
        if (!levelAccessor.getChunk(sectionPos.x(), sectionPos.z())
                .getStatus()
                .isOrAfter(ChunkStatus.STRUCTURE_REFERENCES)) {
            EternalNether.LOGGER.warn(
                    "Bygone Nether: Detected a mod with a broken basalt columns configuredfeature that is trying to place blocks outside the 3x3 safe chunk area for features. Find the broken mod and report to them to fix the placement of their basalt columns feature.");
            return;
        }
        Registry<Structure> configuredStructureFeatureRegistry = levelAccessor.registryAccess()
                .registryOrThrow(Registries.STRUCTURE);
        StructureManager structureFeatureManager = ((WorldGenRegionAccessor) levelAccessor).getStructureFeatureManager();
        for (Holder<Structure> configuredStructureFeature : configuredStructureFeatureRegistry.getOrCreateTag(
                ModRegistry.NO_BASALT_STRUCTURE_TAG_KEY)) {
            if (structureFeatureManager.getStructureAt(mutableBlockPos, configuredStructureFeature.value()).isValid()) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}
