package com.gameminers.powerwolves.entity;

import gminers.kitchensink.Strings;

import java.util.ArrayList;
import java.util.List;

import com.gameminers.powerwolves.PowerWolvesMod;
import com.gameminers.powerwolves.enums.SpecialWolfType;
import com.gameminers.powerwolves.enums.WolfType;
import com.gameminers.powerwolves.gui.GuiWolfInventory;
import com.gameminers.powerwolves.inventory.ContainerWolf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;

public class EntityPowerWolf extends EntityWolf {

	public static final int ENTITY_ID = 0;
	private WolfType type = WolfType.ARCTIC_WOLF;
	public AnimalChest inventory = new AnimalChest("PowerWolfInventory", 3) {
		private boolean oldSitting;
		@Override
		public void openInventory() {
			oldSitting = isSitting();
			setSitting(true);
		}
		@Override
		public void closeInventory() {
			setSitting(oldSitting);
		}
	};
	
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
		dataWatcher.addObject(26, new ItemStack(PowerWolvesMod.FANGS));
		dataWatcher.updateObject(25, null);
		dataWatcher.updateObject(26, null);
	}

	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2);
    }
	
	@Override
	public boolean attackEntityAsMob(Entity entity) {
        float baseDamage = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int knockback = 0;

        if (entity instanceof EntityLivingBase) {
            baseDamage += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)entity);
            knockback += EnchantmentHelper.getEnchantmentLevel(PowerWolvesMod.FORCEFUL.effectId, getFangs());
        }

        boolean shouldDamage = entity.attackEntityFrom(DamageSource.causeMobDamage(this), baseDamage);

        if (shouldDamage) {
            if (knockback > 0) {
                entity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            if (entity instanceof EntityLivingBase) {
            	int poison = EnchantmentHelper.getEnchantmentLevel(PowerWolvesMod.VENOMOUS.effectId, getFangs());
            	if (poison > 0) {
            		((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, 80, poison-1));
            	}
                EnchantmentHelper.func_151384_a((EntityLivingBase)entity, this);
            }

            EnchantmentHelper.func_151385_b(this, entity);
        }

        return shouldDamage;
    }

	protected String getLivingSound() {
		if (isAngry()) {
			return getAngrySound();
		}
		if (rand.nextInt(3) == 0) {
			if (this.isTamed() && dataWatcher.getWatchableObjectFloat(18) < 10.0F) {
				return getWhineSound();
			} else {
				return getPantingSound();
			}
		}
		return getIdleSound();
	}

	protected String getAngrySound() {
		SpecialWolfType special = getSpecialType();
		if (special == SpecialWolfType.SHIZUNE) return null;
		return "mob.wolf.growl";
	}

	protected String getWhineSound() {
		SpecialWolfType special = getSpecialType();
		if (special == SpecialWolfType.SHIZUNE) return null;
		return "mob.wolf.whine";
	}

	protected String getPantingSound() {
		return "mob.wolf.panting";
	}

	protected String getIdleSound() {
		SpecialWolfType special = getSpecialType();
		if (special == SpecialWolfType.SHIZUNE) return null;
		return "mob.wolf.bark";
	}

	@Override
	protected String getHurtSound() {
		SpecialWolfType special = getSpecialType();
		if (special == SpecialWolfType.SHIZUNE) return "game.neutral.hurt";
		return "mob.wolf.hurt";
	}

	@Override
	protected String getDeathSound() {
		SpecialWolfType special = getSpecialType();
		if (special == SpecialWolfType.SHIZUNE) return "game.neutral.die";
		return "mob.wolf.death";
	}
	
	@Override
	protected String getSplashSound() {
		return "game.neutral.swim.splash";
	}
	
	@Override
	protected String getSwimSound() {
		return "game.neutral.swim";
	}

	@Override
	protected void updateAITick() {
		super.updateAITick();
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (!worldObj.isRemote) {
			if (hasCollar() && hasCustomNameTag()) {
				EntityLivingBase owner = getOwner();
				if (owner instanceof EntityPlayer) {
					((EntityPlayer)owner).addChatMessage(source.func_151519_b(this));
				}
			}
			if (hasCollar()) {
				entityDropItem(getCollar(), 0.5f);
			}
			if (hasFangs()) {
				entityDropItem(getFangs(), 0.5f);
			}
			if (!isTamed() && getRNG().nextInt(4) == 0) {
				entityDropItem(new ItemStack(PowerWolvesMod.FANGS), 0.5f);
			}
			//entityDropItem(getArmor(), 0.5f);
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
		inventory.setInventorySlotContents(0, collar);
	}
	
	public ItemStack getFangs() {
		return dataWatcher.getWatchableObjectItemStack(26);
	}

	public void setFangs(ItemStack fangs) {
		dataWatcher.updateObject(26, fangs);
		inventory.setInventorySlotContents(1, fangs);
	}

	public boolean hasCollar() {
		return getCollar() != null;
	}
	
	public boolean hasFangs() {
		return getFangs() != null;
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		List<WolfType> types = new ArrayList<WolfType>();
		int x = (int)posX;
		int y = (int)posY;
		int z = (int)posZ;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(x, z);
		wolves: for (WolfType w : WolfType.values()) {
			for (BiomeGenBase b : w.getBiomes()) {
				if (b == biome) {
					types.add(w);
					continue wolves;
				}
			}
		}
		if (types.isEmpty()) {
			types.add(WolfType.ARCTIC_WOLF);
		}
		String[] names = new String[types.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = types.get(i).getFriendlyName();
		}
		setType(types.get(getRNG().nextInt(types.size())));
		return null;
	}

	@Override
	public boolean hasCustomNameTag() {
		if (!isTamed()) return false;
		ItemStack collar = getCollar();
		return collar == null ? false : collar.hasDisplayName();
	}

	@Override
	public String getCustomNameTag() {
		ItemStack collar = getCollar();
		return collar == null ? "" : collar.getDisplayName();
	}

	@Override
	public void setCustomNameTag(String name) {
		ItemStack collar = getCollar();
		if (collar == null) {
			// you're out of luck, buddy
		} else {
			collar.setStackDisplayName(name);
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
	}
	
	public SpecialWolfType getSpecialType() {
		if (hasCustomNameTag()) {
			WolfType type = getType();
			for (SpecialWolfType swt : SpecialWolfType.values()) {
				if (!swt.getApplicableBreeds().contains(type)) continue;
				if (getCustomNameTag().equals(swt.getRequiredNameTag())) return swt;
			}
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
		if (tag.hasKey("FangsItem")) {
			setFangs(ItemStack.loadItemStackFromNBT(tag.getCompoundTag("FangsItem")));
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
		ItemStack fangs = getFangs();
		if (fangs != null) {
			tag.setTag("FangsItem", fangs.writeToNBT(new NBTTagCompound()));
		} else {
			tag.removeTag("FangsItem");
		}
		tag.removeTag("CustomName");
	}

	public void openGUI(EntityPlayer player) {
        if (!this.worldObj.isRemote) {
        	if (isTamed() && getOwner() == player) {
        		player.openGui(PowerWolvesMod.inst, getEntityId(), worldObj, (int)posX, (int)posY, (int)posZ);
        	} else {
        		playSoundSafely(getAngrySound(), getSoundVolume(), getSoundPitch());
        	}
        }
    }
	
	private void playSoundSafely(String sound, float volume, float pitch) {
		if (sound != null) playSound(sound, volume, pitch);
	}

	public String getCommandSenderName() {
        return hasCustomNameTag() ? this.getCustomNameTag() : I18n.format("entity.powerwolves.wolf.name");
    }
	
	@Override
	public boolean interact(EntityPlayer p) {
		ItemStack itemstack = p.inventory.getCurrentItem();
		if (p.isSneaking()) {
			if (isTamed() && p == getOwner()) {
				openGUI(p);
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

	public void spawnTransmutationParticles() {
		for (int i = 0; i < 100; ++i) {
			double d2 = this.rand.nextGaussian() * 0.02D;
			double d0 = this.rand.nextGaussian() * 0.02D;
			double d1 = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle("witchMagic", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1);
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

	@Override
	public ItemStack getHeldItem() {
		return getFangs();
	}
	
	public float getFangDamage() {
		if (hasFangs()) {
			int dura = getFangs().getItemDamage();
			return (dura == 0 ? 2 : dura == 1 ? 0.5f : dura == 2 ? 3.5f : 0);
		}
		return 0;
	}
}
