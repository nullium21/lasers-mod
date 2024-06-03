package moe.lina.lasers.util;

import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public class BeamSegment {
    public final float length;
    public final Vec3d direction;
    public final Quaternionf quat;

    public BeamSegment(float len, Vec3d dir) {
        length = len;
        direction = dir;
        quat = MathUtil.directionToQuat(dir);
    }
}
