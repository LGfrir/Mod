package me.LGfrir.H_infinity.client.Command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static me.LGfrir.H_infinity.client.H_infinity.functionList;


public class CommandManager {

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        var commandSetting = ClientCommandManager.literal("toggle");
        for(var function : functionList)
            function.commandInitialize(commandSetting);
        dispatcher.register(commandSetting);
    }

}

