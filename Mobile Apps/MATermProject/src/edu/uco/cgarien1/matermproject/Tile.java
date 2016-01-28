// Cole Garien

package edu.uco.cgarien1.matermproject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Tile implements GameEntity{
	
	public static enum TileType {
	    Empty(0), Path(1), Obstacle(2), Tower(3), Obstruction(4), SlowTrap(5), DamageTrap(6);
	    private final int type;
	    TileType(int type) { this.type = type; }
	    public int getValue() { return type; }
	}

	private Sprite sprite;
	private TileType type = TileType.Empty;
	int enemycount = 0;
	
	public Tile(Sprite sprite, TileType type){
		this.sprite = sprite;
		this.type = type;
	}
	public Point getPosition(){
		return sprite.getPosition();
	}
	public Sprite getSprite(){
		return sprite;
	}
	@Override
	public void update() {
		sprite.update();
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		sprite.draw(canvas, paint);
	}
	public TileType getType() {
		return type;
	}
	public void setType(TileType type) {
		this.type = type;
	}
}
