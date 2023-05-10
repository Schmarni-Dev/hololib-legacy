package dev.schmarni.hololib.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public final class Util {
    public interface ICallback<T> {
        void run(T data);
    }

    public static void three_d_loop(Vec3i loops, ICallback<Vec3i> callback) {
        for (int x = 0; x < loops.getX(); x++) {
            for (int y = 0; y < loops.getY(); y++) {
                for (int z = 0; z < loops.getZ(); z++) {
                    callback.run(new Vec3i(x, y, z));
                }
            }
        }
    }
}
