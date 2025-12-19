package fuzs.eternalnether.world.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.PathType;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;

/**
 * @see net.minecraft.world.entity.ai.goal.FollowOwnerGoal
 */
public class FollowOwnerGoal<T extends PathfinderMob & OwnableEntity> extends Goal {
    private final T tamable;
    @Nullable
    private LivingEntity owner;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;

    public FollowOwnerGoal(T tamable, double speedModifier, float startDistance, float stopDistance) {
        this.tamable = tamable;
        this.speedModifier = speedModifier;
        this.navigation = tamable.getNavigation();
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(tamable.getNavigation() instanceof GroundPathNavigation)
                && !(tamable.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.tamable.getOwner();
        if (livingEntity == null) {
            return false;
        } else if (OwnableEntityHelper.unableToMoveToOwner(this.tamable)) {
            return false;
        } else if (this.tamable.distanceToSqr(livingEntity) < this.startDistance * this.startDistance) {
            return false;
        } else {
            this.owner = livingEntity;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else {
            return !OwnableEntityHelper.unableToMoveToOwner(this.tamable) && !(this.tamable.distanceToSqr(this.owner)
                    <= this.stopDistance * this.stopDistance);
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.tamable.getPathfindingMalus(PathType.WATER);
        this.tamable.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.tamable.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        boolean bl = OwnableEntityHelper.shouldTryTeleportToOwner(this.tamable);
        if (!bl) {
            this.tamable.getLookControl().setLookAt(this.owner, 10.0F, this.tamable.getMaxHeadXRot());
        }

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (bl) {
                OwnableEntityHelper.tryToTeleportToOwner(this.tamable);
            } else {
                this.navigation.moveTo(this.owner, this.speedModifier);
            }
        }
    }
}
