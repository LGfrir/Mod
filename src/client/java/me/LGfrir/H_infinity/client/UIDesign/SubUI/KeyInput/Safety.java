package me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput;

import me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig;
import me.LGfrir.H_infinity.client.UIDesign.SubUI.SubUI;
import org.lwjgl.glfw.GLFW;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig.ValueType.FLOAT;

public class Safety {
    // 更新参数值方法
    // 增强的数值更新逻辑
    public static void updateParameterValue(SubUI subUI, ParameterConfig option) {


        try {
            // 保留中间状态（空文本、单独符号）
            if (subUI.getEditingText().isEmpty() ||
                    subUI.getEditingText().equals("-") ||
                    subUI.getEditingText().equals(".")) {
                return;
            }

            // 防止科学计数法
            if (subUI.getEditingText().contains("E") || subUI.getEditingText().contains("e")) {
                subUI.setEditingText(String.valueOf(option.getMinValue()));
            }

            double parsed = Double.parseDouble(subUI.getEditingText());
            double clamped = Math.max(option.getMinValue(), Math.min(option.getMaxValue(), parsed));

            // 更新值但保留用户输入文本
            option.setValue(clamped);

            // 仅当数值被强制修正时更新文本
            if (clamped != parsed) {
                subUI.setEditingText(option.getType() == ParameterConfig.ValueType.INTEGER
                        ? String.valueOf((int) clamped)
                        : String.format("%.3f", clamped));
            }
        } catch (NumberFormatException e) {
            option.setValue(option.getMinValue());
        }
    }

    // 修复后的合法字符检测
    public static String getValidInputCharacter(int keyCode, int scanCode) {
        // 处理主键盘数字键
        if (keyCode >= GLFW.GLFW_KEY_0 && keyCode <= GLFW.GLFW_KEY_9) {
            return String.valueOf(keyCode - GLFW.GLFW_KEY_0);
        }

        // 处理小键盘数字键
        if (keyCode >= GLFW.GLFW_KEY_KP_0 && keyCode <= GLFW.GLFW_KEY_KP_9) {
            return String.valueOf(keyCode - GLFW.GLFW_KEY_KP_0);
        }

        // 处理小数点
        if (keyCode == GLFW.GLFW_KEY_PERIOD || keyCode == GLFW.GLFW_KEY_KP_DECIMAL) {
            return ".";
        }

        // 处理负号
        if (keyCode == GLFW.GLFW_KEY_MINUS) {
            return "-";
        }

        return null;
    }

    public static String handleSafeEnter(SubUI subUI ,ParameterConfig option, String editingText) {
        try {
            // 最终校验逻辑
            if (editingText.isEmpty() || editingText.equals("-") || editingText.equals(".")) {
                editingText = String.valueOf(option.getMinValue());
            }
            // 格式化浮点数
            if (option.getType() == FLOAT) {
                BigDecimal bd = new BigDecimal(editingText.contains(".") ? editingText : editingText + ".000")
                        .setScale(3, RoundingMode.HALF_UP);
                editingText = bd.stripTrailingZeros().toPlainString();
            }
            option.setValue(Double.parseDouble(editingText));
        } catch (Exception e) {
            option.setValue(option.getMinValue());
        }
        subUI.setEditing(false);
        return editingText;
    }
}
