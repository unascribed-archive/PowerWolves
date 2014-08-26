package com.gameminers.powerwolves.entity;

import gminers.kitchensink.Strings;

import java.util.ArrayList;
import java.util.List;

import com.gameminers.powerwolves.PowerWolvesMod;
import com.gameminers.powerwolves.enums.SpecialWolfType;
import com.gameminers.powerwolves.enums.WolfType;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class EntityPowerWolf extends EntityWolf {

	public static final int ENTITY_ID = 0;
	private WolfType type = WolfType.ARCTIC_WOLF;
	
	public EntityPowerWolf(World w) {
		super(w);
	}

	@Override
	public void setLocationAndAngles(double x, double y,
			double z, float yaw, float pitch) {
		super.setLocationAndAngles(x, y, z, yaw, pitch);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(24, 0);
		dataWatcher.addObject(25, new ItemStack(PowerWolvesMod.COLLAR));
		dataWatcher.updateObject(25, null);
	}
	
	protected String getLivingSound() {
		if (getSpecialType() == SpecialWolfType.K9) {
			if (isAngry()) {
				return "powerwolves:lowfiGrowl";
			}
			else if (rand.nextInt(3) == 0) {
				if (isTamed() && this.dataWatcher.getWatchableObjectFloat(18) < 10.0F) {
					return "powerwolves:lowfiWhine";
				} else {
					return "powerwolves:lowfiPanting";
				}
			}
			return "powerwolves:lowfiBark";
		}
        return this.isAngry() ? "mob.wolf.growl" : (this.rand.nextInt(3) == 0 ? (this.isTamed() && this.dataWatcher.getWatchableObjectFloat(18) < 10.0F ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    protected String getHurtSound() {
    	if (getSpecialType() == SpecialWolfType.K9) return "powerwolves:lowfiHurt";
        return "mob.wolf.hurt";
    }

    protected String getDeathSound() {
    	if (getSpecialType() == SpecialWolfType.K9) return "powerwolves:lowfiDeath";
        return "mob.wolf.death";
    }
	
	@Override
	protected void updateAITick() {
		super.updateAITick();
	}
	
	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (!worldObj.isRemote && hasCollar()) {
			EntityLivingBase owner = getOwner();
			if (owner instanceof EntityPlayer) {
				((EntityPlayer)owner).addChatMessage(source.func_151519_b(this));
			}
		}
	}
	
	public WolfType getType() {
		if (dataWatcher.getWatchableObjectInt(24) != type.ordinal()) {
			type = WolfType.values()[dataWatcher.getWatchableObjectInt(24)];
		}
		return type;
	}
	
	public void setType(WolfType type) {
		this.type = type;
		this.dataWatcher.updateObject(24, type.ordinal());
	}
	
	public ItemStack getCollar() {
		return dataWatcher.getWatchableObjectItemStack(25);
	}
	
	public void setCollar(ItemStack collar) {
		dataWatcher.updateObject(25, collar);
	}
	
	public boolean hasCollar() {
		return getCollar() != null;
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		List<WolfType> types = new ArrayList<WolfType>();
		int x = (int)posX;
		int y = (int)posY;
		int z = (int)posZ;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(x, z);
		System.out.println("Spawning wolf in biome "+biome.biomeName);
		wolves: for (WolfType w : WolfType.values()) {
			if (w.getBiomes().length == 0) {
				types.add(w);
				continue;
			}
			for (BiomeGenBase b : w.getBiomes()) {
				if (b == biome) {
					types.add(w);
					continue wolves;
				}
			}
		}
		String[] names = new String[types.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = types.get(i).getFriendlyName();
		}
		System.out.println("Candidate types: "+Strings.formatList(names));
		setType(types.get(getRNG().nextInt(types.size())));
		System.out.println("Chose type "+getType().getFriendlyName());
		return null;
	}
	
	@Override
	public boolean hasCustomNameTag() {
		if (!isTamed()) return false;
		ItemStack collar = getCollar();
		return collar == null ? true : collar.hasDisplayName();
	}
	
	@Override
	public String getCustomNameTag() {
		ItemStack collar = getCollar();
		return collar == null ? "\u00A7cNo Collar" : collar.getDisplayName();
	}
	
	@Override
	public void setCustomNameTag(String name) {
		if (name.equals("\u00A7cNo Collar")) return; // no.
		ItemStack collar = getCollar();
		if (collar == null) {
			// you're out of luck, buddy
		} else {
			collar.setStackDisplayName(name);
		}
	}
	
	public SpecialWolfType getSpecialType() {
		if (hasCustomNameTag()) {
			if (getCustomNameTag().equals("K-9 Mark IV")) return SpecialWolfType.K9;
		}
		return null;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		if (tag.hasKey("WolfType", 8)) {
			setType(WolfType.valueOf(tag.getString("WolfType").toUpperCase().replace(" ", "_")));
		} else if (tag.hasKey("WolfType", 1)) { 
			setType(WolfType.values()[tag.getByte("WolfType")]);
		} else {
			setType(WolfType.ARCTIC_WOLF);
		}
		if (tag.hasKey("CollarItem")) {
			setCollar(ItemStack.loadItemStackFromNBT(tag.getCompoundTag("CollarItem")));
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setByte("WolfType", (byte)type.ordinal());
		ItemStack collar = getCollar();
		if (collar != null) {
			tag.setTag("CollarItem", collar.writeToNBT(new NBTTagCompound()));
		} else {
			tag.removeTag("CollarItem");
		}
		tag.removeTag("CustomName");
	}
	
	@Override
	public boolean interact(EntityPlayer p) {
		ItemStack itemstack = p.inventory.getCurrentItem();
		if (p.isSneaking()) {
			if (!worldObj.isRemote && isTamed() && p == getOwner()) {
				ItemStack collar = getCollar();
				if (collar != null) {
					setCollar(null);
					entityDropItem(collar, height/2f);
					return true;
				} else {
					if (itemstack != null && itemstack.getItem() == PowerWolvesMod.COLLAR) {
						ItemStack copy = itemstack.copy();
						copy.stackSize = 1;
						setCollar(copy);
						itemstack.stackSize--;
						return true;
					}
				}
			}
			return itemstack != null && itemstack.getItem() == Items.name_tag;
		}
		if (itemstack != null && itemstack.getItem() == Items.name_tag) {
			super.interact(p);
			return true;
		} else {
			return super.interact(p);
		}
	}
	
	@Override
	public EntityPowerWolf createChild(EntityAgeable m8) {
		EntityPowerWolf mate = (EntityPowerWolf)m8;
		EntityPowerWolf child = new EntityPowerWolf(worldObj);
		String owner = func_152113_b();
        if (owner != null && owner.trim().length() > 0) {
            child.func_152115_b(owner);
            child.setTamed(true);
        }
        if (getRNG().nextBoolean()) {
        	child.setType(getType());
        } else {
        	child.setType(mate.getType());
        }
		return child;
	}
}
