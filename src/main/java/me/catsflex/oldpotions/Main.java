package me.catsflex.oldpotions;

import me.catsflex.oldpotions.config.ModConfig;
import me.catsflex.oldpotions.util.RefreshUtil;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main implements ModInitializer {
	public static final String MOD_ID = "old-potions";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	@Override
	public void onInitialize() {
		ModConfig.getInstance().load();
		RefreshUtil.refresh();
		LOGGER.info("Mod initialized successfully!");
	}
}
