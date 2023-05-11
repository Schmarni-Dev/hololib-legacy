package dev.schmarni.hololib;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.schmarni.hololib.Impls.HashList3DataHandler;
import dev.schmarni.hololib.Impls.HoloData;
import dev.schmarni.hololib.Impls.HoloDataDataHandler;
import dev.schmarni.hololib.enities.HoloDisplayEntity;
import dev.schmarni.hololib.utils.HoloDataHelper;
import dev.schmarni.hololib.utils.Util;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.*;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static net.minecraft.server.command.SummonCommand.summon;


public class HoloLib implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("hololib");
//	boolean world_loaded;
	public static final Block ERROR_BLOCK = Registry.register(Registries.BLOCK, new Identifier("hololib", "error_block"), new GlassBlock(AbstractBlock.Settings.of(Material.GLASS).strength(0.3f).sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(Util::never).solidBlock(Util::never).suffocates(Util::never).blockVision(Util::never)));

	public static void initEntity(HoloData data, BlockPos pos, MinecraftServer server) {
		var nbt = new NbtCompound();

		HoloDataHelper.writeNbt(HoloDisplayEntity.BLOCK_STATE_NBT_KEY,data,nbt);
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
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		TrackedDataHandlerRegistry.register(HashList3DataHandler.INSTANCE);
		TrackedDataHandlerRegistry.register(HoloDataDataHandler.INSTANCE);
//		ServerTickEvents.END_SERVER_TICK.register(server -> {
//			if (server.getOverworld() == null) {
//				world_loaded = false;
//				return;
//			}
//			if (world_loaded) return;

//			try {
//				server.getCommandManager().getDispatcher().execute("kill @e[type=!minecraft:player]",server.getCommandSource());
//			} catch (CommandSyntaxException ignored) {
//			}


//			var list = new HashList3<BlockState>();
//			int size = 16;
//			three_d_loop(new Vec3i(size,size,size), (pos)->{
//				if (!(pos.getX() == size - 1 || pos.getY() == size - 1 || pos.getZ() == size - 1
//						|| pos.getX() == 0 || pos.getY() == 0 || pos.getZ() == 0)) return;
//				list.setItemAtIndex(pos,Blocks.BLUE_ICE.getDefaultState());
//			});
//			initEntity(HoloData.rotate(new HoloData(list),90),new BlockPos(0,0,0),server);

//			world_loaded = true;
//		});
	}
}
