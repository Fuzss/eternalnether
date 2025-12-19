package fuzs.eternalnether.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.model.geom.ModModelLayers;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class GildedNetheriteShieldSpecialRenderer extends ShieldSpecialRenderer {
    public static final Material SHIELD_BASE = new Material(Sheets.SHIELD_SHEET,
            EternalNether.id("entity/gilded_netherite_shield_base"));
    public static final Material NO_PATTERN_SHIELD = new Material(Sheets.SHIELD_SHEET,
            EternalNether.id("entity/gilded_netherite_shield_base_nopattern"));

    private final MaterialSet materials;
    private final ShieldModel model;

    public GildedNetheriteShieldSpecialRenderer(MaterialSet materials, ShieldModel model) {
        super(materials, model);
        this.materials = materials;
        this.model = model;
    }

    /**
     * @see ShieldSpecialRenderer#submit(DataComponentMap, ItemDisplayContext, PoseStack, SubmitNodeCollector, int,
     *         int, boolean, int)
     */
    @Override
    public void submit(@Nullable DataComponentMap patterns, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor) {
        BannerPatternLayers bannerPatternLayers =
                patterns != null ? patterns.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY) :
                        BannerPatternLayers.EMPTY;
        DyeColor dyeColor = patterns != null ? patterns.get(DataComponents.BASE_COLOR) : null;
        boolean hasPattern = !bannerPatternLayers.layers().isEmpty() || dyeColor != null;
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        // use custom materials
        Material material = hasPattern ? SHIELD_BASE : NO_PATTERN_SHIELD;
        nodeCollector.submitModelPart(this.model.handle(),
                poseStack,
                this.model.renderType(material.atlasLocation()),
                packedLight,
                packedOverlay,
                this.materials.get(material),
                false,
                false,
                -1,
                null,
                outlineColor);
        if (hasPattern) {
            BannerRenderer.submitPatterns(this.materials,
                    poseStack,
                    nodeCollector,
                    packedLight,
                    packedOverlay,
                    this.model,
                    Unit.INSTANCE,
                    material,
                    false,
                    (DyeColor) Objects.requireNonNullElse(dyeColor, DyeColor.WHITE),
                    bannerPatternLayers,
                    hasFoil,
                    null,
                    outlineColor);
        } else {
            nodeCollector.submitModelPart(this.model.plate(),
                    poseStack,
                    this.model.renderType(material.atlasLocation()),
                    packedLight,
                    packedOverlay,
                    this.materials.get(material),
                    false,
                    hasFoil,
                    -1,
                    null,
                    outlineColor);
        }

        poseStack.popPose();
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final GildedNetheriteShieldSpecialRenderer.Unbaked INSTANCE = new GildedNetheriteShieldSpecialRenderer.Unbaked();
        public static final MapCodec<GildedNetheriteShieldSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public MapCodec<GildedNetheriteShieldSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(BakingContext context) {
            return new GildedNetheriteShieldSpecialRenderer(context.materials(),
                    new ShieldModel(context.entityModelSet().bakeLayer(ModModelLayers.GILDED_NETHERITE_SHIELD)));
        }
    }
}
