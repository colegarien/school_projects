/*
	Cole Garien
	Assignment 4
	Due Wednesday, November 12th
	Theory of Computing, MW 5:45
*/

#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <stack>
#include <string>
#include <cstdlib> // for atoi
#include <cctype>   // for toupper

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
		return Transition('#','#','#',-1);
	}
};

struct TM{
private:
	string comment;
	string alphabet;
	int startState;		// index of start state
	vector<State> states;
	vector<int> endStates;
public:
	TM(istream& in, ostream& out){

		getline(in, comment);	// get the comment line
		getline(in, alphabet);	// get the alphabet
		string line = "";
		getline(in, line);		// get the number of states
		int numStates = atoi(line.c_str()); // store it as int
		for(int i = 0; i < numStates; i++)
			states.push_back(State());

		// create each transition
		bool transDone = false;
		while(!transDone){
			getline(in, line);		// get the transition line
			stringstream transReader(line);
			string tempItem = "";

			// state to goto
			transReader >> tempItem;
			if(tempItem.compare("-1") != 0){
				int curState = atoi(tempItem.c_str());
				
				// get next state
				transReader >> tempItem;
				int nextState = atoi(tempItem.c_str());

				// read char, write char, direction
				transReader >> tempItem;
				char read = tempItem.at(0);
				char write = tempItem.at(1);
				char direct = tempItem.at(2);

				Transition tempTrans = Transition(read,write,direct,nextState);
				states.at(curState).addTransition(tempTrans);
			}else{
				transDone = true;
			}
		}
		
		getline(in, line); // start state#
		startState = atoi(line.c_str()); // assign state with this id

		
		getline(in, line);		// get the transition line
		stringstream endsReader(line);
		string tempItem = "";
		endsReader >> tempItem;// num endstates
		int numEnds = atoi(tempItem.c_str()); // assign state with this id
		for(int i = 0; i < numEnds; i++){
			endsReader >> tempItem;
			endStates.push_back(atoi(tempItem.c_str()));
		}

		out<<"Comment: "<<comment<<endl;
		out<<"Alphabet: "<<alphabet<<endl;
		out<<"Start State: "<<startState<<endl;
		out<<"# End State: "<<endStates.size()<<endl;
	}

	string processInput(string input){
		int tapeHead = 0;
		int currentState = startState;
		string tape = input;

		while(true){
			Transition tran = states.at(currentState).getTransition(tape[tapeHead]);
			
			// no transition, if no trans found # is the direction
			if(tran.getDirection() == '#' && tran.getNextState() == -1)
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

			// return accept if in a halt state
			for(int i = 0; i < endStates.size(); i++){
				if(currentState == endStates.at(i))
					return ACCEPT_MSG;
			}
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

	ifstream tmFile (fileName.c_str());
	if (tmFile.is_open()){
		TM tm(tmFile, cout);
		string testInput = "";
		while( getline(tmFile,testInput) ){
			// test the output
			cout << "\t" << testInput<< "---> " << tm.processInput(testInput) <<endl;
		}
		tmFile.close();
	} else {
		cout << "Failed to open \""+fileName+"\", verify the file's existance and try again." << endl;
	}

	return 0;
}