package fuzs.eternalnether.init;

import fuzs.eternalnether.world.item.WarpedEnderpearlItem;
import fuzs.eternalnether.world.item.WitheredBoneMealItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.List;
import java.util.Optional;

public final class ModItems {
    public static final Holder.Reference<Item> COBBLED_BLACKSTONE = ModRegistry.REGISTRIES.registerBlockItem(ModBlocks.COBBLED_BLACKSTONE);

    public static final Holder.Reference<Item> WITHERED_BLACKSTONE = ModRegistry.REGISTRIES.registerBlockItem(ModBlocks.WITHERED_BLACKSTONE);
    public static final Holder.Reference<Item> WITHERED_BLACKSTONE_STAIRS = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.WITHERED_BLACKSTONE_STAIRS);
    public static final Holder.Reference<Item> WITHERED_BLACKSTONE_SLAB = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.WITHERED_BLACKSTONE_SLAB);
    public static final Holder.Reference<Item> WITHERED_BLACKSTONE_WALL = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.WITHERED_BLACKSTONE_WALL);

    public static final Holder.Reference<Item> CRACKED_WITHERED_BLACKSTONE = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.CRACKED_WITHERED_BLACKSTONE);
    public static final Holder.Reference<Item> CRACKED_WITHERED_BLACKSTONE_STAIRS = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.CRACKED_WITHERED_BLACKSTONE_STAIRS);
    public static final Holder.Reference<Item> CRACKED_WITHERED_BLACKSTONE_SLAB = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.CRACKED_WITHERED_BLACKSTONE_SLAB);
    public static final Holder.Reference<Item> CRACKED_WITHERED_BLACKSTONE_WALL = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.CRACKED_WITHERED_BLACKSTONE_WALL);
    public static final Holder.Reference<Item> CHISELED_WITHERED_BLACKSTONE = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.CHISELED_WITHERED_BLACKSTONE);

    public static final Holder.Reference<Item> WITHERED_BASALT = ModRegistry.REGISTRIES.registerBlockItem(ModBlocks.WITHERED_BASALT);
    public static final Holder.Reference<Item> WITHERED_COAL_BLACK = ModRegistry.REGISTRIES.registerBlockItem(ModBlocks.WITHERED_COAL_BLOCK);
    public static final Holder.Reference<Item> WITHERED_QUARTZ_BLOCK = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.WITHERED_QUARTZ_BLOCK);
    public static final Holder.Reference<Item> WITHERED_DEBRIS = ModRegistry.REGISTRIES.registerBlockItem(ModBlocks.WITHERED_DEBRIS);
    public static final Holder.Reference<Item> SOUL_STONE = ModRegistry.REGISTRIES.registerBlockItem(ModBlocks.SOUL_STONE);
    public static final Holder.Reference<Item> WITHERED_BONE_BLOCK = ModRegistry.REGISTRIES.registerBlockItem(ModBlocks.WITHERED_BONE_BLOCK);

    public static final Holder.Reference<Item> WARPED_NETHER_BRICKS = ModRegistry.REGISTRIES.registerBlockItem(ModBlocks.WARPED_NETHER_BRICKS);
    public static final Holder.Reference<Item> CHISELED_WARPED_NETHER_BRICKS = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.CHISELED_WARPED_NETHER_BRICKS);
    public static final Holder.Reference<Item> WARPED_NETHER_BRICK_STAIRS = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.WARPED_NETHER_BRICK_STAIRS);
    public static final Holder.Reference<Item> WARPED_NETHER_BRICK_SLAB = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.WARPED_NETHER_BRICK_SLAB);
    public static final Holder.Reference<Item> WARPED_NETHER_BRICK_WALL = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.WARPED_NETHER_BRICK_WALL);

    public static final ResourceKey<JukeboxSong> WITHER_WALTZ_JUKEBOX_SONG = ModRegistry.REGISTRIES.makeResourceKey(
            Registries.JUKEBOX_SONG,
            "wither_waltz");
    public static final Holder.Reference<Item> WITHER_WALTZ_MUSIC_DISC = ModRegistry.REGISTRIES.registerItem(
            "wither_waltz_music_disc",
            () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(WITHER_WALTZ_JUKEBOX_SONG));
    public static final Holder.Reference<Item> WARPED_ENDER_PEARL = ModRegistry.REGISTRIES.registerItem(
            "warped_ender_pearl",
            WarpedEnderpearlItem::new,
            () -> new Item.Properties().stacksTo(16).useCooldown(1.0F).rarity(Rarity.RARE));
    public static final Holder.Reference<Item> WITHERED_BONE = ModRegistry.REGISTRIES.registerItem("withered_bone");
    public static final Holder.Reference<Item> WITHERED_BONE_MEAL = ModRegistry.REGISTRIES.registerItem(
            "withered_bone_meal",
            WitheredBoneMealItem::new);

    public static final Holder.Reference<Item> NETHERITE_BELL = ModRegistry.REGISTRIES.registerBlockItem(ModBlocks.NETHERITE_BELL,
            () -> new Item.Properties().rarity(Rarity.EPIC).fireResistant());
    public static final Holder.Reference<Item> GILDED_NETHERITE_SHIELD = ModRegistry.REGISTRIES.registerItem(
            "gilded_netherite_shield",
            ShieldItem::new,
            () -> new Item.Properties().durability(1512)
                    .rarity(Rarity.RARE)
                    .fireResistant()
                    .component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
                    .equippableUnswappable(EquipmentSlot.OFFHAND)
                    .component(DataComponents.BLOCKS_ATTACKS,
                            new BlocksAttacks(0.25F,
                                    0.0F,
                                    List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                                    new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                                    Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                                    Optional.of(SoundEvents.SHIELD_BLOCK),
                                    Optional.of(SoundEvents.SHIELD_BREAK)))
                    .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK));
    public static final Holder.Reference<Item> CUTLASS = ModRegistry.REGISTRIES.registerItem("cutlass",
            () -> new Item.Properties().sword(ToolMaterial.IRON, 3.0F, -1.6F)
                    .durability(312)
                    .enchantable(1)
                    .component(DataComponents.BLOCKS_ATTACKS,
                            new BlocksAttacks(0.0F,
                                    0.0F,
                                    List.of(new BlocksAttacks.DamageReduction(180.0F, Optional.empty(), 0.0F, 0.5F)),
                                    BlocksAttacks.ItemDamageFunction.DEFAULT,
                                    Optional.of(ModTags.BYPASSES_CUTLASS_DAMAGE_TYPE_TAG_KEY),
                                    Optional.of(ModSoundEvents.ITEM_SWORD_BLOCK_SOUND_EVENT),
                                    Optional.empty())));

    public static final Holder.Reference<Item> PIGLIN_PRISONER_SPAWN_EGG = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.PIGLIN_PRISONER);
    public static final Holder.Reference<Item> PIGLIN_HUNTER_SPAWN_EGG = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.PIGLIN_HUNTER);
    public static final Holder.Reference<Item> WEX_SPAWN_EGG = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.WEX);
    public static final Holder.Reference<Item> WARPED_ENDERMAN_SPAWN_EGG = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.WARPED_ENDERMAN);
    public static final Holder.Reference<Item> WRAITHER_SPAWN_EGG = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.WRAITHER);
    public static final Holder.Reference<Item> CORPOR_SPAWN_EGG = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.CORPOR);
    public static final Holder.Reference<Item> WITHER_SKELETON_KNIGHT_SPAWN_EGG = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.WITHER_SKELETON_KNIGHT);
    public static final Holder.Reference<Item> WITHER_SKELETON_HORSE_SPAWN_EGG = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.WITHER_SKELETON_HORSE);

    public static void boostrap() {
        // NO-OP
    }
}
