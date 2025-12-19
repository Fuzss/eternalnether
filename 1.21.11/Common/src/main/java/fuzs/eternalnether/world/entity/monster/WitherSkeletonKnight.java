package fuzs.eternalnether.world.entity.monster;

import fuzs.eternalnether.world.entity.ai.goal.ShieldDefenseGoal;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class WitherSkeletonKnight extends WitherSkeleton implements ShieldedMob {
    private static final EntityDataAccessor<Boolean> DATA_IS_SHIELDED = SynchedEntityData.defineId(WitherSkeletonKnight.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_SHIELD_COOLDOWN = SynchedEntityData.defineId(
            WitherSkeletonKnight.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_DISARMORED = SynchedEntityData.defineId(
            WitherSkeletonKnight.class,
            EntityDataSerializers.BOOLEAN);
    private static final Identifier SPEED_MODIFIER_DISARMOURED_ID = Identifier.withDefaultNamespace("disarmoured");
    private static final AttributeModifier SPEED_MODIFIER_DISARMOURED = new AttributeModifier(
            SPEED_MODIFIER_DISARMOURED_ID,
            0.35F,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final float BREAK_HEALTH_PERCENT = 0.35F;

    public WitherSkeletonKnight(EntityType<? extends WitherSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_DISARMORED, false);
        builder.define(DATA_IS_SHIELDED, false);
        builder.define(DATA_SHIELD_COOLDOWN, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ShieldDefenseGoal<>(this, Player.class));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level() instanceof ServerLevel && this.getShieldCooldown() > 0) {
            this.setShieldCooldown(this.getShieldCooldown() - 1);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putBoolean("Disarmored", this.isDisarmored());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.setDisarmored(valueInput.getBooleanOr("Disarmored", false));
    }

    public boolean isDisarmored() {
        return this.entityData.get(DATA_IS_DISARMORED);
    }

    private void setDisarmored(boolean disarmored) {
        this.entityData.set(DATA_IS_DISARMORED, disarmored);
    }

    @Override
    public void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damageAmount) {
        if (super.hurtServer(serverLevel, damageSource, damageAmount)) {
            if (!this.isDisarmored() && this.getHealth() / this.getMaxHealth() < BREAK_HEALTH_PERCENT) {
                this.setDisarmored(true);
                this.playSound(SoundEvents.SHIELD_BREAK.value(),
                        1.2F,
                        0.8F + serverLevel.getRandom().nextFloat() * 0.4F);
                AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attribute != null && !attribute.hasModifier(SPEED_MODIFIER_DISARMOURED_ID)) {
                    attribute.addPermanentModifier(SPEED_MODIFIER_DISARMOURED);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (!this.isUsingShield()) {
            super.knockback(strength, x, z);
        } else {
            this.playSound(SoundEvents.SHIELD_BLOCK.value(), 1.0F, 0.8F + this.level().random.nextFloat() * 0.4F);
        }
    }

    /**
     * @see Player#blockUsingItem(ServerLevel, LivingEntity)
     */
    @Override
    protected void blockUsingItem(ServerLevel level, LivingEntity entity) {
        super.blockUsingItem(level, entity);
        ItemStack itemStack = this.getItemBlockingWith();
        BlocksAttacks blocksAttacks = itemStack != null ? itemStack.get(DataComponents.BLOCKS_ATTACKS) : null;
        float secondsToDisableBlocking = entity.getSecondsToDisableBlocking();
        if (secondsToDisableBlocking > 0.0F && blocksAttacks != null) {
            blocksAttacks.disable(level, this, secondsToDisableBlocking, itemStack);
            this.disableShield();
        }
    }

    private void disableShield() {
        this.setShieldCooldown(60);
        this.stopUsingShield();
        this.level().broadcastEntityEvent(this, (byte) 30);
        this.playSound(SoundEvents.SHIELD_BREAK.value(), 0.8F, 0.8F + this.level().random.nextFloat() * 0.4F);
    }

    @Override
    public boolean isShieldDisabled() {
        return this.getShieldCooldown() > 0;
    }

    @Override
    public void startUsingShield() {
        ShieldedMob.startUsingShield(this);
    }

    @Override
    public void stopUsingShield() {
        ShieldedMob.stopUsingShield(this);
    }

    @Override
    public boolean isUsingShield() {
        return this.entityData.get(DATA_IS_SHIELDED);
    }

    @Override
    public void setUsingShield(boolean isShielded) {
        this.entityData.set(DATA_IS_SHIELDED, isShielded);
    }

    private int getShieldCooldown() {
        return this.entityData.get(DATA_SHIELD_COOLDOWN);
    }

    private void setShieldCooldown(int shieldCooldown) {
        this.entityData.set(DATA_SHIELD_COOLDOWN, Math.max(0, shieldCooldown));
    }
}
