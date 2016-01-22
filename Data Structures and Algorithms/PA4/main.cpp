//------------------------
// Cole Garien
// Data Struct & Algo
// MW 5:45
// PA4
//------------------------

#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <string>
#include "Hash.h"

using namespace std;

int main(int argc, char *argv[]) {
	int size = 211, c1 = 1, c2 = 0, R = 0;
	
	// gets the values above either from the command line or from the user
	if(argc == 5){
		size = atoi(argv[1]);
		R = atoi(argv[2]);
		c1 = atoi(argv[3]);
		c2 = atoi(argv[4]);
	}else if(argc == 4){
		size = atoi(argv[1]);
		R = atoi(argv[2]);
		c1 = atoi(argv[3]);
		cout << "Enter a value for c2" << endl;
		cin >> c2;
	}else if(argc == 3){
		size = atoi(argv[1]);
		R = atoi(argv[2]);
		cout<< "Enter a value for c1"<<endl;
		cin >> c1;
		cout << "Enter a value for c2" << endl;
		cin >> c2;
	}else if(argc == 2){
		size = atoi(argv[1]);
		cout << "Enter a value for R"<<endl;
		cin >> R;
		cout<< "Enter a value for c1"<<endl;
		cin >> c1;
		cout << "Enter a value for c2" << endl;
		cin >> c2;
	}else if(argc == 1){
		cout<<"Enter the Size of the table"<<endl;
		cin>>size;
		cout << "Enter a value for R"<<endl;
		cin >> R;
		cout<< "Enter a value for c1"<<endl;
		cin >> c1;
		cout << "Enter a value for c2" << endl;
		cin >> c2;
	}

	Hash A(size); // for quadratic
	Hash B(size); // for double hash

	// reading input file
	ifstream file;
	// open input file
	file.open("input.txt");

	// to read into
	char temp[5];
	// convert input to an int to place it in the tables
	int number = 0;

	// how many numbers have been read
	int numInputs = 0;
	// total attempts for A and B
	int totA = 0;
	int totB = 0;

	// read till there is nothing to read
	while(!file.eof()){
		// read in number to a c string
		file.getline(temp,5);
		// convert c string to integer
		number = atoi(temp);
		
		// keep track of numbers being input
		numInputs++;

		// try entering the number into the hash tables
		totA += A.QuadraticProbing(number,c1,c2);
		totB += B.DoubleHashing(number,R);
	}

	// close the file up
	file.close();

	cout << endl << endl;
	cout << "Quadratic Probing (c1 = " << c1 << ", c2 = " << c2 <<")"<< endl;
	cout << "------------------------------------------------"<<endl;
	cout << "Total probes: "<<totA<<endl;
	cout << "Average probes: " <<(totA/numInputs)<<endl<<endl;

	cout << "Double Hashing (R = " << R << ")"<< endl;
	cout << "------------------------------------------------"<<endl;
	cout << "Total probes: "<<totB<<endl;
	cout << "Average probes: " <<(totB/numInputs)<<endl<<endl;

}
