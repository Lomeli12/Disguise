package net.lomeli.disguise.core;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class DisguiseData {
    private String itemName;
    private int metadata;
    private String entityClass;
    private List<WatchData> extraData;

    public DisguiseData(String item, int meta, String entityClass, WatchData...extraData) {
        this.itemName = item;
        this.metadata = meta;
        this.entityClass = entityClass;
        this.extraData = Lists.newArrayList();
        if (extraData != null && extraData.length > 0) {
            for (WatchData data : extraData)
                this.extraData.add(data);
        }
    }

    public DisguiseData(String item, String entityClass, WatchData...extraData) {
        this(item, -1, entityClass, extraData);
    }

    public int getMetadata() {
        return metadata;
    }

    public String getItemName() {
        return itemName;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public List<WatchData> getExtraData() {
        return extraData;
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
