package fuzs.eternalnether.client.renderer.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.model.geom.ModModelLayers;
import fuzs.eternalnether.client.renderer.entity.state.WarpedEndermanRenderState;
import fuzs.eternalnether.world.entity.monster.WarpedEnderman;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.monster.enderman.EndermanModel;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.EnderMan;

import java.util.Map;

public class WarpedEndermanRenderer extends EndermanRenderer {
    private static final Map<WarpedEnderman.Variant, Identifier> TEXTURE_LOCATIONS = Maps.immutableEnumMap(ImmutableMap.of(
            WarpedEnderman.Variant.FRESH,
            EternalNether.id("textures/entity/enderman/warped_enderman_fresh.png"),
            WarpedEnderman.Variant.SHORT_VINE,
            EternalNether.id("textures/entity/enderman/warped_enderman_short_vine.png"),
            WarpedEnderman.Variant.LONG_VINE,
            EternalNether.id("textures/entity/enderman/warped_enderman_long_vine.png")));

    public WarpedEndermanRenderer(Context context) {
        super(context);
        this.model = new EndermanModel<>(context.bakeLayer(ModModelLayers.WARPED_ENDERMAN));
    }

    public static LayerDefinition createBodyLayer() {
        return EndermanModel.createBodyLayer().apply((MeshDefinition meshDefinition) -> {
            modifyMesh(meshDefinition);
            return meshDefinition;
        });
    }

    private static void modifyMesh(MeshDefinition meshDefinition) {
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.getChild("head")
                .addOrReplaceChild("stem_body",
                        CubeListBuilder.create()
                                .texOffs(44, 0)
                                .mirror()
                                .addBox(-10.0F, -18.0F, 0.0F, 6.0F, 16.0F, 0.0F),
                        PartPose.ZERO);
        partDefinition.getChild("left_arm")
                .addOrReplaceChild("stem_arm",
                        CubeListBuilder.create().texOffs(32, 0).addBox(1.0F, -1.0F, 0.0F, 6.0F, 16.0F, 0.0F),
                        PartPose.ZERO);
    }

    @Override
    public EndermanRenderState createRenderState() {
        return new WarpedEndermanRenderState();
    }

    @Override
    public void extractRenderState(EnderMan enderMan, EndermanRenderState endermanRenderState, float partialTick) {
        super.extractRenderState(enderMan, endermanRenderState, partialTick);
        ((WarpedEndermanRenderState) endermanRenderState).variant = ((WarpedEnderman) enderMan).getVariant();
    }

    @Override
    public Identifier getTextureLocation(EndermanRenderState renderState) {
        return TEXTURE_LOCATIONS.get(((WarpedEndermanRenderState) renderState).variant);
    }
}
