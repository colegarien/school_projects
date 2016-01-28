// Cole Garien

package edu.uco.cgarien1.program8;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class MyCircle extends MyShape{

	public MyCircle(float cx, float cy, float r, float vx, float vy,
			int color, Rect bounds) {
		super(cx, cy, r, vx, vy, color, bounds);
	}
	
	@Override
	public void Draw(Canvas canvas, Paint paint){
		paint.setColor(color);
		canvas.drawCircle(cx, cy, r, paint);
	}
}
