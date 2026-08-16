package me.catsflex.oldpotions.config.custom;

import me.catsflex.oldpotions.config.gui.ConfigKeyType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public enum EffectColorOverride implements StringRepresentable {
	GLOBAL("global"),
	// These values below must be named the same as in the EffectColorPalette enum.
	V_1_19_3_AND_BELOW("v1.19.3-"),
	V_1_19_4("v1.19.4"),
	V_1_20_AND_ABOVE("v1.20+");
	
	private final String status;
	
	EffectColorOverride(String status) {
		this.status = status;
	}
	
	// Fail fast at classload if someone renames a value in one enum but not the other.
	static {
		for (final var value : values()) {
			value.toPalette();
		}
	}
	
	public @Nullable EffectColorPalette toPalette() {
		if (this == GLOBAL) { return null; }
		return EffectColorPalette.valueOf(name());
	}
	
	@Override
	public @NonNull String getSerializedName() {
		return status;
	}
	
	public Component getComponent() {
		return Component.translatable(ConfigKeyType.getEnumKey(this));
	}
}
