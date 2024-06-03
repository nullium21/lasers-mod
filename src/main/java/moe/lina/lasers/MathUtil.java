package moe.lina.lasers;

import net.minecraft.util.math.Vec3d;
import org.joml.Math;
import org.joml.Quaternionf;

public class MathUtil {
    public static Quaternionf directionToQuat(Vec3d d) {
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
