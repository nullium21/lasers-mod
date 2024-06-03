package moe.lina.lasers;

import moe.lina.lasers.base.HasIdentifier;
import moe.lina.lasers.content.LaserBE;
import moe.lina.lasers.content.LaserBlock;
import net.fabricmc.api.ModInitializer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LasersMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("lasers");

	public static final LaserBlock LASER_BLOCK = blockWithItem(new LaserBlock());
	public static final BlockEntityType<LaserBE> LASER_BE = blockEntity(LASER_BLOCK, LaserBE::new);

	public static Identifier id(String path) {
		return Identifier.of("lasers", path);
	}

	private static <T extends Block & HasIdentifier> T blockWithItem(T t) {
		Registry.register(Registries.BLOCK, t.getId(), t);
		Registry.register(Registries.ITEM, t.getId(), new BlockItem(t, new BlockItem.Settings()));
		return t;
	}

	private static <B extends Block & BlockEntityProvider & HasIdentifier, T extends BlockEntity> BlockEntityType<T> blockEntity(B block, BlockEntityType.BlockEntityFactory<T> factory) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, block.getId(), BlockEntityType.Builder.create(factory, block).build(null));
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}