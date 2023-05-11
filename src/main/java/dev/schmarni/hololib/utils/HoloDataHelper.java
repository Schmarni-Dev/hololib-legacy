package dev.schmarni.hololib.utils;

import dev.schmarni.hololib.Impls.HoloData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public final class HoloDataHelper {
    public static void writeNbt(String name, HoloData value, NbtCompound nbt) {
        HashList3Helper.writeNbt(name, value.getList(),nbt);
    }

    public static HoloData readNbt(String name, NbtCompound nbt, World world) {
        return new HoloData(HashList3Helper.readNbt(name,nbt,world));
    }
    public static String asString(HoloData data) {
        var nbt = new NbtCompound();
        writeNbt("data",data,nbt);
        return nbt.asString();
    }
}
