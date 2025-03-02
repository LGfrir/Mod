package me.LGfrir.H_infinity.client;

import org.lwjgl.glfw.GLFW;
import net.minecraft.client.option.KeyBinding;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class KeybindHandler {
    public static final KeyBinding openGUIkey = new KeyBinding(
            "key.H_infinity.open_gui",
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "HelloWorld"
    );
    public static final KeyBinding test = new KeyBinding(
            "key.H_infinity.test",
            GLFW.GLFW_KEY_G,
            "HelloWorld"
    );

    public static void register() {

        KeyBindingHelper.registerKeyBinding(openGUIkey);
        KeyBindingHelper.registerKeyBinding(test);
    }
}
