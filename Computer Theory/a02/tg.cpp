/*
	Cole Garien
	Assignment 2
	Due Wednesday, September 24th
	Theory of Computing, MW 5:45
*/

#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <cstdlib> // for atoi

using namespace std;

const string ACCEPT_MSG = "ACCEPT";
const string REJECT_MSG = "REJECT";

// string to nav edge and result state
struct Transition{
private:
	string in;  // required input string
	int out; // index of next state
public:
	Transition(){
		in = "";
		out = -1;
	}
	Transition(string i, int o){
		in = i;
		out = o;
	}
	// attempt to transition with given input
	// -1 is returned if input is not valid for this transition
	int makeTransition(string i){
		if(i == in)
			return out;

		return -1;
	}
	int getOut() { return out; }
};

struct State{
private:
	vector<Transition> trans;
public:
	State(){}
	State(vector<Transition> t){
		trans = t;
	}
	void addTransition(Transition t){
		trans.push_back(t);
	}
	int makeTransition(string input){
		int result = -1;
		for(int i = 0; i < trans.size(); i++){
			result = trans.at(i).makeTransition(input);
			if(result != -1)
				return result;
		}
		return result;
	}
};

struct TG{
private:
	string comment;
	string alphabet;
	int startState;		// index of start state
	vector<int> endStates; // list of end states
	vector<State> states;
public:
	TG(){
		comment = "";
		alphabet = "";
	}
	TG(string c, string a, vector<State> s){
		comment = c;
		alphabet = a;
		states = s;
	}
	TG(istream& in, ostream& out){

		getline(in, comment);	// get the comment line
		getline(in, alphabet);	// get the alphabet
		string line = "";
		getline(in, line);		// get the number of states
		int numStates = atoi(line.c_str()); // store it as int
		getline(in, line);		// get the number of edges
		int numEdges = atoi(line.c_str());
		// create each state
		for(int i = 0; i < numStates; i++){
			states.push_back(State()); // add state to list
		}
		// create each edge
		for(int i = 0; i < numEdges; i++){
			getline(in, line); // get the transition line
			stringstream transReader(line);
			while(!transReader.eof()){
				string tempItem = "";
				transReader >> tempItem; // get state to transition from
				int forState = atoi(tempItem.c_str()) -1;
				// state to goto
				transReader >> tempItem;
				int toState = atoi(tempItem.c_str()) -1;
				// string for edge
				transReader >> tempItem;
				Transition tempTrans(tempItem, toState);
				states.at(forState).addTransition(tempTrans);
			}
		}
		
		getline(in, line); // start state#
		startState = atoi(line.c_str())-1; // assign state with this id

		getline(in, line);
		stringstream endReader(line);
		string tempItem = "";
		int numEnd = 0;
		endReader >> tempItem; // get number of end states
		numEnd = atoi(tempItem.c_str());

		for(int i = 0; i < numEnd; i++){
			endReader >> tempItem; // get state that is an endstate
			endStates.push_back(atoi(tempItem.c_str())-1);
		}

		out<<"Comment: "<<comment<<endl;
	}

	string processInput(string input){
		string inputLeft = input;
		int currentState = startState;
		
		while(inputLeft.size()>0){
			int i = inputLeft.size();
			int nextState = -1;
			while(i > 0 && nextState == -1){
				nextState = states.at(currentState).makeTransition(inputLeft.substr(0,i));
				if(nextState==-1)
					i--;
			}
			if(nextState==-1){
				return REJECT_MSG +" (Crash, input left: "+inputLeft+")";
			}
			currentState = nextState;
			inputLeft = inputLeft.substr(i,inputLeft.size()-i);
		}
		for(int i = 0; i < endStates.size(); i++)
			if(currentState == endStates.at(i))
				return ACCEPT_MSG;

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

	ifstream tgFile (fileName.c_str());
	if (tgFile.is_open()){
		TG tg(tgFile, cout);
		string testInput = "";
		while( getline(tgFile,testInput) ){
			// test the output
			cout << "\t" << testInput<< "---> " << tg.processInput(testInput) <<endl;
		}
		tgFile.close();
	} else {
		cout << "Failed to open \""+fileName+"\", verify the file's existance and try again." << endl;
	}

	return 0;
}