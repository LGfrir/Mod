package me.LGfrir.H_infinity.client;

import me.LGfrir.H_infinity.client.Function.Chat.AutoTalkMove;
import me.LGfrir.H_infinity.client.Function.Chat.AutoXYZ;
import me.LGfrir.H_infinity.client.Function.Function;
import me.LGfrir.H_infinity.client.Function.FunctionOperator;
import me.LGfrir.H_infinity.client.Function.World.ElytraPlus;
import me.LGfrir.H_infinity.client.UIDesign.Directory.Directory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static me.LGfrir.H_infinity.client.Command.CommandManager.registerCommands;
import static me.LGfrir.H_infinity.client.Util.Calculate.calculateYaw;


public class H_infinity implements ClientModInitializer {

    public static Identifier jetbrains = Identifier.of("lgfrir", "jetbrains");
    private static final AutoTalkMove AUTO_TALK_MOVE = new AutoTalkMove();
    private static final AutoXYZ AUTO_XYZ = new AutoXYZ();
    private static final ElytraPlus ELYTRA_PLUS = new ElytraPlus();
    public static ArrayList<Function> functionList = new ArrayList<>();

    static {
        functionList.add(AUTO_TALK_MOVE);
        functionList.add(AUTO_XYZ);
        functionList.add(ELYTRA_PLUS);
    }

    @Override
    public void onInitializeClient() {


        Directory directory = new Directory();
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher,
                 registryAccess) -> {
                    registerCommands(dispatcher);
                });
        KeybindHandler.register();

        ClientTickEvents.END_CLIENT_TICK.register(FunctionOperator :: functionOperator);


        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if(KeybindHandler.test.wasPressed() && MinecraftClient.getInstance().player != null) {
                var player = MinecraftClient.getInstance().player;
                MinecraftClient.getInstance().player.setYaw(calculateYaw(player.getX(), player.getZ(), 1000, 1000));
            }
        });


    }
}
