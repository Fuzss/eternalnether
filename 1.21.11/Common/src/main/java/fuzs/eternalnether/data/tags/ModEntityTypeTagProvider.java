package fuzs.eternalnether.data.tags;

import fuzs.eternalnether.init.ModEntityTypes;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTagProvider extends AbstractTagProvider<EntityType<?>> {

    public ModEntityTypeTagProvider(DataProviderContext context) {
        super(Registries.ENTITY_TYPE, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        this.tag(EntityTypeTags.SKELETONS)
                .add(ModEntityTypes.CORPOR.value(),
                        ModEntityTypes.WITHER_SKELETON_HORSE.value(),
                        ModEntityTypes.WITHER_SKELETON_KNIGHT.value(),
                        ModEntityTypes.WRAITHER.value());
        this.tag("enderzoology:concussion_immune").add(ModEntityTypes.WARPED_ENDERMAN.value());
    }
}
