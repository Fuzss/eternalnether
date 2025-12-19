package fuzs.eternalnether.client.renderer.entity;

import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.model.WexModel;
import fuzs.eternalnether.client.model.geom.ModModelLayers;
import fuzs.eternalnether.client.renderer.entity.state.WexRenderState;
import fuzs.eternalnether.world.entity.monster.Wex;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class WexRenderer extends HumanoidMobRenderer<Wex, WexRenderState, WexModel> {
    private static final ResourceLocation TEXTURE_LOCATION = EternalNether.id("textures/entity/skeleton/wex.png");
    private static final ResourceLocation CHARGING_TEXTURE_LOCATION = EternalNether.id(
            "textures/entity/skeleton/wex_charging.png");

    public WexRenderer(Context context) {
        super(context, new WexModel(context.bakeLayer(ModModelLayers.WEX)), 0.3F);
    }

    @Override
    protected int getBlockLightLevel(Wex wex, BlockPos blockPos) {
        return 15;
    }

    @Override
    public WexRenderState createRenderState() {
        return new WexRenderState();
    }

    @Override
    public void extractRenderState(Wex wex, WexRenderState renderState, float partialTick) {
        super.extractRenderState(wex, renderState, partialTick);
        renderState.isCharging = wex.isCharging();
    }

    @Override
    public ResourceLocation getTextureLocation(WexRenderState renderState) {
        return renderState.isCharging ? CHARGING_TEXTURE_LOCATION : TEXTURE_LOCATION;
    }
}
