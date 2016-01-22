// Cole Garien
// Data Structure & Algorithms
// MW 5:45
//-------------------------------
// Project 3
//-------------------------------

#include <iostream>
#include <fstream>
#include <cstdlib>
#include <string>
#include <vector>
#include "Graph.h"

using namespace std;

// create a edge from A to B with weight w
Edge::Edge(Node* one,Node* two, int w){
	A = one;
	B = two;
	weight = w;
}
// checks if the incoming edge is the same
bool Edge::equals(Edge incoming){
	return (incoming.weight == this->weight && (this->A == incoming.B || this->A == incoming.A));
}

// create a graph from a file
Graph::Graph(string file){
	// file to read from
	ifstream infile;
	infile.open(file.c_str());

	// stores a single read in character
	char temp;
	// builds a string from read characters
	string ele = "";
	// to hold nodes to load the adj and edge lists
	vector<Node*> nodes;

	// read whole file
	while(!infile.eof()){
		// get first character
		temp = infile.get();
		// if is valid input
		if(temp != ' ' && temp!= '\n'){
			// if is number of graph nodes
			if(temp == '('){
				ele = "";
				temp = infile.get();
				// read in number of nodes
				do{
					ele += temp;
					temp = infile.get();
				}while(temp != ')');

				// allocate proper space for node info to come
				int numEle = atoi(ele.c_str());
				adjacency.resize(numEle,vector<Node*>());
				
				// create nodes that will be stored in adjaceny and edge list
				for(int i = 0; i < numEle; i++){
					nodes.push_back(new Node());
					nodes[i]->index = i+1;
				}
			}
			// if about to read a node
			else if(temp == '#'){
				ele = "";
				// now get node index
				temp = infile.get();
				do{
					ele += temp;
					temp = infile.get();
				}while(temp != '{'); // stop a begin of adj nodes
				
				// store index we are about to work in
				int index = atoi(ele.c_str());
				// read in adjacent nodes and the weights to them
				do{
					ele = "";
					// read in adjacent index
					temp = infile.get();
					do{
						ele += temp;
						temp = infile.get();
					}while(temp!= '-');
					// store adjacent index
					int adjIndex = atoi(ele.c_str());

					ele = "";
					// get weight
					temp = infile.get();
					do{
						ele += temp;
						temp = infile.get();
					}while(temp!= ',' && temp != '}');
					// store weight
					int edgeWeight = atoi(ele.c_str());

					// add pointer to adjacent node
					adjacency[index-1].push_back(nodes[adjIndex-1]);

					// create an edge that will be added to edge list
					Edge toAdd(nodes[index-1],nodes[adjIndex-1],edgeWeight);

					// make sure edge isnt a duplicate
					int k = 0;
					bool found = false;
					while(k < edges.size() && !found){
						found = toAdd.equals(edges[k]);
						k++;
					}
					// if it isnt a duplicate add it
					if(!found)
						edges.push_back(toAdd);
				}while(temp!= '}');
			}
		}
	}
	// close the file
	infile.close();
}

void Graph::minimumTree(ostream& out, int start){
	// total weight of tree
	int totalWeight = 0;

	// minimum adjacenecy
	vector< vector<Node*> > adj;
	adj.resize(adjacency.size(),vector<Node*>());

	// index of nodes in the min tree
	vector<int> spanner;

	// start on the first node
	int workingDex = start-1;
	// add the first node to the min tree
	spanner.push_back(workingDex);

	// create a min tree
	do{
		int w = -1;
		int minWeight = -1;

		// in relation to spanner
		int minworkdex = 0;
		// min adjacent node
		int minDex = 0;

		// go through all nodes in min tree and find the
		// smallest edge to outside the tree
		for(int wdex = 0; wdex < spanner.size(); wdex++){
			for(int i = 0; i <adjacency[spanner[wdex]].size(); i++){
				// make sure index is not already in min spanner tree
				int z = 0;
				bool inSpanner = false;
				while(z<spanner.size() && !inSpanner){
					if(spanner[z] == adjacency[spanner[wdex]][i]->index-1)
						inSpanner = true;
					z++;
				}

				// if its not in the tree
				if(!inSpanner){
					// find weight of edge
					w = findWeight(spanner[wdex]+1,adjacency[spanner[wdex]][i]->index);
					// if edge is smallest so far then store it
					if((minWeight == -1 && w != -1)||w<minWeight){
						minWeight = w;
						minDex = i;
						minworkdex = wdex;
					}
				}
			}
		}

		// add closest node to adjecent list
		adj[spanner[minworkdex]].push_back(adjacency[spanner[minworkdex]][minDex]);
		// add closest node to the min tree
		spanner.push_back(adjacency[spanner[minworkdex]][minDex]->index-1);
		// accumulate total weight
		totalWeight += minWeight;


	}while(spanner.size() < adjacency.size());


	//print edges based on adjacency list
	out<<"Minimum Spanning Edge List Starting at "<< start <<endl;
	out<<"----------------------------------------------"<<endl;
	for(int row = 0; row < adj.size(); row++){
		for(int col = 0; col < adj[row].size(); col++){
			out<<"{"<<row+1<<", "<<adj[row][col]->index << "}";
			if(col == adj[row].size()-1){
				out<<";";
			}else{
				out<<", ";
			}
		}
		if(adj[row].size()>0)
			out<<endl;
	}
	out<<"Total weight: " << totalWeight << endl;
	out<<endl;
	// end print
}

// start node is the value of the node, index will be start - 1
void Graph::shortestPath(ostream& out, int start){
	// shortest path adjacenecy
	vector< vector<Node*> > adj;
	adj.resize(adjacency.size(),vector<Node*>());

	// list of nodes to work with
	vector<Node*> Q;

	// from starting node
	vector<int> distance;
	distance.resize(adjacency.size());

	// parent
	vector<int> parent;
	parent.resize(adjacency.size());

	// nodes that have been dealt with
	vector<bool> processed;
	processed.resize(adjacency.size());

	// -1 is infinity
	for(int i = 0; i < adjacency.size(); i++){
		distance[i] = -1;
		parent[i] = -1;
	}
	distance[start-1] = 0;

	// start with the input node
	int workingDex = start-1;
	processed[workingDex] = true;

	do{
		// look at all adjacent nodes
		for(int i = 0; i < adjacency[workingDex].size(); i++){
			// usable index of adjacent node
			int dex = adjacency[workingDex][i]->index -1;
			// weight of the edge to the adjacent node
			int w =findWeight(dex + 1,workingDex+1);
			// add acumulated weight
			if(distance[workingDex] > 0)
				w+=distance[workingDex];
			// if a smaller root is found change weight and parent
			if(distance[dex] == - 1 || w < distance[dex] && !processed[dex]){
				distance[dex] = w;
				parent[dex] = workingDex+1;

				// if the node isnt in the list throw it in
				bool inList = false;
				int j = 0;
				while(j < Q.size() && !inList){
					if(Q[j] == adjacency[workingDex][i]){
						inList = true;
					}
					j++;
				}
				if(!inList)
					Q.push_back(adjacency[workingDex][i]);
			}
		}
		// find lowest weight and add it to adj list
		int min = 0;
		for(int i = 1; i<Q.size(); i++){
			if(distance[Q[min]->index-1] > distance[Q[i]->index-1])
				min = i;
		}

		// add the min to the list of found and then remove it from the list
		adj[parent[Q[min]->index-1]-1].push_back(Q[min]);
		workingDex = Q[min]->index-1;

		processed[workingDex] = true;

		//remove element
		for(int i = min; i < Q.size()-1; i++){
			Q[i] = Q[i+1];
		}
		Q.pop_back();
	
	}while(!Q.empty());


	// now print it
	//PRINT edge LIST in edge:weight style
	out<<"Shortest path from node " << start << " to all other Nodes" << endl;
	out<<"--------(printed in edge:weight format)--------"<<endl;
	for(int row = 0; row < adj.size(); row++){
		for(int col = 0; col < adj[row].size(); col++){
			out<<"{"<<row+1<<", "<<adj[row][col]->index << "}:"<<findWeight(row+1,adj[row][col]->index);
			if(col == adj[row].size()-1){
				out<<";";
			}else{
				out<<", ";
			}
		}
		if(adj[row].size()>0)
			out<<endl;
	}
	out<<endl;
	// end print
}

// gets the weight of and edge
int Graph::findWeight(int a, int b){
	for(int i = 0; i < edges.size(); i++){
		if((a == edges[i].A->index && b == edges[i].B->index)||(a == edges[i].B->index && b == edges[i].A->index)){
			return edges[i].weight;
		}
	}
	
	return -1;
}

// prints the whole adjacency list
void Graph::print(ostream & out){
	//PRINT ADJACENCY LIST
	out<<"Graph Adjacency List"<<endl;
	out<<"------------------------------"<<endl;
	for(int row = 0; row < adjacency.size(); row++){
		out<<row+1<<": ";
		for(int col = 0; col < adjacency[row].size(); col++){
			out<<adjacency[row][col]->index;
			if(col == adjacency[row].size()-1){
				out<<";"<<endl;
			}else{
				out<<", ";
			}
		}
	}
	out<<endl;
}
