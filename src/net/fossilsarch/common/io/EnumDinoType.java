package net.fossilsarch.common.io;

import net.fossilsarch.common.entity.*;

public enum EnumDinoType {
	Triceratops(EntityTriceratops.class,true,"entity.triceratops.name","fossilsarch:TriceratopsEgg","fossilsarch:TriceratopsDNA"),
	Raptor(EntityRaptor.class,false,"entity.raptor.name","fossilsarch:RaptorEgg","fossilsarch:RaptorDNA"),
	TRex(EntityTRex.class,false,"entity.trex.name","fossilsarch:TRexEgg","fossilsarch:TRexDNA"),
	Pterosaur(EntityPterosaur.class,true,"entity.pterosaur.name","fossilsarch:PterosaurEgg","fossilsarch:PterosaurDNA"),
	Nautilus(EntityNautilus.class,false,"entity.nautilus.name","fossilsarch:NautilusShell","fossilsarch:NautilusDNA"),
	Plesiosaur(EntityPlesiosaur.class,true,"entity.plesiosaur.name","fossilsarch:PlesiosaurEgg","fossilsarch:PlesiosaurDNA"),
	Mosasaurus(EntityMosasaurus.class,false,"entity.mosasaurus.name","fossilsarch:MosasaurusEgg","fossilsarch:MosasaurusDNA"),
	Stegosaurus(EntityStegosaurus.class,false,"entity.stegosaurus.name","fossilsarch:StegosaurusEgg","fossilsarch:StegosaurusDNA"),
	dilphosaur(Entitydil.class,false,"entity.dilphosaur.name","fossilsarch:dilphosaurEgg","fossilsarch:dilphosaurDNA"),
	Brachiosaurus(EntityBrachiosaurus.class,true,"entity.brachiosaurus.name","fossilsarch:BrachiosaurusEgg","fossilsarch:BrachiosaurusDNA");
	private final Class dinoClass;
	private final boolean modelable; 
	private final String dinoUnlocalized;
	private final String eggTexture;
	private final String dnaTexture;

	private EnumDinoType(Class dinoClassVar,boolean modelableVar,String dinoName, String eggTexture, String dnaTexture) {
		this.dinoClass = dinoClassVar;
		this.modelable=modelableVar;
		this.dinoUnlocalized=dinoName;
		this.eggTexture = eggTexture;
		this.dnaTexture = dnaTexture;
	}
	public Class getDinoClass() {
		return dinoClass;
	}
	public boolean isModelable() {
		return modelable;
	}
	public String getDinoName() {
		return dinoUnlocalized;
	}
	public String getEggTexture() {
		return eggTexture;
	}
	
	public String getDNATexture() {
		return dnaTexture;
	}
}
