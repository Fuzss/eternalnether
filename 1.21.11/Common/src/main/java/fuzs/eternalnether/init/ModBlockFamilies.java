package fuzs.eternalnether.init;

import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;

import java.util.stream.Stream;

public final class ModBlockFamilies {
    public static final BlockFamily WITHERED_BLACKSTONE_FAMILY = BlockFamilies.familyBuilder(ModBlocks.WITHERED_BLACKSTONE.value())
            .slab(ModBlocks.WITHERED_BLACKSTONE_SLAB.value())
            .stairs(ModBlocks.WITHERED_BLACKSTONE_STAIRS.value())
            .wall(ModBlocks.WITHERED_BLACKSTONE_WALL.value())
            .cracked(ModBlocks.CRACKED_WITHERED_BLACKSTONE.value())
            .chiseled(ModBlocks.CHISELED_WITHERED_BLACKSTONE.value())
            .dontGenerateModel()
            .getFamily();
    public static final BlockFamily CRACKED_WITHERED_BLACKSTONE_FAMILY = BlockFamilies.familyBuilder(ModBlocks.CRACKED_WITHERED_BLACKSTONE.value())
            .slab(ModBlocks.CRACKED_WITHERED_BLACKSTONE_SLAB.value())
            .stairs(ModBlocks.CRACKED_WITHERED_BLACKSTONE_STAIRS.value())
            .wall(ModBlocks.CRACKED_WITHERED_BLACKSTONE_WALL.value())
            .getFamily();
    public static final BlockFamily WARPED_NETHER_BRICKS_FAMILY = BlockFamilies.familyBuilder(ModBlocks.WARPED_NETHER_BRICKS.value())
            .slab(ModBlocks.WARPED_NETHER_BRICK_SLAB.value())
            .stairs(ModBlocks.WARPED_NETHER_BRICK_STAIRS.value())
            .wall(ModBlocks.WARPED_NETHER_BRICK_WALL.value())
            .chiseled(ModBlocks.CHISELED_WARPED_NETHER_BRICKS.value())
            .getFamily();

    public static Stream<BlockFamily> getAllFamilies() {
        return Stream.of(WITHERED_BLACKSTONE_FAMILY, CRACKED_WITHERED_BLACKSTONE_FAMILY, WARPED_NETHER_BRICKS_FAMILY);
    }
}
