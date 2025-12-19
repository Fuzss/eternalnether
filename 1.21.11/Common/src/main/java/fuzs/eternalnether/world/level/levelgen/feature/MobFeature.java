package fuzs.eternalnether.world.level.levelgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MobFeature extends Feature<NoneFeatureConfiguration> {
    private final WeightedList<Holder<? extends EntityType<? extends Mob>>> entityTypes;

    public MobFeature(Holder<? extends EntityType<? extends Mob>> entityType) {
        this(WeightedList.of(entityType));
    }

    public MobFeature(WeightedList<Holder<? extends EntityType<? extends Mob>>> entityTypes) {
        super(NoneFeatureConfiguration.CODEC);
        this.entityTypes = entityTypes;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos position = context.origin().below();
        Mob mob = this.entityTypes.getRandom(context.random()).map(Holder::value).map(entityType -> {
            return entityType.create(context.level().getLevel(), EntitySpawnReason.SPAWNER);
        }).orElse(null);
        if (mob != null) {
            mob.snapTo(position.getX() + 0.5, position.getY(), position.getZ() + 0.5, 0.0F, 0.0F);
            mob.finalizeSpawn(context.level(),
                    context.level().getCurrentDifficultyAt(position),
                    EntitySpawnReason.SPAWNER,
                    null);
            mob.setPersistenceRequired();
            context.level().addFreshEntity(mob);
            return true;
        } else {
            return false;
        }
    }
}
