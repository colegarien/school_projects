// Cole Garien

package edu.uco.cgarien1.matermproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LevelBackground {
	Bitmap staticElements;
	Canvas c;
	public LevelBackground(Bitmap image){
		staticElements = image;
		c = new Canvas(staticElements);
	}
	
	public Canvas getCanvas(){
		return c;
	}
	public void draw(Canvas canvas, Paint paint){
		paint.setColor(Color.WHITE);
		canvas.drawBitmap(staticElements, 0, 0, paint);
	}
}
