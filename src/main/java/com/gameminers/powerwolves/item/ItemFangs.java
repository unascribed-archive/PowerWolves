package com.gameminers.powerwolves.item;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public class ItemFangs extends Item {
	private IIcon iron;
	private IIcon natural;
	private IIcon diamond;
	
	public ItemFangs() {
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("fangs");
	}
	@Override
	public boolean isItemTool(ItemStack stack) {
		return true;
	}
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}
	@Override
	public int getItemEnchantability() {
		return 4;
	}
	@Override
	public int getMaxDamage(ItemStack stack) {
		int type = getFangType(stack);
		return (type == 0 ? 345 : type == 1 ? 251 : type == 2 ? 875 : 32767);
	}
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int type = getFangType(stack);
		return "item.fangs."+(type == 0 ? "normal" : type == 1 ? "iron" : type == 2 ? "diamond" : "unknown");
	}
	@Override
	public IIcon getIconIndex(ItemStack stack) {
		return getIcon(stack, 0);
	}
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		int type = getFangType(stack);
		return (type == 0 ? natural : type == 1 ? iron : type == 2 ? diamond : null);
	}
	/**
	 * 0 = natural
	 * 1 = iron
	 * 2 = diamond
	 */
	public int getFangType(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound().getByte("FangType") : 0;
	}
	public ItemStack setFangType(ItemStack stack, int fangType) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setByte("FangType", (byte) fangType);
		return stack;
	}
	@Override
	public void registerIcons(IIconRegister registry) {
		natural = registry.registerIcon("powerwolves:fang");
		iron = registry.registerIcon("powerwolves:iron_fang");
		diamond = registry.registerIcon("powerwolves:diamond_tipped_fang");
	}
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 3; i++) {
			list.add(setFangType(new ItemStack(item), i));
		}
	}
	@Override
	public boolean getHasSubtypes() {
		return true;
	}
	@Override
	public Multimap getAttributeModifiers(ItemStack stack) {
		Multimap multimap = super.getAttributeModifiers(stack);
		int type = getFangType(stack);
		double damage = (type == 0 ? 2 : type == 1 ? 0.5 : type == 2 ? 3.5 : null);
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Fangs modifier", damage, 0));
		return multimap;
	}
}

