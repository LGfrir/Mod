package me.LGfrir.H_infinity.client.UIDesign.Directory;

import me.LGfrir.H_infinity.client.UIDesign.Chat.Chat;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static me.LGfrir.H_infinity.client.UIDesign.Directory.Constants.HEIGHT;
import static me.LGfrir.H_infinity.client.UIDesign.Directory.Constants.WIDTH;

public class Directory extends Screen {

    private final Chat chat = new Chat(100, 100, WIDTH, HEIGHT, Text.literal("Chat"));

    public Directory() {
        super(Text.literal("CLIENT"));
    }

    @Override
    protected void init() {
        addDrawableChild(chat);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }
    @Override
    public boolean shouldPause() { return false; }

}
