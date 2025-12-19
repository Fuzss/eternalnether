package fuzs.eternalnether.world.entity.monster.piglin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public abstract class CrossbowAttackPiglin extends GoalBasedPiglin implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(
            CrossbowAttackPiglin.class,
            EntityDataSerializers.BOOLEAN);

    public CrossbowAttackPiglin(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_CHARGING_CROSSBOW, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new RangedCrossbowAttackGoal<>(this, 1.0, 8.0F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, false) {
            @Override
            public boolean canUse() {
                return !MeleeAttack.isHoldingUsableProjectileWeapon(this.mob) && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !MeleeAttack.isHoldingUsableProjectileWeapon(this.mob) && super.canContinueToUse();
            }

            @Override
            public void stop() {
                super.stop();
                this.mob.setAggressive(false);
            }

            @Override
            public void start() {
                super.start();
                this.mob.setAggressive(true);
            }
        });
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnReason != EntitySpawnReason.STRUCTURE && this.isAdult()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, this.createSpawnWeapon());
        }

        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        this.populateDefaultEquipmentEnchantments(level, level.getRandom(), difficulty);
        return super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        if (this.isAdult()) {
            this.maybeWearArmor(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET), random);
            this.maybeWearArmor(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE), random);
            this.maybeWearArmor(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS), random);
            this.maybeWearArmor(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS), random);
        }
    }

    private void maybeWearArmor(EquipmentSlot slot, ItemStack stack, RandomSource random) {
        if (random.nextFloat() < 0.1F) {
            this.setItemSlot(slot, stack);
        }
    }

    private ItemStack createSpawnWeapon() {
        return this.random.nextFloat() < 0.5 ? new ItemStack(Items.CROSSBOW) : new ItemStack(Items.GOLDEN_SWORD);
    }

    @Nullable
    @Override
    public TagKey<Item> getPreferredWeaponType() {
        return this.isBaby() ? null : ItemTags.PIGLIN_PREFERRED_WEAPONS;
    }

    private boolean isChargingCrossbow() {
        return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
    }

    @Override
    public void setChargingCrossbow(boolean chargingCrossbow) {
        this.entityData.set(DATA_IS_CHARGING_CROSSBOW, chargingCrossbow);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public PiglinArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return PiglinArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(Items.CROSSBOW) && CrossbowItem.isCharged(this.getWeaponItem())) {
            return PiglinArmPose.CROSSBOW_HOLD;
        } else {
            return super.getArmPose();
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        this.performCrossbowAttack(this, 1.6F);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeapon) {
        return projectileWeapon == Items.CROSSBOW;
    }
}
