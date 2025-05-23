package fuzs.eternalnether.world.level.block;

import fuzs.eternalnether.init.ModBlocks;
import fuzs.eternalnether.world.level.block.entity.NetheriteBellBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class NetheriteBellBlock extends BaseEntityBlock {
    public static final MapCodec<NetheriteBellBlock> CODEC = simpleCodec(NetheriteBellBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<BellAttachType> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape NORTH_SOUTH_FLOOR_SHAPE = Block.box(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D);
    private static final VoxelShape EAST_WEST_FLOOR_SHAPE = Block.box(4.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);
    private static final VoxelShape BELL_TOP_SHAPE = Block.box(5.0D, 6.0D, 5.0D, 11.0D, 13.0D, 11.0D);
    private static final VoxelShape BELL_BOTTOM_SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D);
    private static final VoxelShape BELL_SHAPE = Shapes.or(BELL_BOTTOM_SHAPE, BELL_TOP_SHAPE);
    private static final VoxelShape NORTH_SOUTH_BETWEEN = Shapes.or(BELL_SHAPE,
            Block.box(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 16.0D));
    private static final VoxelShape EAST_WEST_BETWEEN = Shapes.or(BELL_SHAPE,
            Block.box(0.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
    private static final VoxelShape TO_WEST = Shapes.or(BELL_SHAPE, Block.box(0.0D, 13.0D, 7.0D, 13.0D, 15.0D, 9.0D));
    private static final VoxelShape TO_EAST = Shapes.or(BELL_SHAPE, Block.box(3.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
    private static final VoxelShape TO_NORTH = Shapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 13.0D));
    private static final VoxelShape TO_SOUTH = Shapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 3.0D, 9.0D, 15.0D, 16.0D));
    private static final VoxelShape CEILING_SHAPE = Shapes.or(BELL_SHAPE,
            Block.box(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D));

    public NetheriteBellBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ATTACHMENT, BellAttachType.FLOOR)
                .setValue(POWERED, false));
    }

    @Override
    protected MapCodec<? extends NetheriteBellBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACHMENT, POWERED);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean bl = level.hasNeighborSignal(pos);
        if (bl != state.getValue(POWERED)) {
            if (bl) {
                this.attemptToRing(level, pos, null);
            }
            level.setBlock(pos, state.setValue(POWERED, bl), 3);
        }
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        Entity entity = projectile.getOwner();
        Player player = entity instanceof Player ? (Player) entity : null;
        this.onHit(level, state, hit, player, true);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.onHit(level, state, hitResult, player, true) ? InteractionResult.sidedSuccess(level.isClientSide) :
                InteractionResult.PASS;
    }

    public boolean onHit(Level level, BlockState state, BlockHitResult result, @Nullable Player player, boolean canRingBell) {
        Direction direction = result.getDirection();
        BlockPos blockPos = result.getBlockPos();
        boolean bl =
                !canRingBell || this.isProperHit(state, direction, result.getLocation().y - (double) blockPos.getY());
        if (bl) {
            boolean bl2 = this.attemptToRing(player, level, blockPos, direction);
            if (bl2 && player != null) {
                player.awardStat(Stats.BELL_RING);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean isProperHit(BlockState pos, Direction direction, double distanceY) {
        if (direction.getAxis() != Direction.Axis.Y && !(distanceY > 0.8123999834060669D)) {
            Direction direction2 = pos.getValue(FACING);
            BellAttachType bellAttachType = pos.getValue(ATTACHMENT);
            return switch (bellAttachType) {
                case FLOOR -> direction2.getAxis() == direction.getAxis();
                case SINGLE_WALL, DOUBLE_WALL -> direction2.getAxis() != direction.getAxis();
                case CEILING -> true;
                default -> false;
            };
        } else {
            return false;
        }
    }

    public boolean attemptToRing(Level level, BlockPos pos, @Nullable Direction direction) {
        return this.attemptToRing(null, level, pos, direction);
    }

    public boolean attemptToRing(@Nullable Entity entity, Level level, BlockPos pos, @Nullable Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!level.isClientSide && blockEntity instanceof NetheriteBellBlockEntity bellBlockEntity) {
            if (direction == null) {
                direction = level.getBlockState(pos).getValue(FACING);
            }
            bellBlockEntity.onHit(direction);
            level.playSound(null, pos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 4.5F, 0.5F);
            level.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
            return true;
        } else {
            return false;
        }
    }

    private VoxelShape getVoxelShape(BlockState state) {
        Direction direction = state.getValue(FACING);
        BellAttachType bellAttachType = state.getValue(ATTACHMENT);
        if (bellAttachType == BellAttachType.FLOOR) {
            return direction != Direction.NORTH && direction != Direction.SOUTH ? EAST_WEST_FLOOR_SHAPE :
                    NORTH_SOUTH_FLOOR_SHAPE;
        } else if (bellAttachType == BellAttachType.CEILING) {
            return CEILING_SHAPE;
        } else if (bellAttachType == BellAttachType.DOUBLE_WALL) {
            return direction != Direction.NORTH && direction != Direction.SOUTH ? EAST_WEST_BETWEEN :
                    NORTH_SOUTH_BETWEEN;
        } else if (direction == Direction.NORTH) {
            return TO_NORTH;
        } else if (direction == Direction.SOUTH) {
            return TO_SOUTH;
        } else {
            return direction == Direction.EAST ? TO_EAST : TO_WEST;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.getVoxelShape(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.getVoxelShape(state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction.Axis axis = direction.getAxis();
        BlockState blockState;
        if (axis == Direction.Axis.Y) {
            blockState = this.defaultBlockState()
                    .setValue(ATTACHMENT, direction == Direction.DOWN ? BellAttachType.CEILING : BellAttachType.FLOOR)
                    .setValue(FACING, context.getHorizontalDirection());
            if (blockState.canSurvive(context.getLevel(), blockPos)) {
                return blockState;
            }
        } else {
            boolean bl = axis == Direction.Axis.X &&
                    level.getBlockState(blockPos.west()).isFaceSturdy(level, blockPos.west(), Direction.EAST) &&
                    level.getBlockState(blockPos.east()).isFaceSturdy(level, blockPos.east(), Direction.WEST) ||
                    axis == Direction.Axis.Z && level.getBlockState(blockPos.north())
                            .isFaceSturdy(level, blockPos.north(), Direction.SOUTH) &&
                            level.getBlockState(blockPos.south())
                                    .isFaceSturdy(level, blockPos.south(), Direction.NORTH);
            blockState = this.defaultBlockState()
                    .setValue(FACING, direction.getOpposite())
                    .setValue(ATTACHMENT, bl ? BellAttachType.DOUBLE_WALL : BellAttachType.SINGLE_WALL);
            if (blockState.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockState;
            }
            boolean bl2 = level.getBlockState(blockPos.below()).isFaceSturdy(level, blockPos.below(), Direction.UP);
            blockState = blockState.setValue(ATTACHMENT, bl2 ? BellAttachType.FLOOR : BellAttachType.CEILING);
            if (blockState.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockState;
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        BellAttachType bellAttachType = state.getValue(ATTACHMENT);
        Direction direction2 = getConnectedDirection(state).getOpposite();
        if (direction2 == direction && !state.canSurvive(level, currentPos) &&
                bellAttachType != BellAttachType.DOUBLE_WALL) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if (direction.getAxis() == state.getValue(FACING).getAxis()) {
                if (bellAttachType == BellAttachType.DOUBLE_WALL &&
                        !neighborState.isFaceSturdy(level, neighborPos, direction)) {
                    return state.setValue(ATTACHMENT, BellAttachType.SINGLE_WALL)
                            .setValue(FACING, direction.getOpposite());
                }
                if (bellAttachType == BellAttachType.SINGLE_WALL && direction2.getOpposite() == direction &&
                        neighborState.isFaceSturdy(level, neighborPos, state.getValue(FACING))) {
                    return state.setValue(ATTACHMENT, BellAttachType.DOUBLE_WALL);
                }
            }
            return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = getConnectedDirection(state).getOpposite();
        return direction == Direction.UP ? Block.canSupportCenter(level, pos.above(), Direction.DOWN) :
                FaceAttachedHorizontalDirectionalBlock.canAttach(level, pos, direction);
    }

    private static Direction getConnectedDirection(BlockState state) {
        return switch (state.getValue(ATTACHMENT)) {
            case FLOOR -> Direction.UP;
            case CEILING -> Direction.DOWN;
            default -> state.getValue(FACING).getOpposite();
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NetheriteBellBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType,
                ModBlocks.NETHERITE_BELL_BLOCK_ENTITY_TYPE.value(),
                level.isClientSide ? NetheriteBellBlockEntity::clientTick : NetheriteBellBlockEntity::serverTick);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
