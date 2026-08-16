package me.catsflex.oldpotions.mixin.invoker;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayer.class)
public interface ServerPlayerInvoker {
	
	@Invoker("onEffectUpdated")
	void invokeOnEffectUpdated(MobEffectInstance effect, boolean isForced, Entity entity);
}
