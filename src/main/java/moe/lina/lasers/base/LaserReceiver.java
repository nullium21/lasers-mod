package moe.lina.lasers.base;

import moe.lina.lasers.content.LaserBE;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface LaserReceiver {
    LaserHitResult onLaserHit(LaserBE laser, BlockHitResult hitResult, World world);

    enum LaserHitResult {
        /** the beam should get redirected towards {@code newDirection} */
        REDIRECT,
        /** your path ends here, beam. */
        CONSUME,
        /** look at me, i don't exist! -block */
        CONTINUE;

        /** only used in {@code REDIRECT} variant. others should leave as null */
        public Vec3d newDirection;
    }
}
