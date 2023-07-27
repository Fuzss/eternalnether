package com.izofar.bygonenether.entity.ai.sensing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PiglinPrisonerSensor extends Sensor<LivingEntity> {

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return ImmutableSet.of(
				MemoryModuleType.VISIBLE_LIVING_ENTITIES,
				MemoryModuleType.LIVING_ENTITIES,
				MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
				MemoryModuleType.NEAREST_VISIBLE_PLAYER,
				MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM,
				MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
				MemoryModuleType.NEARBY_ADULT_PIGLINS,
				MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT,
				MemoryModuleType.NEAREST_REPELLENT
		);
	}
	
	@Override
	protected void doTick(ServerWorld world, LivingEntity entity) {
		Brain<?> brain = entity.getBrain();
		brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(world, entity));
		Optional<MobEntity> optional = Optional.empty();
		Optional<PiglinEntity> optional3 = Optional.empty();
		Optional<LivingEntity> optional4 = Optional.empty();
		Optional<PlayerEntity> optional5 = Optional.empty();
		Optional<PlayerEntity> optional6 = Optional.empty();
		List<AbstractPiglinEntity> list = Lists.newArrayList();
		List<AbstractPiglinEntity> list1 = Lists.newArrayList();

		for (LivingEntity livingentity : brain.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of())) {
			if (livingentity instanceof PiglinBruteEntity) {
				list.add((PiglinBruteEntity) livingentity);
			} else if (livingentity instanceof PiglinEntity) {
				PiglinEntity piglin = (PiglinEntity) livingentity;
				if (piglin.isBaby() && !optional3.isPresent()) {
					optional3 = Optional.of(piglin);
				} else if (piglin.isAdult()) {
					list.add(piglin);
				}
			} else if (livingentity instanceof PlayerEntity) {
				PlayerEntity playerEntity = (PlayerEntity) livingentity;
				if (!optional5.isPresent()) {
					optional5 = Optional.of(playerEntity);
				}
				if (!optional6.isPresent() && !playerEntity.isSpectator() && PiglinTasks.isPlayerHoldingLovedItem(playerEntity)) {
					optional6 = Optional.of(playerEntity);
				}
			} else if (optional.isPresent() || !(livingentity instanceof WitherSkeletonEntity) && !(livingentity instanceof WitherEntity) && !(livingentity instanceof HoglinEntity && ((HoglinEntity) livingentity).isAdult())) {
				if (!optional4.isPresent() && PiglinTasks.isZombified(livingentity.getType())) {
					optional4 = Optional.of(livingentity);
				}
			} else {
				optional = Optional.of((MobEntity)livingentity);
			}
		}

		for (LivingEntity livingentity1 : brain.getMemory(MemoryModuleType.LIVING_ENTITIES).orElse(ImmutableList.of())) {
			if (livingentity1 instanceof AbstractPiglinEntity) {
				AbstractPiglinEntity abstractpiglin = (AbstractPiglinEntity) livingentity1;
				if (abstractpiglin.isAdult()) {
					list1.add(abstractpiglin);
				}
			}
		}

		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional);
		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, optional4);
		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, optional5);
		brain.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, optional6);
		brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, list1);
		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, list);
		brain.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, list.size());
	}
	
	private static Optional<BlockPos> findNearestRepellent(ServerWorld world, LivingEntity entity) {
		return BlockPos.findClosestMatch(entity.blockPosition(), 8, 4, (p_186160_) -> isValidRepellent(world, p_186160_));
	}

	private static boolean isValidRepellent(ServerWorld world, BlockPos blockpos) {
		BlockState blockstate = world.getBlockState(blockpos);
		boolean flag = blockstate.is(BlockTags.PIGLIN_REPELLENTS);
		return flag && blockstate.is(Blocks.SOUL_CAMPFIRE) ? CampfireBlock.isLitCampfire(blockstate) : flag;
	}
}
