package com.gameminers.powerwolves.enchantment;

import com.gameminers.powerwolves.PowerWolvesMod;

import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnchantmentKnockback;
import net.minecraft.item.ItemStack;

public class EnchantmentWolfKnockback extends EnchantmentKnockback {

	public EnchantmentWolfKnockback(int effectId, int weight) {
		super(effectId, weight);
	}
	
	@Override
	public String getName() {
		return "enchantment.forceful";
	}
	
	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() == PowerWolvesMod.FANGS;
	}
	
}
