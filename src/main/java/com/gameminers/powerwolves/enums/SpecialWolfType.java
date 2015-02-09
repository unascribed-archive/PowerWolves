package com.gameminers.powerwolves.enums;

import java.util.EnumSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level=AccessLevel.PRIVATE,makeFinal=true)
public enum SpecialWolfType {
	K9("K-9 Mark IV", "Robotic"), // Blame Drayko
	SHIZUNE("Shizune", "Silent"), // This one's my fault
	MIKAN("Mikan", "Humanoid"), // Blame Science
	BLANK("[          ]", "Split") // Also Science
	;
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
