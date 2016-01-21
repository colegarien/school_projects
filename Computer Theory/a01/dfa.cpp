/*
	Cole Garien
	Assignment 1
	Due Wednesday, September 10th
	Theory of Computing, MW 5:45
*/

#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <cstdlib> // for atoi

using namespace std;

// constants
const int ACCEPT = 0;
const int REJECT = 0;

const string ACCEPT_MSG = "ACCEPT";
const string REJECT_MSG = "REJECT";
// end constants

// a pair of ints that are positions in the array of states held by the DFA
struct Transition{
private:
	int in;  // required input (index of letter in DFA's alphabet)
	int out; // index of next state if required input is met
public:
	Transition(){
		in = -1;
		out = -1;
	}
	Transition(int i, int o){
		in = i;
		out = o;
	}

	// attempt to transition with given input
	// -1 is returned if input is not valid for this transition
	int makeTransition(int i){
		if(i == in)
			return out;

		return -1;
	}

	int getOut() { return out; }
};

struct State{
private:
	int id;    // state id number (not an index for an array)
	vector<Transition> trans;
public:
	State(){
		id = -1;
	}
	State(int i){
		id = i;
	}
	State(int i, vector<Transition> t){
		id = i;
		trans = t;
	}
	int getID() { return id; }
	
	void addTransition(Transition t){
		trans.push_back(t);
	}

	int makeTransition(int input){
		int result = -1;
		for(int i = 0; i < trans.size(); i++){
			result = trans.at(i).makeTransition(input);
			if(result != -1)
				return result;
		}

		return result;
	}

	string getTransRow(){
		stringstream out("");
		for(int i = 0; i < trans.size(); i++){
			out << trans.at(i).getOut()+1 << " ";
		}
		return (out.str());
	}
};

struct DFA{
private:
	string comment;
	string alphabet;
	int startState;		// index of start state in the list (not same as a state's id)
	vector<int> endStates; // list of end states
	vector<State> states;
public:
	DFA(){
		comment = "";
		alphabet = "";
	}
	DFA(string c, string a, vector<State> s){
		comment = c;
		alphabet = a;
		states = s;
	}
	DFA(istream& in, ostream& out){

		getline(in, comment);	// get the comment line
		getline(in, alphabet);	// get the alphabet
		
		string line = "";
		getline(in, line);		// get the number of states
		int numStates = atoi(line.c_str()); // store it as int
		
		// create each state
		for(int i = 1; i <= numStates; i++){
			State tempState(i); // create state

			getline(in, line); // get the transition line
			stringstream transReader(line);
			int tRead = 0;	// this will be index of letter in alphabet
			while(!transReader.eof()){
				string tempItem = "";
				transReader >> tempItem; // get state to transition too

				// tempItem will be 1 - n,
				// tempItem - 1 is the index of the state in the array
				Transition tempTrans(tRead, atoi(tempItem.c_str())-1);
				tempState.addTransition(tempTrans);

				tRead++;
			}
			
			// add state to list
			states.push_back(tempState);
		}
		
		getline(in, line); // start state#
		assignStartState(atoi(line.c_str())); // assign state with this id

		getline(in, line);
		stringstream endReader(line);
		string tempItem = "";
		int numEnd = 0;
		endReader >> tempItem; // get number of end states
		numEnd = atoi(tempItem.c_str());

		for(int i = 0; i < numEnd; i++){
			endReader >> tempItem; // get state that is an endstate
			assignEndState(atoi(tempItem.c_str()));
		}

		out << "comment: " << comment << endl;
		out << "alphabet: " <<alphabet << endl;
		out << "start state: " << startState + 1 << endl;
		out << "end state(s): ";
		for( int i = 0; i < endStates.size(); i++){
			out << endStates.at(i) + 1 << " ";
		}
		out << endl << endl << "Delta Table" << endl;
		string sepBar = "===";
		out << "  |";
		for(int i = 0; i < alphabet.size(); i++){
			out << " " << alphabet.at(i);
			sepBar += "==";
		}
		out << endl << sepBar << endl;
		for(int i = 0; i < states.size(); i++){
			out << i+1 << " | ";
			out << states.at(i).getTransRow() << endl;
		}
		out << endl;
	}

	void addState(State s){
		states.push_back(s);
	}
	void assignStartState(int id){
		for(int i = 0; i < states.size(); i++){
			if(states.at(i).getID() == id){
				startState = i;
				return;
			}
		}
	}
	void assignEndState(int id){
		for(int i = 0; i < states.size(); i++){
			if(states.at(i).getID() == id){
				endStates.push_back(i);
				return;
			}
		}
	}

	string processInput(string input){
		int currentState = startState;
		
		for(int i = 0; i < input.size(); i++){
			int nextState = -1;

			// rejecting word if has letters not in alphabet
			int letInd = alphabet.find(input.at(i));
			if(letInd == -1)
				return REJECT_MSG + " (\""+input.at(i)+"\" not found in alphabet)";
			
			nextState = states.at(currentState).makeTransition(letInd);
			if(nextState != -1)
				currentState = nextState;
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

	ifstream dfaFile (fileName.c_str());
	if (dfaFile.is_open()){
		DFA dfa(dfaFile, cout);
		string testInput = "";
		while( getline(dfaFile,testInput) ){
			// test the output
			cout << "\t" << testInput<< "---> " << dfa.processInput(testInput) <<endl;
		}
		dfaFile.close();
	} else {
		cout << "Failed to open \""+fileName+"\", verify the file's existance and try again." << endl;
	}

	return 0;
}
