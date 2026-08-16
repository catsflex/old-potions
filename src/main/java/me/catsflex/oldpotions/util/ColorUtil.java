package me.catsflex.oldpotions.util;

import me.catsflex.oldpotions.mixin.accessor.MobEffectAccessor;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.OptionalInt;

public final class ColorUtil {
	private ColorUtil() {}
	
	public static int stripAlpha(int argb) {
		return argb & 0x00FFFFFF;
	}
	
	/**
	 * Logic ripped straight from {@link PotionContents#getColorOptional}
	 */
	public static OptionalInt calculateBlend(Iterable<MobEffectInstance> effects, boolean shouldUseVanillaColor) {
		int totalRed = 0;
		int totalGreen = 0;
		int totalBlue = 0;
		int totalWeight = 0;
		
		for (final var instance : effects) {
			if (!instance.isVisible()) { continue; }
			
			final var effectHolder = instance.getEffect();
			
			// Wow, Java allows marking this as final.
			// Technology man.
			final int effectColorARGB;
			if (shouldUseVanillaColor) {
				effectColorARGB = ((MobEffectAccessor) effectHolder.value()).getVanillaColor();
			} else {
				final var resolvedRGB = EffectColorTable.resolve(effectHolder);
				effectColorARGB = (resolvedRGB != null)
					? resolvedRGB
					: ((MobEffectAccessor) effectHolder.value()).getVanillaColor();
			}
			
			final int amplifierWeight = instance.getAmplifier() + 1;
			
			totalRed += amplifierWeight * ARGB.red(effectColorARGB);
			totalGreen += amplifierWeight * ARGB.green(effectColorARGB);
			totalBlue += amplifierWeight * ARGB.blue(effectColorARGB);
			totalWeight += amplifierWeight;
		}
		
		return (totalWeight == 0)
			? OptionalInt.empty()
			: OptionalInt.of(ARGB.color(totalRed / totalWeight, totalGreen / totalWeight, totalBlue / totalWeight));
	}
}
