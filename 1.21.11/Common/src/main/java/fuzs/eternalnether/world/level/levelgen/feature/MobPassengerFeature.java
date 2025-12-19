package fuzs.eternalnether.world.level.levelgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jspecify.annotations.Nullable;

public class MobPassengerFeature extends Feature<NoneFeatureConfiguration> {
    private final Holder<? extends EntityType<? extends Mob>> passengerEntityType;
    private final Holder<? extends EntityType<? extends Mob>> vehicleEntityType;

    public MobPassengerFeature(Holder<? extends EntityType<? extends Mob>> passengerEntityType, Holder<? extends EntityType<? extends Mob>> vehicleEntityType) {
        super(NoneFeatureConfiguration.CODEC);
        this.passengerEntityType = passengerEntityType;
        this.vehicleEntityType = vehicleEntityType;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Mob passenger = this.createMob(context, this.passengerEntityType);
        Mob vehicle = this.createMob(context, this.vehicleEntityType);
        if (passenger != null && vehicle != null) {
            passenger.startRiding(vehicle);
            context.level().addFreshEntityWithPassengers(vehicle);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    private Mob createMob(FeaturePlaceContext<NoneFeatureConfiguration> context, Holder<? extends EntityType<? extends Mob>> entityType) {
        BlockPos blockPos = context.origin().below();
        Mob mob = entityType.value().create(context.level().getLevel(), EntitySpawnReason.SPAWNER);
        if (mob != null) {
            mob.finalizeSpawn(context.level(),
                    context.level().getCurrentDifficultyAt(blockPos),
                    EntitySpawnReason.SPAWNER,
                    null);
            mob.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            mob.setPersistenceRequired();
            return mob;
        } else {
            return null;
        }
    }
}
