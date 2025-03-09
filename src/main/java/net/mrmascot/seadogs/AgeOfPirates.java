package net.mrmascot.seadogs;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.mrmascot.seadogs.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgeOfPirates implements ModInitializer {
	public static final String MOD_ID = "seadogs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		LOGGER.info("ARRRR!!");

		FuelRegistry.INSTANCE.add(ModItems.BANANA, 150);

	}
}