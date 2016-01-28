// Cole Garien

package edu.uco.cgarien1.matermproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class ImageSprite extends Sprite{

	private Point coord = new Point(0,0);
	private Point rawDimension = new Point(64,64);
	
	public ImageSprite(int x, int y, int width, int height, Point coord, Point rawDimension){
		super(x, y, width, height, Color.WHITE);
		
		this.coord = coord;
		this.rawDimension = rawDimension;
	}
	public ImageSprite(int x, int y, int width, int height, Point coord, Point rawDimension, float angle){
		super(x, y, width, height, Color.WHITE, angle);
		
		this.coord = coord;
		this.rawDimension = rawDimension;
	}
	
	public void draw(Canvas canvas, Paint paint){
		paint.setColor(color);
		canvas.rotate(angle, position.x+width/2,position.y+height/2);
		canvas.drawBitmap(TileSetSingleton.getInstance(), new Rect(coord.x,coord.y,coord.x+rawDimension.x,coord.y+rawDimension.y),new Rect(position.x,position.y,position.x+width,position.y+height), paint);
		canvas.rotate(-angle, position.x+width/2,position.y+height/2);
	}
	
	public void setAngle(float angle){ this.angle = angle; }
	public float getAngle(){ return this.angle; }
}
