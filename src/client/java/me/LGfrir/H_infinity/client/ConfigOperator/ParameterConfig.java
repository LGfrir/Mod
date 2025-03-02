package me.LGfrir.H_infinity.client.ConfigOperator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ParameterConfig implements Config {


    public enum ValueType {INTEGER, FLOAT}

    private final String name;
    private final ValueType type;
    private final double minValue;
    private final double maxValue;
    private final int decimalPlaces;

    private final Supplier<Double> valueGetter;  // 新增：获取变量当前值
    private final Consumer<Double> valueSetter;  // 新增：修改变量值

    String description = "";

    @Override
    public String getDescription() {
        return description;
    }


    public ParameterConfig(String name, ValueType type,
                           double min, double max, int decimals,
                           Supplier<Double> getter, Consumer<Double> setter) {
        this.name = name;
        this.type = type;
        this.minValue = min;
        this.maxValue = max;
        this.decimalPlaces = type == ValueType.INTEGER ? 0 : decimals;
        this.valueGetter = getter;
        this.valueSetter = setter;
    }

    public ParameterConfig(String name, ValueType type,
                           double min, double max, int decimals,
                           Supplier<Double> getter, Consumer<Double> setter,
                           String description) {
        this.name = name;
        this.type = type;
        this.minValue = min;
        this.maxValue = max;
        this.decimalPlaces = type == ValueType.INTEGER ? 0 : decimals;
        this.valueGetter = getter;
        this.valueSetter = setter;
        this.description = description;
    }

    public void setValue(double value) {
        double newValue = Math.min(Math.max(value, minValue), maxValue);

        if (type == ValueType.INTEGER) {
            newValue = (int) newValue;
        } else {
            BigDecimal bd = new BigDecimal(newValue)
                    .setScale(decimalPlaces, RoundingMode.HALF_UP);
            newValue = bd.doubleValue();
        }

        // 直接调用 setter 更新外部变量
        valueSetter.accept(newValue);
    }

    public double getValue() {
        return valueGetter.get(); // 通过 getter 获取当前值
    }


    public String getRangeHint() {
        return type == ValueType.INTEGER ?
                String.format("[%d-%d]", (int) minValue, (int) maxValue) :
                String.format("[%.1f-%.1f]", minValue, maxValue);
    }

    public String getName() {
        return name;
    }

    public ValueType getType() {
        return type;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }
}