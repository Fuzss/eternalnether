package fuzs.eternalnether.data;

import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.init.ModEntityTypes;
import fuzs.eternalnether.init.ModItems;
import fuzs.eternalnether.init.ModStructures;
import fuzs.puzzleslib.api.data.v2.AbstractAdvancementProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.function.Consumer;

public class ModAdvancementProvider extends AbstractAdvancementProvider {
    public static final AdvancementToken ROOT_ADVANCEMENT = new AdvancementToken(EternalNether.id("root"));
    public static final AdvancementToken ACQUIRE_WITHER_WALTZ_ADVANCEMENT = new AdvancementToken(EternalNether.id(
            "acquire_wither_waltz"));
    public static final AdvancementToken CATACOMB_ADVANCEMENT = new AdvancementToken(EternalNether.id("catacomb"));
    public static final AdvancementToken CITADEL_ADVANCEMENT = new AdvancementToken(EternalNether.id("citadel"));
    public static final AdvancementToken EXPLORE_STRUCTURES_ADVANCEMENT = new AdvancementToken(EternalNether.id(
            "explore_structures"));
    public static final AdvancementToken PIGLIN_MANOR_ADVANCEMENT = new AdvancementToken(EternalNether.id("piglin_manor"));
    public static final AdvancementToken RESCUE_PIGLIN_PRISONER_ADVANCEMENT = new AdvancementToken(EternalNether.id(
            "rescue_piglin_prisoner"));
    public static final AdvancementToken RIDE_WITHER_SKELETON_HORSE_ADVANCEMENT = new AdvancementToken(EternalNether.id(
            "ride_wither_skeleton_horse"));
    public static final AdvancementToken SUMMON_ENDERMAN_ADVANCEMENT = new AdvancementToken(EternalNether.id(
            "summon_enderman"));

    public ModAdvancementProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addAdvancements(HolderLookup.Provider registries, Consumer<AdvancementHolder> writer) {
        HolderLookup.RegistryLookup<EntityType<?>> entityLookup = registries.lookupOrThrow(Registries.ENTITY_TYPE);
        HolderLookup.RegistryLookup<Structure> structureLookup = registries.lookupOrThrow(Registries.STRUCTURE);
        Advancement.Builder.advancement()
                .display(display(new ItemStack(ModItems.CHISELED_WITHERED_BLACKSTONE),
                        ROOT_ADVANCEMENT.id(),
                        EternalNether.id("textures/block/soul_stone.png"),
                        AdvancementType.TASK,
                        true))
                .addCriterion("entered_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER))
                .save(writer, ROOT_ADVANCEMENT.name());
        Advancement.Builder.advancement()
                .display(display(getNetheriteBootsDisplayItem(registries),
                        EXPLORE_STRUCTURES_ADVANCEMENT.id(),
                        AdvancementType.CHALLENGE))
                .parent(ROOT_ADVANCEMENT.asParent())
                .rewards(AdvancementRewards.Builder.experience(500))
                .addCriterion("in_catacomb",
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(structureLookup.getOrThrow(
                                ModStructures.CATACOMB_STRUCTURE))))
                .addCriterion("in_citadel",
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(structureLookup.getOrThrow(
                                ModStructures.CITADEL_STRUCTURE))))
                .addCriterion("in_piglin_manor",
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(structureLookup.getOrThrow(
                                ModStructures.PIGLIN_MANOR_STRUCTURE))))
                .save(writer, EXPLORE_STRUCTURES_ADVANCEMENT.name());
        Advancement.Builder.advancement()
                .display(display(new ItemStack(ModItems.WITHERED_DEBRIS), CATACOMB_ADVANCEMENT.id()))
                .parent(EXPLORE_STRUCTURES_ADVANCEMENT.asParent())
                .addCriterion("in_catacomb",
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(structureLookup.getOrThrow(
                                ModStructures.CATACOMB_STRUCTURE))))
                .save(writer, CATACOMB_ADVANCEMENT.name());
        Advancement.Builder.advancement()
                .display(display(new ItemStack(ModItems.CHISELED_WARPED_NETHER_BRICKS), CITADEL_ADVANCEMENT.id()))
                .parent(EXPLORE_STRUCTURES_ADVANCEMENT.asParent())
                .addCriterion("in_citadel",
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(structureLookup.getOrThrow(
                                ModStructures.CITADEL_STRUCTURE))))
                .save(writer, CITADEL_ADVANCEMENT.name());
        Advancement.Builder.advancement()
                .display(display(new ItemStack(Items.CRIMSON_PLANKS), PIGLIN_MANOR_ADVANCEMENT.id()))
                .parent(EXPLORE_STRUCTURES_ADVANCEMENT.asParent())
                .addCriterion("in_piglin_manor",
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(structureLookup.getOrThrow(
                                ModStructures.PIGLIN_MANOR_STRUCTURE))))
                .save(writer, PIGLIN_MANOR_ADVANCEMENT.name());
        Advancement.Builder.advancement()
                .display(display(new ItemStack(ModItems.NETHERITE_BELL),
                        RESCUE_PIGLIN_PRISONER_ADVANCEMENT.id(),
                        AdvancementType.CHALLENGE))
                .parent(PIGLIN_MANOR_ADVANCEMENT.asParent())
                .rewards(AdvancementRewards.Builder.experience(500))
                .addCriterion("rescue_prisoner",
                        SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity()
                                .of(entityLookup, ModEntityTypes.PIGLIN_PRISONER.value())))
                .save(writer, RESCUE_PIGLIN_PRISONER_ADVANCEMENT.name());
        Advancement.Builder.advancement()
                .display(display(new ItemStack(Items.SADDLE), RIDE_WITHER_SKELETON_HORSE_ADVANCEMENT.id()))
                .parent(PIGLIN_MANOR_ADVANCEMENT.asParent())
                .addCriterion("ride_wither_skeleton_horse",
                        StartRidingTrigger.TriggerInstance.playerStartsRiding(EntityPredicate.Builder.entity()
                                .vehicle(EntityPredicate.Builder.entity()
                                        .of(entityLookup, ModEntityTypes.WITHER_SKELETON_HORSE.value())
                                        .passenger(EntityPredicate.Builder.entity()
                                                .of(entityLookup, EntityType.PLAYER)))))
                .save(writer, RIDE_WITHER_SKELETON_HORSE_ADVANCEMENT.name());
        Advancement.Builder.advancement()
                .display(display(new ItemStack(Items.ENDER_PEARL), SUMMON_ENDERMAN_ADVANCEMENT.id()))
                .parent(CITADEL_ADVANCEMENT.asParent())
                .addCriterion("summon_enderman",
                        SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity()
                                .of(entityLookup, ModEntityTypes.WARPED_ENDERMAN.value())))
                .save(writer, SUMMON_ENDERMAN_ADVANCEMENT.name());
    }

    private static ItemStack getNetheriteBootsDisplayItem(HolderLookup.Provider registries) {
        ItemStack itemStack = new ItemStack(Items.NETHERITE_BOOTS);
        itemStack.set(DataComponents.TRIM,
                new ArmorTrim(registries.lookupOrThrow(Registries.TRIM_MATERIAL).getOrThrow(TrimMaterials.GOLD),
                        registries.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(TrimPatterns.SENTRY)));
        return itemStack;
    }
}
