package fuzs.eternalnether.world.entity.monster;

import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.services.CommonAbstractions;
import fuzs.eternalnether.world.entity.ai.goal.UseShieldGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class WitherSkeletonKnight extends WitherSkeleton implements ShieldedMob {
    private static final EntityDataAccessor<Boolean> DATA_IS_SHIELDED = SynchedEntityData.defineId(WitherSkeletonKnight.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SHIELD_HAND = SynchedEntityData.defineId(WitherSkeletonKnight.class,
            EntityDataSerializers.BOOLEAN); // True for Main Hand, False for Offhand
    private static final EntityDataAccessor<Integer> DATA_SHIELD_COOLDOWN = SynchedEntityData.defineId(
            WitherSkeletonKnight.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_DISARMORED = SynchedEntityData.defineId(
            WitherSkeletonKnight.class,
            EntityDataSerializers.BOOLEAN);
    private static final ResourceLocation SPEED_MODIFIER_ATTACKING_ID = EternalNether.id("shielded_speed_penalty");
    private static final AttributeModifier SPEED_MODIFIER_BLOCKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_ID,
            -0.10,
            AttributeModifier.Operation.ADD_VALUE);

    private static final float BREAK_HEALTH = 20.0F;

    public WitherSkeletonKnight(EntityType<? extends WitherSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new UseShieldGoal<>(this, Player.class));
        super.registerGoals();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.decrementShieldCooldown();
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Disarmored", this.isDisarmored());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDisarmored(tag.getBoolean("Disarmored"));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_DISARMORED, false);
        builder.define(DATA_IS_SHIELDED, false);
        builder.define(DATA_SHIELD_HAND, false);
        builder.define(DATA_SHIELD_COOLDOWN, 0);
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
    public boolean hurt(DamageSource source, float damage) {
        boolean result = super.hurt(source, damage);
        if (!this.isDisarmored() && this.getHealth() < BREAK_HEALTH) {
            this.setDisarmored(true);
            this.playSound(SoundEvents.SHIELD_BREAK, 1.2F, 0.8F + this.level().random.nextFloat() * 0.4F);
            this.setSpeed(0.25f);
        }
        return result;
    }

    @Override
    public void knockback(double x, double y, double z) {
        if (!this.isUsingShield()) {
            super.knockback(x, y, z);
        } else {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level().random.nextFloat() * 0.4F);
        }
    }

    @Override
    protected void blockUsingShield(LivingEntity attacker) {
        super.blockUsingShield(attacker);
        if (CommonAbstractions.INSTANCE.canDisableShield(attacker.getMainHandItem(), this.useItem, this, attacker)) {
            this.disableShield();
        }
    }

    private void disableShield() {
        this.setShieldCooldown(60);
        this.stopUsingShield();
        this.level().broadcastEntityEvent(this, (byte) 30);
        this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level().random.nextFloat() * 0.4F);
    }

    @Override
    public boolean isShieldDisabled() {
        return this.getShieldCooldown() > 0;
    }

    @Override
    public void startUsingShield() {
        if (this.isUsingShield() || this.isShieldDisabled()) {
            return;
        }
        for (InteractionHand interactionhand : InteractionHand.values()) {
            if (this.getItemInHand(interactionhand).is(Items.SHIELD)) {
                this.startUsingItem(interactionhand);
                this.setUsingShield(true);
                this.setShieldMainhand(interactionhand == InteractionHand.MAIN_HAND);
                AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attributeinstance != null && !attributeinstance.hasModifier(SPEED_MODIFIER_ATTACKING_ID)) {
                    attributeinstance.addTransientModifier(SPEED_MODIFIER_BLOCKING);
                }
            }
        }
    }

    @Override
    public void stopUsingShield() {
        if (!this.isUsingShield()) {
            return;
        }
        for (InteractionHand interactionhand : InteractionHand.values()) {
            if (this.getItemInHand(interactionhand).is(Items.SHIELD)) {
                this.stopUsingItem();
                this.setUsingShield(false);
                AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attributeinstance != null) {
                    attributeinstance.removeModifier(SPEED_MODIFIER_BLOCKING);
                }
            }
        }
    }

    public boolean isUsingShield() {
        return this.entityData.get(DATA_IS_SHIELDED);
    }

    public void setUsingShield(boolean isShielded) {
        this.entityData.set(DATA_IS_SHIELDED, isShielded);
    }

    private boolean isShieldMainhand() {
        return this.entityData.get(DATA_SHIELD_HAND);
    }

    private void setShieldMainhand(boolean isShieldedMainHand) {
        this.entityData.set(DATA_SHIELD_HAND, isShieldedMainHand);
    }

    private int getShieldCooldown() {
        return this.entityData.get(DATA_SHIELD_COOLDOWN);
    }

    private void setShieldCooldown(int newShieldCooldown) {
        this.entityData.set(DATA_SHIELD_COOLDOWN, newShieldCooldown);
    }

    private void decrementShieldCooldown() {
        this.setShieldCooldown(Math.max(this.getShieldCooldown() - 1, 0));
    }

    public InteractionHand getShieldHand() {
        return this.isUsingShield() ? (this.isShieldMainhand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND) :
                null;
    }
}
