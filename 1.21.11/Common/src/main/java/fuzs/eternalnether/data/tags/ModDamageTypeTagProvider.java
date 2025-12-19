package fuzs.eternalnether.data.tags;

import fuzs.eternalnether.init.ModTags;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypeTagProvider extends AbstractTagProvider<DamageType> {

    public ModDamageTypeTagProvider(DataProviderContext context) {
        super(Registries.DAMAGE_TYPE, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        this.tag(ModTags.BYPASSES_CUTLASS_DAMAGE_TYPE_TAG_KEY).addTag(DamageTypeTags.BYPASSES_SHIELD);
    }
}
