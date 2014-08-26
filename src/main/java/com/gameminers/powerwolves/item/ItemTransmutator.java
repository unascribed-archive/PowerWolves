package com.gameminers.powerwolves.item;

import java.util.List;

import com.gameminers.powerwolves.entity.EntityPowerWolf;
import com.gameminers.powerwolves.enums.WolfType;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ItemTransmutator extends Item {
	public ItemTransmutator() {
		setUnlocalizedName("transmutator");
		setTextureName("powerwolves:transmutator");
		setCreativeTab(CreativeTabs.tabMisc);
	}
	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_,
			List p_77624_3_, boolean p_77624_4_) {
		p_77624_3_.add("\u00A77Debug item. Only available in Creative.");
		p_77624_3_.add("\u00A77Right-clicking will cycle the breed of a Power Wolf.");
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
	}

	public boolean itemInteractionForEntity(ItemStack item, EntityPlayer p, EntityLivingBase ent) {
		if (ent instanceof EntityPowerWolf) {
			EntityPowerWolf wolf = (EntityPowerWolf)ent;
			if (!ent.worldObj.isRemote) {
				int nextType = wolf.getType().ordinal()+1;
				WolfType[] values = WolfType.values();
				if (nextType >= values.length) {
					nextType = 0;
				}
				wolf.setType(values[nextType]);
			} else {
				wolf.spawnTransmutationParticles();
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass) {
		return true;
	}
}
