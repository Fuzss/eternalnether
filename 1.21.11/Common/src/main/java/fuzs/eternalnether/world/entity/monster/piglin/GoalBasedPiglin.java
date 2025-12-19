package fuzs.eternalnether.world.entity.monster.piglin;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class GoalBasedPiglin extends AbstractPiglin {
    /**
     * These must be present for {@link AbstractPiglin}.
     */
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.ANGRY_AT);

    public GoalBasedPiglin(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }

    public static boolean checkPiglinSpawnRules(EntityType<? extends AbstractPiglin> piglin, LevelAccessor level, EntitySpawnReason entitySpawnReason, BlockPos pos, RandomSource random) {
        return !level.getBlockState(pos.below()).is(Blocks.NETHER_WART_BLOCK);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5,
                new AvoidEntityGoal<>(this,
                        Mob.class,
                        8.0F,
                        1.0,
                        1.2,
                        (LivingEntity livingEntity) -> PiglinAi.isZombified(livingEntity.getType())));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, WitherSkeleton.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, WitherBoss.class, true));
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return Brain.provider(MEMORY_TYPES, ImmutableList.of());
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean canHunt() {
        return false;
    }

    @Override
    public PiglinArmPose getArmPose() {
        return this.isAggressive() && this.isHoldingMeleeWeapon() ? PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON :
                PiglinArmPose.DEFAULT;
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.target;
    }

    @Override
    public void playAmbientSound() {
        this.makeSound(this.getAmbientSound());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIGLIN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PIGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void playConvertedSound() {
        this.makeSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }
}
