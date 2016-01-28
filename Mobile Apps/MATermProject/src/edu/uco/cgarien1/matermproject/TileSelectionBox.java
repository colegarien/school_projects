// Cole Garien

package edu.uco.cgarien1.matermproject;

import edu.uco.cgarien1.matermproject.Tile.TileType;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class TileSelectionBox implements GameEntity{

	private Sprite sprite;
	private boolean visible = false;
	private Point tileIndex;
	private TileType selectionType;
	private int towerIndex = -1;
	
	public TileSelectionBox(Sprite sprite){
		this.sprite = sprite;
	}
	@Override
	public void update() {
		if(visible)
			sprite.update();
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		if(visible)
			sprite.draw(canvas,paint);
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	public void setBox(Point position, Point tileIndex, TileType type) {
		this.visible = true;
		sprite.setPosition(position);
		this.tileIndex = tileIndex;
		this.selectionType = type;
	}
	public Point getPosition(){ return sprite.getPosition(); }
	public Point getTileIndex(){ return tileIndex; }
	public TileType getSelectionType(){ return selectionType; }

	public void setTowerIndex(int index){ this.towerIndex = index; }
	public int getTowerIndex(){return this.towerIndex;}
}
