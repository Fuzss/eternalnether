package fuzs.eternalnether.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import fuzs.eternalnether.world.entity.monster.Wex;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class WexModel extends HumanoidModel<Wex> {
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public WexModel(ModelPart part) {
        super(part);
        this.leftLeg.visible = false;
        this.hat.visible = false;
        this.rightWing = part.getChild("right_wing");
        this.leftWing = part.getChild("left_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -1.0F, -2.0F, 6.0F, 10.0F, 4.0F),
                PartPose.offset(-1.9F, 12.0F, 0.0F));
        partDefinition.addOrReplaceChild("right_wing",
                CubeListBuilder.create().texOffs(0, 32).addBox(-20.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F),
                PartPose.ZERO);
        partDefinition.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(0, 32).mirror().addBox(0.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F),
                PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.rightWing, this.leftWing));
    }

    @Override
    public void setupAnim(Wex wex, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(wex, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (wex.isCharging()) {
            if (wex.getMainHandItem().isEmpty()) {
                this.rightArm.xRot = ((float) Math.PI * 1.5F);
                this.leftArm.xRot = ((float) Math.PI * 1.5F);
            } else if (wex.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArm.xRot = ((float) Math.PI * 1.2F);
            } else {
                this.leftArm.xRot = ((float) Math.PI * 1.2F);
            }
        }

        float f = ((float) Math.PI * 0.15F);
        this.rightLeg.xRot += ((float) Math.PI / 5F);
        this.rightWing.z = 2.0F;
        this.leftWing.z = 2.0F;
        this.rightWing.y = 1.0F;
        this.leftWing.y = 1.0F;
        this.rightWing.yRot = f + Mth.cos(ageInTicks * 45.836624F * ((float) Math.PI / 180F)) * (float) Math.PI * 0.05F;
        this.leftWing.yRot = -this.rightWing.yRot;
        this.leftWing.zRot = -f;
        this.leftWing.xRot = f;
        this.rightWing.xRot = f;
        this.rightWing.zRot = f;
    }
}
