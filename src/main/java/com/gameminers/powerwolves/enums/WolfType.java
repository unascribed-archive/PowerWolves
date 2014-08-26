package com.gameminers.powerwolves.enums;

import java.util.ArrayList;
import java.util.List;

import gminers.kitchensink.Strings;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.world.biome.BiomeGenBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import static net.minecraft.world.biome.BiomeGenBase.*;

@Getter
public enum WolfType {
	ARCTIC_WOLF			("minecraft:textures/entity/wolf/wolf.png"			/* arctic wolf spawn areas are implied at runtime */),
	GOLDEN_RETRIEVER	("powerwolves:textures/entity/golden_retriever.png",plains, savanna, savannaPlateau, extremeHills, extremeHillsPlus),
	ALASKAN_HUSKY		("powerwolves:textures/entity/alaskan_husky.png",	icePlains, taiga, taigaHills),
	CHOCOLATE_LABRADOR	("powerwolves:textures/entity/chocolate_lab.png",	forest, forestHills, roofedForest, birchForest, birchForestHills),
	GERMAN_SHEPHERD		("powerwolves:textures/entity/german_shepherd.png",	forest, forestHills, birchForest, birchForestHills),
	SHIBA_INU			("powerwolves:textures/entity/shiba.png",			plains, savanna, savannaPlateau, forest),
	RED_MUSHROLF		("powerwolves:textures/entity/red_mushrolf.png",	mushroomIsland, mushroomIslandShore),
	BROWN_MUSHROLF		("powerwolves:textures/entity/brown_mushrolf.png",	mushroomIsland, mushroomIslandShore),
	;
	private final String texture;
	private final BiomeGenBase[] biomes;
	private WolfType(String texture, BiomeGenBase... biomes) {
		this.texture = texture;
		this.biomes = biomes;
	}
	public String getFriendlyName() {
		return Strings.formatTitleCase(name().replace("_", " "));
	}
	public static void printSpawnConditions() {
		for (WolfType w : values()) {
			System.out.print(w.getFriendlyName());
			if (w.getBiomes().length > 0) {
				System.out.print(" spawns in ");
				String[] biomes = new String[w.getBiomes().length];
				for (int i = 0; i < biomes.length; i++) {
					biomes[i] = w.getBiomes()[i].biomeName;
				}
				System.out.println(Strings.formatList(biomes));
			} else {
				System.out.println(" spawns are dependant on other installed mods");
			}
		}
	}
}
