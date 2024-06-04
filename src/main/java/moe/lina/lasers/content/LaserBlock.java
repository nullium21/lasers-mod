package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import moe.lina.lasers.base.HasIdentifier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LaserBlock extends Block implements BlockEntityProvider, HasIdentifier {
    public LaserBlock() {
        super(Settings.create());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LaserBE(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.25, 0.25, 0.25, 0.75, 0.75, 0.75),
                VoxelShapes.cuboid(0.375, 0.125, 0.375, 0.625, 0.25, 0.625),
                VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 0.125, 0.875)
        );
    }

    @Override
    public Identifier getId() {
        return LasersMod.id("laser");
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return LaserBE::tick;
    }
}
