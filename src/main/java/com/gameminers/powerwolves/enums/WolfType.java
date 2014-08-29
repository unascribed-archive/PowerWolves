package com.gameminers.powerwolves.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gminers.kitchensink.Strings;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.world.biome.BiomeGenBase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import static net.minecraft.world.biome.BiomeGenBase.*;

@Getter
@FieldDefaults(level=AccessLevel.PRIVATE,makeFinal=true)
public enum WolfType {
	ARCTIC_WOLF			(0xD6D2D2, 0x97918D, "minecraft:textures/entity/wolf/wolf.png"			/* arctic wolf spawn areas are implied at runtime */),
	GOLDEN_RETRIEVER	(0xB99124, 0xEAD498, "powerwolves:textures/entity/golden_retriever.png",plains, savanna, savannaPlateau, extremeHills, extremeHillsPlus),
	ALASKAN_HUSKY		(0x222222, 0xFFFFFF, "powerwolves:textures/entity/alaskan_husky.png",	icePlains, taiga, taigaHills),
	CHOCOLATE_LAB		(0x462E08, 0x281900, "powerwolves:textures/entity/chocolate_lab.png",	forest, forestHills, roofedForest, birchForest, birchForestHills),
	GERMAN_SHEPHERD		(0xB08A22, 0x3A3535, "powerwolves:textures/entity/german_shepherd.png",	forest, forestHills, birchForest, birchForestHills),
	SHIBA_INU			(0x9D5934, 0xE4D8D9, "powerwolves:textures/entity/shiba.png",			plains, savanna, savannaPlateau, forest),
	RED_MUSHROLF		(0xB12234, 0xFFFFFF, "powerwolves:textures/entity/red_mushrolf.png",	mushroomIsland, mushroomIslandShore),
	BROWN_MUSHROLF		(0x9D7352, 0xDFC7B4, "powerwolves:textures/entity/brown_mushrolf.png",	mushroomIsland, mushroomIslandShore),
	;
	String texture;
	List<BiomeGenBase> biomes;
	String friendlyName;
	int primaryColor;
	int secondaryColor;
	
	private WolfType(int primaryColor, int secondaryColor, String texture, BiomeGenBase... biomes) {
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		this.texture = texture;
		this.biomes = Collections.unmodifiableList(Arrays.asList(biomes));
		this.friendlyName = Strings.formatTitleCase(name().replace("_", " "));
	}
	public static void printSpawnConditions() {
		for (WolfType w : values()) {
			System.out.print(w.getFriendlyName());
			if (w.getBiomes().size() > 0) {
				System.out.print(" spawns in ");
				String[] biomes = new String[w.getBiomes().size()];
				for (int i = 0; i < biomes.length; i++) {
					biomes[i] = w.getBiomes().get(i).biomeName;
				}
				System.out.println(Strings.formatList(biomes));
			} else {
				System.out.println(" spawns are dependant on other installed mods");
			}
		}
	}
}
