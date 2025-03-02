package me.LGfrir.H_infinity.client.UIDesign.Directory;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;


public abstract class DraggableDirectory extends ClickableWidget {

    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;

    public DraggableDirectory(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    // 当鼠标点击时调用
    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isMouseOver(mouseX, mouseY)) {
            dragging = true;
            dragOffsetX = (int) mouseX - getX();
            dragOffsetY = (int) mouseY - getY();
        }
    }

    // 当鼠标释放时调用
    @Override
    public void onRelease(double mouseX, double mouseY) {
        dragging = false;
    }

    // 当鼠标拖拽时调用
    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (dragging) {
            this.setX((int) mouseX - dragOffsetX);
            this.setY((int) mouseY - dragOffsetY);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        // 根据需要添加无障碍提示，这里可以留空
    }
}
