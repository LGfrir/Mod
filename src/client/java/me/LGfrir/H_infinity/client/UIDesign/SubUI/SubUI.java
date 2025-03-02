package me.LGfrir.H_infinity.client.UIDesign.SubUI;

import me.LGfrir.H_infinity.client.ConfigOperator.Config;
import me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig;
import me.LGfrir.H_infinity.client.ConfigOperator.Title;
import me.LGfrir.H_infinity.client.H_infinity;
import me.LGfrir.H_infinity.client.UIDesign.SubUI.Render.CursorBlink;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig.ValueType.FLOAT;
import static me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig.ValueType.INTEGER;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput.BackSpace.handleSafeBackspace;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput.Character.handleCharacterInput;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput.CopyAndPaste.*;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.KeyInput.Safety.handleSafeEnter;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.MouseInput.Hover.adjustValueByWheel;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.MouseInput.Hover.getHoveredParameter;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.Render.CursorBlink.cursorBlink;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.Render.Description.ifMouseHover;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.Render.RenderBasic.renderBasic;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.Render.Scissor.DisableScissor;
import static me.LGfrir.H_infinity.client.UIDesign.SubUI.Render.Scissor.EnableScissor;

public class SubUI extends Screen {
    // 界面位置与尺寸
    private int x, y, width, height;
    // 配置项列表
    private final List<Config> configList;
    // 滚动相关
    private int scrollOffset = 0;
    private int maxScrollOffset = 0;
    private int scrollbarHeight = 20;
    private boolean dragging = false;
    // 文本垂直偏移
    private int textOffsetY = 5;
    // 界面开关
    public boolean isOpen = false;

    // 编辑状态
    private int selectedOptionIndex = -1; // 当前选中参数项索引
    private boolean isEditing = false;    // 是否处于编辑模式
    private String editingText = "";        // 编辑时显示的文本

    // 悬停提示
    private int hoveredIndex = -1;   // 当前悬停项索引

    // 颜色常量
    private static final int TEXT_WHITE = 0xFFFFFF;
    private static final int SCROLLBAR_COLOR = 0xFFFFFFFF;

    /**
     * 构造函数，传入配置项列表
     */
    public SubUI(List<Config> options) {
        super(Text.literal("Sub UI").styled(style -> style.withFont(H_infinity.jetbrains)));
        this.configList = options;
    }

    /**
     * 打开界面
     */
    public void open() {
        if (!isOpen) {
            isOpen = true;
            MinecraftClient.getInstance().setScreen(this);
        }
    }

    /**
     * 关闭界面
     */
    public void close() {
        if (isOpen) {
            isOpen = false;
            MinecraftClient.getInstance().setScreen(null);
        }
    }


    /**
     * 初始化界面参数，根据屏幕尺寸设置宽高、位置及滚动条参数
     */
    @Override
    protected void init() {
        int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        this.width = (int) (screenWidth * 0.3);
        this.height = (int) (this.width * 0.8);
        this.x = (screenWidth - this.width) / 2;
        this.y = 150;
        this.textOffsetY = 5;

        // 模块：滚动条初始化
        int totalOptionHeight = configList.size() * 20;
        this.maxScrollOffset = Math.max(0, totalOptionHeight - height);
        if (maxScrollOffset > 0)
            // 根据比例计算滚动条高度，最小为20
            this.scrollbarHeight = Math.max(20, (int) ((height / (float) totalOptionHeight) * height));
        else this.scrollbarHeight = 0;
    }

    /**
     * 渲染界面，各模块：背景、剪裁区域、参数项模块、标题模块、滚动条模块
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        // 背景绘制
        context.fill(x, y, x + width, y + height, 0x88000000);

        // 启用画面剪裁
        int scale = (int) MinecraftClient.getInstance().getWindow().getScaleFactor();// 剪裁区域
        EnableScissor(x * scale, MinecraftClient.getInstance().getWindow().getHeight() - (y + height) * scale,
                width * scale, height * scale);

        // 遍历所有项渲染
        for (int i = 0; i < configList.size(); i++) {
            Object element = configList.get(i);
            int yPosition = y + textOffsetY - scrollOffset + i * 20;

            /**=============================================================================
             *                                   渲染参数项
             * =============================================================================*/
            if (element instanceof ParameterConfig) {
                // 鼠标悬停检测及描述提示
                hoveredIndex = ifMouseHover(this, mouseX, mouseY, i, context);

                // 绘制参数基础信息
                String valueText = renderBasic(this, i, context);

                // 光标闪烁（仅编辑）
                cursorBlink(this,i, textRenderer.getWidth(Text.literal(valueText).styled(style -> style.withFont(H_infinity.jetbrains))), context);
            }

            /**=============================================================================
             *                                   渲染标题项
             * =============================================================================*/
            if (element instanceof Title title) {
                int centerX = x + (width - textRenderer.getWidth(title.getTitle())) / 2;
                context.drawText(textRenderer,
                        Text.literal(title.getTitle()).styled(style -> style.withFont(H_infinity.jetbrains)),
                        centerX, yPosition, TEXT_WHITE, false);
            }
        }

        // 关闭剪裁区域
        DisableScissor();

        // 模块：滚动条绘制（当内容超出时显示）
        if (scrollbarHeight > 0) {
            int scrollbarX = x + width - 10;
            int scrollbarY = y + (int) ((scrollOffset / (float) maxScrollOffset) * (height - scrollbarHeight));
            context.fill(scrollbarX, scrollbarY, scrollbarX + 5, scrollbarY + scrollbarHeight, SCROLLBAR_COLOR);
        }
    }

    /**
     * 处理鼠标点击事件：
     * 1. 优先检测是否点击滚动条区域进入拖动模式；
     * 2. 检测是否点击参数值区域进入编辑模式；
     * 3. 其他情况调用父类处理。
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 模块：滚动条点击检测
        int scrollbarX = x + width - 10;
        int scrollbarY = y + (int) ((scrollOffset / (float) maxScrollOffset) * (height - scrollbarHeight));
        if (mouseX >= scrollbarX && mouseX <= scrollbarX + 5 &&
                mouseY >= scrollbarY && mouseY <= scrollbarY + scrollbarHeight) {
            dragging = true;
            return true;
        }
        // 模块：检测点击参数项（编辑模式进入）
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        // 使用自定义字体测量显示区域
        int valueWidth = textRenderer.getWidth(Text.literal("Value: 123456.123")
                .styled(style -> style.withFont(H_infinity.jetbrains)));
        for (int i = 0; i < configList.size(); i++) {
            Object element = configList.get(i);
            if (element instanceof ParameterConfig option) {
                int yPosition = y + textOffsetY - scrollOffset + i * 20;
                int valueX = x + 150;
                if (mouseX >= valueX && mouseX <= valueX + valueWidth &&
                        mouseY >= yPosition && mouseY <= yPosition + 10) {
                    selectedOptionIndex = i;
                    isEditing = true;
                    // 模块：初始化编辑文本（含格式化逻辑）
                    if (option.getType() == FLOAT) {
                        BigDecimal bd = BigDecimal.valueOf(option.getValue()).setScale(3, RoundingMode.HALF_UP);
                        editingText = bd.stripTrailingZeros().toPlainString();
                        if (!editingText.contains(".")) {
                            editingText += ".000";
                        } else {
                            int decimalDigits = editingText.length() - editingText.indexOf('.') - 1;
                            if (decimalDigits < 3) {
                                editingText += "0".repeat(3 - decimalDigits);
                            }
                        }
                    }
                    else if(option.getType() == INTEGER) {
                        editingText = String.valueOf((int) option.getValue());
                    }
                    // 模块：更新光标闪烁时间（内联原 updateCursorBlink 逻辑）

                    CursorBlink.setCursorBlinkTime(currentTimeMillis());
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * 处理鼠标释放事件，结束滚动条拖动状态
     */
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalScroll, double verticalScroll) {
        // 1. 优先处理参数值的滚轮调整
        ParameterConfig hoveredParam = getHoveredParameter(this,mouseX, mouseY);
        if (hoveredParam != null && !isEditing) {
            adjustValueByWheel(this, hoveredParam, verticalScroll);
            return true;
        }

        // 2. 处理界面滚动（仅在内容可滚动时生效）
        if (maxScrollOffset > 0) {
            int scrollStep = 15; // 每次滚动的步长（像素）
            scrollOffset += (int) (-verticalScroll * scrollStep);
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScrollOffset));
            return true;
        }

        return false;
    }


    /**
     * 处理鼠标拖动事件，实现滚动条拖动效果
     */
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            float scrollRatio = maxScrollOffset > 0 ? (float) maxScrollOffset / (height - scrollbarHeight) : 1;
            scrollOffset += (int) (deltaY * scrollRatio);
            scrollOffset = Math.max(0, Math.min(maxScrollOffset, scrollOffset));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isEditing) return super.keyPressed(keyCode, scanCode, modifiers);

        ParameterConfig selectedOption = getSelectedParameter();
        if (selectedOption == null) return true;

        // 拦截所有组合键（除了允许的Ctrl+C/V）
        if ((modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
            switch (keyCode) {
                case GLFW.GLFW_KEY_C:
                    copyToClipboard(editingText);
                    return true;
                case GLFW.GLFW_KEY_V:
                    handleSafePaste(this,selectedOption);
                    return true;
                default:
                    return true; // 阻止其他Ctrl组合
            }
        }

        // 阻止其他修饰键组合
        if ((modifiers & (GLFW.GLFW_MOD_ALT | GLFW.GLFW_MOD_SHIFT)) != 0) {
            return true;
        }

        switch (keyCode) {
            case GLFW.GLFW_KEY_ESCAPE:
                isEditing = false;
                return true;
            case GLFW.GLFW_KEY_ENTER:
            case GLFW.GLFW_KEY_KP_ENTER:
                handleSafeEnter(this,selectedOption, editingText);
                return true;
            case GLFW.GLFW_KEY_BACKSPACE:
                handleSafeBackspace(this,selectedOption);
                return true;
        }

        handleCharacterInput(this,selectedOption, keyCode, scanCode);
        return true;
    }


    private ParameterConfig getSelectedParameter() {
        if (selectedOptionIndex < 0 || selectedOptionIndex >= configList.size()) return null;
        Config config = configList.get(selectedOptionIndex);
        return config instanceof ParameterConfig ? (ParameterConfig) config : null;
    }



    /**
     * 该界面不暂停游戏
     */
    @Override
    public boolean shouldPause() { return false; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<Config> getConfigList() { return configList; }
    public int getScrollOffset() { return scrollOffset; }
    public int getMaxScrollOffset() { return maxScrollOffset; }
    public int getScrollbarHeight() { return scrollbarHeight; }
    public int getTextOffsetY() { return textOffsetY; }
    public int getSelectedOptionIndex() { return selectedOptionIndex; }
    public boolean isEditing() { return isEditing; }
    public String getEditingText() { return editingText; }
    public int getHoveredIndex() { return hoveredIndex; }

    public void setEditingText(String editingText) { this.editingText = editingText; }
    public void setEditing(boolean isEditing) { this.isEditing = isEditing; }


}
