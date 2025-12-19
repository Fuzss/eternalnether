package fuzs.eternalnether.world.entity.ai.goal;

import fuzs.eternalnether.world.entity.monster.ShieldedMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;

public class ShieldDefenseGoal<M extends Mob & ShieldedMob, T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private final M mob;
    private int shieldCooldownPeriod;
    private int shieldWarmupPeriod;

    public ShieldDefenseGoal(M mob, Class<T> targetType) {
        super(mob, targetType, true);
        this.mob = mob;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !this.mob.isShieldDisabled() && this.shieldCooldownPeriod > 0;
    }

    @Override
    public void start() {
        super.start();
        this.shieldWarmupPeriod = this.adjustedTickDelay(3 + this.mob.getRandom().nextInt(3));
        this.shieldCooldownPeriod = this.adjustedTickDelay(15 + this.mob.getRandom().nextInt(25));
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.stopUsingShield();
    }

    @Override
    public void tick() {
        if (this.target != null && isUsingProjectileWeapon(this.target)) {
            if (this.shieldWarmupPeriod > 0) {
                this.shieldWarmupPeriod--;
            } else {
                this.mob.getLookControl()
                        .setLookAt(this.target.getX(),
                                this.target.getEyeY(),
                                this.target.getZ(),
                                10.0F,
                                (float) this.mob.getMaxHeadXRot());
                this.mob.startUsingShield();
            }
        } else if (this.shieldCooldownPeriod > 0) {
            this.shieldCooldownPeriod--;
        }
    }

    @Override
    protected void findTarget() {
        super.findTarget();
        if (this.target != null && !isUsingProjectileWeapon(this.target)) {
            this.target = null;
        }
    }

    private static boolean isUsingProjectileWeapon(LivingEntity livingEntity) {
        return livingEntity.isHolding((ItemStack itemStack) -> {
            return (livingEntity.isUsingItem() || CrossbowItem.isCharged(itemStack))
                    && itemStack.getItem() instanceof ProjectileWeaponItem projectileWeaponItem && (
                    !(livingEntity instanceof Mob mob) || mob.canFireProjectileWeapon(projectileWeaponItem));
        });
    }
}
