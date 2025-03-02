package me.LGfrir.H_infinity.client.Function;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.LGfrir.H_infinity.client.ConfigOperator.Config;
import me.LGfrir.H_infinity.client.H_infinity;
import me.LGfrir.H_infinity.client.UIDesign.SubUI.SubUI;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static me.LGfrir.H_infinity.client.UIDesign.Directory.Constants.HEIGHT;
import static me.LGfrir.H_infinity.client.UIDesign.Directory.Constants.WIDTH;

public abstract class Function {
    public boolean toggle = false;
    public static MinecraftClient client;
    public static ClientPlayerEntity player;
    public SubUI subUI;
    protected List<Config> configList = new ArrayList<>();

    public Function() {
        List<Config> configs = new ArrayList<>();
        subUI = new SubUI(configs);
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public boolean getToggle() {
        return toggle;
    }


    public void setToggleCommand(boolean toggle) {
        this.toggle = toggle;
        client.player.sendMessage(Text.literal(getName() + " is now set to: " + (getToggle() ? "on" : "off")), false);
    }

    public boolean getToggleCommand() {
        client.player.sendMessage(Text.literal(getName() + " is current toggle " + (getToggle() ? "on" : "off")), false);
        return toggle;
    }

    public static void setClient(MinecraftClient client) {
        Function.client = client;
        player = client.player;
    }

    /**
     * 以下全用于UI的实现
     * */

    private int offsetX, offsetY;

    // 渲染方法，parentX 和 parentY 为父组件的坐标
    public void render(DrawContext context, int parentX, int parentY, int offsetX, int offsetY, float delta,int mouseX, int mouseY) {
        if (!subUI.isOpen) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            int x = parentX + offsetX;
            int y = parentY + offsetY;
            context.fill(x, y, x + WIDTH, y  + HEIGHT, toggle ? 0xFFF5F5F5 : 0xAA000000);

            Text name = Text.literal(getName()).styled(
                    style -> style.withFont(H_infinity.jetbrains)
            );

            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            int textWidth = textRenderer.getWidth(name);
            context.drawText(
                    textRenderer,
                    name,
                    x + (WIDTH - textWidth) / 2,
                    y + 3,
                    toggle ? 0x333333 : 0xFFFFFF,
                    false
            );
        }
        else {
            subUI.render(context,mouseX,mouseY,delta);
        }
    }


    // 判断鼠标是否在该子组件上（需要加上父组件的偏移）
    public boolean isMouseOver(double mouseX, double mouseY, int parentX, int parentY) {
        int x = parentX + offsetX;
        int y = parentY + offsetY;
        return mouseX >= x && mouseX < x + WIDTH && mouseY >= y && mouseY < y + HEIGHT;
    }

    // 点击时触发的逻辑
    public void onClick(int button) {
        if (button == 0) { // 左键
            setToggle(!toggle);
        } else if (button == 1) { // 右键
            subUI.open(); // 打开子界面
        }
    }

    /**
     * UI实现结束
     * */



    public abstract void execute();
    public abstract String getName();
    public abstract String getDescription();
    public abstract void commandInitialize(LiteralArgumentBuilder<FabricClientCommandSource> command);
}