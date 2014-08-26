package com.gameminers.powerwolves.item;

import com.thoughtcomplex.horizon.entities.CustomSpawnEgg;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemCollar extends Item {
	private IIcon collarBase;
	private IIcon collarStud;
	public ItemCollar() {
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName("collar");
	}
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	@Override
	public int getRenderPasses(int metadata) {
		return 2;
	}
	@Override
	public int getColorFromItemStack(ItemStack is, int pass) {
		if (pass == 1) return 0xFFFFFF;
		if (is.hasTagCompound()) {
			if (is.getTagCompound().hasKey("Color")) {
				return is.getTagCompound().getInteger("Color");
			}
		}
		return 0xFFFFFF;
	}
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return pass == 0 ? collarBase : collarStud;
	}
	@Override
	public void registerIcons(IIconRegister registry) {
		collarBase = registry.registerIcon("powerwolves:collar");
		collarStud = registry.registerIcon("powerwolves:collar_stud");
	}
}
