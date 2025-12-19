package fuzs.eternalnether.world.entity.monster;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import fuzs.eternalnether.init.ModRegistry;
import fuzs.eternalnether.init.ModSoundEvents;
import fuzs.eternalnether.init.ModTags;
import fuzs.puzzleslib.api.network.v4.codec.ExtraStreamCodecs;
import fuzs.puzzleslib.api.util.v1.CodecExtras;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WarpedEnderman extends EnderMan implements Shearable {
    private static final Map<SoundEvent, SoundEvent> SOUND_EVENTS = ImmutableMap.of(SoundEvents.ENDERMAN_AMBIENT,
            ModSoundEvents.WARPED_ENDERMAN_AMBIENT.value(),
            SoundEvents.ENDERMAN_DEATH,
            ModSoundEvents.WARPED_ENDERMAN_DEATH.value(),
            SoundEvents.ENDERMAN_HURT,
            ModSoundEvents.WARPED_ENDERMAN_HURT.value(),
            SoundEvents.ENDERMAN_SCREAM,
            ModSoundEvents.WARPED_ENDERMAN_SCREAM.value(),
            SoundEvents.ENDERMAN_STARE,
            ModSoundEvents.WARPED_ENDERMAN_STARE.value(),
            SoundEvents.ENDERMAN_TELEPORT,
            ModSoundEvents.WARPED_ENDERMAN_TELEPORT.value());
    private static final int SHEAR_COOLDOWN = 20;
    private static final EntityDataAccessor<Variant> VARIANT_ID = SynchedEntityData.defineId(WarpedEnderman.class,
            ModRegistry.WARPED_ENDER_MAN_VARIANT_ENTITY_DATA_SERIALIZER.value());

    private int shearCooldownCounter;

    public WarpedEnderman(EntityType<? extends EnderMan> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 55.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 8.5D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT_ID, Variant.LONG_VINE);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Endermite.class, true, false));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level() instanceof ServerLevel) {
            if (this.shearCooldownCounter > 0) {
                this.shearCooldownCounter--;
            }
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData spawnGroupData1 = super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
        this.setVariant(Variant.random(random));
        return spawnGroupData1;
    }

    @Override
    public void playSound(SoundEvent event, float volume, float pitch) {
        super.playSound(SOUND_EVENTS.getOrDefault(event, event), volume, pitch);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.store("Variant", Variant.CODEC, this.getVariant());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        valueInput.read("Variant", Variant.CODEC).ifPresent(this::setVariant);
    }

    public Variant getVariant() {
        return this.entityData.get(VARIANT_ID);
    }

    public void setVariant(Variant variant) {
        this.entityData.set(VARIANT_ID, variant);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand interactionResult) {
        ItemStack itemStack = player.getItemInHand(interactionResult);
        if (itemStack.is(ModTags.SHEAR_TOOLS_ITEM_TAG_KEY) && this.readyForShearing()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                this.shear(serverLevel, SoundSource.PLAYERS, itemStack);
                this.gameEvent(GameEvent.SHEAR, player);
                itemStack.hurtAndBreak(1, player, interactionResult.asEquipmentSlot());
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, interactionResult);
        }
    }

    @Override
    public void shear(ServerLevel level, SoundSource soundSource, ItemStack shears) {
        level.playSound(null, this, SoundEvents.SHEEP_SHEAR, soundSource, 1.0F, 1.0F);
        this.dropFromShearingLootTable(level,
                ModRegistry.SHEARING_WARPED_ENDER_MAN_LOOT_TABLE,
                shears,
                (ServerLevel serverLevel, ItemStack itemStack) -> {
                    this.spawnAtLocation(serverLevel, itemStack, this.getEyeHeight());
                });
        this.shearCooldownCounter = SHEAR_COOLDOWN;
        this.shearWarp(level);
    }

    private void shearWarp(ServerLevel level) {
        switch (this.getVariant()) {
            case FRESH -> {
                this.convertTo(EntityType.ENDERMAN,
                        ConversionParams.single(this, false, false),
                        (EnderMan enderMan) -> {
                            level.sendParticles(ParticleTypes.EXPLOSION,
                                    this.getX(),
                                    this.getY(0.5),
                                    this.getZ(),
                                    1,
                                    0.0,
                                    0.0,
                                    0.0,
                                    0.0);
                        });
            }
            case SHORT_VINE -> {
                this.setVariant(Variant.FRESH);
            }
            case LONG_VINE -> {
                this.setVariant(Variant.SHORT_VINE);
            }
        }
    }

    @Override
    public boolean readyForShearing() {
        return this.isAlive() && this.shearCooldownCounter == 0;
    }

    public enum Variant {
        FRESH,
        SHORT_VINE,
        LONG_VINE;

        public static final Codec<Variant> CODEC = CodecExtras.fromEnum(Variant.class);
        public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC = ExtraStreamCodecs.fromEnum(Variant.class);
        private static final Variant[] VALUES = values();

        public static Variant random(RandomSource random) {
            return VALUES[random.nextInt(VALUES.length)];
        }
    }
}