package fuzs.eternalnether.world.level.block.entity;

import fuzs.eternalnether.init.ModBlocks;
import fuzs.eternalnether.init.ModEntityTypes;
import fuzs.eternalnether.world.entity.monster.piglin.PiglinPrisoner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;

public class NetheriteBellBlockEntity extends BlockEntity {
    private static final int DURATION = 50;
    private static final int GLOW_DURATION = 300;
    private static final int MIN_TICKS_BETWEEN_SEARCHES = 60;
    private static final int MAX_RESONATION_TICKS = 40;
    private static final int TICKS_BEFORE_RESONATION = 5;
    private static final int SEARCH_RADIUS = 48;
    private static final int HEAR_BELL_RADIUS = 32;
    private static final int HIGHLIGHT_PIGLIN_PRISONERS_RADIUS = 48;

    private long lastRingTimestamp;
    public int ticks;
    public boolean shaking;
    public Direction clickDirection;
    private List<LivingEntity> nearbyEntities;
    private boolean resonating;
    private int resonationTicks;

    public NetheriteBellBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.NETHERITE_BELL_BLOCK_ENTITY_TYPE.value(), blockPos, blockState);
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.updateEntities();
            this.resonationTicks = 0;
            this.clickDirection = Direction.from3DDataValue(type);
            this.ticks = 0;
            this.shaking = true;
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, NetheriteBellBlockEntity blockEntity) {
        tick(level, blockPos, blockState, blockEntity, NetheriteBellBlockEntity::showBellParticles);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, NetheriteBellBlockEntity blockEntity) {
        tick(level, blockPos, blockState, blockEntity, NetheriteBellBlockEntity::completePiglinPrisoneRescue);
    }

    private static void tick(Level level, BlockPos blockPos, BlockState blockState, NetheriteBellBlockEntity blockEntity, NetheriteBellBlockEntity.ResonationEndAction endAction) {
        if (blockEntity.shaking) {
            ++blockEntity.ticks;
        }
        if (blockEntity.ticks >= DURATION) {
            blockEntity.shaking = false;
            blockEntity.ticks = 0;
        }
        if (blockEntity.ticks >= TICKS_BEFORE_RESONATION && blockEntity.resonationTicks == 0 &&
                arePiglinPrisonersNearby(blockPos, blockEntity.nearbyEntities)) {
            blockEntity.resonating = true;
            level.playSound(null, blockPos, SoundEvents.BELL_RESONATE, SoundSource.BLOCKS, 1.5F, 0.8F);
        }
        if (blockEntity.resonating) {
            if (blockEntity.resonationTicks < MAX_RESONATION_TICKS) {
                ++blockEntity.resonationTicks;
            } else {
                endAction.run(level, blockPos, blockEntity.nearbyEntities);
                blockEntity.resonating = false;
            }
        }
    }

    public void onHit(Direction direction) {
        BlockPos blockPos = this.getBlockPos();
        this.clickDirection = direction;
        if (this.shaking) {
            this.ticks = 0;
        } else {
            this.shaking = true;
        }
        this.level.blockEvent(blockPos, this.getBlockState().getBlock(), 1, direction.get3DDataValue());
    }

    private void updateEntities() {
        BlockPos blockpos = this.getBlockPos();
        if (this.level.getGameTime() > this.lastRingTimestamp + MIN_TICKS_BETWEEN_SEARCHES ||
                this.nearbyEntities == null) {
            this.lastRingTimestamp = this.level.getGameTime();
            AABB aabb = (new AABB(blockpos)).inflate(SEARCH_RADIUS);
            this.nearbyEntities = this.level.getEntitiesOfClass(LivingEntity.class, aabb);
        }
        if (!this.level.isClientSide) {
            for (LivingEntity livingentity : this.nearbyEntities) {
                if (isLivingEntityWithinRange(blockpos, livingentity, HEAR_BELL_RADIUS)) {
                    livingentity.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, this.level.getGameTime());
                }
            }
        }
    }

    private static boolean arePiglinPrisonersNearby(BlockPos blockPos, List<LivingEntity> piglinPrisoners) {
        for (LivingEntity livingentity : piglinPrisoners) {
            if (isRescuedPiglinPrisonerWithinRange(blockPos, livingentity, HEAR_BELL_RADIUS)) {
                return true;
            }
        }
        return false;
    }

    private static void completePiglinPrisoneRescue(Level level, BlockPos blockPos, List<LivingEntity> piglinPrisoners) {
        piglinPrisoners.stream()
                .filter((piglinPrisoner) -> isRescuedPiglinPrisonerWithinRange(blockPos,
                        piglinPrisoner,
                        HIGHLIGHT_PIGLIN_PRISONERS_RADIUS))
                .forEach(NetheriteBellBlockEntity::rescue);
    }

    private static void showBellParticles(Level level, BlockPos blockPos, List<LivingEntity> piglinPrisoners) {
        MutableInt mutableint = new MutableInt(16700985);
        int i = (int) piglinPrisoners.stream()
                .filter((LivingEntity livingEntity) -> blockPos.closerToCenterThan(livingEntity.position(),
                        HIGHLIGHT_PIGLIN_PRISONERS_RADIUS))
                .count();
        piglinPrisoners.stream()
                .filter((LivingEntity livingEntity) -> isRescuedPiglinPrisonerWithinRange(blockPos,
                        livingEntity,
                        HIGHLIGHT_PIGLIN_PRISONERS_RADIUS))
                .forEach((LivingEntity livingEntity) -> {
                    double d0 = Math.sqrt((livingEntity.getX() - (double) blockPos.getX()) *
                            (livingEntity.getX() - (double) blockPos.getX()) +
                            (livingEntity.getZ() - (double) blockPos.getZ()) *
                                    (livingEntity.getZ() - (double) blockPos.getZ()));
                    double d1 = (double) ((float) blockPos.getX() + 0.5F) +
                            1.0D / d0 * (livingEntity.getX() - (double) blockPos.getX());
                    double d2 = (double) ((float) blockPos.getZ() + 0.5F) +
                            1.0D / d0 * (livingEntity.getZ() - (double) blockPos.getZ());
                    int j = Mth.clamp((i - 21) / -2, 3, 15);
                    for (int k = 0; k < j; ++k) {
                        int l = mutableint.addAndGet(5);
                        level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, l),
                                d1,
                                (float) blockPos.getY() + 0.5F,
                                d2,
                                0.0,
                                0.0,
                                0.0);
                    }
                });
    }

    private static boolean isLivingEntityWithinRange(BlockPos blockPos, LivingEntity livingentity, int radius) {
        return livingentity.isAlive() && !livingentity.isRemoved() &&
                blockPos.closerToCenterThan(livingentity.position(), radius);
    }

    private static boolean isRescuedPiglinPrisonerWithinRange(BlockPos blockPos, LivingEntity livingEntity, int radius) {
        return isLivingEntityWithinRange(blockPos, livingEntity, radius) &&
                livingEntity.getType() == ModEntityTypes.PIGLIN_PRISONER.value() &&
                ((PiglinPrisoner) livingEntity).getTempter() != null;
    }

    private static void rescue(LivingEntity entity) {
        glow(entity);
        broadcastRescue(entity);
    }

    private static void glow(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOW_DURATION));
    }

    private static void broadcastRescue(LivingEntity livingEntity) {
        ((PiglinPrisoner) livingEntity).rescue();
    }

    @FunctionalInterface
    interface ResonationEndAction {

        void run(Level level, BlockPos blockPos, List<LivingEntity> entities);
    }
}
