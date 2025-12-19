package fuzs.eternalnether.client.renderer.entity;

import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.model.geom.ModModelLayers;
import fuzs.eternalnether.client.renderer.entity.layers.HoglinSkullLayer;
import fuzs.eternalnether.world.entity.monster.ShieldedMob;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.client.renderer.entity.state.PiglinRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.ItemStack;

public class PiglinHunterRenderer extends PiglinRenderer {
    private static final ResourceLocation TEXTURE_LOCATION = EternalNether.id("textures/entity/piglin/piglin_hunter.png");

    public PiglinHunterRenderer(EntityRendererProvider.Context context) {
        super(context,
                ModModelLayers.PIGLIN_HUNTER,
                ModModelLayers.PIGLIN_HUNTER_BABY,
                ModModelLayers.PIGLIN_HUNTER_ARMOR,
                ModModelLayers.PIGLIN_HUNTER_BABY_ARMOR);
        this.addLayer(new HoglinSkullLayer(this, context.getModelSet(), context.getEquipmentAssets()));
    }

    @Override
    public ResourceLocation getTextureLocation(PiglinRenderState renderState) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected HumanoidModel.ArmPose getArmPose(AbstractPiglin abstractPiglin, HumanoidArm humanoidArm) {
        ItemStack itemStack = abstractPiglin.getItemHeldByArm(humanoidArm);
        return ((ShieldedMob) abstractPiglin).isUsingShield() && itemStack.has(DataComponents.BLOCKS_ATTACKS) ?
                HumanoidModel.ArmPose.BLOCK : HumanoidModel.ArmPose.EMPTY;
    }
}
