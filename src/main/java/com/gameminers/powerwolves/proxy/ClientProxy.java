package com.gameminers.powerwolves.proxy;

import com.gameminers.powerwolves.entity.EntityPowerWolf;
import com.gameminers.powerwolves.entity.render.ModelPowerWolf;
import com.gameminers.powerwolves.entity.render.RenderPowerWolf;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerStuff() {
		RenderingRegistry.registerEntityRenderingHandler(EntityPowerWolf.class, new RenderPowerWolf(new ModelPowerWolf(), new ModelPowerWolf(), 0.5F));
	}

}
