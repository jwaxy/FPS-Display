package io.grayray75.fabric.fpsdisplay;

import io.grayray75.fabric.fpsdisplay.config.FpsDisplayConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class FpsDisplayMod implements ClientModInitializer {

    public static Boolean SHOW_FPS_OVERLAY;
    public static FpsDisplayConfig CONFIG;

    @Override
    public void onInitializeClient() {
        LogManager.getLogger().info("Initializing FPS-Display Mod");

        AutoConfig.register(FpsDisplayConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(FpsDisplayConfig.class).getConfig();
        SHOW_FPS_OVERLAY = CONFIG.enabled;

        KeyBinding binding_toggleOverlay = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fpsdisplay.toggleFpsOverlay", InputUtil.Type.KEYSYM, GLFW.GLFW_DONT_CARE, "key.fpsdisplay.category"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (binding_toggleOverlay.wasPressed() && !CONFIG.holdKeyToShowFps) {
                CONFIG.enabled = !CONFIG.enabled;
            }
            if (CONFIG.holdKeyToShowFps) {
                SHOW_FPS_OVERLAY = binding_toggleOverlay.isPressed();
            } else {
                SHOW_FPS_OVERLAY = CONFIG.enabled;
            }
        });
    }
}
