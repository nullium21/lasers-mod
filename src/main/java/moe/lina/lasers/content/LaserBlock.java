package moe.lina.lasers.content;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class LaserBlock extends Block implements BlockEntityProvider {
    public LaserBlock() {
        super(Settings.create());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LaserBE(pos, state);
    }
}
