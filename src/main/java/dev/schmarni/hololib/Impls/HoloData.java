package dev.schmarni.hololib.Impls;

import dev.schmarni.hololib.enities.HoloDisplayEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
    }

    public HashList3<BlockState> getList() {
        return list;
    }

    public HoloData copy() {
        return new HoloData(this.getList());
    }

    public void setSchematicBlockAt(Vec3i pos, BlockState blockState) {
        list.setItemAtIndex(pos,blockState);
    }

    public List<Pair<Vec3i, BlockState>> getAllSchematicBlocksAndPositions() {
        return list.getAllItemsAndIndexes();
    }

    @Environment(EnvType.SERVER)
    public SchematicBlockStatus getBlockStatus(BlockPos pos, HoloDisplayEntity entity) {
        var block = list.getItemAtIndex(pos);
        if (block == null) block = Blocks.AIR.getDefaultState();

        var block_at_location = entity.getWorld().getBlockState(pos.add(entity.getBlockPos()));
        if (block_at_location == block) {
            return  SchematicBlockStatus.Correct;
        }

        if (block_at_location == Blocks.AIR.getDefaultState()) {
            return SchematicBlockStatus.Empty;
        }
        
        return SchematicBlockStatus.Wrong;
    }

    public enum SchematicBlockStatus {
        Correct,
        Wrong,
        Empty,
    }
}
