package me.catsflex.oldpotions.mixin.color;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.catsflex.oldpotions.config.ModConfig;
import me.catsflex.oldpotions.util.EffectColorRemapper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.AreaEffectCloud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AreaEffectCloud.class)
public abstract class AreaEffectCloudMixin {
	
	@ModifyReturnValue(method = "getParticle", at = @At("RETURN"))
	private ParticleOptions remapParticleColor(ParticleOptions original) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get()) { return original; }
		
		return EffectColorRemapper.remapOne(original);
	}
}
