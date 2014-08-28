package com.gameminers.powerwolves.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.minecraft.world.biome.BiomeGenBase;

import static com.gameminers.powerwolves.enums.WolfType.*;

@Getter
@FieldDefaults(level=AccessLevel.PRIVATE,makeFinal=true)
public enum SpecialWolfType {
	K9("K-9 Mark IV", "Robotic"),
	SHIZUNE("Shizune", "Silent"),
	MIKAN("Mikan", "Humanoid");
	String requiredNameTag;
	Set<WolfType> applicableBreeds;
	String description;
	private SpecialWolfType(String requiredNameTag, String description) {
		this.requiredNameTag = requiredNameTag;
		this.description = description;
		applicableBreeds = EnumSet.allOf(WolfType.class);
	}
	private SpecialWolfType(String requiredNameTag, String description, WolfType... applicableBreeds) {
		this.requiredNameTag = requiredNameTag;
		this.description = description;
		this.applicableBreeds = EnumSet.of(applicableBreeds[0], applicableBreeds);
	}
}
