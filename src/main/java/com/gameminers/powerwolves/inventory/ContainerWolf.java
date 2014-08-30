package com.gameminers.powerwolves.inventory;

import com.gameminers.powerwolves.PowerWolvesMod;
import com.gameminers.powerwolves.entity.EntityPowerWolf;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;

public class ContainerWolf extends Container
{
    private IInventory wolfInventory;
    private EntityPowerWolf wolf;
    private static final String __OBFID = "CL_00001751";

    public ContainerWolf(IInventory bottomInventory, final IInventory topInventory, final EntityPowerWolf wolf)
    {
        this.wolfInventory = topInventory;
        this.wolf = wolf;
        topInventory.openInventory();
        this.addSlotToContainer(new Slot(topInventory, 0, 8, 18) {
        	public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == PowerWolvesMod.COLLAR;
            }
        	@Override
        	public void onSlotChanged() {
        		wolf.setCollar(getStack());
        	}
        	@Override
        	public int getSlotStackLimit() {
        		return 1;
        	}
        });
        addSlotToContainer(new Slot(topInventory, 1, 8, 36) {
        	public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == PowerWolvesMod.FANGS;
            }
        	@Override
        	public void onSlotChanged() {
        		wolf.setFangs(getStack());
        	}
        	@Override
        	public int getSlotStackLimit() {
        		return 1;
        	}
        });
        addSlotToContainer(new Slot(topInventory, 2, 8, 54) {
        	public boolean isItemValid(ItemStack stack) {
                return isChestplate(stack) && !this.getHasStack();
            }
			private boolean isChestplate(ItemStack stack) {
				if (stack.getItem() instanceof ItemArmor) {
					return ((ItemArmor)stack.getItem()).armorType == 1;
				}
				return false;
			}
			@Override
        	public int getSlotStackLimit() {
        		return 1;
        	}
        });
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(bottomInventory, k + j * 9 + 9, 8 + k * 18, 102 + j * 18 - 18));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(bottomInventory, j, 8 + j * 18, 160 - 18));
        }
    }

    public boolean canInteractWith(EntityPlayer p) {
        return this.wolfInventory.isUseableByPlayer(p) && wolf.isEntityAlive() && wolf.getDistanceToEntity(p) < 8.0F;
    }

    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(p_82846_2_);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (p_82846_2_ < wolfInventory.getSizeInventory())
            {
                if (!mergeItemStack(itemstack1, wolfInventory.getSizeInventory(), inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (getSlot(1).isItemValid(itemstack1) && !getSlot(1).getHasStack())
            {
                if (!mergeItemStack(itemstack1, 1, 2, false))
                {
                    return null;
                }
            }
            else if (getSlot(0).isItemValid(itemstack1))
            {
                if (!mergeItemStack(itemstack1, 0, 1, false))
                {
                    return null;
                }
            }
            else if (wolfInventory.getSizeInventory() <= 2 || !mergeItemStack(itemstack1, 2, wolfInventory.getSizeInventory(), false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        wolfInventory.closeInventory();
    }
}