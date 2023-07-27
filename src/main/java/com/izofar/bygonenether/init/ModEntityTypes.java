package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.entity.*;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ModEntityTypes {

	public static final DeferredRegister<EntityType<?>> MOD_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, BygoneNetherMod.MODID);
	public static final DeferredRegister<TileEntityType<?>> MOD_TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BygoneNetherMod.MODID);

	public static final RegistryObject<EntityType<WexEntity>> WEX = MOD_ENTITY_TYPES.register("wex", () -> EntityType.Builder.of(WexEntity::new, EntityClassification.MONSTER).fireImmune().sized(0.4F, 0.8F).clientTrackingRange(8).build(new ResourceLocation(BygoneNetherMod.MODID, "wex").toString()));
	public static final RegistryObject<EntityType<WarpedEndermanEntity>> WARPED_ENDERMAN = MOD_ENTITY_TYPES.register("warped_enderman", () -> EntityType.Builder.of(WarpedEndermanEntity::new, EntityClassification.MONSTER).fireImmune().sized(0.6F, 2.9F).clientTrackingRange(8).build(new ResourceLocation(BygoneNetherMod.MODID, "warped_enderman").toString()));
	
	public static final RegistryObject<EntityType<PiglinPrisonerEntity>> PIGLIN_PRISONER = MOD_ENTITY_TYPES.register("piglin_prisoner", () -> EntityType.Builder.of(PiglinPrisonerEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(new ResourceLocation(BygoneNetherMod.MODID, "piglin_prisoner").toString()));
	public static final RegistryObject<EntityType<PiglinHunterEntity>> PIGLIN_HUNTER = MOD_ENTITY_TYPES.register("piglin_hunter", () -> EntityType.Builder.of(PiglinHunterEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(new ResourceLocation(BygoneNetherMod.MODID, "piglin_hunter").toString()));

	public static final RegistryObject<EntityType<WraitherEntity>> WRAITHER = MOD_ENTITY_TYPES.register("wraither", () -> EntityType.Builder.of(WraitherEntity::new, EntityClassification.MONSTER).fireImmune().immuneTo(Blocks.WITHER_ROSE).sized(0.7F, 2.4F).clientTrackingRange(8).build(new ResourceLocation(BygoneNetherMod.MODID, "wraither").toString()));
	public static final RegistryObject<EntityType<WitherSkeletonKnightEntity>> WITHER_SKELETON_KNIGHT = MOD_ENTITY_TYPES.register("wither_skeleton_knight", () -> EntityType.Builder.of(WitherSkeletonKnightEntity::new, EntityClassification.MONSTER).fireImmune().immuneTo(Blocks.WITHER_ROSE).sized(0.7F, 2.4F).clientTrackingRange(8).build(new ResourceLocation(BygoneNetherMod.MODID, "wither_skeleton_knight").toString()));
	public static final RegistryObject<EntityType<CorporEntity>> CORPOR = MOD_ENTITY_TYPES.register("corpor", () -> EntityType.Builder.of(CorporEntity::new, EntityClassification.MONSTER).fireImmune().immuneTo(Blocks.WITHER_ROSE).sized(0.7F, 2.4F).clientTrackingRange(8).build(new ResourceLocation(BygoneNetherMod.MODID, "corpor").toString()));

	public static final RegistryObject<EntityType<WitherSkeletonHorseEntity>> WITHER_SKELETON_HORSE = MOD_ENTITY_TYPES.register("wither_skeleton_horse", () -> EntityType.Builder.of(WitherSkeletonHorseEntity::new, EntityClassification.CREATURE).fireImmune().sized(1.3964844F, 1.6F).clientTrackingRange(10).build(new ResourceLocation(BygoneNetherMod.MODID, "wither_skeleton_horse").toString()));

	public static final RegistryObject<EntityType<ThrownWarpedEnderpearlEntity>> WARPED_ENDER_PEARL = MOD_ENTITY_TYPES.register("warped_ender_pearl", () -> EntityType.Builder.<ThrownWarpedEnderpearlEntity>of(ThrownWarpedEnderpearlEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build(new ResourceLocation(BygoneNetherMod.MODID, "warped_ender_pearl").toString()));

	public static final RegistryObject<TileEntityType<NetheriteBellTileEntity>> NETHERITE_BELL = MOD_TILE_ENTITY_TYPES.register("netherite_bell", () -> TileEntityType.Builder.of(NetheriteBellTileEntity::new, ModBlocks.NETHERITE_BELL.get()).build(Util.fetchChoiceType(TypeReferences.BLOCK_ENTITY, new ResourceLocation(BygoneNetherMod.MODID, "netherite_bell").toString())));

	public static void register(IEventBus eventBus) {
		MOD_ENTITY_TYPES.register(eventBus);
		MOD_TILE_ENTITY_TYPES.register(eventBus);
	}

}