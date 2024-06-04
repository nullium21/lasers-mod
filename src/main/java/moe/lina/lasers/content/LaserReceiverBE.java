package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LaserReceiverBE extends BlockEntity {

    boolean lit = false;
    long lastUpdatedAt = -1;

    public LaserReceiverBE(BlockPos pos, BlockState state) {
        super(LasersMod.LASER_RECEIVER_BE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState blockState, BlockEntity ent) {
        if (!(ent instanceof LaserReceiverBE be)) return;

        // update the state every 5 ticks
        if (be.lit && (world.getTime() - be.lastUpdatedAt) >= 5) {
            be.lit = false;
            world.updateNeighborsAlways(pos, blockState.getBlock());
        }
    }
}
