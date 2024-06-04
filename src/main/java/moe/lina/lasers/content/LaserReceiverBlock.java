package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import moe.lina.lasers.base.HasIdentifier;
import moe.lina.lasers.base.LaserReceiver;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LaserReceiverBlock extends Block implements HasIdentifier, LaserReceiver, BlockEntityProvider {

    public LaserReceiverBlock() {
        super(Settings.create());
        setDefaultState(getDefaultState().with(Properties.FACING, Direction.NORTH));
    }

    @Override
    public Identifier getId() {
        return LasersMod.id("laser_receiver");
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public LaserHitResult onLaserHit(LaserBE laser, BlockHitResult hitResult, World world, BlockState blockState) {
        if (world.getBlockEntity(hitResult.getBlockPos()) instanceof LaserReceiverBE be && !world.isClient()) {
            be.lit = hitResult.getSide() == blockState.get(Properties.FACING);
            be.lastUpdatedAt = world.getTime();
            world.updateNeighborsAlways(hitResult.getBlockPos(), this);
        }

        return LaserHitResult.CONSUME;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LaserReceiverBE(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (type != LasersMod.LASER_RECEIVER_BE) return null;
        return LaserReceiverBE::tick;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (direction != state.get(Properties.FACING) && world.getBlockEntity(pos) instanceof LaserReceiverBE be && be.lit)
            return 15;
        return 0;
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }
}
