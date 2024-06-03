package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import moe.lina.lasers.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.compress.utils.Lists;
import org.joml.Quaternionf;

import java.util.List;

public class LaserBE extends BlockEntity {

    private final List<BeamSegment> segments = Lists.newArrayList();

    public LaserBE(BlockPos pos, BlockState state) {
        super(LasersMod.LASER_BE, pos, state);

        segments.add(new BeamSegment(15.5f, new Vec3d(1, 1, 0).normalize()));
        segments.add(new BeamSegment(10, new Vec3d(0, 1, 0)));
    }

    public List<BeamSegment> getSegments() {
        return segments;
    }

    public static class BeamSegment {
        public final float length;
        public final Vec3d direction;
        public final Quaternionf quat;

        public BeamSegment(float len, Vec3d dir) {
            length = len;
            direction = dir;
            quat = MathUtil.directionToQuat(dir);
        }
    }
}
