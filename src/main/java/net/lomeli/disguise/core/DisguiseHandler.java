package net.lomeli.disguise.core;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DisguiseHandler {
    private DisguiseManager disguiseManager;

    public DisguiseHandler(DisguiseManager manager) {
        disguiseManager = manager;
    }

    public DisguiseManager getDisguiseManager() {
        return disguiseManager;
    }

    @SubscribeEvent
    public void setTargetEvent(LivingSetAttackTargetEvent event) {
        if (!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityLiving) {
            if (event.target instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.target;
                ItemStack helmetStack = player.getCurrentArmor(3);
                if (helmetStack != null) {
                    DisguiseData disguiseData = disguiseManager.getDisguiseForEntity(event.entityLiving);
                    if (disguiseData != null && disguiseData.getEntityClass().equals(event.entityLiving.getClass().getName()) && disguiseData.matchesItem(helmetStack)) {
                        if (disguiseData.getExtraData() != null && disguiseData.getExtraData().size() > 0) {
                            int matchCount = 0;
                            for (WatchData extraData : disguiseData.getExtraData()) {
                                if (extraData == null)
                                    matchCount++;
                                else if (extraData.dataMatches(event.entityLiving))
                                    matchCount++;
                            }
                            if (matchCount == disguiseData.getExtraData().size())
                                ((EntityLiving) event.entityLiving).setAttackTarget(null);
                        } else
                            ((EntityLiving) event.entityLiving).setAttackTarget(null);
                    }
                }
            }
        }
    }
}
