package dev.schmarni.hololib.Impls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.schmarni.hololib.Interfaces.I3DList;
import net.minecraft.block.BlockState;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import static dev.schmarni.hololib.HoloLib.LOGGER;

public class HashList3<T> implements I3DList<T> {
    private HashMap<Vec3i,T> items = new HashMap<>();

    @Override
    public T getItemAtIndex(Vec3i pos) {
        return items.get(pos);
    }

    @Override
    public List<T> getAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void setItemAtIndex(Vec3i pos, T item) {
        items.put(pos, item);
    }

    @Override
    public void setItemAtIndex(Pair<Vec3i, T> data) {
        setItemAtIndex(data.getLeft(),data.getRight());
    }

    public int size() {
        return items.size();
    }

    @Override
    public List<Pair<Vec3i, T>> getAllItemsAndIndexes() {
        //        LOGGER.info("hi:"+data.size());
        return items.entrySet().stream().map((entry)-> {
            //            LOGGER.info("Pair");
            return new Pair<Vec3i, T>(entry.getKey(), entry.getValue());
        }).toList();
    }

    @Override
    public void setItemsAtIndexs(List<Pair<Vec3i, T>> data) {
        data.forEach((pair) -> {
            setItemAtIndex(pair.getLeft(), pair.getRight());
        });
    }

}
