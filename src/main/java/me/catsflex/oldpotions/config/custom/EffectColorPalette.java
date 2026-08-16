package me.catsflex.oldpotions.config.custom;

import me.catsflex.oldpotions.config.gui.ConfigKeyType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public enum EffectColorPalette implements StringRepresentable {
	V_1_19_3_AND_BELOW("v1.19.3-"),
	V_1_19_4("v1.19.4"),
	V_1_20_AND_ABOVE("v1.20+");
	
	private final String status;
	
	EffectColorPalette(String status) { this.status = status; }
	
	public @Nullable EffectColorPalette next() {
		final var values = values();
		final var nextOrdinal = ordinal() + 1;
		return nextOrdinal < values.length ? values[nextOrdinal] : null;
	}
	
	static public EffectColorPalette latest() {
		final var values = values();
		return values[values.length - 1];
	}
	
	@Override
	public @NonNull String getSerializedName() {
		return status;
	}
	
	public Component getComponent() {
		return Component.translatable(ConfigKeyType.getEnumKey(this));
	}
}
