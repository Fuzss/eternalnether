package fuzs.eternalnether.world.entity.monster.piglin;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import fuzs.eternalnether.init.ModFeatures;
import fuzs.eternalnether.init.ModItems;
import fuzs.eternalnether.init.ModSensorTypes;
import fuzs.eternalnether.services.CommonAbstractions;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class PiglinPrisoner extends AbstractPiglin implements CrossbowAttackMob, InventoryCarrier {
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(
            PiglinPrisoner.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_DANCING = SynchedEntityData.defineId(PiglinPrisoner.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(
            PiglinPrisoner.class,
            EntityDataSerializers.OPTIONAL_UUID);
    protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinPrisoner>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ITEMS,
            SensorType.HURT_BY,
            ModSensorTypes.PIGLIN_PRISONER_SPECIFIC_SENSOR_TYPE.value());
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.DOORS_TO_CLOSE,
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
            MemoryModuleType.NEARBY_ADULT_PIGLINS,
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.INTERACTION_TARGET,
            MemoryModuleType.PATH,
            MemoryModuleType.ANGRY_AT,
            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.ADMIRING_ITEM,
            MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM,
            MemoryModuleType.ADMIRING_DISABLED,
            MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM,
            MemoryModuleType.CELEBRATE_LOCATION,
            MemoryModuleType.DANCING,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
            MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
            MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT,
            MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM,
            MemoryModuleType.ATE_RECENTLY,
            MemoryModuleType.NEAREST_REPELLENT,
            MemoryModuleType.TEMPTING_PLAYER,
            MemoryModuleType.IS_TEMPTED);

    protected static final int RESCUE_TIME = 75;
    protected int timeBeingRescued;
    protected boolean isBeingRescued;
    protected boolean hasTempter;

    private final SimpleContainer inventory = new SimpleContainer(8);

    public PiglinPrisoner(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }

    public static boolean checkPiglinSpawnRules(EntityType<? extends AbstractPiglin> piglin, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return !level.getBlockState(pos.below()).is(Blocks.NETHER_WART_BLOCK);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && !this.hasTempter && this.getTempter() != null) {
            this.hasTempter = true;
            this.spawnHeartParticles();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("TimeBeingRescued", this.timeBeingRescued);
        compoundTag.putBoolean("IsBeingRescued", this.isBeingRescued);
        if (this.getTempterUUID() != null) {
            compoundTag.putUUID("Tempter", this.getTempterUUID());
        }
        this.writeInventoryToTag(compoundTag, this.registryAccess());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.timeBeingRescued = compoundTag.getInt("TimeBeingRescued");
        this.isBeingRescued = compoundTag.getBoolean("IsBeingRescued");
        if (compoundTag.hasUUID("Tempter")) {
            this.setTempterUUID(compoundTag.getUUID("Tempter"));
            this.hasTempter = true;
            PiglinPrisonerAi.reloadAllegiance(this, this.getTempter());
        }
        this.readInventoryFromTag(compoundTag, this.registryAccess());
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("piglinBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        PiglinPrisonerAi.updateActivity(this);
        if (this.isBeingRescued) {
            this.timeBeingRescued++;
        } else {
            this.timeBeingRescued = 0;
        }

        if (this.timeBeingRescued > RESCUE_TIME) {
            this.playConvertedSound();
            this.finishRescue();
        }
        super.customServerAiStep();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
    }

    public void addToInventory(ItemStack stack) {
        this.inventory.addItem(stack);
    }

    public boolean canAddToInventory(ItemStack stack) {
        return this.inventory.canAddItem(stack);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_CHARGING_CROSSBOW, false);
        builder.define(DATA_IS_DANCING, false);
        builder.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToPlayer) {
        return false;
    }

    @Override
    protected Brain.Provider<PiglinPrisoner> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return PiglinPrisonerAi.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public Brain<PiglinPrisoner> getBrain() {
        return (Brain<PiglinPrisoner>) super.getBrain();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult interactionresult = super.mobInteract(player, hand);
        if (interactionresult.consumesAction()) {
            return interactionresult;
        } else if (!this.level().isClientSide) {
            return PiglinPrisonerAi.mobInteract(this, player, hand);
        } else {
            boolean flag = PiglinPrisonerAi.canAdmire(this, player.getItemInHand(hand)) &&
                    this.getArmPose() != PiglinArmPose.ADMIRING_ITEM;
            return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    @Override
    public int getBaseExperienceReward() {
        return this.xpReward;
    }

    @Override
    protected void finishConversion(ServerLevel serverlevel) {
        PiglinPrisonerAi.cancelAdmiring(this);
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
        super.finishConversion(serverlevel);
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
    }

    @Override
    public void setChargingCrossbow(boolean bool) {
        this.entityData.set(DATA_IS_CHARGING_CROSSBOW, bool);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float vel) {
        this.performCrossbowAttack(this, 1.6F);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem item) {
        return item == Items.CROSSBOW;
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    protected boolean canHunt() {
        return false;
    }

    @Override
    public PiglinArmPose getArmPose() {
        if (this.isDancing()) {
            return PiglinArmPose.DANCING;
        } else if (PiglinPrisonerAi.isLovedItem(this.getOffhandItem())) {
            return PiglinArmPose.ADMIRING_ITEM;
        } else if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
            return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
        } else if (this.isChargingCrossbow()) {
            return PiglinArmPose.CROSSBOW_CHARGE;
        } else {
            return this.isAggressive() && this.isHolding(is -> is.getItem() instanceof CrossbowItem) ?
                    PiglinArmPose.CROSSBOW_HOLD : PiglinArmPose.DEFAULT;
        }
    }

    public boolean isDancing() {
        return this.entityData.get(DATA_IS_DANCING);
    }

    public void setDancing(boolean isDancing) {
        this.entityData.set(DATA_IS_DANCING, isDancing);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean isHurt = super.hurt(source, amount);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (isHurt && source.getEntity() instanceof LivingEntity) {
                PiglinPrisonerAi.wasHurtBy(this, (LivingEntity) source.getEntity());
            }
            return isHurt;
        }
    }

    public void holdInOffHand(ItemStack itemStack) {
        if (CommonAbstractions.INSTANCE.isPiglinCurrency(itemStack)) {
            this.setItemSlot(EquipmentSlot.OFFHAND, itemStack);
            this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
        } else {
            this.setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, itemStack);
        }
    }

    @Override
    public boolean wantsToPickUp(ItemStack itemstack) {
        return fuzs.puzzleslib.api.core.v1.CommonAbstractions.INSTANCE.getMobGriefingRule(this.level(), this) &&
                this.canPickUpLoot() && PiglinPrisonerAi.wantsToPickup(this, itemstack);
    }

    public boolean canReplaceCurrentItem(ItemStack itemStack) {
        EquipmentSlot equipmentSlot = this.getEquipmentSlotForItem(itemStack);
        ItemStack itemInSlot = this.getItemBySlot(equipmentSlot);
        return this.canReplaceCurrentItem(itemStack, itemInSlot);
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack candidate, ItemStack existing) {
        if (EnchantmentHelper.has(existing, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) {
            return false;
        } else {
            boolean bl = PiglinPrisonerAi.isLovedItem(candidate) || candidate.is(Items.CROSSBOW);
            boolean bl2 = PiglinPrisonerAi.isLovedItem(existing) || existing.is(Items.CROSSBOW);
            if (bl && !bl2) {
                return true;
            } else if (!bl && bl2) {
                return false;
            } else {
                return (!this.isAdult() || candidate.is(Items.CROSSBOW) || !existing.is(Items.CROSSBOW)) &&
                        super.canReplaceCurrentItem(candidate, existing);
            }
        }
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        this.onItemPickup(itemEntity);
        PiglinPrisonerAi.pickUpItem(this, itemEntity);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.level().isClientSide ? null : PiglinPrisonerAi.getSoundForCurrentActivity(this).orElse(null);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PIGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockpos, BlockState blockstate) {
        this.playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
    }

    @Override
    public void playSound(SoundEvent sound) {
        this.playSound(sound, this.getSoundVolume(), this.getVoicePitch());
    }

    @Override
    protected void playConvertedSound() {
        this.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }

    @Nullable
    public Player getTempter() {
        try {
            UUID uuid = this.getTempterUUID();
            return uuid == null ? null : this.level().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getTempterUUID() {
        return this.entityData.get(DATA_OWNERUUID_ID).orElse(null);
    }

    public void setTempterUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(uuid));
    }

    public void spawnHeartParticles() {
        for (int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level()
                    .addParticle(ParticleTypes.HEART,
                            this.getRandomX(1.0D),
                            this.getRandomY() + 1.0D,
                            this.getRandomZ(1.0D),
                            d0,
                            d1,
                            d2);
        }
    }

    public void rescue() {
        PiglinPrisonerAi.startDancing(this);
        PiglinPrisonerAi.broadcastBeingRescued(this);
        CriteriaTriggers.SUMMONED_ENTITY.trigger((ServerPlayer) this.getTempter(), this);
        this.isBeingRescued = true;
    }

    protected void finishRescue() {
        PiglinPrisonerAi.throwItems(this,
                Collections.singletonList(new ItemStack(ModItems.GILDED_NETHERITE_SHIELD.value())));
        EntityType<? extends Mob> entityType = ModFeatures.PIGLIN_PRISONER_CONVERSIONS.getRandom(this.random)
                .map(WeightedEntry.Wrapper::data)
                .map(Holder::value)
                .orElse(null);
        if (entityType != null) {
            Mob mob = this.convertTo(entityType, true);
            if (mob != null) {
                mob.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200));
            }
        }
    }
}