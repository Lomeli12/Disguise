package net.lomeli.disguise.core;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Rotations;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class WatchData {
    private int type;
    private int id;
    private Object value;

    public WatchData(int type, int id, Object value) {
        this.type = type;
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public boolean dataMatches(Entity entity) {
        if (entity == null || entity.getDataWatcher() == null)
            return false;
        if (id > 31)
            return false;
        switch (type) {
            case 1:
                return entity.getDataWatcher().getWatchableObjectShort(id) == ((Double) value).shortValue();
            case 2:
                return entity.getDataWatcher().getWatchableObjectInt(id) == ((Double) value).intValue();
            case 3:
                return entity.getDataWatcher().getWatchableObjectFloat(id) == ((Double) value).floatValue();
            case 4:
                return entity.getDataWatcher().getWatchableObjectString(id).equals(String.valueOf(value));
            case 5:
                String mod = "minecraft";
                String name = String.valueOf(value);
                int metadata = 0;
                if (name.contains(":")) {
                    String[] split = name.split(":");
                    mod = split[0];
                    name = split[1];
                    if (split.length == 3)
                        metadata = Integer.valueOf(split[2]);
                }
                Item item = GameRegistry.findItem(mod, name);
                if (item == null)
                    return false;
                return OreDictionary.itemMatches(new ItemStack(item, 1, metadata), entity.getDataWatcher().getWatchableObjectItemStack(id), metadata != -1);
            case 6:
                DataWatcher.WatchableObject watchObject = (DataWatcher.WatchableObject) ObfUtil.invokeMethod(DataWatcher.class, entity.getDataWatcher(), new String[]{"getWatchedObject", "func_75691_i", "j"}, id);
                if (watchObject != null && watchObject.getObject() instanceof BlockPos) {
                    BlockPos entityBlockPos = (BlockPos) watchObject.getObject();
                    String blockPosString = (String) value;
                    if (blockPosString.contains(";")) {
                        String[] coords = blockPosString.split(";");
                        if (coords.length == 3) {
                            int x = Integer.valueOf(coords[0]);
                            int y = Integer.valueOf(coords[1]);
                            int z = Integer.valueOf(coords[2]);
                            BlockPos blockPosValue = new BlockPos(x, y, z);
                            return blockPosValue.equals(entityBlockPos);
                        }
                    }
                }

                return false;
            case 7:
                Rotations entityRotation = entity.getDataWatcher().getWatchableObjectRotations(id);
                String rotationString = (String) value;
                if (rotationString.contains(";")) {
                    String[] coords = rotationString.split(";");
                    if (coords.length == 3) {
                        float x = Float.valueOf(coords[0]);
                        float y = Float.valueOf(coords[1]);
                        float z = Float.valueOf(coords[2]);
                        Rotations rotationValue = new Rotations(x, y, z);
                        return rotationValue.equals(entityRotation);
                    }
                }
                return false;
            default:
                return entity.getDataWatcher().getWatchableObjectByte(id) == ((Double) value).byteValue();
        }
    }
}