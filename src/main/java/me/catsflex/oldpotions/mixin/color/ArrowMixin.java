package me.catsflex.oldpotions.mixin.color;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.catsflex.oldpotions.config.ModConfig;
import me.catsflex.oldpotions.util.EffectColorRemapper;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Arrow.class)
public abstract class ArrowMixin {
	
	@ModifyReturnValue(method = "getColor", at = @At("RETURN"))
	private int remapArrowColor(int originalColor) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get()) { return originalColor; }
		
		return EffectColorRemapper.remapIntColor(originalColor);
	}
}
