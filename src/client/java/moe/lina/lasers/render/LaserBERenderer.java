package moe.lina.lasers.render;

import moe.lina.lasers.content.LaserBE;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class LaserBERenderer implements BlockEntityRenderer<LaserBE> {

    // model data for the beam, taken from Blender
    private static final float[][] vertices = {
            { -0.100000f, 0.000000f,  0.100000f },
            { -0.100000f, 16.000000f, 0.100000f },
            { -0.100000f, 0.000000f,  -0.100000f },
            { -0.100000f, 16.000000f, -0.100000f },
            { 0.100000f, 0.000000f,  0.100000f },
            { 0.100000f, 16.000000f, 0.100000f },
            { 0.100000f, 0.000000f,  -0.100000f },
            { 0.100000f, 16.000000f, -0.100000f },
    };

    private static final int[] indices = {
            1, 2, 4, 3,
            3, 4, 8, 7,
            7, 8, 6, 5,
            5, 6, 2, 1,
            3, 7, 5, 1,
            8, 4, 2, 6,
    };

    private static final RenderLayer RENDER_LAYER = RenderLayer.of(
            "laser_beam",
            VertexFormats.POSITION_COLOR,
            VertexFormat.DrawMode.QUADS,
            1536,
            false, true,
            RenderLayer.MultiPhaseParameters.builder()
                    .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                    .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
                    .writeMaskState(RenderPhase.ALL_MASK)
                    .program(new RenderPhase.ShaderProgram(() -> {
                        try {
                            var res = MinecraftClient.getInstance().getResourceManager();
                            return new ShaderProgram(res, "rendertype_laser_beam", VertexFormats.POSITION_COLOR);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }))
                    .build(false) // todo: what is `affectsOutline` for here? doesn't seem to affect the resulting picture
    );

    public LaserBERenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public boolean rendersOutsideBoundingBox(LaserBE blockEntity) {
        return true;
    }

    @Override
    public void render(LaserBE entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        var buffer = vertexConsumers.getBuffer(RENDER_LAYER);

        // translate to center
        matrices.translate(0.5, 0.5, 0.5);

        renderBeam(matrices, entity.getLaserQuat(), buffer);

        matrices.pop();
    }

    private static void renderBeam(MatrixStack matrices, Quaternionf rotation, VertexConsumer buffer) {
        matrices.multiply(rotation);

        var matrix = matrices.peek();
        for (int i : indices) {
            float[] pos = vertices[i-1];
            buffer.vertex(matrix, pos[0], pos[1], pos[2]).color(.25f, 0.f, 0.f, 0.5f).next();
        }
    }
}
