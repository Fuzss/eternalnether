package fuzs.eternalnether.mixin;

import fuzs.eternalnether.world.entity.monster.piglin.ModPiglinBruteAi;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.piglin.PiglinBruteAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PiglinBruteAi.class)
abstract class PiglinBruteAiMixin {

    @Inject(
            method = "findNearestValidAttackTarget", at = @At(value = "HEAD"), cancellable = true
    )
    private static void findNearestValidAttackTarget(ServerLevel serverLevel, AbstractPiglin abstractPiglin, CallbackInfoReturnable<Optional<? extends LivingEntity>> callback) {
        callback.setReturnValue(ModPiglinBruteAi.findNearestValidAttackTarget(serverLevel, abstractPiglin));
    }

    @Inject(
            method = "setAngerTarget", at = @At(value = "TAIL")
    )
    private static void setAngerTarget(PiglinBrute piglinBrute, LivingEntity angerTarget, CallbackInfo callback) {
        if (angerTarget instanceof Player && piglinBrute.level() instanceof ServerLevel serverLevel
                && serverLevel.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
            piglinBrute.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
        }
    }
}
