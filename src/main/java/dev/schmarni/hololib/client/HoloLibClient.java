package dev.schmarni.hololib.client;

import dev.schmarni.hololib.enities.HoloDisplayEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;


import static dev.schmarni.hololib.HoloLib.*;

public class HoloLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ERROR_BLOCK, RenderLayer.getTranslucent());
        EntityRendererRegistry.register(HOLO_DISPLAY_ENTITY_TYPE, HoloDisplayEntityRenderer::new);
    }
}
