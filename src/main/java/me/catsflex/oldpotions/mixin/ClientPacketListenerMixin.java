package me.catsflex.oldpotions.mixin;

import me.catsflex.oldpotions.util.EffectColorRemapper;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
	@Unique private static final int TYPE_ARGUMENT_INDEX = 0;
	@Unique private static final int DATA_ARGUMENT_INDEX = 2;
	
	/**
	 * Event IDs below were ripped from
	 * {@link AbstractThrownPotion#onHit}
	 */
	
	@Unique private static final int SPLASH_POTION_EVENT = 2002;
	@Unique private static final int INSTANT_SPLASH_POTION_EVENT = 2007;
	
	@ModifyArgs(
		method = "handleLevelEvent",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;levelEvent(ILnet/minecraft/core/BlockPos;I)V")
	)
	private void modifyPotionSplashEvent(Args args) {
		final int type = args.get(TYPE_ARGUMENT_INDEX);
		if (type != SPLASH_POTION_EVENT && type != INSTANT_SPLASH_POTION_EVENT) { return; }
		
		final int originalColor = args.get(DATA_ARGUMENT_INDEX);
		args.set(DATA_ARGUMENT_INDEX, EffectColorRemapper.remapIntColor(originalColor));
	}
}
