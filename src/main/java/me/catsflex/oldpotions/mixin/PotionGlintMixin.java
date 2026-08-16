package me.catsflex.oldpotions.mixin;

import me.catsflex.oldpotions.config.ModConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PotionItem.class)
public abstract class PotionGlintMixin extends Item {
	public PotionGlintMixin(Item.Properties properties) {
		super(properties);
	}
	
	@Override
	public boolean isFoil(@NonNull ItemStack item) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get() || !config.shouldUseEnchantmentGlint.get()) { return super.isFoil(item); }
		
		// This excludes water bottles, awkward, thick, and mundane potions
		// since they don't have any effects.
		final var potionContents = item.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
		final var hasEffects = potionContents.getAllEffects().iterator().hasNext();
		
		return super.isFoil(item) || hasEffects;
	}
}
