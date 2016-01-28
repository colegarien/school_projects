// Cole Garien

package edu.uco.cgarien1.matermproject;

import java.util.ArrayList;
import java.util.Date;

import edu.uco.cgarien1.matermproject.Tile.TileType;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class Tower implements GameEntity {
	private final float fireRateMinimum = 10f;
	
	private Sprite base=new Sprite();
	private Sprite gun=new Sprite();
	private Point tileIndex;
	private Point target = null;
	
	private float maxFireDelay = 1000f;
	private float curFireDelay = 1000f;
	private int bulletDamage = 10;
	// tile radius (ie 1 == 1 tile left, 1 right, etc)
	private int targetRadius = 10;
	
	ArrayList<Tile> pathInRadius = new ArrayList<Tile>();
	int currentPath = -1;
	
	private float upgradePrice = 100f;
	private float upgradeIncrement = 0.1f;
	private int rangeIncrement = 0;
	private int damageIncrement = 10;
	private int rateIncrement = 100;
	
	public Tower(Sprite base, Sprite gun, Point tileIndex, float maxFireDelay, int bulletDamage, int targetRadius, float upgradePrice, float upgradeIncrement, int rangeIncrement, int damageIncrement, int rateIncrement){
		this.base = base;
		this.gun = gun;
		this.tileIndex = tileIndex;
		this.curFireDelay = this.maxFireDelay = maxFireDelay;
		this.bulletDamage = bulletDamage;
		this.targetRadius = targetRadius;
		this.upgradePrice = upgradePrice;
		this.upgradeIncrement = upgradeIncrement;
		this.rangeIncrement = rangeIncrement;
		this.damageIncrement = damageIncrement;
		this.rateIncrement = rateIncrement;
	}
	
	// deprecated
	@Override
	public void update() {
		if(target == null)
			gun.setAngle(gun.getAngle()+3);
		else{
			Point center = base.getCenter();
			gun.setAngle((float)(Math.atan2(target.y-center.y,target.x-center.x)*180/Math.PI));
		}
	}
	public void update(BulletPool bulletPool, float passedTime) {
		if(currentPath>-1 && pathInRadius.get(currentPath).enemycount<=0){
			target=null;
			currentPath=-1;
		}
		if(pathInRadius.size()>0 && currentPath==-1){
			for(int i =pathInRadius.size()-1; i>=0; i--){
				if(pathInRadius.get(i).enemycount>0){
					target = pathInRadius.get(i).getSprite().getCenter();
					currentPath = i;
					break;
				}else{
					currentPath = -1;
					target = null;
				}
			}
		}
		
		// firing delay
		curFireDelay += passedTime;
		//Log.i("DEBUG_TOWER_DEFENSE", "curTime: "+curTime+" curDelay: "+curFireDelay+" prevTime: "+prevTime + " cur-prev: "+(curTime-prevTime));
		
		if(target == null)
			gun.setAngle(gun.getAngle()+3);
		else{
			Point center = base.getCenter();
			gun.setAngle((float)(Math.atan2(target.y-center.y,target.x-center.x)*180/Math.PI));
			
			if(curFireDelay>=maxFireDelay){
				curFireDelay = 0;
				float hypot = (float)Math.sqrt(Math.pow(target.x-center.x, 2)+Math.pow(target.y-center.y, 2));
				bulletPool.create(new Sprite((int)(center.x-bulletPool.getDimension()/2f),(int)(center.y-bulletPool.getDimension()/2f),(int)bulletPool.getDimension(),(int)bulletPool.getDimension(),Color.YELLOW), (((target.x-center.x)/hypot)*bulletPool.getSpeed()),(((target.y-center.y)/hypot)*bulletPool.getSpeed()), bulletDamage, hypot+bulletPool.getDimension()*2);
			}
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		base.draw(canvas, paint);
		if(target!=null){
			paint.setColor(Color.RED);
			canvas.drawLine(base.getCenter().x, base.getCenter().y, target.x, target.y, paint);
		}
		gun.draw(canvas, paint);
	}
	
	public void setAngle(float angle){
		gun.setAngle(angle);
	}
	
	public Point getPosition(){return base.getPosition();}
	public Point getTileIndex(){ return tileIndex; }
	public Point getTarget(){ return target; }
	public void setTarget(Point target){ this.target = target; }
	
	public void seekPathTargets(Tile[][] tileMap){
		for (int i = -targetRadius; i<=targetRadius; i++)
			for (int j = -targetRadius; j<=targetRadius; j++){
				int x = tileIndex.x + i;
				int y = tileIndex.y + j;
				if(x>=0 && y>=0 && x< tileMap.length && y<tileMap.length){
					Tile curTile = tileMap[x][y];
					if(curTile.getType() != TileType.Obstruction && curTile.getType() != TileType.Tower && curTile.getType() != TileType.Empty)
						pathInRadius.add(curTile);
				}
			}
	}

	public float getUpgradeCost() {
		return upgradePrice;
	}

	public void upgrade(Tile[][] tileMap) {
		maxFireDelay -= rateIncrement;
		if(maxFireDelay < fireRateMinimum)
			maxFireDelay = fireRateMinimum;
		bulletDamage += damageIncrement;
		targetRadius += rangeIncrement;
		if(rangeIncrement>0)
			seekPathTargets(tileMap);
		upgradePrice += upgradePrice*upgradeIncrement;
	}

	public float getSellPrice() {
		return upgradePrice * .75f;
	}
}
