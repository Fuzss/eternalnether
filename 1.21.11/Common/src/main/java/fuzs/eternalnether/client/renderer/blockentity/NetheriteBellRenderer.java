package fuzs.eternalnether.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.model.geom.ModModelLayers;
import net.minecraft.client.model.object.bell.BellModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BellRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BellRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;

public class NetheriteBellRenderer extends BellRenderer {
    public static final Material NETHERITE_BELL_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS,
            EternalNether.id("entity/bell/netherite_bell_body"));

    private final MaterialSet materials;
    private final BellModel model;

    public NetheriteBellRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.materials = context.materials();
        this.model = new BellModel(context.bakeLayer(ModModelLayers.NETHERITE_BELL));
    }

    /**
     * @see BellRenderer#submit(BellRenderState, PoseStack, SubmitNodeCollector, CameraRenderState)
     */
    @Override
    public void submit(BellRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        // use custom model and material
        BellModel.State state = new BellModel.State(renderState.ticks, renderState.shakeDirection);
        this.model.setupAnim(state);
        RenderType renderType = NETHERITE_BELL_MATERIAL.renderType(RenderTypes::entitySolid);
        nodeCollector.submitModel(this.model,
                state,
                poseStack,
                renderType,
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                this.materials.get(BELL_TEXTURE),
                0,
                renderState.breakProgress);
    }
}
