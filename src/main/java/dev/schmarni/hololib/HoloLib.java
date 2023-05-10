package dev.schmarni.hololib;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.schmarni.hololib.Impls.HashList3;
import dev.schmarni.hololib.Impls.HashList3DataHandler;
import dev.schmarni.hololib.enities.HoloDisplayEntity;
import dev.schmarni.hololib.utils.HashList3Helper;
import io.netty.buffer.ByteBufAllocator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.schmarni.hololib.utils.Util.three_d_loop;
import static net.minecraft.server.command.SummonCommand.summon;


public class HoloLib implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("hololib");
	boolean world_loaded;




	public static void initEntity(HashList3<BlockState>list,BlockPos pos,MinecraftServer server) {
		var nbt = new NbtCompound();

		HashList3Helper.writeNbt(HoloDisplayEntity.BLOCK_STATE_NBT_KEY,list,nbt);
		try {
			summon(server.getCommandSource(), HOLO_DISPLAY_ENTITY_TYPE.getRegistryEntry(), Vec3d.of(pos),nbt,false);
		} catch (CommandSyntaxException ignored) {
		}
	}

	public static final EntityType<HoloDisplayEntity> HOLO_DISPLAY_ENTITY_TYPE = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier("hololib", "holo_display"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, HoloDisplayEntity::new)
					.dimensions(EntityDimensions.fixed(0F, 0F))
					.trackRangeBlocks(10)
					.build()
	);

	void TeStS(World world) {
		var data = new HashList3<BlockState>();
		data.setItemAtIndex(new BlockPos(0,0,0),Blocks.ACACIA_PLANKS.getDefaultState());
		data.setItemAtIndex(new BlockPos(0,1,0),Blocks.BLUE_ICE.getDefaultState());
		var nbt = new NbtCompound();
		HashList3Helper.writeNbt("TEST",data,nbt);
		LOGGER.info(nbt.asString());
		var data2 = HashList3Helper.readNbt("TEST",nbt,world);
		LOGGER.info("data2: "+HashList3Helper.asString(data2));
//		if (!data.equals(data2)) throw new RuntimeException("FAILED TEST");

		data = new HashList3<BlockState>();
		data.setItemAtIndex(new BlockPos(0,0,0),Blocks.ACACIA_PLANKS.getDefaultState());
		data.setItemAtIndex(new BlockPos(0,1,0),Blocks.BLUE_ICE.getDefaultState());
		LOGGER.info("data3: "+HashList3Helper.asString(data));
		var boof = new PacketByteBuf(ByteBufAllocator.DEFAULT.buffer());
		HashList3DataHandler.INSTANCE.write(boof,data);
		LOGGER.info("IDK: " + HashList3Helper.asString(HashList3DataHandler.INSTANCE.read(boof)));
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.



		TrackedDataHandlerRegistry.register(HashList3DataHandler.INSTANCE);
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (server.getOverworld() == null) {
				world_loaded = false;
				return;
			}
			if (world_loaded) return;
			// code to run when the world is loaded
			System.out.println("World loaded!");
			try {
				server.getCommandManager().getDispatcher().execute("kill @e[type=!minecraft:player]",server.getCommandSource());
			} catch (CommandSyntaxException ignored) {
			}
			TeStS(server.getOverworld());
			var list = new HashList3<BlockState>();
//			list.setItemAtIndex(new BlockPos(0,0,0),Blocks.ACACIA_PLANKS.getDefaultState());
//			list.setItemAtIndex(new BlockPos(0,1,0),Blocks.BLUE_ICE.getDefaultState());
			int size = 16;
			three_d_loop(new Vec3i(size,size,size), (pos)->{
				if (!(pos.getX() == size - 1 || pos.getY() == size - 1 || pos.getZ() == size - 1
						|| pos.getX() == 0 || pos.getY() == 0 || pos.getZ() == 0)) return;
				list.setItemAtIndex(pos,Blocks.BEDROCK.getDefaultState());
			});
			initEntity(list,new BlockPos(0,0,0),server);
			world_loaded = true;
		});


		LOGGER.info("Hello Fabric world!");
	}
}
