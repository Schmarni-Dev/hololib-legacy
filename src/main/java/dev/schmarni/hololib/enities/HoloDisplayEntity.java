package dev.schmarni.hololib.enities;

import dev.schmarni.hololib.Impls.HashList3;
import dev.schmarni.hololib.Impls.HoloData;
import dev.schmarni.hololib.Impls.HoloDataDataHandler;
import dev.schmarni.hololib.utils.HoloDataHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class HoloDisplayEntity extends DisplayEntity {
    public static final String BLOCK_STATE_NBT_KEY = "holo_data";
    private static final TrackedData<HoloData> HOLO_DATA = DataTracker.registerData(HoloDisplayEntity.class, HoloDataDataHandler.INSTANCE);

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
        this.getDataTracker().startTracking(HOLO_DATA, new HoloData());
    }

    public HoloData getHoloData() {
        return this.getDataTracker().get(HOLO_DATA);
    }

    public HashList3<BlockState> getBlockStates() {
        return getHoloData().getList();
    }

    public void setHoloData(HoloData data) {
        this.getDataTracker().set(HOLO_DATA, data,true);
    }

    @Override
    protected void setRotation(float yaw, float pitch) {
        super.setRotation(0, 0);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setHoloData(HoloDataHelper.readNbt(BLOCK_STATE_NBT_KEY,nbt,world));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        HoloDataHelper.writeNbt(BLOCK_STATE_NBT_KEY,getHoloData(),nbt);
    }
}
