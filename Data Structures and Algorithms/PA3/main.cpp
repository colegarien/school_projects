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
#include "Graph.h"

using namespace std;

int main(){
	srand (123456);

	// CREATE GRAPH FROM FILE
	Graph graph("nfs.txt");

	// print info to console
	// PRINT THE NEWLY CREATED ADJACENCY LIST
	graph.print(cout);

	//SHORTEST PATHS
	for(int i = 1; i <= 14; i++)
		graph.shortestPath(cout,i);
	
	//MINIMUM SPANNING GRAPHS
	// random number starting at 2 going to 13
	graph.minimumTree(cout, rand()%12+2);
	graph.minimumTree(cout,1);
	graph.minimumTree(cout,14);
	// end console printing

	//----------------------------------------------------------

	// print stuff to an output file
	ofstream file;
	file.open("output.txt");

	// PRINT THE NEWLY CREATED ADJACENCY LIST
	graph.print(file);

	//SHORTEST PATHS
	for(int i = 1; i <= 14; i++)
		graph.shortestPath(file,i);
	
	//MINIMUM SPANNING GRAPHS
	// random number starting at 2 going to 13
	graph.minimumTree(file, rand()%12+2);
	graph.minimumTree(file,1);
	graph.minimumTree(file,14);

	// end output to file
	file.close();
}