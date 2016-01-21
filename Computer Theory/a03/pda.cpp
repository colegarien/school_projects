/*
	Cole Garien
	Assignment 3
	Due Wednesday, October 22nd
	Theory of Computing, MW 5:45
*/

#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <stack>
#include <string>
#include <cstdlib> // for atoi

using namespace std;

const string ACCEPT_MSG = "ACCEPT";
const string REJECT_MSG = "REJECT";
const char DELTA = 'D';
const int ACT_NONE = 0;
const int ACT_READ = 1;
const int ACT_PUSH = 2;
const int ACT_POP = 3;

struct Transition{
private:
	char input;
	int output;
public:
	Transition(char i, int o){
		input = i;
		output = o;
	}
	int makeTransition(char in){
		if(in == input)
			return output;		
		return -1;
	}
};

struct State {
private:
	bool accepting;
	bool rejecting;
	int action;
	vector<Transition> transitions;
public:
	void addTransition(Transition t){
		transitions.push_back(t);
	}
	void setAccepting(bool a){ accepting = a; }
	void setRejecting(bool r){ rejecting = r; }
	void setAction(int a){ action = a; }
	bool isAccepting(){ return accepting; }
	bool isRejecting(){ return rejecting; }
	int getAction(){ return action; }

	int makeTransition(char in){
		int out = -1;
		for(int i = 0; i < transitions.size(); i++){
			out = transitions.at(i).makeTransition(in);
			if(out != -1)
				return out;
		}
		return out;
	}
};

struct PDA{
private:
	string comment;
	string alphabet;
	int startState;		// index of start state
	vector<State> states;
	stack<char> theStack;

	void emptyStack(){
		while (!theStack.empty()){
			theStack.pop();
		}
		theStack.push(DELTA);
	}
public:
	PDA(){
		comment = "";
		alphabet = "";
	}
	PDA(string c, string a, vector<State> s){
		comment = c;
		alphabet = a;
		states = s;
	}
	PDA(istream& in, ostream& out){

		getline(in, comment);	// get the comment line
		getline(in, alphabet);	// get the alphabet
		string line = "";
		getline(in, line);		// get the number of states
		int numStates = atoi(line.c_str()); // store it as int

		// create each state
		for(int i = 0; i < numStates; i++){
			getline(in, line);		// get the transition line
			stringstream transReader(line);
			states.push_back(State()); // add state to list
			
			// a state to goto for each letter in alphabet
			for (int a = 0; a < alphabet.length(); a++){
				string tempItem = "";

				// state to goto
				transReader >> tempItem;
				int toState = atoi(tempItem.c_str());
				
				Transition tempTrans(alphabet.at(a), toState);
				states.at(i).addTransition(tempTrans);
			}

		}
		
		getline(in, line); // start state#
		startState = atoi(line.c_str()); // assign state with this id

		// setup state action and etc
		for(int i = 0; i < numStates; i++){
			getline(in, line);		// get the transition line
			stringstream transReader(line);
			string tempItem = "";

			// state to goto
			transReader >> tempItem;
			int accepting = atoi(tempItem.c_str());
			// state to goto
			transReader >> tempItem;
			int rejecting = atoi(tempItem.c_str());
			// state to goto
			transReader >> tempItem;
			int action = atoi(tempItem.c_str());
				
			states.at(i).setAccepting(accepting);
			states.at(i).setRejecting(rejecting);
			states.at(i).setAction(action);
		}

		out<<"Comment: "<<comment<<endl;
		out<<"Alphabet: "<<alphabet<<endl;
	}

	string processInput(string tape){
		emptyStack();
		
		int currentState = startState;
		int tapeHead = 0;
		
		// loop until reject or accept msg is returned
		while(true){
			char currentLetter = DELTA;
			if(tapeHead < tape.length())
				currentLetter = tape.at(tapeHead);
			
			int nextState = -1;
			int curAction = states.at(currentState).getAction();
			if(curAction != ACT_POP)
					nextState = states.at(currentState).makeTransition(currentLetter);

			switch(curAction){
				case ACT_NONE:
					// nothing special is done here
					break;
				case ACT_READ:
					tapeHead++;
					break;
				case ACT_PUSH:
					if(tapeHead - 1 >= 0 && tapeHead <= tape.length())
						theStack.push(tape.at(tapeHead-1)); // push letter that was last read
					break;
				case ACT_POP:
					// pop the stack
					nextState = states.at(currentState).makeTransition(theStack.top());
					if(theStack.top()!=DELTA)
						theStack.pop();
					break;
			}

			if(nextState != -1)
				currentState = nextState;

			if(states.at(currentState).isAccepting())
				return ACCEPT_MSG;
			else if(states.at(currentState).isRejecting())
				return REJECT_MSG;
		}

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

	ifstream pdaFile (fileName.c_str());
	if (pdaFile.is_open()){
		PDA pda(pdaFile, cout);
		string testInput = "";
		while( getline(pdaFile,testInput) ){
			// test the output
			cout << "\t" << testInput<< "---> " << pda.processInput(testInput) <<endl;
		}
		pdaFile.close();
	} else {
		cout << "Failed to open \""+fileName+"\", verify the file's existance and try again." << endl;
	}
	return 0;
}