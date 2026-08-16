package me.catsflex.oldpotions.util;

import me.catsflex.oldpotions.config.ModConfig;
import me.catsflex.oldpotions.config.custom.EffectColorPalette;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class EffectColorTable {
	
	// Each entry is stored ONLY under the last palette its value was still active for.
	private static final Map<EffectColorPalette, Map<Holder<MobEffect>, Integer>> COLORS = new EnumMap<>(EffectColorPalette.class);
	
	static {
		final var v1_19_3AndBelow = new HashMap<Holder<MobEffect>, Integer>();
		final var v1_19_4 = new HashMap<Holder<MobEffect>, Integer>();
		
		// Colors last used in 1.19.3.
		v1_19_3AndBelow.put(MobEffects.SPEED, 0x7CAFC6);
		v1_19_3AndBelow.put(MobEffects.SLOWNESS, 0x5A6C81);
		v1_19_3AndBelow.put(MobEffects.STRENGTH, 0x932423);
		v1_19_3AndBelow.put(MobEffects.INSTANT_DAMAGE, 0x430A09);
		v1_19_3AndBelow.put(MobEffects.JUMP_BOOST, 0x22FF4C);
		v1_19_3AndBelow.put(MobEffects.RESISTANCE, 0x99453A);
		v1_19_3AndBelow.put(MobEffects.FIRE_RESISTANCE, 0xE49A3A);
		v1_19_3AndBelow.put(MobEffects.WATER_BREATHING, 0x2E5299);
		v1_19_3AndBelow.put(MobEffects.INVISIBILITY, 0x7F8392);
		v1_19_3AndBelow.put(MobEffects.NIGHT_VISION, 0x1F1FA1);
		v1_19_3AndBelow.put(MobEffects.LUCK, 0x339900);
		
		// Colors last used in 1.19.4.
		v1_19_4.put(MobEffects.WITHER, 0x352A27);
		v1_19_4.put(MobEffects.SLOW_FALLING, 0xFFEFD1);
		
		COLORS.put(EffectColorPalette.V_1_19_3_AND_BELOW, v1_19_3AndBelow);
		COLORS.put(EffectColorPalette.V_1_19_4, v1_19_4);
	}
	
	private EffectColorTable() {}
	
	public static @Nullable Integer resolve(Holder<MobEffect> effect) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get()) { return null; }
		
		// If no override option exists for this effect at all (its color never changed) OR
		// the option is set to GLOBAL, fall back to the global palette.
		final var overrideColor = config.effectColorOverrides.get(effect);
		final var overridePalette = (overrideColor != null) ? overrideColor.get().toPalette() : null;
		final var currentPalette = (overridePalette != null) ? overridePalette : config.effectColorPalette.get();
		
		return getColor(effect, currentPalette);
	}
	
	// Walks forward from the requested palette
	// until it finds the value that was active at that point in time.
	private static @Nullable Integer getColor(Holder<MobEffect> effect, @Nullable EffectColorPalette palette) {
		
		// If you already use the latest palette available,
		// there is no need to perform any retrievals.
		if (palette == EffectColorPalette.latest()) { return null; }
		
		for (var version = palette; version != null; version = version.next()) {
			final var colors = COLORS.get(version);
			if (colors == null) { continue; }
			
			final var color = colors.get(effect);
			if (color != null) { return color; }
		}
		
		return null;
	}
}
