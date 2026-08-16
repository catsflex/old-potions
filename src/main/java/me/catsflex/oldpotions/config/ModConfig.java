package me.catsflex.oldpotions.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.catsflex.oldpotions.Main;
import me.catsflex.oldpotions.config.custom.EffectColorOverride;
import me.catsflex.oldpotions.config.custom.EffectColorPalette;
import me.catsflex.oldpotions.config.option.BooleanOption;
import me.catsflex.oldpotions.config.option.ConfigOption;
import me.catsflex.oldpotions.config.option.EnumOption;
import me.catsflex.oldpotions.util.RefreshUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ModConfig {
	
	// Config saving stuff.
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final String CONFIG_NAME = Main.MOD_ID + ".json";
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_NAME);
	private static final List<ConfigOption<?>> OPTIONS = new ArrayList<>();
	private static final ModConfig INSTANCE = new ModConfig();
	
	public final BooleanOption isEnabled = new BooleanOption("isEnabled", true);
	public final EnumOption<EffectColorPalette> effectColorPalette =
		new EnumOption<>("effectColorPalette", EffectColorPalette.V_1_20_AND_ABOVE, EffectColorPalette.class);
	public final BooleanOption shouldUseEnchantmentGlint = new BooleanOption("shouldUseEnchantmentGlint", false);
	
	public final EnumOption<EffectColorOverride> speedColor = createColorOverrideOption("speedColor");
	public final EnumOption<EffectColorOverride> slownessColor = createColorOverrideOption("slownessColor");
	public final EnumOption<EffectColorOverride> strengthColor = createColorOverrideOption("strengthColor");
	public final EnumOption<EffectColorOverride> instantDamageColor = createColorOverrideOption("instantDamageColor");
	public final EnumOption<EffectColorOverride> jumpBoostColor = createColorOverrideOption("jumpBoostColor");
	public final EnumOption<EffectColorOverride> resistanceColor = createColorOverrideOption("resistanceColor");
	public final EnumOption<EffectColorOverride> fireResistanceColor = createColorOverrideOption("fireResistanceColor");
	public final EnumOption<EffectColorOverride> waterBreathingColor = createColorOverrideOption("waterBreathingColor");
	public final EnumOption<EffectColorOverride> invisibilityColor = createColorOverrideOption("invisibilityColor");
	public final EnumOption<EffectColorOverride> nightVisionColor = createColorOverrideOption("nightVisionColor");
	public final EnumOption<EffectColorOverride> witherColor = createColorOverrideOption("witherColor");
	public final EnumOption<EffectColorOverride> luckColor = createColorOverrideOption("luckColor");
	public final EnumOption<EffectColorOverride> slowFallingColor = createColorOverrideOption("slowFallingColor");
	
	public final Map<Holder<MobEffect>, EnumOption<EffectColorOverride>> effectColorOverrides = Map.ofEntries(
		Map.entry(MobEffects.SPEED, speedColor),
		Map.entry(MobEffects.SLOWNESS, slownessColor),
		Map.entry(MobEffects.STRENGTH, strengthColor),
		Map.entry(MobEffects.INSTANT_DAMAGE, instantDamageColor),
		Map.entry(MobEffects.JUMP_BOOST, jumpBoostColor),
		Map.entry(MobEffects.RESISTANCE, resistanceColor),
		Map.entry(MobEffects.FIRE_RESISTANCE, fireResistanceColor),
		Map.entry(MobEffects.WATER_BREATHING, waterBreathingColor),
		Map.entry(MobEffects.INVISIBILITY, invisibilityColor),
		Map.entry(MobEffects.NIGHT_VISION, nightVisionColor),
		Map.entry(MobEffects.WITHER, witherColor),
		Map.entry(MobEffects.LUCK, luckColor),
		Map.entry(MobEffects.SLOW_FALLING, slowFallingColor)
	);
	
	private ModConfig() {}
	
	private static EnumOption<EffectColorOverride> createColorOverrideOption(String key) {
		return new EnumOption<>(key, EffectColorOverride.GLOBAL, EffectColorOverride.class);
	}
	
	public static ModConfig getInstance() {
		return INSTANCE;
	}
	
	public static void registerOption(ConfigOption<?> option) {
		OPTIONS.add(option);
	}
	
	public void load() {
		if (!Files.exists(CONFIG_PATH)) {
			save();
			return;
		}
		
		try (var reader = Files.newBufferedReader(CONFIG_PATH)) {
			var element = JsonParser.parseReader(reader);
			if (!element.isJsonObject()) {
				throw new IllegalStateException("Config root is not a JSON object!");
			}
			
			var json = element.getAsJsonObject();
			for (var option : OPTIONS) {
				option.read(json);
			}
			
		} catch (Exception e) {
			Main.LOGGER.warn("Failed to load config, using defaults!", e);
			save();
		}
	}
	
	public void save() {
		var json = new JsonObject();
		
		for (var option : OPTIONS) {
			option.write(json);
		}
		
		try (var writer = Files.newBufferedWriter(CONFIG_PATH)) {
			GSON.toJson(json, writer);
		} catch (Exception e) {
			Main.LOGGER.warn("Failed to save config!", e);
		}
		
		RefreshUtil.refresh();
	}
}
