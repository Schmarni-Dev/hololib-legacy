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
import net.minecraft.util.math.Vec3i;

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
//        var previous = Vec3d.ZERO;

        var scale = 1.001f;
        var offset = (scale - 1f) * 0.5f;
        var inverse_scale = 1/scale;
        for (var data : states) {
            int val = data.getLeft().getX() + data.getLeft().getY() +data.getLeft().getZ();
            boolean is_wrong = val % 2 == 1;

            var pos = Vec3d.of(data.getLeft()).subtract(new Vec3d(offset,offset,offset));
            matrixStack.translate(pos.getX(), pos.getY(), pos.getZ());

            this.blockRenderManager.renderBlockAsEntity(data.getRight(), matrixStack, vertexConsumerProvider, 0x00F00000, OverlayTexture.getUv(0,is_wrong));
            matrixStack.scale(inverse_scale,inverse_scale,inverse_scale);
            matrixStack.translate(-pos.getX(), -pos.getY(), -pos.getZ());
        }

    }
}
