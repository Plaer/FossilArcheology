package net.fossilsarch.common.dinos;

import net.fossilsarch.common.io.EnumEmbyos;
import net.minecraft.entity.player.EntityPlayer;

public interface IViviparous {
	public abstract void showPedia(EntityPlayer checker);
	public abstract void SetEmbyo(EnumEmbyos target);
}
