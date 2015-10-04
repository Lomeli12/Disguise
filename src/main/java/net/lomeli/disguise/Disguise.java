package net.lomeli.disguise;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.lomeli.disguise.core.*;

@Mod(modid = Disguise.MOD_ID, name = Disguise.NAME, version = Disguise.VERSION)
public class Disguise {
    public static final String MOD_ID = "disguise";
    public static final String NAME = "Disguise";
    public static final int MAJOR = 1, MINOR = 0, REV = 0;
    public static final String VERSION = MAJOR + "." + MINOR + "." + REV;

    public DisguiseHandler disguiseHandler;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Gson gson = new Gson();
        DisguiseManager manager = null;
        if (event.getSuggestedConfigurationFile().exists()) {
            try {
                Reader reader = new FileReader(event.getSuggestedConfigurationFile());
                manager = gson.fromJson(reader, DisguiseManager.class);
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (manager == null) {
            manager = new DisguiseManager(null);
            manager.addData(new DisguiseData(new ItemData("skull", 0), EntitySkeleton.class.getName(), new WatchData(0, 13, (byte) 0)));
            manager.addData(new DisguiseData(new ItemData("skull", 1), EntitySkeleton.class.getName(), new WatchData(0, 13, (byte) 1)));
            manager.addData(new DisguiseData(new ItemData("skull", 2), EntityZombie.class.getName()));
            manager.addData(new DisguiseData(new ItemData("skull", 4), EntityCreeper.class.getName()));
        }
        this.disguiseHandler = new DisguiseHandler(manager);

        // This is here simply to give an example for those that want to add custom disguises
        if (!event.getSuggestedConfigurationFile().exists()) {
            try {
                String data = gson.toJson(this.disguiseHandler.getDisguiseManager());
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(event.getSuggestedConfigurationFile()));
                writer.write(data);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this.disguiseHandler);
    }
}
