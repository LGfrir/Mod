package me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput;

import me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig;
import me.LGfrir.H_infinity.client.UIDesign.SubUI.SubUI;

import static me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput.Safety.updateParameterValue;

public class BackSpace {
    /**
     * 处理键盘输入事件（仅在编辑模式下处理数字、点和退格键），并更新对应参数值。
     * 注意：更新参数值的逻辑内联在此方法中，不调用额外方法。
     */

    public static void handleSafeBackspace(SubUI subUI, ParameterConfig option) {
        if (!subUI.getEditingText().isEmpty()) {
            // 安全删除最后一个字符
            subUI.setEditingText(subUI.getEditingText().substring(0, subUI.getEditingText().length() - 1));

            // 处理删除后的特殊状态
            if (subUI.getEditingText().equals("-") || subUI.getEditingText().isEmpty()) {
                option.setValue(option.getMinValue());
            } else if (subUI.getEditingText().endsWith(".")) {
                subUI.setEditingText(subUI.getEditingText().replace(".", ""));
            }
            updateParameterValue(subUI,option);
        }
    }

}
