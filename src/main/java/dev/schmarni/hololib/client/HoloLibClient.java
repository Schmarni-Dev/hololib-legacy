package dev.schmarni.hololib.client;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.schmarni.hololib.enities.HoloDisplayEntity;
import dev.schmarni.hololib.enities.HoloDisplayEntityRenderer;
import dev.schmarni.hololib.utils.HashList3Helper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.registry.Registries;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import static dev.schmarni.hololib.HoloLib.*;

public class HoloLibClient implements ClientModInitializer {
    private static KeyBinding keyBinding;
    @Override
    public void onInitializeClient() {
        LOGGER.info("Client Startup");
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.examplemod.spook", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "category.examplemod.test" // The translation key of the keybinding's category.
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                assert client.world != null;
                try {
                    assert client.player != null;
                    client.player.networkHandler.getCommandDispatcher().execute("kill @e[type=!minecraft:player]",client.player.getCommandSource());
                } catch (CommandSyntaxException ignored) {

                }
            }

        });
        EntityRendererRegistry.register(HOLO_DISPLAY_ENTITY_TYPE, HoloDisplayEntityRenderer::new);
    }
}
