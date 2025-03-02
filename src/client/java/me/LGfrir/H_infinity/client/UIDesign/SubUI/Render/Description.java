package me.LGfrir.H_infinity.client.UIDesign.SubUI.Render;

import me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig;
import me.LGfrir.H_infinity.client.H_infinity;
import me.LGfrir.H_infinity.client.UIDesign.SubUI.SubUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;


public class Description {

    //悬停参数
    private static final long HOVER_DELAY = 1000;
    private static long hoverStartTime;
    private static final int TEXT_WHITE = 0xFFFFFF;
    //渲染颜色
    private static final int TOOLTIP_BG = 0xAA000000;

    public static int ifMouseHover(SubUI subUI, int mouseX, int mouseY, int i, DrawContext context)
    {

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = textRenderer.getWidth(Text.literal("Value: 123213.231")
                .styled(style -> style.withFont(H_infinity.jetbrains)));
        int x = subUI.getX();
        int yPosition = subUI.getY() + subUI.getTextOffsetY() - subUI.getTextOffsetY() + i * 20;
        ParameterConfig param = (ParameterConfig) subUI.getConfigList().get(i);
        int hoveredIndex = subUI.getHoveredIndex();

        // 模块：鼠标悬停检测及描述提示
        boolean isHovered = mouseX >= x + 10 && mouseX <= x + 10 + textWidth &&
                mouseY >= yPosition && mouseY <= yPosition + 10;
        if (isHovered) {
            if (hoveredIndex != i) {
                hoveredIndex = i;
                hoverStartTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - hoverStartTime >= HOVER_DELAY) {
                // 绘制描述提示框（文本同样使用自定义字体）
                String description = param.getDescription(); // 假设存在此方法
                int descX = x + 10 + textWidth + 5;
                context.fill(descX - 2, yPosition - 2, descX + textRenderer.getWidth(
                        Text.literal(description).styled(style -> style.withFont(H_infinity.jetbrains))
                ) + 2, yPosition + 10, TOOLTIP_BG);
                context.drawText(textRenderer,
                        Text.literal(description).styled(style -> style.withFont(H_infinity.jetbrains)),
                        descX, yPosition, TEXT_WHITE, false);
            }
        }

        return  hoveredIndex;
    }
}
