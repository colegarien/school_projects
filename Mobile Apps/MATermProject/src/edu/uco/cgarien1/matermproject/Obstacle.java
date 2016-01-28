// Cole Garien

package edu.uco.cgarien1.matermproject;

import edu.uco.cgarien1.matermproject.Tile.TileType;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Obstacle implements GameEntity {
	private Sprite image=new Sprite();
	private Point tileIndex;
	private TileType type;
	
	public Obstacle(Sprite image, Point tileIndex, TileType type){
		this.image = image;
		this.tileIndex = tileIndex;
		this.type=type;
	}
	
	@Override
	public void update() {}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		image.draw(canvas, paint);
	}
	
	public Point getTileIndex(){ return tileIndex; }
	public TileType getType() {
		return type;
	}
}
