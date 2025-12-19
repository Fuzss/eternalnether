package fuzs.eternalnether.data.client;

import com.google.common.collect.ImmutableMap;
import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.data.ModAdvancementProvider;
import fuzs.eternalnether.init.*;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ModLanguageProvider extends AbstractLanguageProvider {
    static final Map<BlockFamily.Variant, BiFunction<BlockFamilyBuilder, Block, BlockFamilyBuilder>> VARIANT_FUNCTIONS = ImmutableMap.<BlockFamily.Variant, BiFunction<BlockFamilyBuilder, Block, BlockFamilyBuilder>>builder()
            .put(BlockFamily.Variant.BUTTON, BlockFamilyBuilder::button)
            .put(BlockFamily.Variant.CHISELED, BlockFamilyBuilder::chiseled)
            .put(BlockFamily.Variant.CRACKED, BlockFamilyBuilder::cracked)
            .put(BlockFamily.Variant.CUT, BlockFamilyBuilder::cut)
            .put(BlockFamily.Variant.DOOR, BlockFamilyBuilder::door)
            .put(BlockFamily.Variant.CUSTOM_FENCE, BlockFamilyBuilder::fence)
            .put(BlockFamily.Variant.FENCE, BlockFamilyBuilder::fence)
            .put(BlockFamily.Variant.CUSTOM_FENCE_GATE, BlockFamilyBuilder::fenceGate)
            .put(BlockFamily.Variant.FENCE_GATE, BlockFamilyBuilder::fenceGate)
            .put(BlockFamily.Variant.MOSAIC, BlockFamilyBuilder::mosaic)
            .put(BlockFamily.Variant.SIGN, BlockFamilyBuilder::sign)
            .put(BlockFamily.Variant.SLAB, BlockFamilyBuilder::slab)
            .put(BlockFamily.Variant.STAIRS, BlockFamilyBuilder::stairs)
            .put(BlockFamily.Variant.PRESSURE_PLATE, BlockFamilyBuilder::pressurePlate)
            .put(BlockFamily.Variant.POLISHED, BlockFamilyBuilder::polished)
            .put(BlockFamily.Variant.TRAPDOOR, BlockFamilyBuilder::trapdoor)
            .put(BlockFamily.Variant.WALL, BlockFamilyBuilder::wall)
            .put(BlockFamily.Variant.WALL_SIGN, BlockFamilyBuilder::wallSign)
            .build();

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.addCreativeModeTab(ModRegistry.CREATIVE_MODE_TAB, EternalNether.MOD_NAME);

        builder.addBlock(ModBlocks.COBBLED_BLACKSTONE, "Cobbled Blackstone");
        blockFamily(builder, "Withered Blackstone").baseBlock(ModBlocks.WITHERED_BLACKSTONE.value())
                .slab(ModBlocks.WITHERED_BLACKSTONE_SLAB.value())
                .stairs(ModBlocks.WITHERED_BLACKSTONE_STAIRS.value())
                .wall(ModBlocks.WITHERED_BLACKSTONE_WALL.value())
                .chiseled(ModBlocks.CHISELED_WITHERED_BLACKSTONE.value());
        blockFamily(builder,
                "Cracked Withered Blackstone").generateFor(ModBlockFamilies.CRACKED_WITHERED_BLACKSTONE_FAMILY);
        blockFamily(builder,
                "Warped Nether Brick",
                "Warped Nether Bricks").generateFor(ModBlockFamilies.WARPED_NETHER_BRICKS_FAMILY);
        builder.add(ModBlocks.WITHERED_BASALT.value(), "Withered Basalt");
        builder.add(ModBlocks.WITHERED_COAL_BLOCK.value(), "Withered Coal Block");
        builder.add(ModBlocks.WITHERED_QUARTZ_BLOCK.value(), "Withered Quartz Block");
        builder.add(ModBlocks.WITHERED_DEBRIS.value(), "Withered Debris");
        builder.add(ModBlocks.SOUL_STONE.value(), "Soul Stone");
        builder.add(ModBlocks.WITHERED_BONE_BLOCK.value(), "Withered Bone Block");
        builder.add(ModBlocks.NETHERITE_BELL.value(), "Netherite Bell");

        builder.addSpawnEgg(ModItems.WEX_SPAWN_EGG.value(), "Wex");
        builder.addSpawnEgg(ModItems.WARPED_ENDERMAN_SPAWN_EGG.value(), "Warped Enderman");
        builder.addSpawnEgg(ModItems.PIGLIN_PRISONER_SPAWN_EGG.value(), "Piglin Prisoner");
        builder.addSpawnEgg(ModItems.PIGLIN_HUNTER_SPAWN_EGG.value(), "Piglin Hunter");
        builder.addSpawnEgg(ModItems.WRAITHER_SPAWN_EGG.value(), "Wraither");
        builder.addSpawnEgg(ModItems.WITHER_SKELETON_KNIGHT_SPAWN_EGG.value(), "Wither Skeleton Knight");
        builder.addSpawnEgg(ModItems.CORPOR_SPAWN_EGG.value(), "Corpor");
        builder.addSpawnEgg(ModItems.WITHER_SKELETON_HORSE_SPAWN_EGG.value(), "Withered Skeleton Horse");
        builder.add(ModItems.WITHER_WALTZ_JUKEBOX_SONG, "Izofar - Wither Waltz");
        builder.add(ModItems.WITHER_WALTZ_MUSIC_DISC.value(), "Music Disc");
        builder.add(ModItems.WARPED_ENDER_PEARL.value(), "Warped Ender Pearl");
        builder.add(ModItems.WITHERED_BONE.value(), "Withered Bone");
        builder.add(ModItems.WITHERED_BONE_MEAL.value(), "Withered Bone Meal");
        builder.add(ModItems.GILDED_NETHERITE_SHIELD.value(), "Gilded Netherite Shield");
        builder.add(ModItems.CUTLASS.value(), "Cutlass");

        builder.add(ModEntityTypes.WEX.value(), "Wex");
        builder.add(ModEntityTypes.WARPED_ENDERMAN.value(), "Warped Enderman");
        builder.add(ModEntityTypes.PIGLIN_PRISONER.value(), "Piglin Prisoner");
        builder.add(ModEntityTypes.PIGLIN_HUNTER.value(), "Piglin Hunter");
        builder.add(ModEntityTypes.WRAITHER.value(), "Wraither");
        builder.add(ModEntityTypes.WITHER_SKELETON_KNIGHT.value(), "Wither Skeleton Knight");
        builder.add(ModEntityTypes.CORPOR.value(), "Corpor");
        builder.add(ModEntityTypes.WITHER_SKELETON_HORSE.value(), "Withered Skeleton Horse");
        builder.add(ModEntityTypes.WARPED_ENDER_PEARL.value(), "Warped Ender Pearl");

        builder.add(ModSoundEvents.ITEM_SWORD_BLOCK_SOUND_EVENT.value(), "Sword blocks");
        builder.add(ModSoundEvents.WEX_CHARGE.value(), "Wex shrieks");
        builder.add(ModSoundEvents.WEX_DEATH.value(), "Wex dies");
        builder.add(ModSoundEvents.WEX_HURT.value(), "Wex hurts");
        builder.add(ModSoundEvents.WEX_AMBIENT.value(), "Wex wexes");
        builder.add(ModSoundEvents.WARPED_ENDERMAN_DEATH.value(), "Warped Enderman dies");
        builder.add(ModSoundEvents.WARPED_ENDERMAN_HURT.value(), "Warped Enderman hurts");
        builder.add(ModSoundEvents.WARPED_ENDERMAN_AMBIENT.value(), "Warped Enderman vwoops");
        builder.add(ModSoundEvents.WARPED_ENDERMAN_TELEPORT.value(), "Warped Enderman teleports");
        builder.add(ModSoundEvents.WARPED_ENDERMAN_SCREAM.value(), "Warped Enderman screams");
        builder.add(ModSoundEvents.WARPED_ENDERMAN_STARE.value(), "Warped Enderman cries out");

        builder.add(ModAdvancementProvider.ROOT_ADVANCEMENT.title(), EternalNether.MOD_NAME);
        builder.add(ModAdvancementProvider.ROOT_ADVANCEMENT.description(), "Explore the Nether for new structures!");
        builder.add(ModAdvancementProvider.ACQUIRE_WITHER_WALTZ_ADVANCEMENT.title(), "Here I Waltz");
        builder.add(ModAdvancementProvider.ACQUIRE_WITHER_WALTZ_ADVANCEMENT.description(),
                "Acquire the Wither Waltz Music Disc");
        builder.add(ModAdvancementProvider.CATACOMB_ADVANCEMENT.title(), "To Wither Or Not To Wither");
        builder.add(ModAdvancementProvider.CATACOMB_ADVANCEMENT.description(), "Locate a Catacomb structure");
        builder.add(ModAdvancementProvider.CITADEL_ADVANCEMENT.title(), "The Warping Citadel");
        builder.add(ModAdvancementProvider.CITADEL_ADVANCEMENT.description(), "Locate a Citadel structure");
        builder.add(ModAdvancementProvider.EXPLORE_STRUCTURES_ADVANCEMENT.title(), "Hotter Tourist Destinations");
        builder.add(ModAdvancementProvider.EXPLORE_STRUCTURES_ADVANCEMENT.description(),
                "Locate all " + EternalNether.MOD_NAME + " structures");
        builder.add(ModAdvancementProvider.PIGLIN_MANOR_ADVANCEMENT.title(), "Mind Your Manors");
        builder.add(ModAdvancementProvider.PIGLIN_MANOR_ADVANCEMENT.description(), "Locate a Piglin Manor structure");
        builder.add(ModAdvancementProvider.RIDE_WITHER_SKELETON_HORSE_ADVANCEMENT.title(), "Dark Horse");
        builder.add(ModAdvancementProvider.RIDE_WITHER_SKELETON_HORSE_ADVANCEMENT.description(),
                "Ride a Wither Skeleton Horse");
        builder.add(ModAdvancementProvider.SUMMON_ENDERMAN_ADVANCEMENT.title(), "A Little Off The Top");
        builder.add(ModAdvancementProvider.SUMMON_ENDERMAN_ADVANCEMENT.description(),
                "Trim the Warp from a Warped Enderman");
        builder.add(ModAdvancementProvider.RESCUE_PIGLIN_PRISONER_ADVANCEMENT.title(), "Saving Private Swine");
        builder.add(ModAdvancementProvider.RESCUE_PIGLIN_PRISONER_ADVANCEMENT.description(),
                "Rescue a Piglin Prisoner");
    }

    public static BlockFamilyBuilder blockFamily(TranslationBuilder builder, String blockValue) {
        return new BlockFamilyBuilder(builder::add, blockValue);
    }

    public static BlockFamilyBuilder blockFamily(TranslationBuilder builder, String blockValue, String baseBlockValue) {
        return new BlockFamilyBuilder(builder::add, blockValue, baseBlockValue);
    }

    public static class BlockFamilyBuilder {
        private final BiConsumer<Block, String> valueConsumer;
        private final String blockValue;
        private final String baseBlockValue;

        public BlockFamilyBuilder(BiConsumer<Block, String> valueConsumer, String blockValue) {
            this(valueConsumer, blockValue, blockValue);
        }

        public BlockFamilyBuilder(BiConsumer<Block, String> valueConsumer, String blockValue, String baseBlockValue) {
            this.valueConsumer = valueConsumer;
            this.blockValue = blockValue;
            this.baseBlockValue = baseBlockValue;
        }

        public void generateFor(BlockFamily blockFamily) {
            this.baseBlock(blockFamily.getBaseBlock());
            blockFamily.getVariants().forEach((BlockFamily.Variant variant, Block block) -> {
                BiFunction<BlockFamilyBuilder, Block, BlockFamilyBuilder> variantFunction = VARIANT_FUNCTIONS.get(
                        variant);
                if (variantFunction != null) {
                    variantFunction.apply(this, block);
                }
            });
        }

        public BlockFamilyBuilder baseBlock(Block block) {
            this.valueConsumer.accept(block, this.baseBlockValue);
            return this;
        }

        public BlockFamilyBuilder button(Block block) {
            this.valueConsumer.accept(block, this.blockValue + " Button");
            return this;
        }

        public BlockFamilyBuilder chiseled(Block block) {
            this.valueConsumer.accept(block, "Chiseled " + this.blockValue);
            return this;
        }

        public BlockFamilyBuilder cracked(Block block) {
            this.valueConsumer.accept(block, "Cracked " + this.blockValue);
            return this;
        }

        public BlockFamilyBuilder cut(Block block) {
            this.valueConsumer.accept(block, "Cut " + this.blockValue);
            return this;
        }

        public BlockFamilyBuilder door(Block block) {
            this.valueConsumer.accept(block, this.blockValue + " Door");
            return this;
        }

        public BlockFamilyBuilder fence(Block block) {
            this.valueConsumer.accept(block, this.blockValue + " Fence");
            return this;
        }

        public BlockFamilyBuilder fenceGate(Block block) {
            this.valueConsumer.accept(block, this.blockValue + " Fence Gate");
            return this;
        }

        public BlockFamilyBuilder mosaic(Block block) {
            this.valueConsumer.accept(block, "Mosaic " + this.blockValue);
            return this;
        }

        public BlockFamilyBuilder sign(Block block) {
            this.valueConsumer.accept(block, this.blockValue + " Sign");
            return this;
        }

        public BlockFamilyBuilder slab(Block block) {
            this.valueConsumer.accept(block, this.blockValue + " Slab");
            return this;
        }

        public BlockFamilyBuilder stairs(Block block) {
            this.valueConsumer.accept(block, this.blockValue + " Stairs");
            return this;
        }

        public BlockFamilyBuilder pressurePlate(Block block) {
            this.valueConsumer.accept(block, this.blockValue + " Pressure Plate");
            return this;
        }

        public BlockFamilyBuilder polished(Block block) {
            this.valueConsumer.accept(block, "Polished " + this.blockValue);
            return this;
        }

        public BlockFamilyBuilder trapdoor(Block block) {
            this.valueConsumer.accept(block, this.blockValue + " Trapdoor");
            return this;
        }

        public BlockFamilyBuilder wall(Block block) {
            this.valueConsumer.accept(block, this.blockValue + " Wall");
            return this;
        }

        public BlockFamilyBuilder wallSign(Block block) {
            this.valueConsumer.accept(block, this.blockValue + " Wall Sign");
            return this;
        }
    }
}
