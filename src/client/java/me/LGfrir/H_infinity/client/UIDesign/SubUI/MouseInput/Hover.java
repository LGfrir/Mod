package me.LGfrir.H_infinity.client.UIDesign.SubUI.MouseInput;

import me.LGfrir.H_infinity.client.ConfigOperator.Config;
import me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig;
import me.LGfrir.H_infinity.client.ConfigOperator.Title;
import me.LGfrir.H_infinity.client.H_infinity;
import me.LGfrir.H_infinity.client.UIDesign.SubUI.SubUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig.ValueType.FLOAT;

public class Hover {

    // 获取鼠标悬停位置的参数项
    public static ParameterConfig getHoveredParameter(SubUI subUI, double mouseX, double mouseY) {
        List<Config> configList = subUI.getConfigList();
        int yPosition;
        int valueX = subUI.getX() + 150;

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int valueWidth = textRenderer.getWidth(Text.literal("Value: 123456.123")
                .styled(style -> style.withFont(H_infinity.jetbrains)));

        for (int i = 0; i < configList.size(); i++) {
            Config config = configList.get(i);
            if (config instanceof Title) continue;

            if (config instanceof ParameterConfig) {
                yPosition = subUI.getY() + subUI.getTextOffsetY() - subUI.getScrollOffset() + i * 20;

                if (mouseX >= valueX && mouseX <= valueX + valueWidth &&
                        mouseY >= yPosition && mouseY <= yPosition + 10) {
                    return (ParameterConfig) config;
                }
            }
        }
        return null;
    }

    // 滚轮调整数值逻辑
    public static void adjustValueByWheel(SubUI subUI,ParameterConfig param, double scrollDelta) {
        double step = calculateStep(param, Screen.hasShiftDown());
        double newValue = param.getValue() + (scrollDelta > 0 ? step : -step);

        // 应用范围限制
        newValue = Math.max(param.getMinValue(), Math.min(param.getMaxValue(), newValue));

        // 更新值并格式化显示
        param.setValue(newValue);
        if (param.getType() == FLOAT) {
            BigDecimal bd = BigDecimal.valueOf(newValue).setScale(3, RoundingMode.HALF_UP);
            subUI.setEditingText(bd.stripTrailingZeros().toPlainString());
        } else {
            subUI.setEditingText(String.valueOf((int) newValue));
        }
    }

    // 计算调整步长
    private static double calculateStep(ParameterConfig param, boolean isShiftDown) {
        if (param.getType() == FLOAT) {
            return isShiftDown ? 0.1 : 0.01; // Shift加速
        } else {
            return isShiftDown ? 10 : 1; // 整数步长
        }
    }
}
