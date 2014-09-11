package com.gameminers.powerwolves.enchantment;

import com.gameminers.powerwolves.PowerWolvesMod;

import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.item.ItemStack;

public class EnchantmentWolfProtection extends EnchantmentProtection {

	public EnchantmentWolfProtection(int effectId, int weight, int damageType) {
		super(effectId, weight, damageType);
	}
	
	@Override
	public int getMaxLevel() {
		return 3;
	}
	
	@Override
	public String getName() {
		return "enchantment.vicious";
	}
	
	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() == PowerWolvesMod.FANGS;
	}
	
}
