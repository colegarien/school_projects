// Cole Garien

package edu.uco.cgarien1.matermproject;

import java.util.ArrayList;

import edu.uco.cgarien1.matermproject.Tile.TileType;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Enemy implements GameEntity{
	
	int maxHealth = 50;
	int health = 50;
	Point velocity=new Point(0,0);
	int maxSpeed = 10;
	int speed = 10;
	Sprite image=new Sprite();
	int curNode = -1;
	int nxtNode = -1;
	TileType curType=TileType.Path,nxtType=TileType.Path;
	
	public Enemy(Sprite image,int speed, int health, int curNode, int nxtNode,ArrayList<PathNode> path){
		this.create(image,speed, health,curNode,nxtNode,path);
	}

	public Enemy() {
		
	}

	public void create(Sprite image,int speed, int health, int curNode, int nxtNode,ArrayList<PathNode> path){
		this.curNode = curNode;
		this.nxtNode = nxtNode;
		this.speed = this.maxSpeed = speed;
		this.image = image;
		this.maxHealth = this.health = health;
		curType=TileType.Path;
		nxtType=TileType.Path;
		calculateVelocity(path);
	}
	private void calculateVelocity(ArrayList<PathNode> path){
		PathNode cur = path.get(curNode);
		PathNode nxt = path.get(nxtNode);
		velocity = new Point(nxt.getCenter().x-cur.getCenter().x,nxt.getCenter().y-cur.getCenter().y);
		if(velocity.x!=0)
			velocity.x = (velocity.x/Math.abs(velocity.x))*speed;
		if(velocity.y!=0)
			velocity.y = (velocity.y/Math.abs(velocity.y))*speed;
	}
	
	@Override
	public void update() {
	}
	
	// boolean: destroy this?
	public boolean update(ArrayList<PathNode> path, Tile[][] tileMap) {
		if(nxtNode != -1){
			PathNode nxt = path.get(nxtNode);
			image.setPosition(new Point (velocity.x+image.getPosition().x,velocity.y+image.getPosition().y));
			float dist = (float)Math.hypot(nxt.getCenter().x-(image.getPosition().x+image.getWidth()/2), nxt.getCenter().y-(image.getPosition().y+image.getHeight()/2));
			
			if(dist<speed){
				// nodes move forward
				curNode = nxtNode;
				nxtNode = nxt.getNextNode();
				// at end of path
				if (nxtNode == -1){
					return true;
				}
				// enemy is on new tile
				Point curNodeIndex = path.get(curNode).getTile();
				Point nxtNodeIndex = path.get(nxtNode).getTile();
				
				if(curNodeIndex.x>=0 && curNodeIndex.x < tileMap.length&&curNodeIndex.y>=0 && curNodeIndex.y < tileMap.length){
					curType = tileMap[curNodeIndex.x][curNodeIndex.y].getType();
					tileMap[curNodeIndex.x][curNodeIndex.y].enemycount--;
				}else
					curType = TileType.Path;
				if(nxtNodeIndex.x>=0 && nxtNodeIndex.x < tileMap.length&&nxtNodeIndex.y>=0 && nxtNodeIndex.y < tileMap.length){
					nxtType = tileMap[nxtNodeIndex.x][nxtNodeIndex.y].getType();
					tileMap[nxtNodeIndex.x][nxtNodeIndex.y].enemycount++;
				}else
					nxtType = TileType.Path;
					
				if(curType == TileType.SlowTrap || nxtType == TileType.SlowTrap)
					speed = maxSpeed/2;
				else
					speed = maxSpeed;
				
				calculateVelocity(path);
			}
			if(curType==TileType.DamageTrap || nxtType==TileType.DamageTrap)
				health--;
			
			if(health <= 0){
				Point nxtNodeIndex = path.get(nxtNode).getTile();
				if(nxtNodeIndex.x>=0 && nxtNodeIndex.x < tileMap.length&&nxtNodeIndex.y>=0 && nxtNodeIndex.y < tileMap.length)
					tileMap[nxtNodeIndex.x][nxtNodeIndex.y].enemycount--;
				return true;
			}
			
			return false;
		}
		return true;
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		if(nxtNode != -1)
			image.draw(canvas, paint);
	}

	public void copy(Enemy enemy) {
		this.image = enemy.image;
		this.speed = enemy.speed;
		this.curNode = enemy.curNode;
		this.nxtNode = enemy.nxtNode;
		this.velocity = enemy.velocity;
		this.maxHealth = enemy.maxHealth;
		this.health = enemy.health;
		this.curType= enemy.curType;
		this.nxtType= enemy.nxtType;
	}

}
