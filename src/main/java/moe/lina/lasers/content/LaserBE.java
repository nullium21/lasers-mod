package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import moe.lina.lasers.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.compress.utils.Lists;
import org.joml.Math;
import org.joml.Quaternionf;

import java.util.List;

public class LaserBE extends BlockEntity {

    private BlockPos pointsAt;
    private final List<BeamSegment> segments = Lists.newArrayList();

    public LaserBE(BlockPos pos, BlockState state) {
        this(pos, state, null);
    }

    public LaserBE(BlockPos pos, BlockState state, BlockPos pointsAt) {
        super(LasersMod.LASER_BE, pos, state);
        setPointsAt(pointsAt);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        setPointsAt(components.get(LaserItem.TARGETED_BLOCK));
    }

    public void setPointsAt(BlockPos pointsAt) {
        this.pointsAt = pointsAt;
        if (pointsAt == null) return;

        float distance = (float) Math.sqrt(pos.getSquaredDistance(pointsAt));
        Vec3d direction = pointsAt.toCenterPos().subtract(pos.toCenterPos()).multiply(1 / distance);
        segments.add(new BeamSegment(distance, direction));
    }

    public BlockPos getPointsAt() {
        return pointsAt;
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
