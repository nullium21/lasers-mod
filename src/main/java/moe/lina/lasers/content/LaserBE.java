package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import moe.lina.lasers.base.LaserReceiver;
import moe.lina.lasers.util.BeamSegment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Lists;
import org.joml.Math;

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

    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity ent) {
        if (!(ent instanceof LaserBE be)) return;
        if (be.pointsAt == null) return;

        var posVec = pos.toCenterPos();

        var direction = be.segments.isEmpty()
                ? be.pointsAt.toCenterPos().subtract(posVec).normalize()
                : be.segments.getFirst().direction;

        be.segments.clear();
        raycast(world, posVec, direction, be, be.segments);
    }

    private static void raycast(World world, Vec3d from, Vec3d direction, LaserBE be, List<BeamSegment> addTo) {
        var raycast = world.raycast(new RaycastContext(
                from.add(direction), from.add(direction.multiply(1024)),
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY,
                ShapeContext.absent()
        ));

        if (raycast.getType() == HitResult.Type.MISS) {
            addTo.add(new BeamSegment(1024, direction));
            return;
        }

        addTo.add(new BeamSegment((float) raycast.getPos().distanceTo(from), direction));

        if (world.getBlockEntity(raycast.getBlockPos()) instanceof LaserReceiver recv) {
            var result = recv.onLaserHit(be, raycast, world);
            if (result == LaserReceiver.LaserHitResult.CONSUME) return;

            var newDirection = result == LaserReceiver.LaserHitResult.REDIRECT ? result.newDirection : direction;
            raycast(world, raycast.getPos(), newDirection, be, addTo);
        }
    }
}
