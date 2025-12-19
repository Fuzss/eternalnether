package fuzs.eternalnether.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.Structure;

public final class ModStructureUtils {

    private ModStructureUtils() {
        // NO-OP
    }

    public static boolean isLavaLake(NoiseColumn blockReader) {
        boolean isLake = true;
        if (!blockReader.getBlock(31).is(Blocks.LAVA)) {
            isLake = false;
        } else {
            for (int i = 32; i < 70; i++) {
                isLake &= blockReader.getBlock(i).isAir();
            }
        }
        return isLake;
    }

    public static boolean isBuried(NoiseColumn blockReader, int minHeight, int maxHeight) {
        for (int i = minHeight; i < maxHeight; i++) {
            if (blockReader.getBlock(i + 1).isAir() && !blockReader.getBlock(i).isAir()) {
                return false;
            }
        }
        return true;
    }

    public static boolean verticalSpace(NoiseColumn blockReader, int minHeight, int maxHeight, int structureHeight) {
        int newHeight = 0;
        for (int i = maxHeight; i >= minHeight && newHeight < structureHeight; i--) {
            if (blockReader.getBlock(i).isAir()) {
                newHeight++;
            } else {
                newHeight = 0;
            }
        }
        return newHeight == structureHeight;
    }

    public static BlockPos getElevation(Structure.GenerationContext context, int minHeight, int maxHeight) {
        BlockPos blockPos = context.chunkPos().getMiddleBlockPosition(0);
        NoiseColumn blockReader = context.chunkGenerator()
                .getBaseColumn(blockPos.getX(), blockPos.getZ(), context.heightAccessor(), context.randomState());

        for (int i = minHeight; i < maxHeight; i++) {
            if (blockReader.getBlock(i + 1).isAir() && !blockReader.getBlock(i).isAir()) {
                return new BlockPos(blockPos.getX(), i + 1, blockPos.getZ());
            }
        }

        return new BlockPos(blockPos.getX(),
                context.random().nextInt(maxHeight - minHeight) + minHeight,
                blockPos.getZ());
    }

    public static int getScaledNetherHeight(Structure.GenerationContext context, int maxHeight) {
        return (int) (maxHeight / 128.0F * context.chunkGenerator().getGenDepth());
    }
}
