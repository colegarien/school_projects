// Cole Garien

package edu.uco.cgarien1.matermproject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import edu.uco.cgarien1.matermproject.Tile.TileType;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.Toast;

public class Level{
	private final int tileDimension =(int)GameScreenActivity.toPixel(32);
	final int allowedEnemies = 10;
	//   delay, damage, range, upgrade price, price increm, range incre, damage increm, rate increm
	private final float[][] towerSpecs = new float[][]{
		{1000f,10,1,100,0.3f,0,10,50}, // tower 1
		{800f,10,1,110,0.4f,0,10,200}, // tower 2
		{1000f,10,2,300,0.1f,0,10,100}, // tower 3
		{2000f,10,1,300,0.3f,1,10,10}, // tower 4
		{1000f,1,1,0,0.1f,6,100,1000}  // tower 5
	};
	
	Tile[][] tileMap;
	LevelBackground background;
	int mapWidth = 0;
	int mapHeight = 0;
	
	int endNodes = 0;
	int startNodes = 0;
	//Enemy sandwich;
	
	ArrayList<PathNode> path = new ArrayList<PathNode>();
	ArrayList<Obstruction> obstructions = new ArrayList<Obstruction>();
	
	EnemyPool enemyPool;
	BulletPool bulletPool;
	ArrayList<Tower> towerList = new ArrayList<Tower>();
	ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>();
	
	TileSelectionBox selectionBox;
	long previousTime = 0;
	
	float wallet = 300.27f;
	
	int towersBought = 0;
	int towersSold = 0;
	int trapsBought = 0;
	float moneySpent = 0;
	
	// flag is for one-time only gameover check
	boolean flag = true;
	
	public Level(Bitmap level, Paint paint){
		
		// load code
		int levelWidth = level.getWidth();
		int levelHeight = level.getHeight();
		//int dimension = 
		mapWidth = levelWidth-2;
		mapHeight = levelHeight-2;
		tileMap = new Tile[mapWidth][mapHeight];
		//background = new LevelBackground(new Sprite(dimension,dimension,mapWidth*dimension,mapHeight*dimension,Color.GREEN));
		//background = new LevelBackground(new ImageSprite(dimension,dimension,mapWidth*dimension,mapHeight*dimension,new Point(0,128), new Point(128,128), "tileset"));
		background = new LevelBackground(Bitmap.createBitmap(levelWidth*tileDimension,levelHeight*tileDimension,Config.ARGB_8888));
		
		Random random = new Random();
		
		for(int x = 0; x < levelWidth; x++)
			for(int y = 0; y < levelHeight; y++){
				int pX = x*tileDimension;
				int pY = y*tileDimension;
				int pixelColor = level.getPixel(x,y);
				if(x==0||y==0||x==levelWidth-1||y==levelHeight-1){
					if(pixelColor == Color.rgb(0, 255, 0)){
						startNodes++;
						path.add(0,new PathNode(pX+tileDimension/2,pY+tileDimension/2,tileDimension/2, new Point(x-1,y-1)));
					}else if(pixelColor == Color.rgb(255,0,0)){
						endNodes++;
						path.add(path.size(),new PathNode(pX+tileDimension/2,pY+tileDimension/2,tileDimension/2, new Point(x-1,y-1)));
					}
				}else{
					if(pixelColor == Color.rgb(0,0,0)){
						//tileMap[x-1][y-1] = new Tile(new Sprite(pX,pY,dimension,dimension,Color.GREEN),0);
						tileMap[x-1][y-1] = new Tile(new ImageSprite(pX,pY,tileDimension,tileDimension, new Point(random.nextInt(11)*64,128), new Point(64,64)),Tile.TileType.Empty);
					}else if(pixelColor == Color.rgb(255, 255, 255)){
						// add boulder here
						//tileMap[x-1][y-1] = new Tile(new Sprite(pX,pY,dimension,dimension,pixelColor),0);
						tileMap[x-1][y-1] = new Tile(new ImageSprite(pX,pY,tileDimension,tileDimension, new Point(random.nextInt(11)*64,64), new Point(64,64)),Tile.TileType.Obstruction);
						obstructions.add(new Obstruction(new Point(x-1,y-1)));
					}else{
						Point textCoord = getPathTexture(new Point(x,y), level);
						
						//tileMap[x-1][y-1] = new Tile(new Sprite(pX,pY,dimension,dimension,pixelColor),0);
						tileMap[x-1][y-1] = new Tile(new ImageSprite(pX,pY,tileDimension,tileDimension, textCoord, new Point(64,64)),Tile.TileType.Path);
						path.add(startNodes,new PathNode(pX+tileDimension/2,pY+tileDimension/2,tileDimension/2, new Point(x-1,y-1)));
					}
				}
			}
		
		// connect path up
		for(int i = 0; i < startNodes; i ++)
			createPath(path.get(i), level);
		
		// draw static things
		for(int x = 0; x<mapWidth; x++)
			for(int y = 0; y<mapHeight; y++)
				tileMap[x][y].draw(background.getCanvas(), paint);
		
		selectionBox = new TileSelectionBox(new Sprite(0,0,tileDimension,tileDimension,Color.BLUE));
		
		enemyPool=new EnemyPool();
		bulletPool=new BulletPool();
	}
	
	private Point getPathTexture(Point current, Bitmap level){
		int currentColor= level.getPixel(current.x,current.y);
		Point textCoord = new Point(0,0);
		
		boolean up = (false), 
				down = (false), 
				left = (false), 
				right = (false);
		if(current.x-1>0){
			int leftcolor = level.getPixel(current.x-1, current.y);
			if(leftcolor == Color.rgb(255, 150, 0) || leftcolor == Color.rgb(0, 0, 255))
				left= true;
			else if (currentColor == Color.rgb(0,255,255))
				left = true;
			else if (currentColor == Color.rgb(255,150,0) && (leftcolor != Color.BLACK && leftcolor!=Color.WHITE && leftcolor !=Color.rgb(150, 150, 150)))
				left= true;
		}
		if(current.x+1<level.getWidth()){
			int rightcolor = level.getPixel(current.x+1, current.y);
			if(rightcolor == Color.rgb(255, 150, 0) || rightcolor == Color.rgb(0, 255, 255))
				right= true;
			else if (currentColor == Color.rgb(0,0,255))
				right = true;
			else if (currentColor == Color.rgb(255,150,0) && (rightcolor != Color.BLACK && rightcolor!=Color.WHITE && rightcolor !=Color.rgb(150, 150, 150)))
				right = true;
		}
		if(current.y-1>=0){
			int upcolor = level.getPixel(current.x, current.y-1);
			if(upcolor == Color.rgb(255, 150, 0) || upcolor == Color.rgb(255, 0, 255))
				up= true;
			else if (currentColor == Color.rgb(255,255,0))
				up = true;
			else if (currentColor == Color.rgb(255,150,0) && (upcolor != Color.BLACK && upcolor!=Color.WHITE && upcolor !=Color.rgb(150, 150, 150)))
				up = true;
		}
		if(current.y+1<level.getHeight()){
			int downcolor = level.getPixel(current.x, current.y+1);
			if(downcolor == Color.rgb(255, 150, 0) || downcolor == Color.rgb(255, 255, 0))
				down= true;
			else if (currentColor == Color.rgb(255,0,255))
				down = true;
			else if (currentColor == Color.rgb(255,150,0) && (downcolor != Color.BLACK && downcolor!=Color.WHITE && downcolor !=Color.rgb(150, 150, 150)))
				down = true;
		}
		
		if(up&&down&&left&&right)
			textCoord.x = 10*64;
		else if(down&&left&&right)
			textCoord.x = 9*64;
		else if(up&&left&&right)
			textCoord.x = 7*64;
		else if(up&&down&&right)
			textCoord.x = 8*64;
		else if(up&&down&&left)
			textCoord.x = 6*64;
		else if(up&&right)
			textCoord.x = 3*64;
		else if(up&&left)
			textCoord.x = 2*64;
		else if(down&&left)
			textCoord.x = 5*64;
		else if(down&&right)
			textCoord.x = 4*64;
		else if(up&&down)
			textCoord.x = 1*64;
		else if(left&&right)
			textCoord.x = 0*64;
		
		return textCoord;
	}
	
	private int nodeFromTile(int x, int y){
		for(int i = 0; i < path.size(); i++){
			if(path.get(i).getTile().x == x && path.get(i).getTile().y == y)
				return i;
		}
		return -1;
	}
	private void createPath(PathNode currentNode, Bitmap level){
		int imgX = currentNode.getTile().x+1;
		int imgY = currentNode.getTile().y+1;
		if(level.getPixel(imgX, imgY) == Color.rgb(0, 255, 0)){
			for(int x = imgX-1; x <= imgX+1; x++)
				for(int y = imgY-1; y<=imgY+1; y++)
					if(!(x==imgX+1&&y==imgY+1) &&!(x==imgX+1&&y==imgY-1) &&!(x==imgX-1&&y==imgY+1) &&!(x==imgX-1&&y==imgY-1) && !(x==imgX && y== imgY) && x >= 0 && y >= 0 && x < level.getWidth() && y<level.getHeight()){
						// find next node
						if(level.getPixel(x,y)!=Color.rgb(0, 0, 0) && level.getPixel(x,y)!=Color.rgb(255, 255, 255)&&level.getPixel(x,y)!=Color.rgb(150, 150, 150)){
							int nextNode = nodeFromTile(x-1,y-1);
							currentNode.nextNode.add(nextNode);
							if(nextNode > -1)
								createPath(path.get(nextNode),level);
							
							// break out!
							return;
						}
					}
		}else if(level.getPixel(imgX,imgY) == Color.rgb(0, 0, 255)){
			int nextNode = nodeFromTile(imgX-1+1,imgY-1);
			currentNode.nextNode.add(nextNode);
			if(nextNode > -1)
				createPath(path.get(nextNode),level);
		}else if(level.getPixel(imgX, imgY) == Color.rgb(0, 255, 255)){
			int nextNode = nodeFromTile(imgX-1-1,imgY-1);
			currentNode.nextNode.add(nextNode);
			if(nextNode > -1)
				createPath(path.get(nextNode),level);
		}else if(level.getPixel(imgX, imgY) == Color.rgb(255, 255, 0)){
			int nextNode = nodeFromTile(imgX-1,imgY-1-1);
			currentNode.nextNode.add(nextNode);
			if(nextNode > -1)
				createPath(path.get(nextNode),level);
		}else if(level.getPixel(imgX, imgY) == Color.rgb(255, 0, 255)){
			int nextNode = nodeFromTile(imgX-1,imgY-1+1);
			currentNode.nextNode.add(nextNode);
			if(nextNode > -1)
				createPath(path.get(nextNode),level);
		}else if(level.getPixel(imgX, imgY) == Color.rgb(255, 150, 0)){
			for(int x = imgX-1; x <= imgX+1; x++)
				for(int y = imgY-1; y<=imgY+1; y++)
					if(!(x==imgX+1&&y==imgY+1) &&!(x==imgX+1&&y==imgY-1) &&!(x==imgX-1&&y==imgY+1) &&!(x==imgX-1&&y==imgY-1) && !(x==imgX && y== imgY) && x >= 0 && y >= 0 && x < level.getWidth() && y<level.getHeight()){
						// find next node
						if(level.getPixel(x,y)!=Color.rgb(0, 0, 0) && level.getPixel(x,y)!=Color.rgb(255, 255, 255)&&level.getPixel(x,y)!=Color.rgb(150, 150, 150)&&level.getPixel(x,y)!=Color.rgb(0, 255, 0)){
							if((x==imgX-1&&level.getPixel(x,y)!=Color.rgb(0,0,255))||(x==imgX+1&&level.getPixel(x,y)!=Color.rgb(0,255,255))||(y==imgY-1&&level.getPixel(x,y)!=Color.rgb(255,0,255))||(y==imgY+1&&level.getPixel(x,y)!=Color.rgb(255,255,0))){
								int nextNode = nodeFromTile(x-1,y-1);
								currentNode.nextNode.add(nextNode);
								if(nextNode > -1)
									createPath(path.get(nextNode),level);
							}
						}
					}
		}
	}
	
	public Level(int mapWidth, int mapHeight){
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		tileMap = new Tile[mapWidth][mapHeight];
		int dimension = (int)GameScreenActivity.toPixel(32);
		for(int x = 0; x<mapWidth; x++)
			for(int y = 0; y<mapHeight; y++){
				if(x==y || x-1==y){
					tileMap[x][y] = new Tile(new Sprite(x*dimension,y*dimension,dimension,dimension,Color.WHITE),Tile.TileType.Obstruction);
					int width = tileMap[x][y].getSprite().getWidth()/2;
					int tx = tileMap[x][y].getPosition().x+width;
					int ty = tileMap[x][y].getPosition().y+width;
					path.add(new PathNode(tx,ty,width,new Point(x,y)));
				}else
					tileMap[x][y] = new Tile(new Sprite(x*dimension,y*dimension,dimension,dimension,Color.rgb(20, y*18, x*20)),Tile.TileType.Empty);
			}
		
	}
	
	public Point getSize(){
		return new Point(mapWidth*(int)GameScreenActivity.toPixel(32)+(int)GameScreenActivity.toPixel(64),mapWidth*(int)GameScreenActivity.toPixel(32)+(int)GameScreenActivity.toPixel(64));
	}
	
	public Rect getRect(){
		return new Rect(0,0,getSize().x,getSize().y);
	}
	

	public void update() {
		Date time = new Date();
		long currentTime = time.getTime();
		
		// UPDATE ALL IN A SINGLE FOR LOOP
		int biggestSize = 0;
		if(bulletPool.getInUse()>=enemyPool.getInUse()&&bulletPool.getInUse()>=towerList.size())
			biggestSize = bulletPool.getInUse();
		else if(enemyPool.getInUse()>=bulletPool.getInUse()&&enemyPool.getInUse()>=towerList.size())
			biggestSize = enemyPool.getInUse();
		else 
			biggestSize = towerList.size();

		for(int i = biggestSize-1; i>=0; i--){
			bulletPool.update(i);
			
			// a reward is returned if enemy is dead
			wallet += enemyPool.update(i,path, tileMap);
			
			//for(int i = 0; i < towerList.size(); i++)
			if(i < towerList.size())
				towerList.get(i).update(bulletPool,currentTime-previousTime);
		}
		
		if(enemyPool.enemyGenerator(currentTime-previousTime, startNodes,path))
			clearObstacles();
		
		/* temp spawn code
		if(Math.random()*100 >= 50){
			int dimension = (int)GameScreenActivity.toPixel(10);
			int startInd = (int)(Math.random()*startNodes);
			enemyPool.create(new Sprite(path.get(startInd).getCenter().x-dimension/2,path.get(startInd).getCenter().y-dimension/2,dimension,dimension,Color.RED), (int)GameScreenActivity.toPixel(4), 10, startInd,path.get(startInd).getNextNode(), path);
		}*/
		
		previousTime = currentTime;
	}


	public void draw(Canvas canvas, Paint paint) {
		background.draw(canvas, paint);
		
		for(int i = 0; i < obstacleList.size(); i++)
			obstacleList.get(i).draw(canvas,paint);

		// DRAW ENEMY, TOWER, AND BULLETS IN SINGLE LOOP
		int biggestSize = 0;
		if(bulletPool.getInUse()>=enemyPool.getInUse()&&bulletPool.getInUse()>=towerList.size())
			biggestSize = bulletPool.getInUse();
		else if(enemyPool.getInUse()>=bulletPool.getInUse()&&enemyPool.getInUse()>=towerList.size())
			biggestSize = enemyPool.getInUse();
		else 
			biggestSize = towerList.size();
		for(int i = biggestSize-1; i>=0; i--){
			//sandwich.draw(canvas, paint);
			enemyPool.draw(i,canvas,paint);
				
			//for(int i = 0; i < towerList.size(); i++)
			if(i < towerList.size())
				towerList.get(i).draw(canvas,paint);
			
			bulletPool.draw(i,canvas, paint);
		}
		
		// PERFORM BULLET / ENEMY COLLISION
		for(int b = bulletPool.getInUse()-1; b>=0; b--){
			RectF tempB = bulletPool.getBulletBounds(b);
			for(int e = enemyPool.getInUse()-1; e>=0; e--){
				RectF tempE = enemyPool.getEnemyBounds(e);
				if(RectF.intersects(tempB, tempE)){
					enemyPool.takeDamage(e,bulletPool.getDamage(b));
					bulletPool.invalidateBullet(b);
				}
				
			}
		}
		
		selectionBox.draw(canvas, paint);
	}
	
	public void setSelection(boolean selection){
		this.selectionBox.setVisible(selection);
	}
	public TileSelectionBox getSelectionBox(){ return selectionBox; }
	public boolean makeSelection(float x, float y){
		boolean out = false;
		
		int dimension = (int)GameScreenActivity.toPixel(32);
		int boxx= (int)Math.floor(x/dimension)*dimension;
		int boxy= (int)Math.floor(y/dimension)*dimension;
		
		if(boxx >= dimension && boxy >= dimension && boxx < (mapWidth+1)*dimension && boxy < (mapHeight+1)*dimension){
			// in selectable tile
			int tileX = (int)Math.floor(x/dimension)-1;
			int tileY = (int)Math.floor(y/dimension)-1;
			Tile.TileType tilesType = tileMap[tileX][tileY].getType();
			if(tilesType==Tile.TileType.Empty || tilesType==Tile.TileType.Tower || tilesType==Tile.TileType.Path){
				setSelection(false);
				selectionBox.setBox(new Point(boxx,boxy), new Point(tileX,tileY), tilesType);
				if(tilesType == Tile.TileType.Tower){
					// get tower's index
					for(int i = 0; i < towerList.size(); i++)
						if(towerList.get(i).getTileIndex().x==selectionBox.getTileIndex().x&&towerList.get(i).getTileIndex().y==selectionBox.getTileIndex().y){
							selectionBox.setTowerIndex(i);
							break;
						}
				}
				
				out = true;
			}else{
				setSelection(false);
			}
		}else{
			setSelection(false);
		}
		
		return out;
	}

	public boolean addTower(int position, float price) {
		if(wallet >= price){
			towersBought++;
			wallet -= price;
			moneySpent += price;
			float fireDelay = towerSpecs[position][0];
			int damage = (int)towerSpecs[position][1];
			int range = (int)towerSpecs[position][2];
			float upgradePrice = towerSpecs[position][3];;
			float upgradeIncrement = towerSpecs[position][4];;
			int rangeIncrement = (int)towerSpecs[position][5];;
			int damageIncrement = (int)towerSpecs[position][6];;
			int rateIncrement = (int)towerSpecs[position][7];;
			
			towerList.add( new Tower(new ImageSprite(selectionBox.getPosition().x,selectionBox.getPosition().y,tileDimension,tileDimension, new Point(64*(position*2),192), new Point(64,64)),new ImageSprite(selectionBox.getPosition().x,selectionBox.getPosition().y,tileDimension,tileDimension, new Point(64*(position*2+1),192), new Point(64,64)), selectionBox.getTileIndex(), fireDelay,damage,range,upgradePrice,upgradeIncrement,rangeIncrement,damageIncrement,rateIncrement) );
			tileMap[selectionBox.getTileIndex().x][selectionBox.getTileIndex().y].setType(Tile.TileType.Tower);
			// asses possible targets to watch
			towerList.get(towerList.size()-1).seekPathTargets(tileMap);
			return true;
		}
		return false;
	}

	public boolean addObstacle(int position, float price) {
		if(wallet >= price){
			trapsBought++;
			wallet -= price;
			moneySpent+=price;
			Tile.TileType obstacleType = Tile.TileType.Obstacle;
			
			switch(position){
			case 0:
				obstacleType= Tile.TileType.SlowTrap;
				break;
			case 1:
				obstacleType= Tile.TileType.DamageTrap;
				break;
			}
			tileMap[selectionBox.getTileIndex().x][selectionBox.getTileIndex().y].setType(obstacleType);
			obstacleList.add(new Obstacle(new ImageSprite(selectionBox.getPosition().x,selectionBox.getPosition().y,tileDimension,tileDimension, new Point(64*(6+position),256), new Point(64,64)),selectionBox.getTileIndex(),obstacleType));
			return true;
		}
		return false;
	}
	public void sellTower() {
		towersSold++;
		wallet += towerList.get(selectionBox.getTowerIndex()).getSellPrice();
		towerList.remove(selectionBox.getTowerIndex());
		tileMap[selectionBox.getTileIndex().x][selectionBox.getTileIndex().y].setType(Tile.TileType.Empty);
	}
	
	public void clearObstacles(){
		for(int i =obstacleList.size()-1; i >= 0; i--){
			tileMap[obstacleList.get(i).getTileIndex().x][obstacleList.get(i).getTileIndex().y].setType(TileType.Path);
			obstacleList.remove(i);
		}
	}

	public boolean upgradeTower() {
		if(wallet >= towerList.get(selectionBox.getTowerIndex()).getUpgradeCost()){
			float price = towerList.get(selectionBox.getTowerIndex()).getUpgradeCost();
			wallet -= price;
			moneySpent+=price;
			towerList.get(selectionBox.getTowerIndex()).upgrade(tileMap);
			return true;
		}
		return false;
	}

	public boolean isGameOver() {
		boolean out = allowedEnemies <= enemyPool.getLived() && flag;
		if (out)
			flag = false;
		return out;
	}

	public Intent getStats() {
		Intent intent = new Intent();
		
		// add stats
		intent.putExtra(GameScreenActivity.EXTRA_STAT_BULLETS_SHOT, bulletPool.getBulletsShot());
		intent.putExtra(GameScreenActivity.EXTRA_STAT_ENEMY_KILLED, enemyPool.getKilled());
		intent.putExtra(GameScreenActivity.EXTRA_STAT_MONEY_SPENT, moneySpent);
		intent.putExtra(GameScreenActivity.EXTRA_STAT_TOWERS_BOUGHT, towersBought);
		intent.putExtra(GameScreenActivity.EXTRA_STAT_TOWERS_SOLD, towersSold);
		intent.putExtra(GameScreenActivity.EXTRA_STAT_TRAPS_BOUGHT, trapsBought);
		
		return intent;
	}
}
