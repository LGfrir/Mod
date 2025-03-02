package me.LGfrir.H_infinity.client.Function.Chat;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.LGfrir.H_infinity.client.Function.Function;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class AutoTalkMove extends Function {
    private static final AutoTalkMove.playerPos playerPos = new playerPos();

    private static class playerPos {
        private double x, y, z;
        private int ticksSinceLastMove;

        public boolean setPlayerPos(double x, double y, double z) {
            boolean ifChange = !(this.x == x && this.y == y && this.z == z);
            this.x = x;
            this.y = y;
            this.z = z;
            if (ifChange && ticksSinceLastMove >= 40) {
                ticksSinceLastMove = 0;
                return true;
            }
            ticksSinceLastMove++;
            return false;
        }
    }

    //实现触发机制
    @Override
    public void execute() {
        if (playerPos.setPlayerPos(player.getX(), player.getY(), player.getZ())) {
            player.networkHandler.sendChatMessage("我正在移动！");
        }
    }

    @Override
    public String getName() {
        return "AutoTalkMove";
    }

    @Override
    public String getDescription() {
        return "automatically report when you change your position";
    }

    @Override
    public void commandInitialize(LiteralArgumentBuilder<FabricClientCommandSource> manager) {
        manager.then(ClientCommandManager.literal(getName())
                .then(ClientCommandManager.literal("on")
                        .executes(commandContext -> {
                            setToggleCommand(true);
                            return 1;
                        }))
                .then(ClientCommandManager.literal("off")
                        .executes(commandContext -> {
                            setToggleCommand(false);
                            return 1;
                        })));
    }
}
