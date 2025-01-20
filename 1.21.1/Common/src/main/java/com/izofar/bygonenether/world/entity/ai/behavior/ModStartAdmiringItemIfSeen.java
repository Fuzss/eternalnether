package com.izofar.bygonenether.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.world.entity.monster.PiglinPrisoner;
import com.izofar.bygonenether.world.entity.ai.PiglinPrisonerAi;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;

public class ModStartAdmiringItemIfSeen<E extends PiglinPrisoner> extends Behavior<E> {

    private final int admireDuration;

    public ModStartAdmiringItemIfSeen(int admireDuration) {
        super(ImmutableMap.of(
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ADMIRING_ITEM,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ADMIRING_DISABLED,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM,
                MemoryStatus.VALUE_ABSENT)
            );
        this.admireDuration = admireDuration;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E piglinPrisoner) {
        ItemEntity itementity = piglinPrisoner.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).orElse(null);
        return itementity != null && PiglinPrisonerAi.isLovedItem(itementity.getItem());
    }

    @Override
    protected void start(ServerLevel serverLevel, E piglinPrisoner, long gameTime) {
        piglinPrisoner.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, this.admireDuration);
    }
}