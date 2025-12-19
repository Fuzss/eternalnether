package fuzs.eternalnether.data.tags;

import fuzs.eternalnether.init.ModItems;
import fuzs.eternalnether.init.ModTags;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ModItemTagProvider extends AbstractTagProvider<Item> {

    public ModItemTagProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.STONE_CRAFTING_MATERIALS).add(ModItems.COBBLED_BLACKSTONE.value());
        this.tag(ItemTags.STONE_TOOL_MATERIALS).add(ModItems.COBBLED_BLACKSTONE.value());
        this.tag("c:music_discs").add(ModItems.WITHER_WALTZ_MUSIC_DISC.value());
        this.tag("c:bones").add(ModItems.WITHERED_BONE.value());
        this.tag("c:fertilizers").add(ModItems.WITHERED_BONE_MEAL.value());
        this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(ModItems.GILDED_NETHERITE_SHIELD.value());
        this.tag("c:tools/shield").add(ModItems.GILDED_NETHERITE_SHIELD.value());
        this.tag(ItemTags.SWORDS).add(ModItems.CUTLASS.value());
        this.tag("c:tools/melee_weapon").add(ModItems.CUTLASS.value());
        this.tag(ModTags.PIGLIN_BRUTE_SAFE_ARMOR_ITEM_TAG_KEY)
                .add(Items.NETHERITE_HELMET,
                        Items.NETHERITE_CHESTPLATE,
                        Items.NETHERITE_LEGGINGS,
                        Items.NETHERITE_BOOTS);
    }
}
