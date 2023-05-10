package dev.schmarni.hololib.enities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.DisplayEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import static dev.schmarni.hololib.HoloLib.LOGGER;

@Environment(value= EnvType.CLIENT)
public class HoloDisplayEntityRenderer extends DisplayEntityRenderer<HoloDisplayEntity> {
    private final BlockRenderManager blockRenderManager;

    public HoloDisplayEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render(HoloDisplayEntity holoDisplayEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, float f) {
        var states = holoDisplayEntity.getBlockStates().getAllItemsAndIndexes();
        var previous = Vec3d.ZERO;
        var scale = 1.1f;
        var inverse_scale = 1/scale;
        for (var data : states) {
            var pos = data.getLeft();
            matrixStack.translate(pos.getX() - previous.getX(), pos.getY() - previous.getY(), pos.getZ() - previous.getZ());
            matrixStack.scale(scale,scale,scale);

            previous = Vec3d.of(pos);
            this.blockRenderManager.renderBlockAsEntity(data.getRight(), matrixStack, vertexConsumerProvider, 0x00F00000, OverlayTexture.getUv(0,true));
            matrixStack.scale(inverse_scale,inverse_scale,inverse_scale);
        }

    }
}
