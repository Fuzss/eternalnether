package fuzs.eternalnether.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.model.geom.ModModelLayers;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.monster.hoglin.HoglinModel;
import net.minecraft.client.model.monster.piglin.PiglinModel;
import net.minecraft.client.model.player.PlayerCapeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PiglinRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

import java.util.Collections;

public class HoglinSkullLayer extends RenderLayer<PiglinRenderState, PiglinModel> {
    private static final Identifier SKULL_TEXTURE_LOCATION = EternalNether.id(
            "textures/entity/piglin/piglin_hunter_skull.png");

    private final HumanoidModel<PiglinRenderState> model;
    private final EquipmentAssetManager equipmentAssets;

    public HoglinSkullLayer(RenderLayerParent<PiglinRenderState, PiglinModel> renderLayerParent, EntityModelSet entityModels, EquipmentAssetManager equipmentAssets) {
        super(renderLayerParent);
        this.model = new HumanoidModel<>(entityModels.bakeLayer(ModModelLayers.PIGLIN_HUNTER_SKULL));
        this.equipmentAssets = equipmentAssets;
    }

    /**
     * @see PlayerCapeModel#createCapeLayer()
     * @see HoglinModel#createBodyLayer()
     */
    public static LayerDefinition createSkullLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.retainExactParts(Collections.emptySet());

        PartDefinition partDefinition3 = partDefinition.getChild("body");
        PartDefinition partDefinition4 = partDefinition3.addOrReplaceChild("skull",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, -8.9F, -3.3F, 0.87266463F * 0.75F, Mth.PI, 0.0F));

        PartDefinition partDefinition5 = partDefinition4.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(61, 1).addBox(-7.0F, -3.0F, -19.0F, 14.0F, 6.0F, 19.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, -12.0F, 0.87266463F, 0.0F, 0.0F));
        partDefinition5.addOrReplaceChild("right_ear",
                CubeListBuilder.create().texOffs(1, 1).addBox(-6.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(-6.0F, -2.0F, -3.0F, 0.0F, 0.0F, (float) (-Math.PI * 2.0 / 9.0)));
        partDefinition5.addOrReplaceChild("left_ear",
                CubeListBuilder.create().texOffs(1, 6).addBox(0.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(6.0F, -2.0F, -3.0F, 0.0F, 0.0F, (float) (Math.PI * 2.0 / 9.0)));
        partDefinition5.addOrReplaceChild("right_horn",
                CubeListBuilder.create().texOffs(10, 13).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F),
                PartPose.offset(-7.0F, 2.0F, -12.0F));
        partDefinition5.addOrReplaceChild("left_horn",
                CubeListBuilder.create().texOffs(1, 13).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F),
                PartPose.offset(7.0F, 2.0F, -12.0F));

        return LayerDefinition.create(meshDefinition, 128, 64);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, PiglinRenderState renderState, float yRot, float xRot) {
        if (!renderState.isBaby) {
            poseStack.pushPose();
            if (this.hasLayer(renderState.chestEquipment, EquipmentClientInfo.LayerType.HUMANOID)) {
                poseStack.translate(0.0F, 0.0F, 0.06875F);
            }

            if (renderState.isPassenger) {
                poseStack.translate(0.0F, -0.4375F, 0.0F);
            }

            nodeCollector.submitModel(this.model,
                    renderState,
                    poseStack,
                    RenderTypes.entitySolid(SKULL_TEXTURE_LOCATION),
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    renderState.outlineColor,
                    null);
            poseStack.popPose();
        }
    }

    private boolean hasLayer(ItemStack stack, EquipmentClientInfo.LayerType layer) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable != null && !equippable.assetId().isEmpty()) {
            EquipmentClientInfo equipmentClientInfo = this.equipmentAssets.get(equippable.assetId().get());
            return !equipmentClientInfo.getLayers(layer).isEmpty();
        } else {
            return false;
        }
    }
}
