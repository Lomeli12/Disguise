package net.lomeli.disguise.core;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.entity.Entity;

public class DisguiseManager {
    private List<DisguiseData> disguiseData;

    public DisguiseManager(List<DisguiseData> data) {
        this.disguiseData = data;
    }

    public List<DisguiseData> getDisguiseData() {
        return disguiseData;
    }

    public void addData(DisguiseData data) {
        if (data == null) return;
        if (disguiseData == null) disguiseData = Lists.newArrayList();
        disguiseData.add(data);
    }

    public DisguiseData getDisguiseForEntity(Entity entity) {
        if (entity != null && this.disguiseData != null && this.disguiseData.size() > 0) {
            for (DisguiseData data : this.disguiseData) {
                if (data != null && data.getEntityClass().equals(entity.getClass().getName()))
                    return data;
            }
        }
        return null;
    }
}
