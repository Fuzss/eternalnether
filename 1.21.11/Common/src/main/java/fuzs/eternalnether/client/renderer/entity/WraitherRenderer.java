package fuzs.eternalnether.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.model.geom.ModModelLayers;
import fuzs.eternalnether.client.renderer.entity.state.WraitherRenderState;
import fuzs.eternalnether.world.entity.monster.Wraither;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class WraitherRenderer extends AbstractSkeletonRenderer<Wraither, WraitherRenderState> {
    private static final Identifier TEXTURE_LOCATION = EternalNether.id("textures/entity/skeleton/wraither.png");
    private static final Identifier POSSESSED_TEXTURE_LOCATION = EternalNether.id(
            "textures/entity/skeleton/wraither_possessed.png");
    private static final RenderType EYES_RENDER_TYPE = RenderTypes.eyes(EternalNether.id(
            "textures/entity/skeleton/wraither_eyes.png"));

    public WraitherRenderer(EntityRendererProvider.Context context) {
        super(context, ModModelLayers.WRAITHER, ModModelLayers.WRAITHER_ARMOR);
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, WraitherRenderState renderState, float yRot, float xRot) {
                if (renderState.isPossessed) {
                    super.submit(poseStack, nodeCollector, packedLight, renderState, yRot, xRot);
                }
            }

            @Override
            public RenderType renderType() {
                return EYES_RENDER_TYPE;
            }
        });
    }

    @Override
    public Identifier getTextureLocation(WraitherRenderState renderState) {
        return renderState.isPossessed ? POSSESSED_TEXTURE_LOCATION : TEXTURE_LOCATION;
    }

    @Override
    public WraitherRenderState createRenderState() {
        return new WraitherRenderState();
    }

    @Override
    public void extractRenderState(Wraither wraither, WraitherRenderState renderState, float partialTick) {
        super.extractRenderState(wraither, renderState, partialTick);
        renderState.isPossessed = wraither.isPossessed();
    }
}
