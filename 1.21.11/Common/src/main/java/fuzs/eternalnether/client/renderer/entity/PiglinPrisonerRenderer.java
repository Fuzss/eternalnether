package fuzs.eternalnether.client.renderer.entity;

import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.model.geom.ModModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.client.renderer.entity.state.PiglinRenderState;
import net.minecraft.resources.Identifier;

public class PiglinPrisonerRenderer extends PiglinRenderer {
    private static final Identifier TEXTURE_LOCATION = EternalNether.id(
            "textures/entity/piglin/piglin_prisoner.png");

    public PiglinPrisonerRenderer(EntityRendererProvider.Context context) {
        super(context,
                ModModelLayers.PIGLIN_PRISONER,
                ModModelLayers.PIGLIN_PRISONER_BABY,
                ModModelLayers.PIGLIN_PRISONER_ARMOR,
                ModModelLayers.PIGLIN_PRISONER_BABY_ARMOR);
    }

    @Override
    public Identifier getTextureLocation(PiglinRenderState renderState) {
        return TEXTURE_LOCATION;
    }
}
