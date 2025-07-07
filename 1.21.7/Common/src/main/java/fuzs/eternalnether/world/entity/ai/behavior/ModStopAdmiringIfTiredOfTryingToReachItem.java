package fuzs.eternalnether.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import fuzs.eternalnether.world.entity.monster.piglin.PiglinPrisoner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;

public class ModStopAdmiringIfTiredOfTryingToReachItem<E extends PiglinPrisoner> extends Behavior<E> {
    private final int maxTimeToReachItem;
    private final int disableTime;

    public ModStopAdmiringIfTiredOfTryingToReachItem(int maxTimeToReachItem, int disableTime) {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM,
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM,
                MemoryStatus.REGISTERED,
                MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM,
                MemoryStatus.REGISTERED));
        this.maxTimeToReachItem = maxTimeToReachItem;
        this.disableTime = disableTime;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E piglinPrisoner) {
        return piglinPrisoner.getOffhandItem().isEmpty();
    }

    @Override
    protected void start(ServerLevel serverLevel, E piglinPrisoner, long gameTime) {
        Brain<PiglinPrisoner> brain = piglinPrisoner.getBrain();
        Optional<Integer> optional = brain.getMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
        if (optional.isEmpty()) {
            brain.setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, 0);
        } else {
            int reachAttemptTime = optional.get();
            if (reachAttemptTime > this.maxTimeToReachItem) {
                brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
                brain.eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
                brain.setMemoryWithExpiry(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, true, this.disableTime);
            } else {
                brain.setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, reachAttemptTime + 1);
            }
        }
    }
}