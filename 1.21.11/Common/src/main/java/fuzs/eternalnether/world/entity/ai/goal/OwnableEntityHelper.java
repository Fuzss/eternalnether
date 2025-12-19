package fuzs.eternalnether.world.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * @see net.minecraft.world.entity.TamableAnimal
 */
public class OwnableEntityHelper {

    public static <T extends PathfinderMob & OwnableEntity> void tryToTeleportToOwner(T mob) {
        LivingEntity livingEntity = mob.getOwner();
        if (livingEntity != null) {
            teleportToAroundBlockPos(mob, livingEntity.blockPosition());
        }
    }

    public static <T extends PathfinderMob & OwnableEntity> boolean shouldTryTeleportToOwner(T mob) {
        LivingEntity livingEntity = mob.getOwner();
        return livingEntity != null && mob.distanceToSqr(mob.getOwner()) >= 144.0;
    }

    private static <T extends PathfinderMob & OwnableEntity> void teleportToAroundBlockPos(T mob, BlockPos pos) {
        for (int i = 0; i < 10; i++) {
            int j = mob.getRandom().nextIntBetweenInclusive(-3, 3);
            int k = mob.getRandom().nextIntBetweenInclusive(-3, 3);
            if (Math.abs(j) >= 2 || Math.abs(k) >= 2) {
                int l = mob.getRandom().nextIntBetweenInclusive(-1, 1);
                if (maybeTeleportTo(mob, pos.getX() + j, pos.getY() + l, pos.getZ() + k)) {
                    return;
                }
            }
        }
    }

    private static <T extends PathfinderMob & OwnableEntity> boolean maybeTeleportTo(T mob, int x, int y, int z) {
        if (!canTeleportTo(mob, new BlockPos(x, y, z))) {
            return false;
        } else {
            mob.snapTo(x + 0.5, y, z + 0.5, mob.getYRot(), mob.getXRot());
            mob.getNavigation().stop();
            return true;
        }
    }

    private static <T extends PathfinderMob & OwnableEntity> boolean canTeleportTo(T mob, BlockPos pos) {
        PathType pathType = WalkNodeEvaluator.getPathTypeStatic(mob, pos);
        if (pathType != PathType.WALKABLE) {
            return false;
        } else {
            BlockState blockState = mob.level().getBlockState(pos.below());
            if (blockState.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockPos = pos.subtract(mob.blockPosition());
                return mob.level().noCollision(mob, mob.getBoundingBox().move(blockPos));
            }
        }
    }

    public static <T extends PathfinderMob & OwnableEntity> boolean unableToMoveToOwner(T mob) {
        return mob.isPassenger() || mob.mayBeLeashed() || mob.getOwner() != null && mob.getOwner().isSpectator();
    }
}
