package com.gameminers.powerwolves.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public class ItemWolfArmor extends Item {
	private IIcon[] icons = new IIcon[6];
	private int[] maxDamages = {
			81,
			241,
			241,
			113,
			529,
			4500
	};
	public ItemWolfArmor() {
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("wolf_armor");
	}
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int type = getType(stack);
		return "item.wolf_armor."+(type == 0 ? "leather" : type == 1 ? "chain" : type == 2 ? "iron" : type == 3 ? "gold" : type == 4 ? "diamond" : type == 5 ? "invincium" : "unknown");
	}
	/**
	 * 0 = leather
	 * 1 = chain
	 * 2 = iron
	 * 3 = gold
	 * 4 = diamond
	 * 5 = invincium
	 */
	public int getType(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound().getByte("ArmorType") : 0;
	}
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return icons[getType(stack)];
	}
	@Override
	public IIcon getIconIndex(ItemStack stack) {
		return getIcon(stack, 0);
	}
	@Override
	public int getMaxDamage(ItemStack stack) {
		return maxDamages[getType(stack)];
	}
	public ItemStack setType(ItemStack stack, int fangType) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setByte("ArmorType", (byte) fangType);
		return stack;
	}
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 6; i++) {
			list.add(setType(new ItemStack(item), i));
		}
	}
	@Override
	public void registerIcons(IIconRegister p_94581_1_) {
		icons[0] = p_94581_1_.registerIcon("powerwolves:wolf_armor_leather");
		icons[1] = p_94581_1_.registerIcon("powerwolves:wolf_armor_chain");
		icons[2] = p_94581_1_.registerIcon("powerwolves:wolf_armor_iron");
		icons[3] = p_94581_1_.registerIcon("powerwolves:wolf_armor_gold");
		icons[4] = p_94581_1_.registerIcon("powerwolves:wolf_armor_diamond");
		icons[5] = p_94581_1_.registerIcon("powerwolves:wolf_armor_invincium");
	}
	@Override
	public boolean getHasSubtypes() {
		return true;
	}
	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_,
			List p_77624_3_, boolean p_77624_4_) {
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
		if (getType(p_77624_1_) == 5) {
			p_77624_3_.add("\u00A77Creative-only. Makes wolves invincible.");
		}
	}
}
