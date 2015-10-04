package net.lomeli.disguise.core;

import java.util.regex.Pattern;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DisguiseHandler {
    private DisguiseManager disguiseManager;

    public DisguiseHandler(DisguiseManager manager) {
        disguiseManager = manager;
    }

    public DisguiseManager getDisguiseManager() {
        return disguiseManager;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void playerAttacksEntity(LivingAttackEvent event) {
        if (!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityLiving && !(event.entityLiving instanceof EntityPlayer) && damageFromPlayer(event.source)) {
            EntityPlayer source = (EntityPlayer) getSourceOfDamage(event.source);
            if (!isFakePlayer(source))
                ObfUtil.setFieldValue(EntityLiving.class, event.entityLiving, source, "attackTarget", "field_70696_bz", "c");
        }
    }

    private Entity getSourceOfDamage(DamageSource source) {
        if (source != null)
            return source.isProjectile() ? source.getEntity() : source.getSourceOfDamage();
        return null;
    }

    private boolean damageFromPlayer(DamageSource source) {
        if (source != null && (source.getDamageType().equals("player") || source.getSourceOfDamage() instanceof EntityPlayer || source.getEntity() instanceof EntityPlayer))
            return true;
        return false;
    }

    private final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");

    public boolean isFakePlayer(EntityPlayer player) {
        return player != null ? !(player instanceof EntityPlayerMP) || (player instanceof FakePlayer) || FAKE_PLAYER_PATTERN.matcher(player.getName()).matches() : false;
    }
}
