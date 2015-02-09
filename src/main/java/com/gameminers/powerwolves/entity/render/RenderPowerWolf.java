package com.gameminers.powerwolves.entity.render;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.gameminers.powerwolves.PowerWolvesMod;
import com.gameminers.powerwolves.entity.EntityPowerWolf;
import com.gameminers.powerwolves.enums.SpecialWolfType;
import com.gameminers.powerwolves.enums.WolfType;

import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class RenderPowerWolf extends RenderWolf {
	private static final ResourceLocation mikanTex = new ResourceLocation("powerwolves", "textures/entity/mikan.png");
	private static final ResourceLocation k9Tex = new ResourceLocation("powerwolves", "textures/entity/k9.png");
	private static final ResourceLocation wolfCollarTextures = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
	private static final ResourceLocation[] armorTextures = {
		new ResourceLocation("powerwolves", "textures/model/armor/leather.png"),
		new ResourceLocation("powerwolves", "textures/model/armor/chain.png"),
		new ResourceLocation("powerwolves", "textures/model/armor/iron.png"),
		new ResourceLocation("powerwolves", "textures/model/armor/gold.png"),
		new ResourceLocation("powerwolves", "textures/model/armor/diamond.png"),
		new ResourceLocation("powerwolves", "textures/model/armor/invincium.png")
	};
	
	private final ModelBiped player = new ModelBiped();
	
	public RenderPowerWolf(ModelBase mainModel, ModelBase overlayModel, float shadowSize) {
		super(mainModel, overlayModel, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity ent) {
		EntityPowerWolf wolf = ((EntityPowerWolf)ent);
		WolfType t = wolf.getType();
		SpecialWolfType st = wolf.getSpecialType();
		if (st == SpecialWolfType.K9) {
			return k9Tex;
		}
		/*if (st == SpecialWolfType.MIKAN) {
			return mikanTex;
		}*/
        return t == null ? PowerWolvesMod.wolfResources.get(WolfType.ARCTIC_WOLF) : PowerWolvesMod.wolfResources.get(t);
    }
	
	@Override
	public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
	
	@Override
	protected void renderEquippedItems(EntityLivingBase ent, float partialTicks) {
        super.renderEquippedItems(ent, partialTicks);
        EntityPowerWolf wolf = (EntityPowerWolf)ent;
        WolfType type = wolf.getType();
        Block mushroom = null;
        if (type == WolfType.BROWN_MUSHROLF) {
        	mushroom = Blocks.brown_mushroom;
        } else if (type == WolfType.RED_MUSHROLF) {
        	mushroom = Blocks.red_mushroom;
        }
        if (!ent.isChild() && mushroom != null) {
            this.bindTexture(TextureMap.locationBlocksTexture);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPushMatrix();
            ((ModelWolf)mainModel).wolfBody.postRender(0.0625F);
            GL11.glScalef(0.5F, -0.5F, 0.5F);
            GL11.glTranslatef(0f, 0f, 0.9f);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            this.field_147909_c.renderBlockAsItem(mushroom, 0, 1.0F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            ModelRenderer wolfMane = ObfuscationReflectionHelper.getPrivateValue(ModelWolf.class, ((ModelWolf)mainModel), 7);
            wolfMane.postRender(0.0625F);
            GL11.glScalef(0.5F, -0.5F, 0.5F);
            GL11.glTranslatef(0f, 0f, 1.0f);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            this.field_147909_c.renderBlockAsItem(mushroom, 0, 1.0F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            ((ModelWolf)mainModel).wolfHeadMain.postRender(0.0625F);
            GL11.glScalef(0.5F, -0.5F, 0.5F);
            GL11.glTranslatef(0f, 0.9f, 0f);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            this.field_147909_c.renderBlockAsItem(mushroom, 0, 1.0F);
            GL11.glPopMatrix();
            GL11.glDisable(GL11.GL_CULL_FACE);
        }
    }
	
	@Override
	protected int shouldRenderPass(EntityLivingBase ent, int pass, float partialTicks) {
		EntityPowerWolf wolf = (EntityPowerWolf)ent;
		/*boolean mikan = wolf.getSpecialType() == SpecialWolfType.MIKAN;
		if (mikan) {
			setRenderPassModel(player);
			GL11.glScalef(0.75f, 0.75f, 0.75f);
			player.isSneak = wolf.isSitting();
			player.isChild = wolf.isChild();
		} else {
			setRenderPassModel(mainModel);
		}*/
		if (pass == 0) {
			float brightness = wolf.getBrightness(partialTicks) * wolf.getShadingWhileShaking(partialTicks);
			bindTexture(getEntityTexture(ent));
			GL11.glColor3f(brightness, brightness, brightness);
			return 1;
		} else if (pass == 1) {
			/*if (mikan) {
				return 0;
			} else {*/
				ItemStack collar = wolf.getCollar();
				if (collar == null) return -1;
				int color;
				NBTTagCompound nbt = collar.getTagCompound();
				if (nbt != null && nbt.hasKey("Color")) {
					color = nbt.getInteger("Color");
				} else {
					color = 0xFFFFFF;
				}
				int red = color >> 16 & 255;
				int green = color >> 8 & 255;
				int blue = color & 255;
				bindTexture(wolfCollarTextures);
				float s = 1.001f;
				GL11.glScalef(s, s, s);
				GL11.glColor3f(red/255f, green/255f, blue/255f);
				return collar.hasEffect(0) ? 15 : 1;
			//}
		} else if (pass == 2) {
			if (false || wolf.hasArmor()) {
				ItemStack armor = wolf.getArmor();
				GL11.glTranslatef(-0.1f, -0.1f, -0.1f);
				GL11.glScalef(1.2f, 1.2f, 1.2f);
				bindTexture(armorTextures[PowerWolvesMod.WOLF_ARMOR.getType(armor)]);
				return armor.hasEffect(0) ? 15 : 1;
			}
		}
		return -1;
    }
}
