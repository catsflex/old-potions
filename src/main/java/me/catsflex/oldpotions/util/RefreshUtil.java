package me.catsflex.oldpotions.util;

import me.catsflex.oldpotions.mixin.invoker.ServerPlayerInvoker;
import net.minecraft.client.Minecraft;

public final class RefreshUtil {
	private RefreshUtil() {}
	
	public static void refresh() {
		EffectColorRemapper.refresh();
		forceRefreshEffectsInSingleplayer();
	}
	
	private static void forceRefreshEffectsInSingleplayer() {
		var server = Minecraft.getInstance().getSingleplayerServer();
		if (server == null) { return; }
		
		// Effect updates happen every 30s by default.
		// Force it to see effect color changes immediately.
		server.execute(() -> {
			for (var level : server.getAllLevels()) {
				for (var player : level.players()) {
					for (var effect : player.getActiveEffects()) {
						((ServerPlayerInvoker) player).invokeOnEffectUpdated(effect, false, null);
					}
				}
			}
		});
	}
}
