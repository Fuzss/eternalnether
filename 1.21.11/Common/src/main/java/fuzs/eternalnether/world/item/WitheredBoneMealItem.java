package fuzs.eternalnether.world.item;

import fuzs.puzzleslib.api.util.v1.InteractionResultHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.Nullable;

public class WitheredBoneMealItem extends BoneMealItem {
    private static final int GROWTH_BONUS_COUNT = 3;

    public WitheredBoneMealItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockPos blockPos2 = blockPos.relative(context.getClickedFace());
        if (growCrop(context.getItemInHand(), level, blockPos)) {
            if (!level.isClientSide()) {
                context.getPlayer().gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, blockPos, 15);
            }

            return InteractionResultHelper.sidedSuccess(level.isClientSide());
        } else {
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.isFaceSturdy(level, blockPos, context.getClickedFace())) {
                if (growWaterPlant(context.getItemInHand(), level, blockPos2, context.getClickedFace())) {
                    if (!level.isClientSide()) {
                        context.getPlayer().gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                        level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, blockPos2, 15);
                    }

                    return InteractionResultHelper.sidedSuccess(level.isClientSide());
                }
            }

            return InteractionResult.PASS;
        }
    }

    public static boolean growCrop(ItemStack itemStack, Level level, BlockPos blockPos) {
        boolean wasPlantGrown = false;
        for (int i = 0; i < GROWTH_BONUS_COUNT; i++) {
            if (BoneMealItem.growCrop(itemStack, level, blockPos)) {
                wasPlantGrown = true;
            } else {
                return wasPlantGrown;
            }
        }
        return wasPlantGrown;
    }

    public static boolean growWaterPlant(ItemStack itemStack, Level level, BlockPos blockPos, @Nullable Direction direction) {
        boolean wasPlantGrown = false;
        for (int i = 0; i < GROWTH_BONUS_COUNT; i++) {
            if (BoneMealItem.growWaterPlant(itemStack, level, blockPos, direction)) {
                wasPlantGrown = true;
            } else {
                return wasPlantGrown;
            }
        }
        return wasPlantGrown;
    }
}
