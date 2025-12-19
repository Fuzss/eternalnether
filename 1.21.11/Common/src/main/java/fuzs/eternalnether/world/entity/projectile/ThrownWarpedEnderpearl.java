package fuzs.eternalnether.world.entity.projectile;

import fuzs.eternalnether.init.ModEntityTypes;
import fuzs.eternalnether.init.ModItems;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownWarpedEnderpearl extends ThrownEnderpearl {

    public ThrownWarpedEnderpearl(EntityType<? extends ThrownWarpedEnderpearl> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownWarpedEnderpearl(Level level, LivingEntity entity, ItemStack itemStack) {
        this(ModEntityTypes.WARPED_ENDER_PEARL.value(), level);
        // copied from vanilla super
        this.setOwner(entity);
        this.setItem(itemStack);
        this.setPos(entity.getX(), entity.getEyeY() - 0.1F, entity.getZ());
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.WARPED_ENDER_PEARL.value();
    }

    public static EventResult onEnderPearlTeleport(ServerPlayer serverPlayer, Vec3 targetPosition, ThrownEnderpearl thrownEnderpearl, MutableFloat damageAmount, HitResult hitResult) {
        if (thrownEnderpearl instanceof ThrownWarpedEnderpearl) {
            damageAmount.accept(0.0F);
            ServerLevel serverLevel = serverPlayer.level();
            if (serverLevel.getBlockState(thrownEnderpearl.blockPosition()).is(Blocks.WATER)) {
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 300));
            } else if (serverPlayer.isInLava() || serverLevel.getBlockState(thrownEnderpearl.blockPosition())
                    .is(Blocks.LAVA)) {
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 900));
            } else if (!thrownEnderpearl.onGround()) {
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 160));
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 300, 1));
            }
        }

        return EventResult.PASS;
    }
}
