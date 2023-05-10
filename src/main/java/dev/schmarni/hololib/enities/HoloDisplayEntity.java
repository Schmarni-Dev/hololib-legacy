package dev.schmarni.hololib.enities;

import dev.schmarni.hololib.Impls.HashList3;
import dev.schmarni.hololib.Impls.HashList3DataHandler;
import dev.schmarni.hololib.mixin.DisplayEntityAccessor;
import dev.schmarni.hololib.utils.HashList3Helper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;

import static dev.schmarni.hololib.HoloLib.LOGGER;

public class HoloDisplayEntity extends DisplayEntity {
    public static final String BLOCK_STATE_NBT_KEY = "block_state";
    private static final TrackedData<HashList3<BlockState>> BLOCK_STATES = DataTracker.registerData(HoloDisplayEntity.class, HashList3DataHandler.INSTANCE);

    public HoloDisplayEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(BLOCK_STATES, new HashList3<>());
    }

    public HashList3<BlockState> getBlockStates() {
        return this.getDataTracker().get(BLOCK_STATES);
    }

    public void setBlockStates(HashList3<BlockState> states) {
        LOGGER.info("setBlockStates:" + states.size());
        this.getDataTracker().set(BLOCK_STATES, states,true);
        LOGGER.info("setBlockStates:" + getBlockStates().size());
    }

    public void setBlockState(BlockPos pos, BlockState state) {
        var states = getBlockStates();
        states.setItemAtIndex(pos,state);
        setBlockStates(states);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setBlockStates(HashList3Helper.readNbt(BLOCK_STATE_NBT_KEY,nbt,world));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        HashList3Helper.writeNbt(BLOCK_STATE_NBT_KEY,getBlockStates(),nbt);
    }
}
