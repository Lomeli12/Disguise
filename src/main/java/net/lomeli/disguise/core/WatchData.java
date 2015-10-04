package net.lomeli.disguise.core;

import java.lang.reflect.Method;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Rotations;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
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
                return entity.getDataWatcher().getWatchableObjectShort(id) == (Short) value;
            case 2:
                return entity.getDataWatcher().getWatchableObjectInt(id) == (Integer) value;
            case 3:
                return entity.getDataWatcher().getWatchableObjectFloat(id) == (Float) value;
            case 4:
                return entity.getDataWatcher().getWatchableObjectString(id).equals(String.valueOf(value));
            case 5:
                return OreDictionary.itemMatches((ItemStack) value, entity.getDataWatcher().getWatchableObjectItemStack(id), true);
            case 6:
                //TODO read blockpos from datawatcher
                DataWatcher.WatchableObject watchObject = (DataWatcher.WatchableObject) invokeMethod(DataWatcher.class, entity.getDataWatcher(), new String[]{"getWatchedObject", "func_75691_i", "j"}, id);
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
                return entity.getDataWatcher().getWatchableObjectByte(id) == (Byte) value;
        }
    }

    public <T, E> Object invokeMethod(Class<? extends E> clazz, E instance, String[] names, Object... args) {
        try {
            Method method = getMethod(clazz, names);
            if (method != null)
                return method.invoke(instance, args);
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToFindMethodException(names, e);
        }
        return null;
    }

    public Method getMethod(Class<?> clazz, String... names) {
        try {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                for (String methodName : names) {
                    if (method.getName().equalsIgnoreCase(methodName)) {
                        method.setAccessible(true);
                        return method;
                    }
                }
            }
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToFindMethodException(names, e);
        }
        return null;
    }
}
