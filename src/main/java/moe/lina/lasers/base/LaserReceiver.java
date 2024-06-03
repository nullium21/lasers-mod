package moe.lina.lasers.base;

import moe.lina.lasers.content.LaserBE;
import net.minecraft.util.hit.BlockHitResult;

public interface LaserReceiver {
    void onLaserHit(LaserBE laser, BlockHitResult hitResult);
}
