package fuzs.eternalnether.client.renderer.entity;

import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.model.geom.ModModelLayers;
import fuzs.eternalnether.world.entity.monster.Corpor;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.monster.skeleton.SkeletonModel;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.resources.Identifier;

public class CorporRenderer extends AbstractSkeletonRenderer<Corpor, SkeletonRenderState> {
    private static final Identifier TEXTURE_LOCATION = EternalNether.id("textures/entity/skeleton/corpor.png");

    public CorporRenderer(EntityRendererProvider.Context context) {
        super(context, ModModelLayers.CORPOR, ModModelLayers.CORPOR_ARMOR);
    }

    public static LayerDefinition createBodyLayer() {
        return SkeletonModel.createBodyLayer().apply((MeshDefinition meshDefinition) -> {
            modifyMesh(meshDefinition);
            return meshDefinition;
        });
    }

    private static void modifyMesh(MeshDefinition meshDefinition) {
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -9.0F, -4.0F, 9.0F, 10.0F, 6.0F),
                PartPose.ZERO);
    }

    @Override
    public Identifier getTextureLocation(SkeletonRenderState renderState) {
        return TEXTURE_LOCATION;
    }

    @Override
    public SkeletonRenderState createRenderState() {
        return new SkeletonRenderState();
    }
}
