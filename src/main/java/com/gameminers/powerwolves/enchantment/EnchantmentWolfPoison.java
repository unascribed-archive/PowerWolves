package com.gameminers.powerwolves.enchantment;

import com.gameminers.powerwolves.PowerWolvesMod;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnchantmentKnockback;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

public class EnchantmentWolfPoison extends Enchantment {

	public EnchantmentWolfPoison(int effectId, int weight) {
		super(effectId, weight, null);
	}
	
	@Override
	public String getName() {
		return "enchantment.venomous";
	}
	
	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() == PowerWolvesMod.FANGS;
	}
	
	public int getMinEnchantability(int p_77321_1_)
    {
        return 5 + 20 * (p_77321_1_ - 1);
    }

    public int getMaxEnchantability(int p_77317_1_)
    {
        return super.getMinEnchantability(p_77317_1_) + 50;
    }

    public int getMaxLevel()
    {
        return 2;
    }
	
}
