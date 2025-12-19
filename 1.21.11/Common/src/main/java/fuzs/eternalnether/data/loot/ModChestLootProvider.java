package fuzs.eternalnether.data.loot;

import fuzs.eternalnether.init.ModItems;
import fuzs.eternalnether.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractLootProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ModChestLootProvider extends AbstractLootProvider.Simple {

    public ModChestLootProvider(DataProviderContext context) {
        super(LootContextParamSets.CHEST, context);
    }

    @Override
    public void addLootTables() {
        this.add(ModRegistry.CITADEL_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ModItems.WARPED_ENDER_PEARL.value())
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.ENDER_PEARL)
                                        .setWeight(12)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(Items.DIAMOND)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(Items.ANCIENT_DEBRIS).setWeight(10))
                                .add(LootItem.lootTableItem(Items.NETHERITE_SCRAP).setWeight(8))
                                .add(LootItem.lootTableItem(Items.TWISTING_VINES)
                                        .setWeight(4)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))))
                                .add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE)
                                        .setWeight(6)
                                        .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F)))
                                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries())))
                                .add(LootItem.lootTableItem(Items.DIAMOND_HELMET)
                                        .setWeight(6)
                                        .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F)))
                                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries())))
                                .add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS)
                                        .setWeight(6)
                                        .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F)))
                                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries())))
                                .add(LootItem.lootTableItem(Items.DIAMOND_BOOTS)
                                        .setWeight(6)
                                        .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F)))
                                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries())))
                                .add(LootItem.lootTableItem(Items.DIAMOND_SWORD).setWeight(6))
                                .add(LootItem.lootTableItem(Items.NETHERITE_INGOT).setWeight(4))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(4)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2.0F))
                                .add(LootItem.lootTableItem(Items.GOLD_BLOCK)
                                        .setWeight(1)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
                                .add(LootItem.lootTableItem(Items.IRON_BLOCK)
                                        .setWeight(1)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
                                .add(LootItem.lootTableItem(Items.ENDER_PEARL)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(ModItems.WARPED_ENDER_PEARL.value())
                                        .setWeight(8)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.TWISTING_VINES)
                                        .setWeight(4)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))))
                                .add(LootItem.lootTableItem(Items.WARPED_FUNGUS_ON_A_STICK)
                                        .setWeight(6)
                                        .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F)))
                                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries()))))
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0F, 3.0F))
                                .add(LootItem.lootTableItem(Items.WARPED_ROOTS)
                                        .setWeight(1)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(12.0F, 24.0F))))
                                .add(LootItem.lootTableItem(Items.GOLD_INGOT)
                                        .setWeight(1)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT)
                                        .setWeight(1)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                                .add(LootItem.lootTableItem(Items.CRYING_OBSIDIAN)
                                        .setWeight(1)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F))))
                                .add(LootItem.lootTableItem(Items.QUARTZ)
                                        .setWeight(1)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 24.0F))))
                                .add(LootItem.lootTableItem(Items.WARPED_STEM)
                                        .setWeight(1)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
                                .add(LootItem.lootTableItem(Items.MAGMA_CREAM)
                                        .setWeight(1)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
                                .add(LootItem.lootTableItem(Items.NETHER_BRICK)
                                        .setWeight(1)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 16.0F))))));
        this.add(ModRegistry.CATACOMB_TREASURE_RIB_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(3.0F, 5.0F))
                                .add(LootItem.lootTableItem(Items.BONE)
                                        .setWeight(12)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 10.0F))))
                                .add(LootItem.lootTableItem(Items.SOUL_SAND)
                                        .setWeight(12)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 12.0F))))
                                .add(LootItem.lootTableItem(Items.SOUL_SOIL)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
                                .add(LootItem.lootTableItem(ModItems.SOUL_STONE.value())
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 10.0F))))
                                .add(LootItem.lootTableItem(Items.COAL)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT)
                                        .setWeight(8)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(Items.GOLD_INGOT)
                                        .setWeight(8)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
                                .add(LootItem.lootTableItem(Items.GOLDEN_SWORD)
                                        .setWeight(8)
                                        .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F)))
                                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries())))
                                .add(LootItem.lootTableItem(Items.CRYING_OBSIDIAN)
                                        .setWeight(8)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                                .add(LootItem.lootTableItem(Items.COAL_BLOCK)
                                        .setWeight(8)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.DIAMOND)
                                        .setWeight(7)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.FLINT_AND_STEEL).setWeight(7))
                                .add(LootItem.lootTableItem(ModItems.WITHER_WALTZ_MUSIC_DISC.value()).setWeight(6))
                                .add(LootItem.lootTableItem(Items.SADDLE).setWeight(5))
                                .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR).setWeight(4))
                                .add(LootItem.lootTableItem(Items.ANCIENT_DEBRIS).setWeight(3))
                                .add(LootItem.lootTableItem(Items.WITHER_SKELETON_SKULL).setWeight(2))
                                .add(LootItem.lootTableItem(Items.NETHERITE_INGOT).setWeight(1))));
    }
}
