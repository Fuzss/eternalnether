package fuzs.eternalnether.data.client;

import fuzs.eternalnether.client.renderer.special.GildedNetheriteShieldSpecialRenderer;
import fuzs.eternalnether.init.ModBlockFamilies;
import fuzs.eternalnether.init.ModBlocks;
import fuzs.eternalnether.init.ModItems;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.client.data.v2.models.ItemModelGenerationHelper;
import fuzs.puzzleslib.api.client.data.v2.models.ModelLocationHelper;
import fuzs.puzzleslib.api.client.data.v2.models.ModelTemplateHelper;
import net.minecraft.resources.Identifier;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModModelProvider extends AbstractModelProvider {
    public static final ModelTemplate SHIELD_MODEL_TEMPLATE = ModelTemplateHelper.createItemModelTemplate(
            Identifier.withDefaultNamespace("shield"),
            TextureSlot.PARTICLE);
    public static final ModelTemplate SHIELD_BLOCKING_MODEL_TEMPLATE = ModelTemplateHelper.createItemModelTemplate(
            Identifier.withDefaultNamespace("shield_blocking"),
            TextureSlot.PARTICLE);
    public static final TextureSlot BAR_TEXTURE_SLOT = TextureSlot.create("bar");
    public static final TextureSlot POST_TEXTURE_SLOT = TextureSlot.create("post");
    public static final ModelTemplate BELL_BETWEEN_WALLS_MODEL_TEMPLATE = ModelTemplates.create("bell_between_walls",
            TextureSlot.PARTICLE,
            BAR_TEXTURE_SLOT);
    public static final ModelTemplate BELL_CEILING_MODEL_TEMPLATE = ModelTemplates.create("bell_ceiling",
            TextureSlot.PARTICLE,
            BAR_TEXTURE_SLOT);
    public static final ModelTemplate BELL_FLOOR_MODEL_TEMPLATE = ModelTemplates.create("bell_floor",
            TextureSlot.PARTICLE,
            BAR_TEXTURE_SLOT,
            POST_TEXTURE_SLOT);
    public static final ModelTemplate BELL_WALL_MODEL_TEMPLATE = ModelTemplates.create("bell_wall",
            TextureSlot.PARTICLE,
            BAR_TEXTURE_SLOT);

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators blockModelGenerators) {
        BlockModelGenerators.TEXTURED_MODELS.put(ModBlocks.WITHERED_BLACKSTONE.value(),
                TexturedModel.COLUMN_WITH_WALL.get(ModBlocks.WITHERED_BLACKSTONE.value())
                        .updateTextures(map -> map.put(TextureSlot.SIDE,
                                TextureMapping.getBlockTexture(ModBlocks.WITHERED_BLACKSTONE.value()))));
        BlockModelGenerators.TEXTURED_MODELS.put(ModBlocks.CHISELED_WITHERED_BLACKSTONE.value(),
                TexturedModel.COLUMN.get(ModBlocks.CHISELED_WITHERED_BLACKSTONE.value())
                        .updateTextures(map -> map.put(TextureSlot.SIDE,
                                TextureMapping.getBlockTexture(ModBlocks.CHISELED_WITHERED_BLACKSTONE.value()))));
        ModBlockFamilies.getAllFamilies()
                .filter(BlockFamily::shouldGenerateModel)
                .forEach(blockFamily -> blockModelGenerators.family(blockFamily.getBaseBlock())
                        .generateFor(blockFamily));
        blockModelGenerators.family(ModBlocks.WITHERED_BLACKSTONE.value())
                .slab(ModBlocks.WITHERED_BLACKSTONE_SLAB.value())
                .stairs(ModBlocks.WITHERED_BLACKSTONE_STAIRS.value())
                .wall(ModBlocks.WITHERED_BLACKSTONE_WALL.value())
                .fullBlockVariant(ModBlocks.CHISELED_WITHERED_BLACKSTONE.value());
        blockModelGenerators.createTrivialCube(ModBlocks.COBBLED_BLACKSTONE.value());
        blockModelGenerators.createTrivialCube(ModBlocks.WITHERED_BASALT.value());
        blockModelGenerators.createTrivialCube(ModBlocks.WITHERED_COAL_BLOCK.value());
        blockModelGenerators.createTrivialCube(ModBlocks.WITHERED_QUARTZ_BLOCK.value());
        blockModelGenerators.createTrivialBlock(ModBlocks.WITHERED_DEBRIS.value(), TexturedModel.COLUMN);
        blockModelGenerators.createTrivialCube(ModBlocks.SOUL_STONE.value());
        blockModelGenerators.createAxisAlignedPillarBlock(ModBlocks.WITHERED_BONE_BLOCK.value(), TexturedModel.COLUMN);
        TextureMapping textureMapping = bell(ModBlocks.NETHERITE_BELL.value(),
                Blocks.CRIMSON_PLANKS,
                Blocks.BLACKSTONE);
        this.createBell(ModBlocks.NETHERITE_BELL.value(), textureMapping, blockModelGenerators);
    }

    @Override
    public void addItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModItems.PIGLIN_PRISONER_SPAWN_EGG.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.PIGLIN_HUNTER_SPAWN_EGG.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WEX_SPAWN_EGG.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WARPED_ENDERMAN_SPAWN_EGG.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WRAITHER_SPAWN_EGG.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.CORPOR_SPAWN_EGG.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WITHER_SKELETON_KNIGHT_SPAWN_EGG.value(),
                ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WITHER_SKELETON_HORSE_SPAWN_EGG.value(),
                ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WITHER_WALTZ_MUSIC_DISC.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WARPED_ENDER_PEARL.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WITHERED_BONE.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WITHERED_BONE_MEAL.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.CUTLASS.value(), ModelTemplates.FLAT_HANDHELD_ITEM);
        ItemModelGenerationHelper.generateShield(ModItems.GILDED_NETHERITE_SHIELD.value(),
                Blocks.CRIMSON_PLANKS,
                GildedNetheriteShieldSpecialRenderer.Unbaked::new,
                itemModelGenerators);
    }

    public static TextureMapping bell(Block bellBlock, Block barBlock, Block postBlock) {
        return new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(bellBlock, "_bottom"))
                .put(BAR_TEXTURE_SLOT, TextureMapping.getBlockTexture(barBlock))
                .put(POST_TEXTURE_SLOT, TextureMapping.getBlockTexture(postBlock));
    }

    /**
     * @see BlockModelGenerators#createBell()
     */
    public final void createBell(Block block, TextureMapping textureMapping, BlockModelGenerators blockModelGenerators) {
        Identifier identifier = BELL_FLOOR_MODEL_TEMPLATE.create(ModelLocationHelper.getBlockModel(block,
                "_floor"), textureMapping, blockModelGenerators.modelOutput);
        Identifier resourceLocation2 = BELL_CEILING_MODEL_TEMPLATE.create(ModelLocationHelper.getBlockModel(block,
                "_ceiling"), textureMapping, blockModelGenerators.modelOutput);
        Identifier resourceLocation3 = BELL_WALL_MODEL_TEMPLATE.create(ModelLocationHelper.getBlockModel(block,
                "_wall"), textureMapping, blockModelGenerators.modelOutput);
        Identifier resourceLocation4 = BELL_BETWEEN_WALLS_MODEL_TEMPLATE.create(ModelLocationHelper.getBlockModel(
                block,
                "_between_walls"), textureMapping, blockModelGenerators.modelOutput);
        MultiVariant multiVariant = BlockModelGenerators.plainVariant(identifier);
        MultiVariant multiVariant2 = BlockModelGenerators.plainVariant(resourceLocation2);
        MultiVariant multiVariant3 = BlockModelGenerators.plainVariant(resourceLocation3);
        MultiVariant multiVariant4 = BlockModelGenerators.plainVariant(resourceLocation4);
        blockModelGenerators.registerSimpleFlatItemModel(block.asItem());
        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING,
                                BlockStateProperties.BELL_ATTACHMENT)
                        .select(Direction.NORTH, BellAttachType.FLOOR, multiVariant)
                        .select(Direction.SOUTH,
                                BellAttachType.FLOOR,
                                multiVariant.with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.EAST, BellAttachType.FLOOR, multiVariant.with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.WEST, BellAttachType.FLOOR, multiVariant.with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.NORTH, BellAttachType.CEILING, multiVariant2)
                        .select(Direction.SOUTH,
                                BellAttachType.CEILING,
                                multiVariant2.with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.EAST,
                                BellAttachType.CEILING,
                                multiVariant2.with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.WEST,
                                BellAttachType.CEILING,
                                multiVariant2.with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.NORTH,
                                BellAttachType.SINGLE_WALL,
                                multiVariant3.with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.SOUTH,
                                BellAttachType.SINGLE_WALL,
                                multiVariant3.with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.EAST, BellAttachType.SINGLE_WALL, multiVariant3)
                        .select(Direction.WEST,
                                BellAttachType.SINGLE_WALL,
                                multiVariant3.with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.SOUTH,
                                BellAttachType.DOUBLE_WALL,
                                multiVariant4.with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.NORTH,
                                BellAttachType.DOUBLE_WALL,
                                multiVariant4.with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.EAST, BellAttachType.DOUBLE_WALL, multiVariant4)
                        .select(Direction.WEST,
                                BellAttachType.DOUBLE_WALL,
                                multiVariant4.with(BlockModelGenerators.Y_ROT_180))));
    }
}
