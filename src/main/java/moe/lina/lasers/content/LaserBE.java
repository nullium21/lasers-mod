package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Math;
import org.joml.Quaternionf;

public class LaserBE extends BlockEntity {

    private Vec3d laserDirection;
    private Quaternionf laserQuat;

    public LaserBE(BlockPos pos, BlockState state) {
        super(LasersMod.LASER_BE, pos, state);

        laserDirection = new Vec3d(1, 1, 0).normalize();
        recalcLaserQuat();
    }

    private void recalcLaserQuat() {
        laserQuat = directionToQuat(laserDirection);
    }

    public Vec3d getLaserDirection() {
        return laserDirection;
    }

    public Quaternionf getLaserQuat() {
        return laserQuat;
    }

    private static Quaternionf directionToQuat(Vec3d d) {
        // SO: https://stackoverflow.com/a/73103883
        var Q = new Quaternionf(0, 0, 0, 1);
        var U = new Quaternionf(0, 1, 0, 0);
        var V = new Quaternionf(d.x, d.y, d.z, 0);

        var dst = new Quaternionf();
        var tmp = new Quaternionf();

        // length(U*V)*Q
        U.mul(V, dst);
        Q.mul(Math.sqrt(dst.lengthSquared()), dst);

        // V*Q*U = (V*Q)*U
        V.mul(Q, tmp).mul(U);

        // a - b = a + (-b)
        return dst.add(tmp.mul(-1)).normalize();
    }
}
