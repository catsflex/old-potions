package me.catsflex.oldpotions.mixin.color;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.catsflex.oldpotions.config.ModConfig;
import me.catsflex.oldpotions.util.ColorUtil;
import net.minecraft.client.color.item.Potion;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Potion.class)
public abstract class PotionMixin {
	
	@ModifyReturnValue(method = "calculate", at = @At("RETURN"))
	private int remapPotionColor(int originalColor, ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get()) { return originalColor; }
		
		final var potionContents = itemStack.get(DataComponents.POTION_CONTENTS);
		if (potionContents == null) { return originalColor; }
		
		final var blended = ColorUtil.calculateBlend(potionContents.getAllEffects(), false);
		return blended.isEmpty()
			? originalColor
			: ARGB.opaque(blended.getAsInt());
	}
}
