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
import com.gameminers.powerwolves.proxy.CommonProxy;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;

import net.minecraft.block.Block;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
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
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.InstanceFactory;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="powerwolves", name="Power Wolves", version="0.2", dependencies="required-after:KitchenSink;after:hmt")
public class PowerWolvesMod {
	public static final String DOGECOIN_DONATION_ADDRESS = "D8dNpUyW2UwXGTxSr7VSPKo34BeBKnHjoY";
	public static final Map<WolfType, ResourceLocation> wolfResources = Maps.newHashMap();
	public static Item COLLAR = new ItemCollar();
	public static Item TRANSMUTATOR = new ItemTransmutator();
	@Instance("powerwolves")
	public static PowerWolvesMod inst;
	@SidedProxy(clientSide="com.gameminers.powerwolves.proxy.ClientProxy", serverSide="com.gameminers.powerwolves.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		WolfType.printSpawnConditions();
		EntityRegistry.registerModEntity(EntityPowerWolf.class, "wolf", EntityPowerWolf.ENTITY_ID, this, 96, 1, true);
		try {
			Class.forName("com.thoughtcomplex.horizon.items.ItemCustomSpawnEgg");
			for (WolfType wolf : WolfType.values()) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setByte("WolfType", (byte)wolf.ordinal());
				com.thoughtcomplex.horizon.items.ItemCustomSpawnEgg.registerCustomEgg("powerwolves", "wolf."+wolf.name().toLowerCase(), 0, nbt, new Color(wolf.getPrimaryColor()), new Color(wolf.getSecondaryColor()));
			}
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
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new PowerWolvesGuiHandler());
		MinecraftForge.EVENT_BUS.register(this);
		CraftingManager.getInstance().getRecipeList().add(new RecipesCollarDyes());
		proxy.registerStuff();
	}
	
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent e) {
		if (e.entity.getClass() == EntityWolf.class) {
			EntityPowerWolf power = new EntityPowerWolf(e.world);
			power.setType(WolfType.ARCTIC_WOLF);
			power.setPositionAndRotation(e.entity.posX, e.entity.posY, e.entity.posZ, e.entity.rotationYaw, e.entity.rotationPitch);
			power.rotationYawHead = ((EntityWolf)e.entity).rotationYawHead;
			power.spawnTransmutationParticles();
			String owner = ((EntityWolf)e.entity).func_152113_b();
			power.func_152115_b(owner);
			if (!e.world.isRemote) {
				e.world.spawnEntityInWorld(power);
				e.entity.setDead();
			}
		}
	}
}
