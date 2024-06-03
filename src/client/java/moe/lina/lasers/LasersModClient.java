package moe.lina.lasers;

import moe.lina.lasers.render.LaserBERenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import static moe.lina.lasers.LasersMod.*;

public class LasersModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		BlockEntityRendererFactories.register(LASER_BE, LaserBERenderer::new);
	}
}