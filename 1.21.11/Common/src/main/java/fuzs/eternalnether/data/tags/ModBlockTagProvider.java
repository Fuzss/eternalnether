package fuzs.eternalnether.data.tags;

import fuzs.eternalnether.init.ModBlocks;
import fuzs.eternalnether.init.ModTags;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

public class ModBlockTagProvider extends AbstractTagProvider<Block> {

    public ModBlockTagProvider(DataProviderContext context) {
        super(Registries.BLOCK, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.WITHERED_BLOCK_TAG_KEY)
                .add(ModBlocks.WITHERED_BLACKSTONE.value(),
                        ModBlocks.WITHERED_BLACKSTONE_STAIRS.value(),
                        ModBlocks.WITHERED_BLACKSTONE_SLAB.value(),
                        ModBlocks.WITHERED_BLACKSTONE_WALL.value(),
                        ModBlocks.CHISELED_WITHERED_BLACKSTONE.value(),
                        ModBlocks.CRACKED_WITHERED_BLACKSTONE.value(),
                        ModBlocks.CRACKED_WITHERED_BLACKSTONE_STAIRS.value(),
                        ModBlocks.CRACKED_WITHERED_BLACKSTONE_SLAB.value(),
                        ModBlocks.CRACKED_WITHERED_BLACKSTONE_WALL.value(),
                        ModBlocks.WITHERED_BASALT.value(),
                        ModBlocks.WITHERED_COAL_BLOCK.value(),
                        ModBlocks.WITHERED_QUARTZ_BLOCK.value(),
                        ModBlocks.WITHERED_DEBRIS.value());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.COBBLED_BLACKSTONE.value(),
                        ModBlocks.SOUL_STONE.value(),
                        ModBlocks.WITHERED_BONE_BLOCK.value(),
                        ModBlocks.WARPED_NETHER_BRICKS.value(),
                        ModBlocks.WARPED_NETHER_BRICK_STAIRS.value(),
                        ModBlocks.WARPED_NETHER_BRICK_SLAB.value(),
                        ModBlocks.CHISELED_WARPED_NETHER_BRICKS.value())
                .addTag(ModTags.WITHERED_BLOCK_TAG_KEY);
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).addTag(ModTags.WITHERED_BLOCK_TAG_KEY);
        this.tag(BlockTags.WITHER_SUMMON_BASE_BLOCKS).add(ModBlocks.SOUL_STONE);
    }
}
