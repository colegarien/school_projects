//------------------------
// Cole Garien
// Data Struct & Algo
// MW 5:45
// PA4
//------------------------

#include <iostream>
#include <vector>
#include <cmath>
#include "Hash.h"

using namespace std;

// return number of attempts
int Hash::QuadraticProbing(int val, int c1, int c2){
	// the intial place to try and insert
	int base = hashFunc(val);
	// track num of attempts
	int attempts = 0;

	// first place to look
	int index = base;
	// havent found the value
	bool inserted = false;
	do{
		// if the current index is deleted then insert
		if(table[index].deleted){
			table[index].value = val;
			table[index].deleted = false;
			inserted = true;
		// if found the same value then delete it
		}else if(table[index].value == val){
			table[index].deleted = true;
			inserted = true;
		// if the current spot is filled, find the next spot
		}else{
			// move to next spot
			index = base + jumpSize(attempts + 1,c1,c2);
			// if next index is outside of array then wrap it back in
			while(index >= table.size()){ index -= table.size(); }
		}

		// an attempt has been completed
		attempts++;
	// only try 99 times or until the value is found
	}while(attempts < 99 && !inserted);

	// return attempts it took
	return attempts;
}

// returns number of attempts
int Hash::DoubleHashing(int val, int R){
	// first place to look
	int base = hashFunc(val);
	int attempts = 0;

	// start here
	int index = base;
	// havent found anything
	bool inserted = false;
	do{
		// if spot has nothing then insert
		if(table[index].deleted){
			table[index].value = val;
			table[index].deleted = false;
			inserted = true;
		// if spot has same value, delete
		}else if(table[index].value == val){
			table[index].deleted = true;
			inserted = true;
		// look for another spot
		}else{
			// use doubleHash
			index =(attempts+1) * doubleHash(val,R);
			// if spot is outside of array then wrap it back in
			while(index >= table.size()){ index -= table.size(); }
		}

		// another attempt complete
		attempts++;
	// run for 99 attempts or till inserted
	}while(attempts < 99 && !inserted);

	// return number of tries
	return attempts;
}

// constructor
Hash::Hash(int size){
	table.resize(size);
	for(int i = 0; i < table.size(); i++){
		table[i].deleted = true;
	}
}

// hashes the value
int Hash::hashFunc(int k){
	return k%table.size();
}

// the double hash function
int Hash::doubleHash(int k, int R){
	return R - (k%R);
}

// find the jumpsize for the Quadratic probing
int Hash::jumpSize(int i, int c1, int c2){
	return ((c1*i)+(c2*i*i));
}