package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import moe.lina.lasers.base.HasIdentifier;
import moe.lina.lasers.base.LaserReceiver;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MirrorBlock extends Block implements HasIdentifier, LaserReceiver {
    public MirrorBlock() {
        super(Settings.create());
        setDefaultState(getDefaultState().with(Properties.FACING, Direction.NORTH));
    }

    @Override
    public Identifier getId() {
        return LasersMod.id("mirror");
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(Properties.FACING)) {
            case UP -> VoxelShapes.cuboid(0, 0, 0, 1, 0.5, 1);
            case DOWN -> VoxelShapes.cuboid(0, 0.5, 0, 1, 1, 1);
            case NORTH -> VoxelShapes.cuboid(0, 0, 0.5, 1, 1, 1);
            case SOUTH -> VoxelShapes.cuboid(0, 0, 0, 1, 1, 0.5);
            case WEST -> VoxelShapes.cuboid(0.5, 0, 0, 1, 1, 1);
            case EAST -> VoxelShapes.cuboid(0, 0, 0, 0.5, 1, 1);
        };
    }

    @Override
    public LaserHitResult onLaserHit(LaserBE laser, BlockHitResult hitResult, World world, BlockState blockState) {
        var goodSide = blockState.get(Properties.FACING);
        if (hitResult.getSide() != goodSide) return LaserHitResult.CONSUME;

        var incomingDirection = (hitResult.getBlockPos().toCenterPos().subtract(laser.getPos().toCenterPos())).normalize();
        var normal = goodSide.getUnitVector();

        // math from https://math.stackexchange.com/a/13263
        // r = d - 2(d . n)n, where d . n = dot product
        var reflectDirection = incomingDirection.toVector3f();
        reflectDirection.sub(normal.mul(2).mul(incomingDirection.toVector3f().dot(normal)));

        var result = LaserHitResult.REDIRECT;
        result.newDirection = new Vec3d(reflectDirection.normalize());
        return result;
    }
}
