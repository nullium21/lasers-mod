package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import moe.lina.lasers.base.LaserReceiver;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MirrorBE extends BlockEntity implements LaserReceiver {
    public MirrorBE(BlockPos pos, BlockState state) {
        super(LasersMod.MIRROR_BE, pos, state);
    }

    @Override
    public LaserHitResult onLaserHit(LaserBE laser, BlockHitResult hitResult, World world) {
        var goodSide = world.getBlockState(hitResult.getBlockPos()).get(Properties.FACING);
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
