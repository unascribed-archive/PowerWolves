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
	public String getUnlocalizedName(ItemStack stack) {
		int dura = stack.getItemDamage();
		return "item.fangs."+(dura == 0 ? "normal" : dura == 1 ? "iron" : dura == 2 ? "diamond" : "unknown");
	}
	@Override
	public IIcon getIconIndex(ItemStack stack) {
		return getIcon(stack, 0);
	}
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		int dura = stack.getItemDamage();
		return (dura == 0 ? natural : dura == 1 ? iron : dura == 2 ? diamond : null);
	}
	@Override
	public void registerIcons(IIconRegister registry) {
		natural = registry.registerIcon("powerwolves:fang");
		iron = registry.registerIcon("powerwolves:iron_fang");
		diamond = registry.registerIcon("powerwolves:diamond_tipped_fang");
	}
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
		list.add(new ItemStack(item, 1, 2));
	}
	@Override
	public boolean getHasSubtypes() {
		return true;
	}
	@Override
	public Multimap getAttributeModifiers(ItemStack stack) {
		Multimap multimap = super.getAttributeModifiers(stack);
		int dura = stack.getItemDamage();
		double damage = (dura == 0 ? 2 : dura == 1 ? 0.5 : dura == 2 ? 3.5 : null);
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Fangs modifier", damage, 0));
		return multimap;
	}
}

