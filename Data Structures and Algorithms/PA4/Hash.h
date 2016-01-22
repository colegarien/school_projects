//------------------------
// Cole Garien
// Data Struct & Algo
// MW 5:45
// PA4
//------------------------

#ifndef HASH_H
#define HASH_H

#include <iostream>
#include <vector>
#include <cmath>

using namespace std;

struct Element{
	int value;
	bool deleted;
};

class Hash{
	public:
		Hash(int size);
		int QuadraticProbing(int value, int c1, int c2);
		int DoubleHashing(int value, int R);
	private:
		vector<Element> table;
		int hashFunc(int k);
		int doubleHash(int k, int R);
		int jumpSize(int i, int c1, int c2);
};

#endif
