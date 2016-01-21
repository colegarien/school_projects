//==============================================================================================================
// Author:         Mr. Cole Garien
// Course:         CMSC 3833 - Computer Organization II
// CRN:            11404 Autumn 2013
// Project:        p01
// Due:            October 11, 2013
//==============================================================================================================
//Includes
#include <iostream>
#include <iomanip>
#include <vector>
#include <string>
#include <sstream>
#include <fstream>
#include <cmath>
#include <string>
using namespace std;

// gets the decimal version of a binary number (represented by bools)
int binaryToInt(vector<bool> a){
	 int out = 0;
	 for(int i = 0; i < a.size(); i++){
		out += a[i] ? (int)pow(2.0,(int)(a.size()-(i+1))) : 0;
	 }
	 return out;
}

// converts a string into an array of bools
vector<bool> stringToBool(string s){
	vector<bool> temp;

	for(int i = 0; i < s.length(); i++){
		istringstream buffer(s.substr(i,1));
		int value;
		buffer >> value;
		temp.push_back((value ? true : false));
	}
	return temp;
}

// simple 2 input NAND gate
bool NAND(bool a, bool b)
{
	return !(a&&b);
}

class DFlipFlop
{
	bool d;
	bool q;

public:
	DFlipFlop() : q(0), d(0) {}
	
	// inputs a value into d
	void SetD(bool pd) { d = pd; }

	// simulates clock tick of a d-flipflop
	void ClockTick() { q = d; }

	// returns q
	bool Q() { return q; }
	// returns the complement of q
	bool NotQ() { return !q; }
};

class Register
{
    DFlipFlop mFlops[8];
public:
	Register() {}
	// fills the register up with values
	void load(vector<bool> a){
		for(int i = 0; i < a.size(); i++)
			mFlops[i].SetD(a[i]);
	}
	// simulates the tick of the clock, updating the flipflops
	void ClockTick(){
		for(int i = 0; i < 8; i++)
			mFlops[i].ClockTick();
	}
	// returns the registers value
	vector<bool> get(){
		vector<bool> a;
		for(int i = 0; i < 8; i++)
			a.push_back(mFlops[i].Q());
		return a;
	}
	// returns a selected bit
	bool getBit(int bit){
		return mFlops[bit].Q();
	}
	// sets a selected bit
	void setBit(int bit, bool val){
		mFlops[bit].SetD(val);
	}
	// prints the register's bits
	void print(ostream& out){
		string temp = "";
		for(int i = 0; i < 8; i++)
			temp += mFlops[i].Q() ? "1":"0";
		out<<setw(15)<<temp;
	}
};

// type of operations
enum DecodeType
{
    AND, OR, XOR, XNOR,
    LOAD_R0, LOAD_R1,
    PRINT_R0, PRINT_R1
};

class Decoder{
	vector<bool> in;
	DecodeType out;
public:
	Decoder(){
		for(int i =0; i < 3; i++)
			in.push_back(false);
	}
	
	// recieves bool to fill the input of the decoder
	void load(vector<bool> a){
		for(int i =0; i < in.size(); i++)
			in[i] = a[i];

		// convert binary to int to decide operation to use
		int temp = binaryToInt(in);
		switch (temp){
			case 0: out = XOR; break;
			case 1: out = OR; break;
			case 2: out = XNOR; break;
			case 3: out = AND; break;
			case 4: out = LOAD_R0; break;
			case 5: out = LOAD_R1; break;
			case 6: out = PRINT_R0; break;
			case 7: out = PRINT_R1; break;
		}
	}
	// return operation to do
	DecodeType get(){
		return out;
	}
};

// INPUT VALIDATION
bool isInputValid(string s, int sz){
	bool out = true;

	// input should only be 3bits for a command and 8 for register loading
	if(s.length()!=sz)
		return false;

	// input should only consist of 1's and 0's
	for(int i = 0; i < s.length(); i++){
		if(s.substr(i,1) != "0" && s.substr(i,1) != "1")
			return false;
	}

	// no errors!
	return true;
}

int main(int argc, char* argv[])
{
	Register R0;
	Register R1;
	Decoder decoder;

	ifstream infile;
	ofstream outfile;

	// check what information is supplied and what wasn't
	if(argc >= 3){
		infile.open(argv[1]);
		outfile.open(argv[2]);
	}else if(argc >= 2){
		infile.open(argv[1]);
		cout<<"Enter the output file name: ";
		string temp = "";
		cin>>temp;
		outfile.open(temp.c_str());
	}else{
		cout<<"Enter the input file name: ";
		string temp = "";
		cin>>temp;
		infile.open(temp.c_str());
		cout<<"Enter the output file name: ";
		temp = "";
		cin>>temp;
		outfile.open(temp.c_str());
	}

	// ERROR TEST
	if(!infile.is_open()){
		cout<<"ERROR: Failed to open the input file\n";
		outfile<<"ERROR: Failed to open the input file\n";
		return 0;
	}
	
	// a vector is loaded with commands from the file
	vector<string> arr;
	// read commands while file has something to read
	while(!infile.eof()){
		string temp = "";
		infile >> temp;
		// make sure its not reading eof char
		if(temp!="")
			arr.push_back(temp);
	}
	// done reading file
	infile.close();

	// header for output file
	outfile<<"+------------------------------------------------------------------------------+\n";
	outfile<<"|"<<setw(15)<<"R0 Before"<<setw(15)<<"R1 Before"<<setw(7)<<"Code"<<setw(10)<<"Operation"<<setw(15)<<"R0 After"<<setw(15)<<"R1 After"<<" |\n";
	outfile<<"|------------------------------------------------------------------------------|\n";

	// go through the list of commands read from the file
	int i = 0;
	while(i < arr.size())
	{
		//ERROR TEST: CHECK IF COMMAND IS VALID, IF NOT THEN CEASE EXECUTION
		if(!isInputValid(arr[i],3)){
			cout<<"ERROR: INVALID INPUT!!!\nERROR: CHECK INPUT FILE\n";
			outfile<<"ERROR: INVALID INPUT!!!\nERROR: CHECK INPUT FILE\n";
			return 0;
		}

		// get the next command
		vector<bool> in=stringToBool(arr[i++]);
		// load the command into the decoder
		decoder.load(in);
		// get the resulting operation from the decoder
		DecodeType operation = decoder.get();

		// print the register's pre-operation values
		outfile<<"|";
		outfile<<setw(10);
		R0.print(outfile);
		outfile<<setw(15);
		R1.print(outfile);

		// print the operation code
		outfile<<setw(7)<<arr[i-1];

		// process the operation, using NAND's if is logical op
		switch(operation){
		case AND : 
			// print operation name
			outfile<<setw(10)<<"AND";

			// apply operation to each bit
			for(int bit = 0; bit < 8; bit++){
				// first get not AND
				bool nandA = (NAND(R0.getBit(bit),R1.getBit(bit)));
				// then get not not AND which is AND
				R0.setBit(bit,NAND(nandA,nandA));
			}
			break;
		case OR : 
			// print operation name
			outfile<<setw(10)<<"OR";

			// apply operation to each bit
			for(int bit = 0; bit < 8; bit++){
				// get not AND, if bit is 1 then this is false, else its true
				bool nandA = NAND(R0.getBit(bit),R0.getBit(bit));
				// get not AND, if bit is 1 then this is false, else its true
				bool nandB = NAND(R1.getBit(bit),R1.getBit(bit));
				// get not AND, if one of the above is false(meaning atleast one bit is 1) then returns true, else false
				R0.setBit(bit,NAND(nandA,nandB));
			}
			break;
		case XOR :  
			// print operation name
			outfile<<setw(10)<<"XOR";

			// apply operation to each bit
			for(int bit = 0; bit < 8; bit++){
				// get not AND, if both are 1 this is false, else true
				bool nandA = NAND(R0.getBit(bit),R1.getBit(bit));
				// too be true bit must be 0 and nandA must be false
				bool nandB = NAND(R0.getBit(bit),nandA);
				// too be true bit must be 0 and nandA must be false
				bool nandC = NAND(R1.getBit(bit),nandA);
				// returns true if one of the above is false, meaning that the current bits are different
				R0.setBit(bit,NAND(nandB,nandC));
			}
			break;
		case XNOR :  
			// print operation name
			outfile<<setw(10)<<"XNOR";

			// apply operation to each bit
			for(int bit = 0; bit < 8; bit++){
				// get not AND, if both are 1 this is false, else true
				bool nandA = NAND(R0.getBit(bit),R1.getBit(bit));
				// too be true bit must be 0 and nandA must be false
				bool nandB = NAND(R0.getBit(bit),nandA);
				// too be true bit must be 0 and nandA must be false
				bool nandC = NAND(R1.getBit(bit),nandA);
				// returns true if one of the above is false, meaning that the current bits are different
				bool nandD = NAND(nandB, nandC);
				// this inverts the result of nandD
				R0.setBit(bit,NAND(nandD,nandD));
			}
			break;
		case LOAD_R0 : 
			//ERROR TEST: CHECK IF COMMAND IS VALID, IF NOT THEN CEASE EXECUTION
			if(!isInputValid(arr[i],8)){
				cout<<"ERROR: INVALID INPUT!!!\nERROR: CHECK INPUT FILE\n";
				outfile<<"ERROR: INVALID INPUT!!!\nERROR: CHECK INPUT FILE\n";
				return 0;
			}

			// print operation name
			outfile<<setw(10)<<"LOAD_R0";

			in=stringToBool(arr[i++]);
			R0.load(in);
			break;
		case LOAD_R1 :
			//ERROR TEST: CHECK IF COMMAND IS VALID, IF NOT THEN CEASE EXECUTION
			if(!isInputValid(arr[i],8)){
				cout<<"ERROR: INVALID INPUT!!!\nERROR: CHECK INPUT FILE\n";
				outfile<<"ERROR: INVALID INPUT!!!\nERROR: CHECK INPUT FILE\n";
				return 0;
			}
			
			// print operation name
			outfile<<setw(10)<<"LOAD_R1";

			in=stringToBool(arr[i++]);
			R1.load(in);
			break;
		case PRINT_R0 : 
			// print operation name
			outfile<<setw(10)<<"PRINT_R0";

			break;
		case PRINT_R1 : 
			// print operation name
			outfile<<setw(10)<<"PRINT_R1";

			break;
		}

		// Simulate clock tick to update flip-flops
		R0.ClockTick();
		R1.ClockTick();

		// print the register's post-operation values
		R0.print(outfile);
		R1.print(outfile);
		outfile<<" |\n";

	}
	
	// bottom of the table
	outfile<<"+------------------------------------------------------------------------------+\n";

	// close up the output file
	outfile.close();
}
