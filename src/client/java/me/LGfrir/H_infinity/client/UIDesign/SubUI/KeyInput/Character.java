package me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput;

import me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig;
import me.LGfrir.H_infinity.client.UIDesign.SubUI.SubUI;

import static me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig.ValueType.FLOAT;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput.Safety.getValidInputCharacter;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput.Safety.updateParameterValue;

public class Character {
    public static void handleCharacterInput(SubUI subUI, ParameterConfig option, int keyCode, int scanCode) {


        String input = getValidInputCharacter(keyCode, scanCode);
        if (input == null) return;

        // 处理特殊字符
        if (input.equals("-")) {
            if (subUI.getEditingText().isEmpty() && option.getMinValue() < 0) {
                subUI.setEditingText("-");
            }
        } else if (input.equals(".") && option.getType() == FLOAT) {
            if (!subUI.getEditingText().contains(".") && !subUI.getEditingText().endsWith("-")) {
                subUI.setEditingText(subUI.getEditingText() + ((subUI.getEditingText().isEmpty()) ? "0." : "."));
            }
        } else if (java.lang.Character.isDigit(input.charAt(0))) {
            subUI.setEditingText(subUI.getEditingText() + input);
        }

        updateParameterValue(subUI,option);
    }
}
