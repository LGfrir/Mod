package me.LGfrir.H_infinity.client.Function.Chat;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.LGfrir.H_infinity.client.Function.Function;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class AutoXYZ extends Function {
    private static boolean ifSneakLastTick = false;


    @Override
    public void execute() {
        if (player.isSneaking() && !ifSneakLastTick) {
            player.networkHandler.sendChatMessage(
                    String.format("我在 X: %.0f, Y: %.0f, Z: %.0f，有本事就来揍我呀~~~",
                            player.getX(), player.getY(), player.getZ()));
        }
        ifSneakLastTick = player.isSneaking();
    }

    @Override
    public String getName() {
        return "AutoXYZ";
    }

    @Override
    public String getDescription() {
        return "automatically announce yours location to your enemy when you are sneaking";
    }
    public void commandInitialize(LiteralArgumentBuilder<FabricClientCommandSource> command) {
        command.then(ClientCommandManager.literal(getName())
                .then(ClientCommandManager.literal("on").executes(
                        commandContext -> {
                            setToggleCommand(true);
                            return 1;
                        }))
                .then(ClientCommandManager.literal("off").executes(
                        commandContext -> {
                            setToggleCommand(false);
                            return 1;
                        })));
    }

}
