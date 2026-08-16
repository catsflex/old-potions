package me.catsflex.oldpotions.config.gui;

import dev.isxander.yacl3.api.YetAnotherConfigLib;
import me.catsflex.oldpotions.config.ModConfig;
import me.catsflex.oldpotions.config.custom.EffectColorOverride;
import me.catsflex.oldpotions.config.custom.EffectColorPalette;
import net.minecraft.client.gui.screens.Screen;

public final class YACLIntegration {
	private YACLIntegration() {}
	
	public static Screen createScreen(Screen parent) {
		final var config = ModConfig.getInstance();
		
		return YetAnotherConfigLib.createBuilder().title(YACLHelper.createTitle())
			
			.category(YACLHelper.createCategory("general")
				
				.group(YACLHelper.createGroup("main")
					.option(YACLHelper.tickBoxOption(config.isEnabled))
					.option(YACLHelper.enumOption(config.effectColorPalette, EffectColorPalette::getComponent))
					.option(YACLHelper.tickBoxOption(config.shouldUseEnchantmentGlint))
					.build())
				
				.group(YACLHelper.createGroup("color-overrides")
					.option(YACLHelper.enumOption(config.speedColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.slownessColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.strengthColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.instantDamageColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.jumpBoostColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.resistanceColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.fireResistanceColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.waterBreathingColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.invisibilityColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.nightVisionColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.witherColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.luckColor, EffectColorOverride::getComponent))
					.option(YACLHelper.enumOption(config.slowFallingColor, EffectColorOverride::getComponent))
					.build())
				
				.build())
			
			.save(config::save)
			.build()
			.generateScreen(parent);
	}
}
