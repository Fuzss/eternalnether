package fuzs.eternalnether.world.entity.monster;

import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.init.ModEntityTypes;
import fuzs.eternalnether.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.scores.PlayerTeam;

public class Wraither extends WitherSkeleton {
    private static final EntityDataAccessor<Boolean> DATA_IS_POSSESSED = SynchedEntityData.defineId(Wraither.class,
            EntityDataSerializers.BOOLEAN);
    private static final ResourceLocation SPEED_MODIFIER_DISPOSSESSED_ID = EternalNether.id("dispossessed");
    private static final AttributeModifier SPEED_MODIFIER_DISPOSSESSED = new AttributeModifier(
            SPEED_MODIFIER_DISPOSSESSED_ID,
            0.1,
            AttributeModifier.Operation.ADD_VALUE);
    private static final float DISPOSSESS_HEALTH_PERCENT = 0.35F;

    public Wraither(EntityType<? extends WitherSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.MAX_HEALTH, 24.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_POSSESSED, true);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putBoolean("Possessed", this.isPossessed());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.setPossessed(valueInput.getBooleanOr("Possessed", false));
    }

    @Override
    public void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.CUTLASS));
    }

    public boolean isPossessed() {
        return this.entityData.get(DATA_IS_POSSESSED);
    }

    private void setPossessed(boolean possessed) {
        this.entityData.set(DATA_IS_POSSESSED, possessed);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damageAmount) {
        if (super.hurtServer(serverLevel, damageSource, damageAmount)) {
            if (this.isPossessed() && this.getHealth() / this.getMaxHealth() < DISPOSSESS_HEALTH_PERCENT) {
                this.dispossess();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see Evoker.EvokerSummonSpellGoal#performSpellCasting()
     */
    private void dispossess() {
        this.setPossessed(false);
        this.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(SPEED_MODIFIER_DISPOSSESSED);
        if (this.level() instanceof ServerLevel serverLevel) {
            Wex wex = ModEntityTypes.WEX.value().create(serverLevel, EntitySpawnReason.MOB_SUMMONED);
            if (wex != null) {
                BlockPos blockPos = this.blockPosition().above();
                wex.snapTo(blockPos, this.yBodyRot, this.xRotO);
                wex.finalizeSpawn(serverLevel,
                        serverLevel.getCurrentDifficultyAt(this.blockPosition()),
                        EntitySpawnReason.MOB_SUMMONED,
                        null);
                wex.setOwner(this);
                wex.setBoundOrigin(blockPos);
                wex.setLimitedLife(20 * (30 + this.random.nextInt(90)));
                PlayerTeam playerTeam = this.getTeam();
                if (playerTeam != null) {
                    serverLevel.getScoreboard().addPlayerToTeam(wex.getScoreboardName(), playerTeam);
                }
                serverLevel.addFreshEntity(wex);
            }
        }
    }
}
