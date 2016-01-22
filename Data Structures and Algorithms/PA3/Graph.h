// Cole Garien
// Data Structure & Algorithms
// MW 5:45
//-------------------------------
// Project 3
//-------------------------------

#ifndef GRAPH_H
#define GRAPH_H

#include <iostream>
#include <fstream>
#include <cstdlib>
#include <string>
#include <vector>

using namespace std;

// stores index of node
struct Node{
	int index;
};

// contains two nodes and the weight between them
struct Edge{
	Edge(Node* one, Node* two, int w);
	bool equals(Edge incoming);
	Node* A;
	Node* B;
	int weight;
};


// has an adjacency list and methods to output
// a shortest path and minimum spanning tree
class Graph{
public:
	Graph(string file);
	void shortestPath(ostream& out, int start);
	void minimumTree(ostream& out, int start);
	int findWeight(int a, int b);
	void print(ostream & out);
private:
	vector< vector<Node*> > adjacency;
	vector<Edge> edges;
};

#endif
