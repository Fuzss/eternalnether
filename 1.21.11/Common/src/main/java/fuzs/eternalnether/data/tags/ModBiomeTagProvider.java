package fuzs.eternalnether.data.tags;

import fuzs.eternalnether.init.ModTags;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class ModBiomeTagProvider extends AbstractTagProvider<Biome> {

    public ModBiomeTagProvider(DataProviderContext context) {
        super(Registries.BIOME, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.HAS_CATACOMB_BIOME_TAG_KEY)
                .addKey(Biomes.SOUL_SAND_VALLEY)
                .addOptionalTag("biomesoplenty:withered_abyss",
                        "incendium:weeping_valley",
                        "incendium:withered_forest",
                        "byg:wailing_garth");
        this.tag(ModTags.HAS_CITADEL_BIOME_TAG_KEY)
                .addKey(Biomes.WARPED_FOREST)
                .addOptionalTag("incendium:inverted_forest", "byg:glowstone_gardens");
        this.tag(ModTags.HAS_PIGLIN_MANOR_BIOME_TAG_KEY)
                .addKey(Biomes.CRIMSON_FOREST)
                .addOptionalTag("incendium:ash_barrens", "byg:crimson_gardens");
    }
}
