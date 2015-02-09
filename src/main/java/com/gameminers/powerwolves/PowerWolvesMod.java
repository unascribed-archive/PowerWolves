package com.gameminers.powerwolves;

import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.gameminers.powerwolves.enchantment.EnchantmentWolfDamage;
import com.gameminers.powerwolves.enchantment.EnchantmentWolfKnockback;
import com.gameminers.powerwolves.enchantment.EnchantmentWolfPoison;
import com.gameminers.powerwolves.entity.EntityPowerWolf;
import com.gameminers.powerwolves.enums.WolfType;
import com.gameminers.powerwolves.item.ItemCollar;
import com.gameminers.powerwolves.item.ItemFangs;
import com.gameminers.powerwolves.item.ItemTransmutator;
import com.gameminers.powerwolves.item.ItemWolfArmor;
import com.gameminers.powerwolves.proxy.CommonProxy;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="powerwolves", name="Power Wolves", version="0.4.2", dependencies="required-after:KitchenSink;after:hmt")
public class PowerWolvesMod {
	public static final String DOGECOIN_DONATION_ADDRESS = "D8dNpUyW2UwXGTxSr7VSPKo34BeBKnHjoY";
	public static final Map<WolfType, ResourceLocation> wolfResources = Maps.newHashMap();
	
	@Instance("powerwolves")
	public static PowerWolvesMod inst;
	@SidedProxy(clientSide="com.gameminers.powerwolves.proxy.ClientProxy", serverSide="com.gameminers.powerwolves.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static ItemCollar COLLAR = new ItemCollar();
	public static ItemFangs FANGS = new ItemFangs();
	public static ItemTransmutator TRANSMUTATOR = new ItemTransmutator();
	public static Item IRON_NUGGET;
	public static ItemWolfArmor WOLF_ARMOR = new ItemWolfArmor();
	
	public static EnchantmentWolfDamage VICIOUS;
	public static EnchantmentWolfKnockback FORCEFUL;
	public static EnchantmentWolfPoison VENOMOUS;
	
	public static Achievement aPowerful = new Achievement("find_doges", "find_doges", 0, 0, Items.bone, null).initIndependentStat().registerStat();
	public static Achievement aFriends = new Achievement("collar", "collar", 2, 0, COLLAR, aPowerful).registerStat();
	public static Achievement aFindAll = new Achievement("find_all", "find_all", 0, -3, Items.porkchop, aPowerful).setSpecial().registerStat();
	public static Achievement aMushrolves = new Achievement("find_mushrolves", "find_mushrolves", -2, -2, Blocks.brown_mushroom, aPowerful).setSpecial().registerStat();
	public static Achievement aDentist = new Achievement("fangs", "fangs", 3, 1, FANGS, aFriends).registerStat();
	public static Achievement aDiamond = new Achievement("diamond_collar", "diamond_collar", 2, -2, new ItemStack(COLLAR, 1, 1), aFriends).registerStat();
	public static Achievement aOP = new Achievement("op", "op", 4, 0, ench(new ItemStack(FANGS, 1, 2)), aDentist).setSpecial().registerStat();
	public static AchievementPage achievements = new AchievementPage("Power Wolves", aPowerful, aFriends, aDentist, aOP, aFindAll, aMushrolves, aDiamond);
	
	private static Configuration config = new Configuration(new File("config/powerwolves.cfg"));
	
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		WolfType.printSpawnConditions();
		AchievementPage.registerAchievementPage(achievements);
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
		GameRegistry.registerItem(FANGS, "fangs", "powerwolves");
		GameRegistry.registerItem(TRANSMUTATOR, "transmutator", "powerwolves");
		GameRegistry.registerItem(WOLF_ARMOR, "wolf_armor", "powerwolves");
		IRON_NUGGET = new Item().setUnlocalizedName("ironNugget").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("powerwolves:iron_nugget");
		GameRegistry.registerItem(IRON_NUGGET, "iron_nugget", "powerwolves");
		OreDictionary.registerOre("nuggetIron", IRON_NUGGET);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(IRON_NUGGET, 9), 
				"I",
				'I', "ingotIron"
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(Items.iron_ingot, 
				",,,",
				",,,",
				",,,",
				',', "nuggetIron"
				));
		FMLCommonHandler.instance().bus().register(this);
		GameRegistry.addRecipe(new ShapedOreRecipe(COLLAR, 
				"SSS",
				"SGS",
				'S', Items.string,
				'G', "nuggetGold"
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(COLLAR, 1, 1), 
				"SSS",
				"SDS",
				'S', Items.string,
				'D', "diamond"
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(FANGS.setFangType(new ItemStack(FANGS), 1), 
				"III",
				"NNN",
				'I', "ingotIron",
				'N', "nuggetIron"
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(FANGS.setFangType(new ItemStack(FANGS), 2), 
				" F ",
				"D D",
				'F', new ItemStack(FANGS, 1, 0),
				'D', "gemDiamond"
				));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(WOLF_ARMOR.setType(new ItemStack(WOLF_ARMOR), 0), 
				"  H",
				"III",
				"B B",
				'H', Items.leather_helmet,
				'I', Items.leather,
				'B', Items.leather_boots 
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(WOLF_ARMOR.setType(new ItemStack(WOLF_ARMOR), 1), 
				"  H",
				"III",
				"B B",
				'H', Items.chainmail_helmet,
				'I', "ingotIron",
				'B', Items.chainmail_boots 
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(WOLF_ARMOR.setType(new ItemStack(WOLF_ARMOR), 2), 
				"  H",
				"III",
				"B B",
				'H', Items.iron_helmet,
				'I', "ingotIron",
				'B', Items.iron_boots 
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(WOLF_ARMOR.setType(new ItemStack(WOLF_ARMOR), 3), 
				"  H",
				"III",
				"B B",
				'H', Items.golden_helmet,
				'I', "ingotGold",
				'B', Items.golden_boots 
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(WOLF_ARMOR.setType(new ItemStack(WOLF_ARMOR), 4), 
				"  H",
				"III",
				"B B",
				'H', Items.diamond_helmet,
				'I', "gemDiamond",
				'B', Items.diamond_boots 
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(WOLF_ARMOR.setType(new ItemStack(WOLF_ARMOR), 5), 
				"  H",
				"III",
				"B B",
				'H', Items.diamond_helmet,
				'I', Blocks.bedrock,
				'B', Items.diamond_boots 
				));
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new PowerWolvesGuiHandler());
		MinecraftForge.EVENT_BUS.register(this);
		CraftingManager.getInstance().getRecipeList().add(new RecipesCollarDyes());
		VICIOUS = registerEnchant(new EnchantmentWolfDamage(getEnchantId("vicious"), 10, 0));
		FORCEFUL = registerEnchant(new EnchantmentWolfKnockback(getEnchantId("forceful"), 5));
		VENOMOUS = registerEnchant(new EnchantmentWolfPoison(getEnchantId("venomous"), 3));
		config.save();
		proxy.registerStuff();
	}
	
	private static ItemStack ench(ItemStack itemStack) {
		itemStack.setTagCompound(new NBTTagCompound());
		itemStack.getTagCompound().setTag("ench", new NBTTagList());
		return itemStack;
	}

	private int enchantId = 150;
	
	private int getEnchantId(String string) {
		return config.getInt(string, "enchantmentIds", enchantId++, 0, 256, "");
	}

	private <T extends Enchantment> T registerEnchant(T ench) {
		return ench;
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


