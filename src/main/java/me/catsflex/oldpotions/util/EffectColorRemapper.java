package me.catsflex.oldpotions.util;

import me.catsflex.oldpotions.config.ModConfig;
import me.catsflex.oldpotions.mixin.accessor.ColorParticleOptionAccessor;
import me.catsflex.oldpotions.mixin.accessor.MobEffectAccessor;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ARGB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EffectColorRemapper {
	
	// Used for swapping effect particle colors since they are evaluated server-side.
	private static final Map<Integer, Integer> VANILLA_RGB_TO_OVERRIDE_RGB = new HashMap<>();
	
	private EffectColorRemapper() {}
	
	// Reconstructs the table every time the config is changed.
	public static void refresh() {
		VANILLA_RGB_TO_OVERRIDE_RGB.clear();
		
		// Load single effects.
		for (final var entry : ModConfig.getInstance().effectColorOverrides.entrySet()) {
			final var effect = entry.getKey();
			final var resolvedRGB = EffectColorTable.resolve(effect);
			if (resolvedRGB == null) { continue; }
			
			final var vanillaARGB = ((MobEffectAccessor) effect.value()).getVanillaColor();
			VANILLA_RGB_TO_OVERRIDE_RGB.put(ColorUtil.stripAlpha(vanillaARGB), resolvedRGB);
		}
		
		// Load vanilla combined effects.
		for (final var potion : BuiltInRegistries.POTION) {
			final var effects = potion.getEffects();
			if (effects.size() <= 1) { continue; }
			
			final var vanillaBlendedARGB = ColorUtil.calculateBlend(effects, true);
			if (vanillaBlendedARGB.isEmpty()) { continue; }
			
			final var overrideBlendedARGB = ColorUtil.calculateBlend(effects, false);
			if (overrideBlendedARGB.isEmpty()) { continue; }
			
			VANILLA_RGB_TO_OVERRIDE_RGB.put(ColorUtil.stripAlpha(vanillaBlendedARGB.getAsInt()), ColorUtil.stripAlpha(overrideBlendedARGB.getAsInt()));
		}
		
		// Any non-vanilla potions/arrows (e.g., 2-in-1 Speed & Strength potion) are partially supported.
		// Arrow trail, splash potion smash, and area effect cloud particle color will remain unchanged
		// due to Minecraft's technical limitations. There's not much I can do.
	}
	
	public static List<ParticleOptions> remap(List<ParticleOptions> particles) {
		if (particles.isEmpty() || VANILLA_RGB_TO_OVERRIDE_RGB.isEmpty()) { return particles; }
		return particles.stream().map(EffectColorRemapper::remapOne).toList();
	}
	
	public static ParticleOptions remapOne(ParticleOptions options) {
		if (!(options instanceof ColorParticleOption colorOption)) { return options; }
		
		final var vanillaARGB = ((ColorParticleOptionAccessor) colorOption).getVanillaColor();
		final var overrideARGB = remapIntColor(vanillaARGB);
		if (vanillaARGB == overrideARGB) { return options; }
		
		return ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, overrideARGB);
	}
	
	public static int remapIntColor(int argb) {
		if (VANILLA_RGB_TO_OVERRIDE_RGB.isEmpty()) { return argb; }
		
		final var overrideRGB = VANILLA_RGB_TO_OVERRIDE_RGB.get(ColorUtil.stripAlpha(argb));
		if (overrideRGB == null) { return argb; }
		
		final var alpha = ARGB.alpha(argb);
		return ARGB.color(alpha, overrideRGB);
	}
}
