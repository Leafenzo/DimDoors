package org.dimdev.dimdoors.screen;

import org.dimdev.dimdoors.DimensionalDoors;

import net.minecraft.screen.ScreenHandlerType;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;

public class ModScreenHandlerTypes {

	public static final ScreenHandlerType<TesselatingScreenHandler> TESSELATING_LOOM = ScreenHandlerRegistry.registerSimple(DimensionalDoors.id("tesselating_loom"), TesselatingScreenHandler::new);

	public static void init() {
	}
}
