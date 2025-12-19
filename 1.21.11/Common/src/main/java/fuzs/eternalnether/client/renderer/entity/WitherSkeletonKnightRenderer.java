package fuzs.eternalnether.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.model.geom.ModModelLayers;
import fuzs.eternalnether.client.renderer.entity.state.WitherSkeletonKnightRenderState;
import fuzs.eternalnether.world.entity.monster.WitherSkeletonKnight;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.SkeletonClothingLayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class WitherSkeletonKnightRenderer extends AbstractSkeletonRenderer<WitherSkeletonKnight, WitherSkeletonKnightRenderState> {
    private static final ResourceLocation TEXTURE_LOCATION = EternalNether.id(
            "textures/entity/skeleton/wither_skeleton_knight.png");
    private static final ResourceLocation OVERLAY_TEXTURE_LOCATION = EternalNether.id(
            "textures/entity/skeleton/wither_skeleton_knight_overlay.png");

    public WitherSkeletonKnightRenderer(EntityRendererProvider.Context context) {
        super(context, ModModelLayers.WITHER_SKELETON_KNIGHT, ModModelLayers.WITHER_SKELETON_KNIGHT_ARMOR);
        this.addLayer(new SkeletonClothingLayer<>(this,
                context.getModelSet(),
                ModModelLayers.WITHER_SKELETON_KNIGHT_OUTER_LAYER,
                OVERLAY_TEXTURE_LOCATION) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, WitherSkeletonKnightRenderState renderState, float yRot, float xRot) {
                if (!renderState.isDisarmored) {
                    super.submit(poseStack, nodeCollector, packedLight, renderState, yRot, xRot);
                }
            }
        });
    }

    @Override
    public WitherSkeletonKnightRenderState createRenderState() {
        return new WitherSkeletonKnightRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(WitherSkeletonKnightRenderState renderState) {
        return TEXTURE_LOCATION;
    }

    @Override
    public void extractRenderState(WitherSkeletonKnight witherSkeletonKnight, WitherSkeletonKnightRenderState renderState, float partialTick) {
        super.extractRenderState(witherSkeletonKnight, renderState, partialTick);
        renderState.isDisarmored = witherSkeletonKnight.isDisarmored();
        // this is a hack to prevent the skeleton model from messing up arms for the shield blocking pose
        renderState.isAggressive &= !witherSkeletonKnight.isUsingShield();
    }

    @Override
    protected HumanoidModel.ArmPose getArmPose(WitherSkeletonKnight witherSkeletonKnight, HumanoidArm humanoidArm) {
        ItemStack itemStack = witherSkeletonKnight.getItemHeldByArm(humanoidArm);
        return witherSkeletonKnight.isUsingShield() && itemStack.has(DataComponents.BLOCKS_ATTACKS) ?
                HumanoidModel.ArmPose.BLOCK : HumanoidModel.ArmPose.EMPTY;
    }
}
