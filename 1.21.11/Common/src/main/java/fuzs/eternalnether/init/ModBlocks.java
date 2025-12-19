package fuzs.eternalnether.init;

import fuzs.eternalnether.world.level.block.NetheriteBellBlock;
import fuzs.eternalnether.world.level.block.entity.NetheriteBellBlockEntity;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class ModBlocks {
    public static final Holder.Reference<Block> COBBLED_BLACKSTONE = ModRegistry.REGISTRIES.registerBlock(
            "cobbled_blackstone",
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE).strength(2.0F, 6.0F));

    public static final Holder.Reference<Block> WITHERED_BLACKSTONE = ModRegistry.REGISTRIES.registerBlock(
            "withered_blackstone",
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE)
                    .strength(50.0F, 1200.0F)
                    .sound(SoundType.DEEPSLATE));
    public static final Holder.Reference<Block> WITHERED_BLACKSTONE_STAIRS = ModRegistry.REGISTRIES.registerBlock(
            "withered_blackstone_stairs",
            (BlockBehaviour.Properties properties) -> new StairBlock(WITHERED_BLACKSTONE.value().defaultBlockState(),
                    properties),
            () -> BlockBehaviour.Properties.ofLegacyCopy(WITHERED_BLACKSTONE.value()));
    public static final Holder.Reference<Block> WITHERED_BLACKSTONE_SLAB = ModRegistry.REGISTRIES.registerBlock(
            "withered_blackstone_slab",
            SlabBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(WITHERED_BLACKSTONE.value()));
    public static final Holder.Reference<Block> WITHERED_BLACKSTONE_WALL = ModRegistry.REGISTRIES.registerBlock(
            "withered_blackstone_wall",
            WallBlock::new,
            () -> BlockBehaviour.Properties.ofLegacyCopy(WITHERED_BLACKSTONE.value()).forceSolidOn());
    public static final Holder.Reference<Block> CRACKED_WITHERED_BLACKSTONE = ModRegistry.REGISTRIES.registerBlock(
            "cracked_withered_blackstone",
            () -> BlockBehaviour.Properties.ofFullCopy(WITHERED_BLACKSTONE.value()));
    public static final Holder.Reference<Block> CRACKED_WITHERED_BLACKSTONE_STAIRS = ModRegistry.REGISTRIES.registerBlock(
            "cracked_withered_blackstone_stairs",
            (BlockBehaviour.Properties properties) -> new StairBlock(WITHERED_BLACKSTONE.value().defaultBlockState(),
                    properties),
            () -> BlockBehaviour.Properties.ofLegacyCopy(WITHERED_BLACKSTONE.value()));
    public static final Holder.Reference<Block> CRACKED_WITHERED_BLACKSTONE_SLAB = ModRegistry.REGISTRIES.registerBlock(
            "cracked_withered_blackstone_slab",
            SlabBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(WITHERED_BLACKSTONE.value()));
    public static final Holder.Reference<Block> CRACKED_WITHERED_BLACKSTONE_WALL = ModRegistry.REGISTRIES.registerBlock(
            "cracked_withered_blackstone_wall",
            WallBlock::new,
            () -> BlockBehaviour.Properties.ofLegacyCopy(WITHERED_BLACKSTONE.value()).forceSolidOn());
    public static final Holder.Reference<Block> CHISELED_WITHERED_BLACKSTONE = ModRegistry.REGISTRIES.registerBlock(
            "chiseled_withered_blackstone",
            () -> BlockBehaviour.Properties.ofFullCopy(WITHERED_BLACKSTONE.value()));

    public static final Holder.Reference<Block> WITHERED_BASALT = ModRegistry.REGISTRIES.registerBlock("withered_basalt",
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.BASALT).strength(50.0F, 1200.0F));
    public static final Holder.Reference<Block> WITHERED_COAL_BLOCK = ModRegistry.REGISTRIES.registerBlock(
            "withered_coal_block",
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_BLOCK).strength(50.0F, 1200.0F));
    public static final Holder.Reference<Block> WITHERED_QUARTZ_BLOCK = ModRegistry.REGISTRIES.registerBlock(
            "withered_quartz_block",
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK).strength(50.0F, 1200.0F));
    public static final Holder.Reference<Block> WITHERED_DEBRIS = ModRegistry.REGISTRIES.registerBlock("withered_debris",
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ANCIENT_DEBRIS).strength(50.0F, 1200.0F));

    public static final Holder.Reference<Block> SOUL_STONE = ModRegistry.REGISTRIES.registerBlock("soul_stone",
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F));
    public static final Holder.Reference<Block> WITHERED_BONE_BLOCK = ModRegistry.REGISTRIES.registerBlock(
            "withered_bone_block",
            RotatedPillarBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.BONE_BLOCK).mapColor(MapColor.COLOR_BLACK));

    public static final Holder.Reference<Block> WARPED_NETHER_BRICKS = ModRegistry.REGISTRIES.registerBlock(
            "warped_nether_bricks",
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS).mapColor(MapColor.WARPED_STEM));
    public static final Holder.Reference<Block> WARPED_NETHER_BRICK_STAIRS = ModRegistry.REGISTRIES.registerBlock(
            "warped_nether_brick_stairs",
            (BlockBehaviour.Properties properties) -> new StairBlock(WARPED_NETHER_BRICKS.value().defaultBlockState(),
                    properties),
            () -> BlockBehaviour.Properties.ofFullCopy(WARPED_NETHER_BRICKS.value()));
    public static final Holder.Reference<Block> WARPED_NETHER_BRICK_SLAB = ModRegistry.REGISTRIES.registerBlock(
            "warped_nether_brick_slab",
            SlabBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(WARPED_NETHER_BRICKS.value()));
    public static final Holder.Reference<Block> WARPED_NETHER_BRICK_WALL = ModRegistry.REGISTRIES.registerBlock(
            "warped_nether_brick_wall",
            WallBlock::new,
            () -> BlockBehaviour.Properties.ofLegacyCopy(WARPED_NETHER_BRICKS.value()).forceSolidOn());
    public static final Holder.Reference<Block> CHISELED_WARPED_NETHER_BRICKS = ModRegistry.REGISTRIES.registerBlock(
            "chiseled_warped_nether_bricks",
            () -> BlockBehaviour.Properties.ofFullCopy(WARPED_NETHER_BRICKS.value()));

    public static final Holder.Reference<Block> NETHERITE_BELL = ModRegistry.REGISTRIES.registerBlock("netherite_bell",
            NetheriteBellBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(50.0F, 1200.0F)
                    .sound(SoundType.ANVIL)
                    .forceSolidOn()
                    .pushReaction(PushReaction.DESTROY));

    public static final Holder.Reference<BlockEntityType<NetheriteBellBlockEntity>> NETHERITE_BELL_BLOCK_ENTITY_TYPE = ModRegistry.REGISTRIES.registerBlockEntityType(
            "netherite_bell",
            NetheriteBellBlockEntity::new,
            NETHERITE_BELL);

    public static void boostrap() {
        // NO-OP
    }
}
