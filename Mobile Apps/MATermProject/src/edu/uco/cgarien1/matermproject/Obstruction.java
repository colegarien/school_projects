// Cole Garien

package edu.uco.cgarien1.matermproject;

import android.graphics.Point;

public class Obstruction {
	Point tileIndex = new Point(0,0);
	public Obstruction(Point point){
		tileIndex = point;
	}
	public Point getTile() {return tileIndex;}

}
