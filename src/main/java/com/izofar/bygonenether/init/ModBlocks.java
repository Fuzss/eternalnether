package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.block.NetheriteBellBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ModBlocks {

	public static final DeferredRegister<Block> MODDED_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BygoneNetherMod.MODID);

	public static final RegistryObject<Block> COBBLED_BLACKSTONE = MODDED_BLOCKS.register("cobbled_blackstone", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
	public static final RegistryObject<Block> WITHERED_BLACKSTONE = MODDED_BLOCKS.register("withered_blackstone", () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 1200.0F).sound(SoundType.NETHER_BRICKS)));
	public static final RegistryObject<Block> WITHERED_BLACKSTONE_STAIRS = MODDED_BLOCKS.register("withered_blackstone_stairs", () -> new StairsBlock(() -> WITHERED_BLACKSTONE.get().defaultBlockState(), AbstractBlock.Properties.copy(WITHERED_BLACKSTONE.get())));
	public static final RegistryObject<Block> WITHERED_BLACKSTONE_SLAB = MODDED_BLOCKS.register("withered_blackstone_slab",  () -> new SlabBlock(AbstractBlock.Properties.copy(WITHERED_BLACKSTONE.get())));
	public static final RegistryObject<Block> CRACKED_WITHERED_BLACKSTONE = MODDED_BLOCKS.register("cracked_withered_blackstone", () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 1200.0F).sound(SoundType.NETHER_BRICKS)));
	public static final RegistryObject<Block> CRACKED_WITHERED_BLACKSTONE_STAIRS = MODDED_BLOCKS.register("cracked_withered_blackstone_stairs", () -> new StairsBlock(() -> WITHERED_BLACKSTONE.get().defaultBlockState(), AbstractBlock.Properties.copy(WITHERED_BLACKSTONE.get())));
	public static final RegistryObject<Block> CRACKED_WITHERED_BLACKSTONE_SLAB = MODDED_BLOCKS.register("cracked_withered_blackstone_slab",  () -> new SlabBlock(AbstractBlock.Properties.copy(WITHERED_BLACKSTONE.get())));
	public static final RegistryObject<Block> CHISELED_WITHERED_BLACKSTONE = MODDED_BLOCKS.register("chiseled_withered_blackstone", () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 1200.0F).sound(SoundType.NETHER_BRICKS)));
	public static final RegistryObject<Block> WITHERED_BASALT = MODDED_BLOCKS.register("withered_basalt", () -> new Block(AbstractBlock.Properties.copy(Blocks.BASALT)));
	public static final RegistryObject<Block> WITHERED_COAL_BLOCK = MODDED_BLOCKS.register("withered_coal_block", () -> new Block(AbstractBlock.Properties.copy(Blocks.COAL_BLOCK)));
	public static final RegistryObject<Block> WITHERED_QUARTZ_BLOCK = MODDED_BLOCKS.register("withered_quartz_block", () -> new Block(AbstractBlock.Properties.copy(Blocks.QUARTZ_BLOCK)));
	public static final RegistryObject<Block> WITHERED_DEBRIS = MODDED_BLOCKS.register("withered_debris", () -> new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(30.0F, 1200.0F).sound(SoundType.ANCIENT_DEBRIS)));
	public static final RegistryObject<Block> SOUL_STONE = MODDED_BLOCKS.register("soul_stone", () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	
	public static final RegistryObject<Block> WARPED_NETHER_BRICKS = MODDED_BLOCKS.register("warped_nether_bricks", () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.NETHER).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));
	public static final RegistryObject<Block> CHISELED_WARPED_NETHER_BRICKS = MODDED_BLOCKS.register("chiseled_warped_nether_bricks", () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.NETHER).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));
	public static final RegistryObject<Block> WARPED_NETHER_BRICK_STAIRS = MODDED_BLOCKS.register("warped_nether_brick_stairs", () -> new StairsBlock(() -> WARPED_NETHER_BRICKS.get().defaultBlockState(), AbstractBlock.Properties.copy(WARPED_NETHER_BRICKS.get())));
	public static final RegistryObject<Block> WARPED_NETHER_BRICK_SLAB = MODDED_BLOCKS.register("warped_nether_brick_slab",  () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.NETHER).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));
	public static final RegistryObject<Block> NETHERITE_BELL = MODDED_BLOCKS.register("netherite_bell", () -> new NetheriteBellBlock(AbstractBlock.Properties.of(Material.METAL, MaterialColor.NETHER).strength(50.0F).sound(SoundType.ANVIL)));

	public static void register(IEventBus eventBus) { MODDED_BLOCKS.register(eventBus); }

}
