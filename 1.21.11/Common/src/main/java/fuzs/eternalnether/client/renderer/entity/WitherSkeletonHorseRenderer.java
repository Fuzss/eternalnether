package fuzs.eternalnether.client.renderer.entity;

import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.model.geom.ModModelLayers;
import net.minecraft.client.model.AbstractEquineModel;
import net.minecraft.client.model.EquineSaddleModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class WitherSkeletonHorseRenderer extends AbstractHorseRenderer<AbstractHorse, EquineRenderState, AbstractEquineModel<EquineRenderState>> {
    private static final Type WITHER_SKELETON_HORSE = new Type(EternalNether.id(
            "textures/entity/horse/wither_skeleton_horse.png"),
            ModModelLayers.WITHER_SKELETON_HORSE,
            ModModelLayers.WITHER_SKELETON_HORSE_BABY,
            EquipmentClientInfo.LayerType.SKELETON_HORSE_SADDLE,
            ModModelLayers.WITHER_SKELETON_HORSE_SADDLE,
            ModModelLayers.WITHER_SKELETON_HORSE_BABY_SADDLE);

    private final ResourceLocation texture;

    public WitherSkeletonHorseRenderer(EntityRendererProvider.Context context) {
        this(context, WITHER_SKELETON_HORSE);
    }

    public WitherSkeletonHorseRenderer(EntityRendererProvider.Context context, Type type) {
        super(context,
                new HorseModel(context.bakeLayer(type.model)),
                new HorseModel(context.bakeLayer(type.babyModel)));
        this.texture = type.texture;
        this.addLayer(new SimpleEquipmentLayer<>(this,
                context.getEquipmentRenderer(),
                type.saddleLayer,
                state -> state.saddle,
                new EquineSaddleModel(context.bakeLayer(type.saddleModel)),
                new EquineSaddleModel(context.bakeLayer(type.babySaddleModel))));
    }

    @Override
    public ResourceLocation getTextureLocation(EquineRenderState renderState) {
        return this.texture;
    }

    @Override
    public EquineRenderState createRenderState() {
        return new EquineRenderState();
    }

    public record Type(ResourceLocation texture,
                       ModelLayerLocation model,
                       ModelLayerLocation babyModel,
                       EquipmentClientInfo.LayerType saddleLayer,
                       ModelLayerLocation saddleModel,
                       ModelLayerLocation babySaddleModel) {
    }
}
