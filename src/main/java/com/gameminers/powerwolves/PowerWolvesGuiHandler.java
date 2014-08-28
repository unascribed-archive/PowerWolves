package com.gameminers.powerwolves;

import com.gameminers.powerwolves.entity.EntityPowerWolf;
import com.gameminers.powerwolves.gui.GuiWolfInventory;
import com.gameminers.powerwolves.inventory.ContainerWolf;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class PowerWolvesGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Entity ent = world.getEntityByID(id);
		if (ent == null || !(ent instanceof EntityPowerWolf)) return null;
		EntityPowerWolf wolf = (EntityPowerWolf) ent;
		return new ContainerWolf(player.inventory, wolf.inventory, wolf);
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Entity ent = world.getEntityByID(id);
		if (ent == null || !(ent instanceof EntityPowerWolf)) return null;
		EntityPowerWolf wolf = (EntityPowerWolf) ent;
		return new GuiWolfInventory(player.inventory, wolf.inventory, wolf);
	}

}
