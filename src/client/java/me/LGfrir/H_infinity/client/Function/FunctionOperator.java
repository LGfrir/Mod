package me.LGfrir.H_infinity.client.Function;

import net.minecraft.client.MinecraftClient;

import static me.LGfrir.H_infinity.client.H_infinity.functionList;


public class FunctionOperator {
    static boolean ifClient = false;

    public static void functionOperator(MinecraftClient client) {
        if(!ifClient) {
            Function.setClient(client);
        }
        if (client.player != null) {
            for(var function : functionList)
                if(function.getToggle()) function.execute();
        }
    }
}
