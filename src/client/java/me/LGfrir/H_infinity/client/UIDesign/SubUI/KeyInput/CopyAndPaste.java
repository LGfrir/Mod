package me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput;

import me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig;
import me.LGfrir.H_infinity.client.UIDesign.SubUI.SubUI;
import net.minecraft.client.MinecraftClient;

import static me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput.Safety.updateParameterValue;

public class CopyAndPaste {
    public static void copyToClipboard(String content) {
        MinecraftClient.getInstance().keyboard.setClipboard(content);
    }
    public static String getClipboardText() {
        return MinecraftClient.getInstance().keyboard.getClipboard();
    }

    // 安全粘贴方法
    public static void handleSafePaste(SubUI subUI, ParameterConfig option) {
        String clipboard = getClipboardText();
        if (clipboard == null) return;

        String filtered = clipboard.replaceAll("[^0-9.-]", "");
        // 符号处理
        filtered = filtered
                .replaceFirst("^-\\.", "-0.")  // 处理 "-." 开头的情况
                .replaceFirst("^\\.", "0.");   // 处理 "." 开头的情况

        // 长度限制
        int maxLength = option.getType() == ParameterConfig.ValueType.INTEGER ? 10 : 15;
        filtered = filtered.substring(0, Math.min(filtered.length(), maxLength));

        subUI.setEditingText(filtered.isEmpty() ? String.valueOf(option.getMinValue()) : filtered);
        updateParameterValue(subUI,option);
    }
}
