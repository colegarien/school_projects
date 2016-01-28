// Cole Garien

package edu.uco.cgarien1.program8;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class MySquare extends MyShape{

	public MySquare(float cx, float cy, float r, float vx, float vy,
			int color, Rect bounds) {
		super(cx, cy, r, vx, vy, color, bounds);
	}
	
	// Create a point on the edge of the shape toward the second shape
	@Override
	protected boolean getCollisionPoint(MyShape B){
		
		float a = cx-B.cx;
		float b = cy-B.cy;
		float ang = (float)Math.atan(b/a);
		
		// diagnoal is farthest point from center
		// 2r=[len of a side], diag =[len of a side] * sqrt(2)
		// lastly diag/2 is the farthest dist from center
		float diagLen = (float)(2f*r*Math.sqrt(2))/2f;
		
		// create a point toward the shape B
		float x = (float)(diagLen*Math.cos(ang)) + cx;
		float y = (float)(diagLen*Math.sin(ang)) + cy;

		// this makes the collide as a rectangle
		// essentially limit x and y to inside square
		x = (x>=cx+r)? (cx+r):(x<=cx-r?(cx-r):x);
		y = (y>=cy+r)? (cy+r):(y<=cy-r?(cy-r):y);
		
		return B.checkCollisionPoint(x, y);
	}
	
	// See if given point is in the object
	@Override
	protected boolean checkCollisionPoint(float x, float y){
		boolean intersect = false;
		
		// check for intersection like a square
		if(x >= cx-r && x <= cx+r && y >= cy-r && y<=cy+r)
			intersect = true;
		
		return intersect;
	}

	@Override
	public void Draw(Canvas canvas, Paint paint){
		paint.setColor(color);
		canvas.drawRect(cx-r,cy-r,cx+r,cy+r, paint);
	}
}
