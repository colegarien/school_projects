// Cole Garien

package edu.uco.cgarien1.matermproject;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class EnemyPool {
	private final int dimension = (int)GameScreenActivity.toPixel(10);
	private final int speed = (int)GameScreenActivity.toPixel(4);
	private final int maxEnemies = 25;
	private int inuse = 0;
	private Enemy enemyPool[] = new Enemy[maxEnemies];
	
	private int enemyKilled = 0;
	private int enemyLived = 0;
	
	float maxSpawnDelay = 200f;
	float curSpawnDelay = 200f;
	float maxIntermission = 4000f;
	float curIntermission = 4000f;
	int enemiesThisWave = 1;
	int spawnedEnemies = 0;
	int enemyHealth = 10;
	
	public EnemyPool(){
		enemyKilled = 0;
		enemyLived = 0;
		for(int i = 0; i < enemyPool.length; i++)
			enemyPool[i] = new Enemy();
	}
	
	public boolean create(Sprite image,int speed, int health, int curNode, int nxtNode,ArrayList<PathNode> path){
		if(inuse < maxEnemies){
			enemyPool[inuse].create(image,speed, health,curNode,nxtNode,path);
			inuse++;
			return true;
		}
		return false;
	}
	public float update(ArrayList<PathNode> path, Tile[][] tileMap){
		float totalReward = 0;
		
		for(int i =inuse-1; i >= 0; i--){
			totalReward += update(i,path,tileMap);
		}
		
		return totalReward;
	}
	public float update(int index, ArrayList<PathNode> path, Tile[][] tileMap){
		float reward = 0;
		
		if(index >= 0 && index < inuse){
			boolean destroy = enemyPool[index].update(path, tileMap);
			if(destroy){
				if(enemyPool[index].health<=0){
					enemyKilled++;
					reward += enemyPool[index].maxHealth;
				}else
					enemyLived++;
				if(index<inuse-1){
					// copy value of valid one at the end of the list 
					enemyPool[index].copy(enemyPool[inuse-1]);
				}
				inuse--;
			}
		}
		
		return reward;
	}
	public void draw(Canvas canvas, Paint paint){
		for(int i =0; i < inuse; i++){
			draw(i, canvas,paint);
		}
	}
	public void draw(int index, Canvas canvas, Paint paint){
		if (index >= 0 && index < inuse)
			enemyPool[index].draw(canvas, paint);
	}
	
	public int getInUse(){ return inuse; }
	public Enemy[] getEnemyList(){
		return enemyPool;
	}

	public RectF getEnemyBounds(int i) {
		RectF out = new RectF(0,0,0,0);
		if(i>=0 && i<inuse){
			out = new RectF(enemyPool[i].image.getPosition().x,enemyPool[i].image.getPosition().y,enemyPool[i].image.getPosition().x+enemyPool[i].image.getWidth(),enemyPool[i].image.getPosition().y+enemyPool[i].image.getHeight());
		}
		return out;
	}

	public void takeDamage(int i, int damage) {
		if(i>=0 && i<inuse){
			enemyPool[i].health -= damage;
		}
	}
	
	// is changing to intermission?
	public boolean enemyGenerator(long passedTime, int startNodes, ArrayList<PathNode> path){
		// if in intermission
		if(maxIntermission>curIntermission){
			curIntermission += passedTime;
		}else{
			curSpawnDelay += passedTime;
			if(curSpawnDelay>=maxSpawnDelay && enemiesThisWave > spawnedEnemies){
				int startInd = (int)(Math.random()*startNodes);
				if(create(new Sprite(path.get(startInd).getCenter().x-dimension/2,path.get(startInd).getCenter().y-dimension/2,dimension,dimension,Color.RED), speed, enemyHealth, startInd,path.get(startInd).getNextNode(), path)){
					curSpawnDelay = 0;
					spawnedEnemies++;
				}
			}else if(inuse <= 0){
				curIntermission = 0;
				spawnedEnemies = 0;
				enemiesThisWave *= 2;
				curSpawnDelay=maxSpawnDelay;
				enemyHealth += 2;
				
				// change to intermission mode
				return true;
			}
		}
		return false;
	}
	
	public int getKilled() { return enemyKilled; }
	public int getLived() { return enemyLived; }
}
