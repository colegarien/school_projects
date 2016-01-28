// Cole Garien

package edu.uco.cgarien1.matermproject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Bullet implements GameEntity{

	Sprite image;
	float vx = 0, vy  = 0;
	int damage = 10;
	float maxDistance = 100;
	float traveledDistance = 0;
	
	public Bullet(){
		this.vx = 0;
		this.vy = 0;
	}
	
	public Bullet(Sprite image,float vx, float vy, int damage){
		this.image = image;
		this.vx=vx;
		this.vy=vy;
		this.damage = damage;
	}
	
	//deprecated
	@Override
	public void update() {
		image.setPosition(new Point((int)(vx+image.getPosition().x),(int)(vy+image.getPosition().y)));
	}
	
	// destroy?
	public boolean update(float speed) {
		image.setPosition(new Point((int)(vx+image.getPosition().x),(int)(vy+image.getPosition().y)));
		traveledDistance += speed;
		return (traveledDistance>=maxDistance);
	}


	@Override
	public void draw(Canvas canvas, Paint paint) {
		image.draw(canvas, paint);
	}


	public void copy(Bullet bullet) {
		this.image = bullet.image;
		this.vx= bullet.vx;
		this.vy= bullet.vy;
		this.damage = bullet.damage;
		this.maxDistance = bullet.maxDistance;
		this.traveledDistance = bullet.traveledDistance;
	}


	public void create(Sprite image, float vx, float vy, int damage, float maxDistance) {
		this.image = image;
		this.vx=vx;
		this.vy=vy;
		this.damage = damage;
		this.maxDistance = maxDistance;
		this.traveledDistance = 0;
	}

}
