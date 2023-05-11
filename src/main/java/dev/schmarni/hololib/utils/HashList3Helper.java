package dev.schmarni.hololib.utils;

import dev.schmarni.hololib.Impls.HashList3;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Objects;
public final class HashList3Helper {
    public static void writeNbt(String name, HashList3<BlockState> value, NbtCompound nbt) {
        NbtList data = new NbtList();
//        LOGGER.info(String.valueOf(data.getHeldType()));
        value.getAllItemsAndIndexes().stream().map((pair)->{
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.put("pos", NbtHelper.fromBlockPos(new BlockPos(pair.getLeft())));
            nbtCompound.put("state", NbtHelper.fromBlockState(pair.getRight()));
            return nbtCompound;
        }).forEach(data::add);
//        LOGGER.info(data.asString());
        nbt.put(name,data);
    }

    public static HashList3<BlockState> readNbt(String name, NbtCompound nbt, World world) {
        NbtList data = (NbtList) nbt.get(name);
//        LOGGER.info("List: " +data.asString());
        var list = new HashList3<BlockState>();
        assert data != null;
        data.stream().map((nbtElement)-> {
            if (!(nbtElement instanceof NbtCompound nbtCompound)) return null;
            var pos = NbtHelper.toBlockPos(nbtCompound.getCompound("pos"));
            var state = NbtHelper.toBlockState(world.createCommandRegistryWrapper(RegistryKeys.BLOCK), nbtCompound.getCompound("state"));
            return new Pair<>((Vec3i)pos,state);
        }).filter(Objects::nonNull).forEach(list::setItemAtIndex);
        return list;
    }
    public static String asString(HashList3<BlockState> data) {
        var nbt = new NbtCompound();
        writeNbt("data",data,nbt);
        return nbt.asString();
    }
}
