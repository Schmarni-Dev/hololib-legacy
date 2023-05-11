package dev.schmarni.hololib.Impls;

import dev.schmarni.hololib.enities.HoloDisplayEntity;
import dev.schmarni.hololib.utils.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import java.util.List;


public class HoloData {
    HashList3<BlockState> list;

    public HoloData() {
        list = new HashList3<>();
    }

    public HoloData(HashList3<BlockState> list) {
        this.list = list;
        processList();
    }

    void processList() {
        BlockPos.Mutable length = new BlockPos.Mutable();
        list.getAllItemsAndIndexes().stream().map(Pair::getLeft).forEach(pos->{
            if (pos.getX() > length.getX()) {
                length.setX(pos.getX());
            }
            if (pos.getY() > length.getY()) {
                length.setY(pos.getY());
            }
            if (pos.getZ() > length.getZ()) {
                length.setZ(pos.getZ());
            }
        });
        Util.three_d_loop(length.toImmutable(),data -> {
            if (list.getItemAtIndex(data) == null) {
                list.setItemAtIndex(data,Blocks.AIR.getDefaultState());
            }
        });
    }

    public HashList3<BlockState> getList() {
        return list;
    }

    public HoloData copy() {
        return new HoloData(this.getList());
    }

    public List<Pair<Vec3i, BlockState>> getAllSchematicBlocksAndPositions() {
        return list.getAllItemsAndIndexes();
    }

    public SchematicBlockStatus getBlockStatus(BlockPos pos, HoloDisplayEntity entity) {
        var block = list.getItemAtIndex(pos);

        var block_at_location = entity.getWorld().getBlockState(pos.add(entity.getBlockPos()));
        if (block_at_location == block) {
            return  SchematicBlockStatus.Correct;
        }

        if (block_at_location == Blocks.AIR.getDefaultState()) {
            return SchematicBlockStatus.Empty;
        }

        return SchematicBlockStatus.Wrong;
    }

    public static HoloData rotate(HoloData current, int degrees) {
        var new_list = new HashList3<BlockState>();
        current.getList().getAllItemsAndIndexes().stream().map(data-> new Pair<>(rotate_inner(data.getLeft(), degrees), data.getRight())).forEach(new_list::setItemAtIndex);
        return new HoloData(new_list);
    }

    private static Vec3i rotate_inner(Vec3i in, int degrees) {
        return switch (degrees) {
            case 0 -> in;
            case 90 -> new Vec3i(-in.getZ(), in.getY(), in.getX());
            case 180 -> new Vec3i(-in.getX(), in.getY(), -in.getZ());
            case 270 -> new Vec3i(in.getZ(), -in.getY(), in.getX());
            default -> throw new IllegalArgumentException("HoloData::rotate degrees are only allowed to be: 0, 90, 180, 270! got value: " + degrees);
        };
    }



    public enum SchematicBlockStatus {
        Correct,
        Wrong,
        Empty,
    }
}
