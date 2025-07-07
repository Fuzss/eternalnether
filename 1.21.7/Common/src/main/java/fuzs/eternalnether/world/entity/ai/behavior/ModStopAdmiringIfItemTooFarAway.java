package fuzs.eternalnether.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import fuzs.eternalnether.world.entity.monster.piglin.PiglinPrisoner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.Optional;

public class ModStopAdmiringIfItemTooFarAway<E extends PiglinPrisoner> extends Behavior<E> {
    private final int maxDistanceToItem;

    public ModStopAdmiringIfItemTooFarAway(int maxDistanceToItem) {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM,
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                MemoryStatus.REGISTERED));
        this.maxDistanceToItem = maxDistanceToItem;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E piglinPrisoner) {
        if (!piglinPrisoner.getOffhandItem().isEmpty()) {
            return false;
        } else {
            Optional<ItemEntity> optional = piglinPrisoner.getBrain()
                    .getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
            return optional.map(itemEntity -> !itemEntity.closerThan(piglinPrisoner, this.maxDistanceToItem))
                    .orElse(true);
        }
    }

    @Override
    protected void start(ServerLevel serverLevel, E piglinPrisoner, long gameTime) {
        piglinPrisoner.getBrain().eraseMemory(MemoryModuleType.ADMIRING_ITEM);
    }
}