package dev.schmarni.hololib.Impls;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.schmarni.hololib.Interfaces.I3DList;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.util.ByteProcessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dev.schmarni.hololib.HoloLib.LOGGER;
import static dev.schmarni.hololib.utils.Util.Vec3iFromBlockPos;

public class TrackedHashList3 extends HashList3<BlockState> implements TrackedDataHandler<TrackedHashList3> {
    @Override
    public void write(PacketByteBuf buf, TrackedHashList3 value) {
        var data = getAllItemsAndIndexes();
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
    public TrackedHashList3 read(PacketByteBuf buf) {
        var length = buf.readInt();
        var out = new TrackedHashList3();
        for (int i = 1; i < length; i++) {
            var pos = buf.readBlockPos();
            Codec<BlockState> codec = BlockState.CODEC;
            NbtOps ops = NbtOps.INSTANCE;
            DataResult<BlockState> result = codec.parse(ops, (NbtElement) buf.readNbt());
            if (result.result().isPresent()) {
                BlockState state = result.result().get();
                out.setItemAtIndex(Vec3iFromBlockPos(pos),state);
            } else {
                LOGGER.error("COULD NOT DESERIALIZE BLOCK-STATE!!!!");
                out.setItemAtIndex(Vec3iFromBlockPos(pos), Blocks.AIR.getDefaultState());
            }
        }
        return out;
    }

    @Override
    public TrackedHashList3 copy(TrackedHashList3 value) {
        var boof = new PacketByteBuf(ByteBufAllocator.DEFAULT.buffer());
        write(boof,value);
        return read(boof);
    }
}
