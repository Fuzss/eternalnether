package fuzs.eternalnether.data;

import com.google.common.collect.ImmutableMap;
import fuzs.eternalnether.init.ModBlockFamilies;
import fuzs.eternalnether.init.ModBlocks;
import fuzs.eternalnether.init.ModItems;
import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.*;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;
import java.util.function.BiFunction;

public class ModRecipeProvider extends AbstractRecipeProvider {
    private static final Map<BlockFamily.Variant, BiFunction<ItemLike, ItemLike, RecipeBuilder>> STONECUTTING_BUILDERS = ImmutableMap.<BlockFamily.Variant, BiFunction<ItemLike, ItemLike, RecipeBuilder>>builder()
            .put(BlockFamily.Variant.CHISELED,
                    (itemLike, itemLike2) -> SingleItemRecipeBuilder.stonecutting(Ingredient.of(itemLike2),
                            RecipeCategory.BUILDING_BLOCKS,
                            itemLike))
            .put(BlockFamily.Variant.CUT,
                    (itemLike, itemLike2) -> SingleItemRecipeBuilder.stonecutting(Ingredient.of(itemLike2),
                            RecipeCategory.BUILDING_BLOCKS,
                            itemLike))
            .put(BlockFamily.Variant.SLAB,
                    (itemLike, itemLike2) -> SingleItemRecipeBuilder.stonecutting(Ingredient.of(itemLike2),
                            RecipeCategory.BUILDING_BLOCKS,
                            itemLike,
                            2))
            .put(BlockFamily.Variant.STAIRS,
                    (itemLike, itemLike2) -> SingleItemRecipeBuilder.stonecutting(Ingredient.of(itemLike2),
                            RecipeCategory.BUILDING_BLOCKS,
                            itemLike))
            .put(BlockFamily.Variant.POLISHED,
                    (itemLike, itemLike2) -> SingleItemRecipeBuilder.stonecutting(Ingredient.of(itemLike2),
                            RecipeCategory.BUILDING_BLOCKS,
                            itemLike))
            .put(BlockFamily.Variant.WALL,
                    (itemLike, itemLike2) -> SingleItemRecipeBuilder.stonecutting(Ingredient.of(itemLike2),
                            RecipeCategory.DECORATIONS,
                            itemLike))
            .build();

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        this.generateForEnabledBlockFamilies(FeatureFlags.DEFAULT_FLAGS);
        ShapedRecipeBuilder.shaped(this.items(), RecipeCategory.BUILDING_BLOCKS, ModBlocks.WARPED_NETHER_BRICKS.value())
                .define('W', Items.WARPED_ROOTS)
                .define('N', Items.NETHER_BRICK)
                .pattern("NW")
                .pattern("WN")
                .unlockedBy(getHasName(Items.WARPED_ROOTS), this.has(Items.WARPED_ROOTS))
                .save(recipeOutput);
        this.smeltingResultFromBase(ModBlocks.COBBLED_BLACKSTONE.value(), Blocks.BLACKSTONE);
        ShapelessRecipeBuilder.shapeless(this.items(), RecipeCategory.MISC, ModItems.WITHERED_BONE_MEAL.value(), 3)
                .requires(ModItems.WITHERED_BONE.value())
                .group(getItemName(ModItems.WITHERED_BONE_MEAL.value()))
                .unlockedBy(getHasName(ModItems.WITHERED_BONE.value()), this.has(ModItems.WITHERED_BONE.value()))
                .save(recipeOutput);
        this.nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC,
                ModItems.WITHERED_BONE_MEAL.value(),
                RecipeCategory.BUILDING_BLOCKS,
                ModItems.WITHERED_BONE_BLOCK.value(),
                getConversionRecipeName(ModItems.WITHERED_BONE_MEAL.value(), ModItems.WITHERED_BONE_BLOCK.value()),
                getItemName(ModItems.WITHERED_BONE_MEAL.value()));
    }

    public void generateForEnabledBlockFamilies(FeatureFlagSet enabledFeatures) {
        ModBlockFamilies.getAllFamilies()
                .filter(BlockFamily::shouldGenerateRecipe)
                .forEach(blockFamily -> this.generateRecipes(blockFamily, enabledFeatures));
    }

    public void generateRecipes(RecipeOutput recipeOutput, BlockFamily blockFamily, FeatureFlagSet requiredFeatures) {
        this.generateRecipes(blockFamily, requiredFeatures);
        // also automatically generate stone-cutting recipes
        blockFamily.getVariants().forEach((BlockFamily.Variant variant, Block block) -> {
            if (block.requiredFeatures().isSubsetOf(requiredFeatures)) {
                BiFunction<ItemLike, ItemLike, RecipeBuilder> biFunction = STONECUTTING_BUILDERS.get(variant);
                ItemLike itemLike = this.getBaseBlock(blockFamily, variant);
                if (biFunction != null) {
                    RecipeBuilder recipeBuilder = biFunction.apply(block, itemLike);
                    recipeBuilder.unlockedBy(blockFamily.getRecipeUnlockedBy().orElseGet(() -> getHasName(itemLike)),
                            this.has(itemLike));
                    recipeBuilder.save(recipeOutput, getStonecuttingRecipeName(block, itemLike));
                }
            }
        });
    }
}
