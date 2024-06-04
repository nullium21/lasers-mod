package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import moe.lina.lasers.base.LaserReceiver;
import moe.lina.lasers.util.BeamSegment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.DyeColor;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Lists;
import org.joml.Math;

import java.util.List;

public class LaserBE extends BlockEntity {

    public static final int MAX_RAYCAST_BOUNCES = 128;

    private BlockPos pointsAt;
    private final List<BeamSegment> segments = Lists.newArrayList();

    public LaserBE(BlockPos pos, BlockState state) {
        this(pos, state, null);
    }

    public LaserBE(BlockPos pos, BlockState state, BlockPos pointsAt) {
        super(LasersMod.LASER_BE, pos, state);
        setPointsAt(pointsAt);
    }

    //? if >=1.20.6 {
    @Override
    protected void readComponents(ComponentsAccess components) {
        setPointsAt(components.get(LaserItem.TARGETED_BLOCK));
    }
    //?}

    @Override
    //? if >=1.20.6 {
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    //?} else {
    /*public void readNbt(NbtCompound nbt) {
    *///?}
        setPointsAt(BlockPos.CODEC.parse(NbtOps.INSTANCE, nbt.get("TargetedBlockPos")).result().orElse(null));
    }

    @Override
    //? if >=1.20.6 {
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    //?} else {
    /*public void writeNbt(NbtCompound nbt) {
    *///?}
        nbt.put("TargetedBlockPos", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, getPointsAt()).result().orElse(null));
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
        raycast(world, posVec, direction, be, be.segments, null);
    }

    private static void raycast(World world, Vec3d from, Vec3d direction, LaserBE be, List<BeamSegment> addTo, DyeColor color) {
        if (addTo.size() > MAX_RAYCAST_BOUNCES) return;

        var raycast = world.raycast(new RaycastContext(
                from.add(direction), from.add(direction.multiply(1024)),
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY,
                //? if >=1.20.6 {
                ShapeContext.absent()
                 //?} else {
                /*null
                *///?}
        ));

        if (raycast.getType() == HitResult.Type.MISS) {
            var segment = new BeamSegment(1024, direction);
            segment.dyeColor = color;
            addTo.add(segment);
            return;
        }

        var segment = new BeamSegment((float) raycast.getPos().distanceTo(from), direction);
        segment.dyeColor = color;
        addTo.add(segment);

        var blockstate = world.getBlockState(raycast.getBlockPos());
        if (blockstate.getBlock() instanceof Stainable st) color = st.getColor();

        if (blockstate.isIn(LasersMod.LASER_TRANSPARENT)) {
            raycast(world, raycast.getPos(), direction, be, addTo, color);
        }

        if (blockstate.getBlock() instanceof LaserReceiver recv) {
            var result = recv.onLaserHit(be, raycast, world, blockstate);
            if (result == LaserReceiver.LaserHitResult.CONSUME) return;

            var newDirection = result == LaserReceiver.LaserHitResult.REDIRECT ? result.newDirection : direction;
            raycast(world, raycast.getPos(), newDirection, be, addTo, color);
        }
    }
}
