package fuzs.eternalnether.world.entity.monster.piglin;

import fuzs.eternalnether.init.ModTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.piglin.PiglinBruteAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

import java.util.Optional;

public class ModPiglinBruteAi extends PiglinBruteAi {

    /**
     * Copied from
     * {@link net.minecraft.world.entity.monster.piglin.PiglinAi#findNearestValidAttackTarget(ServerLevel, Piglin)}, but
     * no longer restricted to {@link Piglin} and removed check for zombified piglins.
     */
    public static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel serverLevel, AbstractPiglin abstractPiglin) {
        Brain<?> brain = abstractPiglin.getBrain();
        Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(abstractPiglin,
                MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(serverLevel,
                abstractPiglin,
                optional.get())) {
            return optional;
        } else {
            if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
                Optional<Player> optional2 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
                if (optional2.isPresent()) {
                    return optional2;
                }
            }

            Optional<Mob> optional2 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
            if (optional2.isPresent()) {
                return optional2;
            } else {
                Optional<Player> optional3 = brain.getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
                return optional3.isPresent() && Sensor.isEntityAttackable(serverLevel,
                        abstractPiglin,
                        optional3.get()) ? optional3 : Optional.empty();
            }
        }
    }

    public static void setAngerTargetToNearestTargetablePlayerIfFound(PiglinBrute piglinBrute, LivingEntity defaultEntity) {
        Optional<Player> optional =
                piglinBrute.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER) ?
                        piglinBrute.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER) :
                        Optional.empty();
        PiglinBruteAi.setAngerTarget(piglinBrute, optional.isPresent() ? optional.get() : defaultEntity);
    }

    public static boolean isWearingSafeArmor(LivingEntity livingEntity) {
        for (EquipmentSlot equipmentSlot : EquipmentSlotGroup.ARMOR) {
            if (makesPiglinBrutesNeutral(livingEntity.getItemBySlot(equipmentSlot))) {
                return true;
            }
        }

        return false;
    }

    private static boolean makesPiglinBrutesNeutral(ItemStack itemStack) {
        return itemStack.is(ModTags.PIGLIN_BRUTE_SAFE_ARMOR_ITEM_TAG_KEY) && makesPiglinsNeutral(itemStack);
    }

    public static boolean makesPiglinsNeutral(ItemStack itemStack) {
        ArmorTrim armorTrim = itemStack.get(DataComponents.TRIM);
        return armorTrim != null && armorTrim.material().is(ModTags.PIGLIN_SAFE_TRIM_MATERIAL_TAG_KEY);
    }
}
