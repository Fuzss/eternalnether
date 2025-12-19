package fuzs.eternalnether.world.level.block.entity;

import fuzs.eternalnether.init.ModBlocks;
import fuzs.eternalnether.init.ModEntityTypes;
import fuzs.eternalnether.world.entity.monster.piglin.PiglinPrisoner;
import fuzs.puzzleslib.api.block.v1.entity.TickingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * The implementation omits the client-side particles, which are not very impactful.
 */
public class NetheriteBellBlockEntity extends BellBlockEntity implements TickingBlockEntity {

    public NetheriteBellBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlocks.NETHERITE_BELL_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    public void clientTick() {
        this.tick();
    }

    @Override
    public void serverTick() {
        if (this.tick()) {
            this.nearbyEntities.stream().filter(this::isPiglinPrisonerWithinRange).forEach(this::rescue);
        }
    }

    /**
     * @see BellBlockEntity#tick(Level, BlockPos, BlockState, BellBlockEntity, ResonationEndAction)
     */
    private boolean tick() {
        if (this.shaking) {
            ++this.ticks;
        }
        if (this.ticks >= DURATION) {
            this.shaking = false;
            this.ticks = 0;
        }
        if (this.ticks >= TICKS_BEFORE_RESONATION && this.resonationTicks == 0
                && this.arePiglinPrisonersNearby(this.getBlockPos(), this.nearbyEntities)) {
            this.resonating = true;
            this.getLevel()
                    .playSound(null, this.getBlockPos(), SoundEvents.BELL_RESONATE, SoundSource.BLOCKS, 1.5F, 0.8F);
        }
        if (this.resonating) {
            if (this.resonationTicks < MAX_RESONATION_TICKS) {
                ++this.resonationTicks;
            } else {
                this.resonating = false;
                return true;
            }
        }
        return false;
    }

    /**
     * @see BellBlockEntity#areRaidersNearby(BlockPos, List)
     */
    protected boolean arePiglinPrisonersNearby(BlockPos pos, List<LivingEntity> livingEntities) {
        for (LivingEntity livingEntity : livingEntities) {
            if (livingEntity.isAlive() && !livingEntity.isRemoved() && pos.closerToCenterThan(livingEntity.position(),
                    HEAR_BELL_RADIUS) && this.isRescuedPiglinPrisoner(livingEntity)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @see BellBlockEntity#isRaiderWithinRange(BlockPos, LivingEntity)
     */
    protected boolean isPiglinPrisonerWithinRange(LivingEntity livingEntity) {
        return livingEntity.isAlive() && !livingEntity.isRemoved() && this.getBlockPos()
                .closerToCenterThan(livingEntity.position(), SEARCH_RADIUS)
                && this.isRescuedPiglinPrisoner(livingEntity);
    }

    private boolean isRescuedPiglinPrisoner(LivingEntity livingEntity) {
        return livingEntity.getType() == ModEntityTypes.PIGLIN_PRISONER.value()
                && ((PiglinPrisoner) livingEntity).getOwner() != null;
    }

    /**
     * @see BellBlockEntity#glow(LivingEntity)
     */
    protected void rescue(LivingEntity livingEntity) {
        livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 300));
        ((PiglinPrisoner) livingEntity).isBeingRescued();
    }
}
