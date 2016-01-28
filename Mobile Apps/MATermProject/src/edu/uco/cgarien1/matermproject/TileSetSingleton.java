// Cole Garien

package edu.uco.cgarien1.matermproject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

// Only one copy of the tile set is stored
public class TileSetSingleton{
	private static volatile Bitmap instance = null;
	private TileSetSingleton(){}
	public static Bitmap getInstance(){
		if(instance ==null){
			synchronized (TileSetSingleton.class){
				if(instance == null){
					instance = Bitmap.createBitmap(0,0,Config.ARGB_8888);
				}
			}
		}
		
		return instance;
	}
	public static void setBitmap(Bitmap tileSet){
		instance = tileSet;
	}
}
