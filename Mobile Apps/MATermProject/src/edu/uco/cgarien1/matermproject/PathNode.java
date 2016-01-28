// Cole Garien

package edu.uco.cgarien1.matermproject;

import java.util.ArrayList;
import android.graphics.Point;

public class PathNode {
	Point center = new Point(0,0);
	Point tileIndex = new Point(0,0);
	
	int radius= 0;
	ArrayList<Integer> nextNode = new ArrayList<Integer>();
	
	public PathNode(int x, int y, int radius){
		center.x = x;
		center.y = y;
		this.radius = radius;
	}
	public PathNode(int x, int y, int radius, Point tile){
		center.x = x;
		center.y = y;
		this.radius = radius;
		tileIndex = tile;
	}
	public PathNode(int x, int y, int radius, int nextNode, Point tile){
		center.x = x;
		center.y = y;
		this.nextNode.add(nextNode);
		tileIndex = tile;
	}
	public PathNode(int x, int y, int radius, ArrayList<Integer> nextNode, Point tile){
		center.x = x;
		center.y = y;
		this.nextNode = nextNode;
		tileIndex = tile;
	}
	public void addNextNode(int nextNode){
		this.nextNode.add(nextNode);
	}
	
	public int getNextNode(){
		int node = -1;
		if(nextNode.size()>0)
			node = nextNode.get((int)(Math.random()*nextNode.size()));
		return node;
	}
	public Point getTile() {return tileIndex;}
	public Point getCenter(){ return center; }
	public int getRadius(){return radius;
		
	}
}
