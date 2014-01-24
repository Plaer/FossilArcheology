package net.fossilsarch.client;

import java.io.File;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;



public class DinoSoundHandler  {

	@ForgeSubscribe
	public void onSound(SoundLoadEvent event){
		event.manager.addSound("fossilsarch:Brach_death1.ogg");
		event.manager.addSound("fossilsarch:Brach_death2.ogg");
		event.manager.addSound("fossilsarch:Brach_living1.ogg");
		event.manager.addSound("fossilsarch:Brach_living2.ogg");
		event.manager.addSound("fossilsarch:DiloCall1.ogg");
		event.manager.addSound("fossilsarch:DiloCall2.ogg");
		event.manager.addSound("fossilsarch:DiloDeath.ogg");
		event.manager.addSound("fossilsarch:DiloHurt.ogg");
		event.manager.addSound("fossilsarch:DiloLiving.ogg");
		event.manager.addSound("fossilsarch:drum_single.ogg");
		event.manager.addSound("fossilsarch:drum_triple.ogg");
		event.manager.addSound("fossilsarch:Mammoth_death.ogg");
		event.manager.addSound("fossilsarch:Mammoth_hurt.ogg");
		event.manager.addSound("fossilsarch:Mammoth_living.ogg");
		event.manager.addSound("fossilsarch:Pls_hurt.ogg");
		event.manager.addSound("fossilsarch:Pls_Living.ogg");
		event.manager.addSound("fossilsarch:PTS_hurt.ogg");
		event.manager.addSound("fossilsarch:PTS_living1.ogg");
		event.manager.addSound("fossilsarch:PTS_living2.ogg");
		event.manager.addSound("fossilsarch:Raptor_attack1.ogg");
		event.manager.addSound("fossilsarch:Raptor_attack2.ogg");
		event.manager.addSound("fossilsarch:Raptor_death.ogg");
		event.manager.addSound("fossilsarch:Raptor_hurt1.ogg");
		event.manager.addSound("fossilsarch:Raptor_hurt2.ogg");
		event.manager.addSound("fossilsarch:Raptor_hurt3.ogg");
		event.manager.addSound("fossilsarch:Raptor_living_friendly1.ogg");
		event.manager.addSound("fossilsarch:Raptor_living_friendly2.ogg");
		event.manager.addSound("fossilsarch:Raptor_living_wild1.ogg");
		event.manager.addSound("fossilsarch:Raptor_living_wild2.ogg");
		event.manager.addSound("fossilsarch:SaberCat_death.ogg");
		event.manager.addSound("fossilsarch:SaberCat_Hurt.ogg");
		event.manager.addSound("fossilsarch:SaberCat_Living1.ogg");
		event.manager.addSound("fossilsarch:SaberCat_Living2.ogg");
		event.manager.addSound("fossilsarch:SaberCat_Living3.ogg");
		event.manager.addSound("fossilsarch:Steg_death.ogg");
		event.manager.addSound("fossilsarch:Steg_Hurt.ogg");
		event.manager.addSound("fossilsarch:Steg_living1.ogg");
		event.manager.addSound("fossilsarch:Steg_living2.ogg");
		event.manager.addSound("fossilsarch:Steg_living3.ogg");
		event.manager.addSound("fossilsarch:TRex_Death1.ogg");
		event.manager.addSound("fossilsarch:TRex_Death2.ogg");
		event.manager.addSound("fossilsarch:TRex_hit.ogg");
		event.manager.addSound("fossilsarch:TRex_Living1.ogg");
		event.manager.addSound("fossilsarch:TRex_Living2.ogg");
		event.manager.addSound("fossilsarch:TRex_Living3.ogg");
		event.manager.addSound("fossilsarch:TRex_scream1.ogg");
		event.manager.addSound("fossilsarch:TRex_scream2.ogg");
		event.manager.addSound("fossilsarch:TRex_scream3.ogg");
		event.manager.addSound("fossilsarch:tri_death.ogg");
		event.manager.addSound("fossilsarch:tri_roar1.ogg");
		event.manager.addSound("fossilsarch:tri_roar2.ogg");
		event.manager.addSound("fossilsarch:tri_roar3.ogg");
	}

}
