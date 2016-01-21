/*
	Cole Garien
	Assignment 5
	Due Wednesday, November 24th
	Theory of Computing, MW 5:45
*/

#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <stack>
#include <string>
#include <cstdlib>  // for atoi
#include <cctype>   // for toupper
#include <map>      // for decoded code word
#include <algorithm>// for replace

using namespace std;

const string ACCEPT_MSG = "ACCEPT";
const string REJECT_MSG = "REJECT";
const char DELTA = 'D';
const char MOVE_LEFT = 'L';
const char MOVE_RIGHT = 'R';

struct Transition{
private:
	char readChar;
	char writeChar;
	char direction;
	int nextState;
public:
	Transition(char r,char w, char d, int n){
		readChar = r;
		writeChar = w;
		direction = toupper(d);
		nextState = n;
	}
	bool hasTransition(char in){
		if(in == readChar)
			return true;		
		return false;
	}
	char getReadChar(){return readChar;}
	char getWriteChar(){return writeChar;}
	char getDirection(){return direction;}
	int getNextState(){return nextState;}
};

struct State {
private:
	vector<Transition> transitions;
public:
	void addTransition(Transition t){
		transitions.push_back(t);
	}

	Transition getTransition(char in){
		for(int i = 0; i < transitions.size(); i++){
			bool success = transitions.at(i).hasTransition(in);
			if(success)
				return transitions.at(i);
		}
		// bad transition
		return Transition('.','.','.',-1);
	}
};

struct UTM{
private:
	string comment;
	int startState;		// index of start state
	int endState;		// index of start state
	string codeword;
	vector<State> states;
public:
	UTM(istream& in, ostream& out){
		getline(in, comment);	// get the comment line
		string line = "";
		getline(in, line);		// get the start/halt state line
		stringstream starthaltReader(line);
		string tempItem = "";
		// start state
		starthaltReader >> tempItem;
		startState = atoi(tempItem.c_str())-1; // store it as int
		// end state
		starthaltReader >> tempItem;
		endState = atoi(tempItem.c_str())-1; // store it as int

		codeword = "";
		bool getcode = true;
		while (getcode){
			getline(in, line);
			codeword+=line;
			// last line of code word
			int index = line.find('#');
			if(index >= 0 && index < line.size() && line[index]=='#')
				getcode = false;
		}
		// clean codeword
		replace(codeword.begin(),codeword.end(),'\n',(char)0);
		replace(codeword.begin(),codeword.end(),'\r',(char)0);
		replace(codeword.begin(),codeword.end(),' ',(char)0);
		replace(codeword.begin(),codeword.end(),'\f',(char)0);
		replace(codeword.begin(),codeword.end(),'\t',(char)0);
		replace(codeword.begin(),codeword.end(),'\v',(char)0);
		replace(codeword.begin(),codeword.end(),'\b',(char)0);
		codeword = codeword.substr(0,codeword.size()-1); // remove #
		// end clean
		
		decodeCodeWord();

		out<<"Comment: "<<comment<<endl;
		out<<"Start State: "<<startState<<endl;
		out<<"End State: "<<endState<<endl;
		out<<"# State's: "<<states.size()<<endl;
		out << "\t" << codeword<< "---> " << processInput(codeword) <<endl;
	}
	
	void decodeCodeWord(){
		string line = codeword;
		map<string,char> letterMap;
		letterMap["aaa"] = 'a';
		letterMap["aab"] = 'b';
		letterMap["aba"] = 'D';
		letterMap["abb"] = '#';
		letterMap["baa"] = 'A';
		letterMap["bab"] = 'B';
		map<string,char> moveMap;
		moveMap["a"] = 'L';
		moveMap["b"] = 'R';

		while(line[0]!=(char)0){
			// get from and to states
			int x = 0;
			while(line[x]!='b')
				x++;
			int tempFrom = x-1;
			line = line.substr(x+1);
			x = 0;
			while(line[x]!='b')
				x++;
			int tempTo = x-1;
			line = line.substr(x+1);
			
			// read char and write char
			char tempRead = letterMap[line.substr(0,3)];
			line = line.substr(3);
			char tempWrite = letterMap[line.substr(0,3)];
			line = line.substr(3);

			// direction
			char tempDirection = moveMap[line.substr(0,1)];
			if(line.size()>1)
				line = line.substr(1);
			else
				line = (char)0;
			
			// add states if needed
			while(tempFrom>=states.size() || tempTo>=states.size())
				states.push_back(State());
			Transition tempTrans = Transition(tempRead,tempWrite,tempDirection,tempTo);
			states.at(tempFrom).addTransition(tempTrans);
		}
	}

	string processInput(string input){
		int tapeHead = 0;
		int currentState = startState;
		string tape = input;

		while(true){
			Transition tran = states.at(currentState).getTransition(tape[tapeHead]);
			
			// no transition, if no trans found # is the direction
			if(tran.getDirection() == '.' && tran.getNextState() == -1)
				return REJECT_MSG+" (crash, no leaving edge)";
			
			tape[tapeHead] = tran.getWriteChar();
			if(tran.getDirection()==MOVE_LEFT){
				tapeHead--;
				// moved to far left
				if(tapeHead < 0)
					return REJECT_MSG + " (tapehead is to far left)";
			}else if(tran.getDirection()==MOVE_RIGHT){
				tapeHead++;
				// expand tape with deltas on right side if needed
				if(tapeHead==tape.size())
					tape+=DELTA;
			}
			
			currentState = tran.getNextState();
			// return accept if in halt state
			if(currentState == endState)
				return ACCEPT_MSG;
		}
		// machine failure
		return REJECT_MSG;
	}
};

int main(int argc, char *argv[]){
	string fileName = "";

	if(argc > 1)
		fileName = argv[1];
	else{
		cout<< "Please enter input file name: ";
		cin >> fileName;
	}
	ifstream utmFile (fileName.c_str());
	if (utmFile.is_open()){
		UTM utm(utmFile, cout);
		string testInput = "";
		while( getline(utmFile,testInput) ){
			// test the output
			cout << "\t" << testInput<< "---> " << utm.processInput(testInput) <<endl;
		}
		utmFile.close();
	} else {
		cout << "Failed to open \""+fileName+"\", verify the file's existance and try again." << endl;
	}
	return 0;
}