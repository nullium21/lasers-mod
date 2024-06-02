package moe.lina.lasers;

import moe.lina.lasers.content.LaserBlock;
import net.fabricmc.api.ModInitializer;

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

	public static final LaserBlock LASER_BLOCK = Registry.register(Registries.BLOCK, id("laser"), new LaserBlock());

	public static Identifier id(String path) {
		return Identifier.of("lasers", path);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		Registry.register(Registries.ITEM, id("laser"), new BlockItem(LASER_BLOCK, new BlockItem.Settings()));
	}
}