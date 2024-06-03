package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class LaserBE extends BlockEntity {
    public LaserBE(BlockPos pos, BlockState state) {
        super(LasersMod.LASER_BE, pos, state);
    }
}
