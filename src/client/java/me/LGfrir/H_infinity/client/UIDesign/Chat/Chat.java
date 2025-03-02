package me.LGfrir.H_infinity.client.UIDesign.Chat;

import me.LGfrir.H_infinity.client.Function.Function;
import me.LGfrir.H_infinity.client.H_infinity;
import me.LGfrir.H_infinity.client.UIDesign.Directory.DraggableDirectory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import static me.LGfrir.H_infinity.client.H_infinity.functionList;
import static me.LGfrir.H_infinity.client.UIDesign.Directory.Constants.HEIGHT;

public class Chat extends DraggableDirectory {

    public Chat(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean bl = false;
        Function tarSubUI = null;
        for (var child : functionList) {
            if (child.subUI!= null && child.subUI.isOpen) {
                bl = true;
                tarSubUI = child;
                break;
            }
        }
        if (bl) tarSubUI.subUI.render(context, mouseX, mouseY, delta);

        else {
            // 绘制背景
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xAA000000);

            // 绘制标题文本（居中显示）
            Text customText = Text.literal("Chat").styled(
                    style -> style.withFont(H_infinity.jetbrains)
            );
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            int textWidth = textRenderer.getWidth(customText);
            context.drawText(
                    textRenderer,
                    customText,
                    getX() + (getWidth() - textWidth) / 2,
                    getY() + 3,
                    0xFFFFFF,
                    false
            );
            //BuildList
            int i = 1;
            for (var child : functionList) {
                child.render(context, getX(), getY(), 0, HEIGHT * i++, delta,mouseX, mouseY);
            }
        }
    }

    // 重写鼠标点击事件，传递给子组件
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 检测是否点击到某个子组件
        for (var child : functionList) {
            if (child.isMouseOver(mouseX, mouseY, getX(), getY())) {
                child.onClick(button);
                return true;
            }
        }
        // 否则交给父类处理（例如拖动窗口）
        return super.mouseClicked(mouseX, mouseY, button);
    }


}
