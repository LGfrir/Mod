package me.LGfrir.H_infinity.client.UIDesign.SubUI.Render;

import me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig;
import me.LGfrir.H_infinity.client.H_infinity;
import me.LGfrir.H_infinity.client.UIDesign.SubUI.SubUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig.ValueType.FLOAT;

public class RenderBasic {

    // 基本颜色
    private static final int TEXT_WHITE = 0xFFFFFF;
    private static final int TEXT_RED = 0xFFFF0000;
    private static final int BACKGROUND_YELLOW = 0x88FFFF00;

    public static String renderBasic(SubUI subUI, int i, DrawContext context) {

        boolean isEditing = subUI.isEditing();
        int x = subUI.getX();
        int yPosition = subUI.getY() + subUI.getTextOffsetY() - subUI.getScrollOffset() + i * 20;
        int selectedOptionIndex = subUI.getSelectedOptionIndex();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        String editingText = subUI.getEditingText();
        int textWidth = textRenderer.getWidth(Text.literal("Value: 123456.123").styled(style -> style.withFont(H_infinity.jetbrains)));
        ParameterConfig param = (ParameterConfig) subUI.getConfigList().get(i);

        context.drawText(textRenderer,
                Text.literal(param.getName()).styled(style -> style.withFont(H_infinity.jetbrains)),
                x + 10, yPosition, TEXT_WHITE, false);

        // 模块：构建参数值显示文本
        String valueText;
        int valueX = x + 150;

        if (isEditing && selectedOptionIndex == i) {
            valueText = "Value: " + editingText;
        } else {
            if (param.getType() == FLOAT) {
                BigDecimal bd = BigDecimal.valueOf(param.getValue()).setScale(3, RoundingMode.HALF_UP);
                valueText = "Value: " + bd.stripTrailingZeros().toPlainString();
            } else {
                valueText = "Value: " + (int) param.getValue();
            }
        }

        // 模块：编辑模式下的高亮显示
        if (isEditing && selectedOptionIndex == i) {
            context.fill(valueX - 2, yPosition - 3, valueX + textWidth + 2, yPosition + 10, BACKGROUND_YELLOW);
            context.drawText(textRenderer,
                    Text.literal(valueText).styled(style -> style.withFont(H_infinity.jetbrains)),
                    valueX, yPosition, TEXT_RED, false);
        } else {
            context.drawText(textRenderer,
                    Text.literal(valueText).styled(style -> style.withFont(H_infinity.jetbrains)),
                    valueX, yPosition, TEXT_WHITE, false);
        }
        return valueText;
    }
}
