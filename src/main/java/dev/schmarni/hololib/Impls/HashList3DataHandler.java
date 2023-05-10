package dev.schmarni.hololib.Impls;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBufAllocator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import static dev.schmarni.hololib.HoloLib.LOGGER;


public class HashList3DataHandler implements TrackedDataHandler<HashList3<BlockState>> {
    public static final HashList3DataHandler INSTANCE = new HashList3DataHandler();
    @Override
    public void write(PacketByteBuf buf, HashList3<BlockState> value) {
        var data = value.getAllItemsAndIndexes();
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
    public HashList3<BlockState> read(PacketByteBuf buf) {
        var length = buf.readInt();
        LOGGER.info(String.valueOf(length));
        var out = new HashList3<BlockState>();
        for (int i = 0; i < length; i++) {
            var pos = buf.readBlockPos();
            Codec<BlockState> codec = BlockState.CODEC;
            NbtOps ops = NbtOps.INSTANCE;
            DataResult<BlockState> result = codec.parse(ops, (NbtElement) buf.readNbt());
            if (result.result().isPresent()) {
                BlockState state = result.result().get();
                out.setItemAtIndex(pos,state);
            } else {
                LOGGER.error("COULD NOT DESERIALIZE BLOCK-STATE!!!!");
                out.setItemAtIndex(pos, Blocks.AIR.getDefaultState());
            }
        }
        return out;
    }

    @Override
    public HashList3<BlockState> copy(HashList3<BlockState> value) {
        HashList3<BlockState> copy = new HashList3<>();

        for (Pair<Vec3i, BlockState> pair : value.getAllItemsAndIndexes()) {
            copy.setItemAtIndex(pair.getLeft(), pair.getRight());
        }

        return copy;
    }
}
