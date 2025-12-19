package fuzs.eternalnether.world.entity.monster.piglin;

import fuzs.eternalnether.init.ModFeatures;
import fuzs.eternalnether.init.ModItems;
import fuzs.eternalnether.world.entity.ai.goal.FollowOwnerGoal;
import fuzs.puzzleslib.api.util.v1.EntityHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.InteractGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PiglinPrisoner extends GoalBasedPiglin implements OwnableEntity {
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(
            PiglinPrisoner.class,
            EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    private static final int RESCUE_TIME = 75;

    private int timeBeingRescued;
    private boolean isBeingRescued;
    private int admiringItem;

    public PiglinPrisoner(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(6, new FollowOwnerGoal<>(this, 1.0, 10.0F, 2.0F));
        this.goalSelector.addGoal(8, new InteractGoal(this, Player.class, 3.0F, 1.0F));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putInt("TimeBeingRescued", this.timeBeingRescued);
        valueOutput.putBoolean("IsBeingRescued", this.isBeingRescued);
        EntityReference.store(this.getOwnerReference(), valueOutput, "Owner");
    }

    @Override
    public void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.timeBeingRescued = valueInput.getIntOr("TimeBeingRescued", 0);
        this.isBeingRescued = valueInput.getBooleanOr("IsBeingRescued", false);
        this.setOwnerReference(EntityReference.read(valueInput, "Owner"));
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        super.customServerAiStep(serverLevel);
        if (this.admiringItem > 0 && --this.admiringItem == 0) {
            this.stopHoldingOffHandItem(serverLevel);
        }

        if (this.isBeingRescued) {
            this.timeBeingRescued++;
        } else {
            this.timeBeingRescued = 0;
        }

        if (this.timeBeingRescued > RESCUE_TIME) {
            this.playConvertedSound();
            this.throwItems(Collections.singletonList(new ItemStack(ModItems.GILDED_NETHERITE_SHIELD)));
            ModFeatures.PIGLIN_PRISONER_CONVERSIONS.getRandom(this.random).map(Holder::value).ifPresent(entityType -> {
                this.finishRescue(serverLevel, entityType);
            });
        }
    }

    private void finishRescue(ServerLevel serverLevel, EntityType<? extends Mob> entityType) {
        this.convertTo(entityType, ConversionParams.single(this, false, false), mob -> {
            mob.finalizeSpawn(serverLevel,
                    serverLevel.getCurrentDifficultyAt(mob.blockPosition()),
                    EntitySpawnReason.CONVERSION,
                    null);
            mob.setPersistenceRequired();
            mob.removeAllEffects();
            mob.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200));
        });
    }

    public void stopHoldingOffHandItem(ServerLevel serverLevel) {
        ItemStack itemInHand = this.getItemInHand(InteractionHand.OFF_HAND);
        this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        if (EntityHelper.isPiglinCurrency(itemInHand)) {
            this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 3));
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
        } else if (this.equipItemIfPossible(serverLevel, itemInHand).isEmpty()) {
            this.throwItems(Collections.singletonList(itemInHand));
        }
    }

    private void throwItems(List<ItemStack> stacks) {
        Player player = this.getOwner();
        if (player != null) {
            this.throwItemsTowardPlayer(player, stacks);
        } else {
            this.throwItemsTowardRandomPos(stacks);
        }
    }

    private void throwItemsTowardRandomPos(List<ItemStack> stacks) {
        this.throwItemsTowardPos(stacks, this.getRandomNearbyPos());
    }

    private void throwItemsTowardPlayer(Player player, List<ItemStack> stacks) {
        this.throwItemsTowardPos(stacks, player.position());
    }

    private void throwItemsTowardPos(List<ItemStack> stacks, Vec3 pos) {
        if (!stacks.isEmpty()) {
            this.swing(InteractionHand.OFF_HAND);

            for (ItemStack itemStack : stacks) {
                BehaviorUtils.throwItem(this, itemStack, pos.add(0.0, 1.0, 0.0));
            }
        }
    }

    private Vec3 getRandomNearbyPos() {
        Vec3 vec3 = LandRandomPos.getPos(this, 4, 2);
        return vec3 == null ? this.position() : vec3;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        InteractionResult interactionresult = super.mobInteract(player, interactionHand);
        if (interactionresult.consumesAction()) {
            return interactionresult;
        } else {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (this.level() instanceof ServerLevel serverLevel) {
                if (this.canAdmire(itemInHand)) {
                    this.holdInOffhand(serverLevel, itemInHand.split(1));
                    this.setOwner(player);
                    this.admireGoldItem();
                    this.stopWalking();
                    return InteractionResult.CONSUME;
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                if (this.canAdmire(itemInHand) && this.getArmPose() != PiglinArmPose.ADMIRING_ITEM) {
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.PASS;
                }
            }
        }
    }

    private void holdInOffhand(ServerLevel serverLevel, ItemStack itemStack) {
        if (!this.getOffhandItem().isEmpty()) {
            this.spawnAtLocation(serverLevel, this.getItemInHand(InteractionHand.OFF_HAND));
        }

        this.setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, itemStack);
    }

    private void admireGoldItem() {
        this.admiringItem = 120;
    }

    private void stopWalking() {
        this.getNavigation().stop();
    }

    public boolean canAdmire(ItemStack itemStack) {
        return !this.isAdmiringItem() && this.isAdult() && EntityHelper.isPiglinCurrency(itemStack);
    }

    public boolean isAdmiringItem() {
        return this.admiringItem > 0;
    }

    public void cancelAdmiring(ServerLevel serverLevel) {
        if (this.isAdmiringItem() && !this.getOffhandItem().isEmpty()) {
            this.spawnAtLocation(serverLevel, this.getOffhandItem());
            this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    public void isBeingRescued() {
        this.isBeingRescued = true;
    }

    @Override
    protected void finishConversion(ServerLevel serverLevel) {
        this.cancelAdmiring(serverLevel);
        super.finishConversion(serverLevel);
    }

    @Override
    public PiglinArmPose getArmPose() {
        return EntityHelper.isPiglinCurrency(this.getOffhandItem()) ? PiglinArmPose.ADMIRING_ITEM : super.getArmPose();
    }

    @Override
    public boolean canHoldItem(ItemStack itemStack) {
        return itemStack.is(ItemTags.PIGLIN_LOVED);
    }

    @Override
    public @Nullable Player getOwner() {
        return (Player) OwnableEntity.super.getOwner();
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getOwnerReference() {
        return this.entityData.get(DATA_OWNERUUID_ID).orElse(null);
    }

    public void setOwner(@Nullable Player player) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(player).map(EntityReference::of));
    }

    private void setOwnerReference(@Nullable EntityReference<LivingEntity> owner) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(owner));
    }
}
