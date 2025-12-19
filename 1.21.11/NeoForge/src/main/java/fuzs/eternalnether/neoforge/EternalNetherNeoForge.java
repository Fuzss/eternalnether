package fuzs.eternalnether.neoforge;

import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.data.ModAdvancementProvider;
import fuzs.eternalnether.data.ModRecipeProvider;
import fuzs.eternalnether.data.loot.ModBlockLootProvider;
import fuzs.eternalnether.data.loot.ModChestLootProvider;
import fuzs.eternalnether.data.loot.ModEntityTypeLootProvider;
import fuzs.eternalnether.data.loot.ModShearingLootProvider;
import fuzs.eternalnether.data.tags.*;
import fuzs.eternalnether.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(EternalNether.MOD_ID)
public class EternalNetherNeoForge {

    public EternalNetherNeoForge() {
        ModConstructor.construct(EternalNether.MOD_ID, EternalNether::new);
        DataProviderHelper.registerDataProviders(EternalNether.MOD_ID,
                ModRegistry.REGISTRY_SET_BUILDER,
                ModBlockTagProvider::new,
                ModItemTagProvider::new,
                ModEntityTypeTagProvider::new,
                ModBiomeTagProvider::new,
                ModTrimMaterialTagProvider::new,
                ModDamageTypeTagProvider::new,
                ModBlockLootProvider::new,
                ModEntityTypeLootProvider::new,
                ModChestLootProvider::new,
                ModShearingLootProvider::new,
                ModRecipeProvider::new,
                ModAdvancementProvider::new);
    }
}
