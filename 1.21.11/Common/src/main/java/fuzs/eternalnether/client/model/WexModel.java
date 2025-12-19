package fuzs.eternalnether.client.model;

import fuzs.eternalnether.client.renderer.entity.state.WexRenderState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * Adapted from {@link net.minecraft.client.model.VexModel} from before Minecraft 1.19.3.
 */
public class WexModel extends HumanoidModel<WexRenderState> {
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public WexModel(ModelPart part) {
        super(part);
        this.rightWing = part.getChild("right_wing");
        this.leftWing = part.getChild("left_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = SkeletonModel.createBodyLayer().mesh;
        modifyMesh(meshDefinition);
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    private static void modifyMesh(MeshDefinition meshDefinition) {
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.clearChild("right_leg");
        partDefinition.clearChild("left_leg");
        partDefinition.addOrReplaceChild("right_wing",
                CubeListBuilder.create().texOffs(0, 32).addBox(-20.0F, 0.0F, 0.0F, 20.0F, 12.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, Mth.PI * 0.15F, Mth.PI * 0.15F, Mth.PI * 0.15F));
        partDefinition.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(0, 32).mirror().addBox(0.0F, 0.0F, 0.0F, 20.0F, 12.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, Mth.PI * 0.15F, Mth.PI * -0.15F, Mth.PI * -0.15F));
    }

    @Override
    public void setupAnim(WexRenderState renderState) {
        super.setupAnim(renderState);
        if (renderState.isCharging) {
            if (renderState.getMainHandItem().isEmpty()) {
                this.rightArm.xRot = Mth.PI * 1.5F;
                this.leftArm.xRot = Mth.PI * 1.5F;
            } else {
                if (!renderState.rightHandItem.isEmpty()) {
                    this.rightArm.xRot = Mth.PI * 1.2F;
                }
                if (!renderState.leftHandItem.isEmpty()) {
                    this.leftArm.xRot = Mth.PI * 1.2F;
                }
            }
        }

        float flapAmount = Mth.cos(renderState.ageInTicks * 45.836624F * Mth.DEG_TO_RAD) * Mth.PI * 0.05F;
        this.rightWing.yRot += flapAmount;
        this.leftWing.yRot -= flapAmount;
    }
}
