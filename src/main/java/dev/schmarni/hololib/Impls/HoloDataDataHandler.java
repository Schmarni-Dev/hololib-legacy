package dev.schmarni.hololib.Impls;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import static dev.schmarni.hololib.HoloLib.LOGGER;


public class HoloDataDataHandler implements TrackedDataHandler<HoloData> {
    public static final HoloDataDataHandler INSTANCE = new HoloDataDataHandler();
    @Override
    public void write(PacketByteBuf buf, HoloData value) {
        var data = value.getAllSchematicBlocksAndPositions();
        var length = data.size();
        buf.writeInt(length);
        data.forEach((pair) -> {
            buf.writeBlockPos(new BlockPos(pair.getLeft()));

            Codec<BlockState> codec = BlockState.CODEC;
            NbtOps ops = NbtOps.INSTANCE;
            DataResult<NbtElement> result = codec.encodeStart(ops, pair.getRight());
            if (result.result().isPresent()) {
                buf.writeNbt((NbtCompound) result.result().get());
            } else {
                LOGGER.error("COULD NOT SERIALIZE BLOCK-STATE!!!!");
            }
        });
    }

    @Override
    public HoloData read(PacketByteBuf buf) {
        var length = buf.readInt();
        LOGGER.info(String.valueOf(length));
        var out = new HoloData();
        for (int i = 0; i < length; i++) {
            var pos = buf.readBlockPos();
            Codec<BlockState> codec = BlockState.CODEC;
            NbtOps ops = NbtOps.INSTANCE;
            DataResult<BlockState> result = codec.parse(ops, (NbtElement) buf.readNbt());
            if (result.result().isPresent()) {
                BlockState state = result.result().get();
                out.setSchematicBlockAt(pos,state);
            } else {
                LOGGER.error("COULD NOT DESERIALIZE BLOCK-STATE!!!!");
                out.setSchematicBlockAt(pos, Blocks.AIR.getDefaultState());
            }
        }
        return out;
    }

    @Override
    public HoloData copy(HoloData value) {
        return value.copy();
    }
}
