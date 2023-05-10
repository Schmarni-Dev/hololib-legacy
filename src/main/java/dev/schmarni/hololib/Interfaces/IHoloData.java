package dev.schmarni.hololib.Interfaces;

import org.lwjgl.system.NonnullDefault;

public interface IHoloData {
    @NonnullDefault
    public I3DList<IBlockData> getBlocks();
}
