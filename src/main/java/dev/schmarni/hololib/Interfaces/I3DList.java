package dev.schmarni.hololib.Interfaces;

import java.util.List;

import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3i;

public interface I3DList<T> {
    public T getItemAtIndex(Vec3i pos);

    public List<T> getAllItems();

    public void setItemAtIndex(Vec3i pos, T item);
    public void setItemAtIndex(Pair<Vec3i,T> data);

    public List<Pair<Vec3i,T>> getAllItemsAndIndexes();

    public void setItemsAtIndexs(List<Pair<Vec3i,T>> data);

}
