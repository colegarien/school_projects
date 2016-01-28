// Cole Garien

package edu.uco.cgarien1.matermproject;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class BulletPool {
	private final int maxBullets = 100;
	private final float bulletSpeed = GameScreenActivity.toPixel(6);
	private final float bulletDimension = GameScreenActivity.toPixel(8);
	private int inuse = 0;
	private Bullet bulletPool[] = new Bullet[maxBullets];
	
	private int bulletsShot = 0;
	
	public BulletPool(){
		bulletsShot = 0;
		for(int i = 0; i < bulletPool.length; i++)
			bulletPool[i] = new Bullet();
	}
	
	public void create(Sprite image,float vx, float vy, int damage, float maxDistance){
		if(inuse < maxBullets){
			bulletPool[inuse].create(image,vx,vy, damage, maxDistance);
			inuse++;
			bulletsShot++;
		}
	}
	public void update(){
		for(int i =inuse-1; i >= 0; i--){
			update(i);
		}
	}
	public void update(int index){
		if (index >= 0 && index < inuse){
			boolean destroy = bulletPool[index].update(bulletSpeed);
			if(destroy){
				if(index<inuse-1){
					// copy value of valid one at the end of the list 
					bulletPool[index].copy(bulletPool[inuse-1]);
				}
				inuse--;
			}
		}
	}
	public void draw(Canvas canvas, Paint paint){
		for(int i =0; i < inuse; i++){
			bulletPool[i].draw(canvas, paint);
		}
	}
	public void draw(int index, Canvas canvas, Paint paint){
		if (index >= 0 && index < inuse)
			bulletPool[index].draw(canvas, paint);
	}
	
	public int getInUse(){ return inuse; }
	public float getSpeed() {return bulletSpeed;}
	public float getDimension() {return bulletDimension;}
	public Bullet[] getBulletList(){
		return bulletPool;
	}

	public RectF getBulletBounds(int i) {
		RectF out = new RectF(0,0,0,0);
		if(i>=0 && i<inuse){
			out = new RectF(bulletPool[i].image.getPosition().x,bulletPool[i].image.getPosition().y,bulletPool[i].image.getPosition().x+bulletPool[i].image.getWidth(),bulletPool[i].image.getPosition().y+bulletPool[i].image.getHeight());
		}
		return out;
	}

	public int getDamage(int i) {
		int damage = 0;
		if(i>=0 && i<inuse)
			damage = bulletPool[i].damage;
		return damage;
	}

	public void invalidateBullet(int i) {
		if(i>=0 && i<inuse){
			bulletPool[i].traveledDistance = bulletPool[i].maxDistance;
		}
	}
	
	public int getBulletsShot(){ return bulletsShot; }
}
