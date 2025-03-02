package me.LGfrir.H_infinity.client.UIDesign.SubUI.Render;

import com.mojang.blaze3d.systems.RenderSystem;

public class Scissor {
    public static void EnableScissor(int scissorX, int scissorY, int scissorWidth, int scissorHeight) {
        RenderSystem.enableScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }
    public static void DisableScissor() {
        RenderSystem.disableScissor();
    }
}
