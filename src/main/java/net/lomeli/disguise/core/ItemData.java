package net.lomeli.disguise.core;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemData {
    private String itemName;
    private int metadata;

    public ItemData(String itemName, int metadata) {
        this.itemName = itemName;
        this.metadata = metadata;
    }

    public ItemData(String itemName) {
        this(itemName, -1);
    }

    public String getItemName() {
        return itemName;
    }

    public int getMetadata() {
        return metadata;
    }

    public boolean matchesItem(ItemStack stack) {
        if (stack == null)
            return false;
        String mod = "minecraft";
        String name = itemName;
        if (itemName.contains(":")) {
            mod = itemName.split(":")[0];
            name = itemName.split(":")[1];
        }
        Item item = GameRegistry.findItem(mod, name);
        if (item == null)
            return false;
        return stack.getItem() == item && (metadata == -1 ? true : stack.getItemDamage() == metadata);
    }
}
