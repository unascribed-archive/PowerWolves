package com.gameminers.powerwolves;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import com.gameminers.powerwolves.entity.EntityPowerWolf;
import com.gameminers.powerwolves.entity.render.ModelPowerWolf;
import com.gameminers.powerwolves.entity.render.RenderPowerWolf;
import com.gameminers.powerwolves.enums.WolfType;
import com.gameminers.powerwolves.item.ItemCollar;
import com.gameminers.powerwolves.item.ItemTransmutator;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;

import net.minecraft.block.Block;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="powerwolves", name="Power Wolves", version="0.1.1", dependencies="required-after:KitchenSink;after:hmt")
public class PowerWolvesMod {
	public static final String DOGECOIN_DONATION_ADDRESS = "D8dNpUyW2UwXGTxSr7VSPKo34BeBKnHjoY";
	public static final Map<WolfType, ResourceLocation> wolfResources = Maps.newHashMap();
	public static Item COLLAR = new ItemCollar();
	public static Item TRANSMUTATOR = new ItemTransmutator();
	
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		RenderingRegistry.registerEntityRenderingHandler(EntityPowerWolf.class, new RenderPowerWolf(new ModelPowerWolf(), new ModelPowerWolf(), 0.5F));
		WolfType.printSpawnConditions();
		EntityRegistry.registerModEntity(EntityPowerWolf.class, "wolf", EntityPowerWolf.ENTITY_ID, this, 96, 1, true);
		try {
			Class.forName("com.thoughtcomplex.horizon.entities.CustomSpawnEgg");
			com.thoughtcomplex.horizon.entities.CustomSpawnEgg.registerCustomEgg("powerwolves","wolf", EntityPowerWolf.ENTITY_ID, new Color(0xD2C59B), new Color(0xC29240));
		} catch (Throwable t) {}
		for (BiomeGenBase bgp : BiomeGenBase.getBiomeGenArray()) {
			if (bgp == null) continue;
			List<SpawnListEntry> epy = bgp.getSpawnableList(EnumCreatureType.creature);
			for (SpawnListEntry sle : epy) {
				if (EntityWolf.class.isAssignableFrom(sle.entityClass)) {
					sle.entityClass = EntityPowerWolf.class;
				}
			}
			
		}
		for (WolfType w : WolfType.values()) {
			for (BiomeGenBase bgp : w.getBiomes()) {
				List<SpawnListEntry> epy = bgp.getSpawnableList(EnumCreatureType.creature);
				epy.add(new SpawnListEntry(EntityPowerWolf.class, 8, 4, 8));
			}
			String[] split = w.getTexture().split(":");
			wolfResources.put(w, new ResourceLocation(split[0], split[1]));
		}
		GameRegistry.registerItem(COLLAR, "collar", "powerwolves");
		GameRegistry.registerItem(TRANSMUTATOR, "transmutator", "powerwolves");
		FMLCommonHandler.instance().bus().register(this);
		GameRegistry.addRecipe(new ShapedOreRecipe(COLLAR, 
				"SSS",
				"SGS",
				'S', Items.string,
				'G', "nuggetGold"
				));
		MinecraftForge.EVENT_BUS.register(this);
	}
}
