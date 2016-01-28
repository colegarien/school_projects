// Cole Garien

package edu.uco.cgarien1.matermproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;

public class Sprite {
	Point position = new Point(0,0);
	int width = 0;
	int height = 0;
	int color = Color.WHITE;
	protected float angle = 0;
	public Sprite(){
		
	}
	public Sprite(int x, int y, int width, int height, int color){
		this.position.x = x;
		this.position.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	public Sprite(int x, int y, int width, int height, int color, float angle){
		this.position.x = x;
		this.position.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.angle = angle;
	}
	public void update(){
		
	}
	public void draw(Canvas canvas, Paint paint){
		paint.setColor(color);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(4);
		canvas.rotate(angle, position.x+width/2,position.y+height/2);
		canvas.drawRect(new Rect(position.x,position.y,position.x+width,position.y+height), paint);
		canvas.rotate(-angle, position.x+width/2,position.y+height/2);
	}
	
	public void setPosition(Point p){
		position = p;
	}
	
	public Point getPosition() {
		return position;
	}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public void setAngle(float angle){ this.angle = angle; }
	public float getAngle(){ return this.angle; }

	public Point getCenter(){
		Point out = new Point(position.x + width/2, position.y+height/2);
		return out;
	}
}
