package fuzs.eternalnether.world.level.block;

import com.mojang.serialization.MapCodec;
import fuzs.eternalnether.init.ModBlocks;
import fuzs.eternalnether.world.level.block.entity.NetheriteBellBlockEntity;
import fuzs.puzzleslib.api.block.v1.entity.TickingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NetheriteBellBlock extends BellBlock implements TickingEntityBlock<NetheriteBellBlockEntity> {
    public static final MapCodec<BellBlock> CODEC = simpleCodec(NetheriteBellBlock::new);

    public NetheriteBellBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<BellBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntityType<? extends NetheriteBellBlockEntity> getBlockEntityType() {
        return ModBlocks.NETHERITE_BELL_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TickingEntityBlock.super.newBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return TickingEntityBlock.super.getTicker(level, state, blockEntityType);
    }
}
