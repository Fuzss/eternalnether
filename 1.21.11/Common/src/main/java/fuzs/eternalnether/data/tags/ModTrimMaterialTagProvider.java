package fuzs.eternalnether.data.tags;

import fuzs.eternalnether.init.ModTags;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;

public class ModTrimMaterialTagProvider extends AbstractTagProvider<TrimMaterial> {

    public ModTrimMaterialTagProvider(DataProviderContext context) {
        super(Registries.TRIM_MATERIAL, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        this.tag(ModTags.PIGLIN_SAFE_TRIM_MATERIAL_TAG_KEY).addKey(TrimMaterials.GOLD);
    }
}
