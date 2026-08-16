package me.catsflex.oldpotions.mixin.color;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.catsflex.oldpotions.config.ModConfig;
import me.catsflex.oldpotions.util.EffectColorTable;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEffect.class)
public abstract class MobEffectMixin {
	@Shadow @Final private static int AMBIENT_ALPHA;
	@Unique private static final int DEFAULT_ALPHA = 0xFF;
	
	@ModifyReturnValue(method = "createParticleOptions", at = @At("RETURN"))
	private ParticleOptions remapEffectColor(ParticleOptions original, MobEffectInstance instance) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get()) { return original; }
		if (!(original instanceof ColorParticleOption)) { return original; }
		
		final var color = EffectColorTable.resolve(holder());
		if (color == null) { return original; }
		
		final var alpha = instance.isAmbient() ? AMBIENT_ALPHA : DEFAULT_ALPHA;
		return ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.color(alpha, color));
	}
	
	@Unique
	private Holder<MobEffect> holder() {
		final var self = (MobEffect) (Object) this;
		return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(self);
	}
}
