// Cole Garien

package edu.uco.cgarien1.program8;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class MyShape {
	
	// center of shape
	protected float cx = 0, cy = 0;
	// radius
	protected float r = 0;
	// velocity
	protected float vx = 0, vy = 0;
	// color
	protected int color = Color.WHITE;
	// play area
	protected Rect bounds = new Rect(0,0,0,0);
	
	public MyShape(float cx, float cy, float r, float vx, float vy, int color, Rect bounds){
		this.cx = cx;
		this.cy = cy;
		this.r = r;
		this.vx = vx;
		this.vy = vy;
		this.color = color;
		this.bounds = bounds;
	}
	
	public void Update(){
		if(cx+vx-r<bounds.left || cx+vx+r > bounds.right)
			vx = -vx;
		else
			cx+=vx;

		if(cy+vy-r<bounds.top || cy+vy+r > bounds.bottom)
			vy = -vy;
		else
			cy+=vy;
	}
	
	public void Draw(Canvas canvas, Paint paint){
		paint.setColor(color);
		canvas.drawLine(cx-r, cy, cx+r, cy, paint);
		canvas.drawLine(cx, cy-r, cx, cy+r, paint);
	}
	
	// default behavior is like a circle
	// Create a point on the edge of the shape toward the second shape
	protected boolean getCollisionPoint(MyShape B){
		
		float a = cx-B.cx;
		float b = cy-B.cy;
		float ang = (float)Math.atan(b/a);
		
		float x = (float)(r*Math.cos(ang)) + cx;
		float y = (float)(r*Math.sin(ang)) + cy;
		
		return B.checkCollisionPoint(x, y);
	}
	
	// default behavior is like a circle
	// See if given point is in the object
	protected boolean checkCollisionPoint(float x, float y){
		boolean intersect = false;
		
		if(Math.sqrt(Math.pow(x-cx, 2)+Math.pow(y-cy,2)) <= r)
			intersect = true;
		
		return intersect;
	}
	
	public static void Collision(MyShape A, MyShape B){
		boolean collision = A.getCollisionPoint(B);
		// if collide then swap velocities
		if(collision){
			float tempVx = A.vx;
			float tempVy = A.vy;
			A.vx = B.vx;
			A.vy = B.vy;
			A.Update();
			B.vx = tempVx;
			B.vy = tempVy;
			B.Update();
		}
	}
}
