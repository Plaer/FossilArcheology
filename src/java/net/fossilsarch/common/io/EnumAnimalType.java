package net.fossilsarch.common.io;

public enum EnumAnimalType {
	
	Pig("entity.Pig.name", "fossilsarch:PigDNA"),
	Sheep("entity.Sheep.name","fossilsarch:SheepDNA"),
	Cow("entity.Cow.name","fossilsarch:CowDNA"),
	Chicken("entity.Chicken.name", "fossilsarch:ChickenDNA"),
	SaberCat("entity.sabercat.name","fossilsarch:SaberCatDNA"),
	Mammoth("entity.mammoth.name","fossilsarch:MammothDNA");
	
	private String mAnimalName;
	private String mDNATexture;
	
	private EnumAnimalType(String animalName, String dnaTexture) {
		mAnimalName = animalName;
		mDNATexture = dnaTexture;
	}
	
	public String getAnimalName() {
		return mAnimalName;
	}
	
	public String getDNATexture() {
		return mDNATexture;
	}
}

