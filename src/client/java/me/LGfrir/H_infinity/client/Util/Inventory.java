package me.LGfrir.H_infinity.client.Util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

public class Inventory {
    public static int getStoredItemCount(ItemStack shulkerStack, Item targetItem) {
        int totalCount = 0;
        ContainerComponent container = shulkerStack.getComponents().get(DataComponentTypes.CONTAINER);
        if (container != null) {
            Iterator<ItemStack> iterator = container.streamNonEmpty().iterator();
            while (iterator.hasNext()) {
                ItemStack stack = iterator.next();
                if (stack.getItem() == targetItem) {
                    totalCount += stack.getCount();
                }
            }
        }
        return totalCount;
    }
}
