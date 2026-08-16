package me.catsflex.oldpotions.mixin.color;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.catsflex.oldpotions.config.ModConfig;
import me.catsflex.oldpotions.util.EffectColorRemapper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow @Final private static EntityDataAccessor<List<ParticleOptions>> DATA_EFFECT_PARTICLES;
	
	@WrapOperation(
		method = "tickEffects",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/network/syncher/SynchedEntityData;get(Lnet/minecraft/network/syncher/EntityDataAccessor;)Ljava/lang/Object;"
		)
	)
	private Object remapParticleColor(SynchedEntityData entityData, EntityDataAccessor<?> accessor, Operation<Object> original) {
		final var list = original.call(entityData, accessor);
		if (accessor != DATA_EFFECT_PARTICLES) { return list; }
		
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get()) { return list; }
		
		@SuppressWarnings("unchecked") final var particles = (List<ParticleOptions>) list;
		return EffectColorRemapper.remap(particles);
	}
}
