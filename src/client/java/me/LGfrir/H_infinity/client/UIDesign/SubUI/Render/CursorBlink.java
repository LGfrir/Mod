package me.LGfrir.H_infinity.client.UIDesign.SubUI.Render;

import me.LGfrir.H_infinity.client.UIDesign.SubUI.SubUI;
import net.minecraft.client.gui.DrawContext;

public class CursorBlink {

    // 闪烁参数
    private static long cursorBlinkTime = 0;
    public static void setCursorBlinkTime(long time) {cursorBlinkTime = time;}
    private static final int CURSOR_BLINK_INTERVAL = 500; // 闪烁间隔500毫秒

    // 光标颜色
    private static final int CURSOR_COLOR = 0xFFFFFFFF;

    public static void cursorBlink(SubUI subUI, int i ,int valueTextLength, DrawContext context)
    {
        int valueX = subUI.getX() + 150;
        int selectedOptionIndex = subUI.getSelectedOptionIndex();
        boolean isEditing = subUI.isEditing();
        int yPosition = subUI.getY() + subUI.getTextOffsetY() - subUI.getScrollOffset() + i * 20;

        if (isEditing && selectedOptionIndex == i) {
            int cursorPosX = valueX + valueTextLength;

            long currentTime = System.currentTimeMillis();
            if ((currentTime - cursorBlinkTime) % (CURSOR_BLINK_INTERVAL * 2) < CURSOR_BLINK_INTERVAL)
                context.drawVerticalLine(cursorPosX, yPosition - 3, yPosition + 10, CURSOR_COLOR);

        }
    }
}
