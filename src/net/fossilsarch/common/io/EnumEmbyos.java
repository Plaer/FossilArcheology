package net.fossilsarch.common.io;

public enum EnumEmbyos {
	Pig("entity.Pig.name","fossilsarch:EmbryoSyringePig"),
	Sheep("entity.Sheep.name","fossilsarch:EmbryoSyringeSheep"),
	Cow("entity.Cow.name","fossilsarch:EmbryoSyringeCow"),
	SaberCat("entity.sabercat.name","fossilsarch:EmbryoSyringeSabreCat"),
	Mammoth("entity.mammoth.name","fossilsarch:EmbryoSyringeMammoth");
	
	private String mAnimalName;
	private String mTexture;
	
	private EnumEmbyos(String animalName, String texture) {
		this.mAnimalName = animalName;
		this.mTexture = texture;
	}
	
	public String getAnimalName() {
		return mAnimalName;
	}
	
	public String getTexture() {
		return mTexture;
	}
}
