package net.fossilsarch.common.io;

import net.fossilsarch.mod_Fossil;
import net.minecraft.util.StatCollector;

public enum EnumOrderType {
	Stay,Follow,FreeMove;

	public final EnumOrderType Next(){
		switch(this){
			case Stay:
				return Follow;
			case Follow:
				return FreeMove;
			case FreeMove:
				return Stay;
		}
		return FreeMove;
	}
	public final int ToInt(){
		switch(this){
		case Stay:
			return 0;
		case Follow:
			return 1;
		case FreeMove:
			return 2;
		}
		return 0;
	}
	public final String GetOrderString(){
    	return StatCollector.translateToLocal("Order."+this.toString());
    }
}