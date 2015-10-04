package net.lomeli.disguise.core;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.item.ItemStack;

public class DisguiseData {
    private ItemData itemData;
    private String entityClass;
    private List<WatchData> extraData;

    public DisguiseData(ItemData itemData, String entityClass, WatchData... extraData) {
        this.itemData = itemData;
        this.entityClass = entityClass;
        this.extraData = Lists.newArrayList();
        if (extraData != null && extraData.length > 0) {
            for (WatchData data : extraData)
                this.extraData.add(data);
        }
    }

    public ItemData getItemData() {
        return itemData;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public List<WatchData> getExtraData() {
        return extraData;
    }

    public boolean matchesItem(ItemStack stack) {
        return this.itemData.matchesItem(stack);
    }
}