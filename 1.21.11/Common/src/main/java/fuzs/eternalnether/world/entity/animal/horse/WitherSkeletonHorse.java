package fuzs.eternalnether.world.entity.animal.horse;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class WitherSkeletonHorse extends SkeletonHorse {

    public WitherSkeletonHorse(EntityType<? extends SkeletonHorse> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 35.0D).add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    protected void randomizeAttributes(RandomSource random) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(35.0);
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(1.0);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35);
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide() && this.random.nextInt(3) == 0) {
            this.level()
                    .addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                            this.getX(this.random.nextGaussian() * 0.25),
                            this.getRandomY() + 0.15,
                            this.getZ(this.random.nextGaussian() * 0.25),
                            0.0,
                            this.random.nextDouble() * -0.05,
                            0.0);
        }

        this.floatHorse();
        super.aiStep();
    }

    private void floatHorse() {
        if (this.isInLava()) {
            CollisionContext collisionContext = CollisionContext.of(this);
            if (collisionContext.isAbove(LiquidBlock.SHAPE_STABLE, this.blockPosition(), true) && !this.level()
                    .getFluidState(this.blockPosition().above())
                    .is(FluidTags.LAVA)) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.05D, 0.0D));
            }
        }
    }

    @Override
    public boolean isTamed() {
        return true;
    }

    @Override
    public boolean canStandOnFluid(FluidState fluid) {
        return fluid.is(FluidTags.LAVA);
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        if (blockState.liquid()) {
            SoundType soundtype = blockState.getSoundType();
            if (this.isVehicle() && this.canGallop) {
                ++this.gallopSoundCounter;
                if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                    this.playGallopSound(soundtype);
                } else if (this.gallopSoundCounter <= 5) {
                    this.playSound(SoundEvents.HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
                }
            }
        }
    }
}