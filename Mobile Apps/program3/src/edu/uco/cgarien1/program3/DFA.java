// Cole Garien

package edu.uco.cgarien1.program3;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class DFA implements Parcelable{
	private boolean acceptNull = false;
	private String alphabet = "ab";
	private int startState = 0;
	private ArrayList<Integer> endStates=new ArrayList<Integer>();
	private ArrayList<DFAState> states = new ArrayList<DFAState>();
	
	// GETTER/SETTER
	public void setStartState(int start){ startState = start; }
	public int getStartState(){ return startState; }
	public void setEndStates(ArrayList<Integer> end){ endStates = end; }
	public ArrayList<Integer> getEndStates(){ return endStates; }
	public void setAlphabet(String a){
		alphabet = a;
	}
	public String getAlphabet(){ return alphabet; }
	public void setNumberOfStates(int s){
		startState = 0;
		
		if(states!=null)
			states.clear();
		else
			states = new ArrayList<DFAState>();

		for(int i = 0; i < s; i++){
			DFAState tempState = new DFAState();
			tempState.initTransitions(alphabet);
			states.add(tempState);
		}
	}
	public int getNumberOfStates(){
		if(states!=null)
			return states.size();
		else{
			states = new ArrayList<DFAState>();
			return 0;
		}
	}
	public void setStateTransition(int state, String letter, int resState){
		if(state >= 0 && state < states.size())
			states.get(state).modTransition(letter, resState);
	}
	public void setAcceptNull(boolean n) { acceptNull=n; }
	public boolean getAcceptNull(){ return acceptNull; }
	//END GETTER/SETTER
	
	public DFA(){
		alphabet="ab";
		acceptNull = false;
		startState=0;
		endStates=new ArrayList<Integer>();
		states = new ArrayList<DFAState>();
	}
	
	// Parcelling part
    public DFA(Parcel in){
    	
    	alphabet= in.readString();
		acceptNull = in.readByte() != 0;
		startState= in.readInt();
		in.readList(endStates, Integer.class.getClassLoader());
		//endStates= (ArrayList<Integer>) in.readSerializable();
		//in.readTypedList(states, DFAState.CREATOR);
		in.readList(states, getClass().getClassLoader());
    }
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flag) {
		// TODO Auto-generated method stub
		out.writeString(alphabet);
		out.writeByte((byte) (acceptNull ? 1 : 0));
		out.writeInt(startState);
		out.writeList(endStates);
		//out.writeSerializable(endStates);
		//out.writeTypedList(states);
		out.writeList(states);
	}
	
	
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DFA createFromParcel(Parcel in) {
            return new DFA(in); 
        }
        public DFA[] newArray(int size) {
            return new DFA[size];
        }
    };

    public String proccessInput(String in){
    	// accepting and rejecting null string
    	if(in.isEmpty() && acceptNull)
			return "ACCEPT->"+in;
    	else if(in.isEmpty() && !acceptNull)
			return "REJECT->"+in;
    		
    	int currentState = startState;
    	for(int i = 0; i < in.length(); i++){
    		int nextState = -1;
    		
    		// index of letter in the alphabet
    		int letterIndex = alphabet.indexOf(in.charAt(i));
    		if(letterIndex == -1)
    			return "REJECT->"+in+"(has foreign letter)";
    		
    		nextState = states.get(currentState).getTransition(in.substring(i, i+1));
    		
    		if(nextState!=-1)
    			currentState = nextState;
    	}
    	
    	// check if currentstate is end state and if so then accept
    	for(int i = 0; i < endStates.size(); i++)
    		if(currentState == endStates.get(i))
    			return "ACCEPT->"+in;
    	
    	return "REJECT->"+in;
    }
	public void setEndStates(int id, boolean add) {
		for(int i = 0; i < endStates.size(); i++){
			if(endStates.get(i) == id){
				if(!add){
					// remove the state
					endStates.remove(i);
					return;
				}else{
					// state already in
					return;
				}
					
			}
		}
		// if not already in the array then add the state
		if(add)
			endStates.add(id);
	}
	public boolean isEndState(int id) {
		
		for(int i = 0; i < endStates.size(); i++){
			if(id == endStates.get(i))
				return true;
		}
		
		return false;
	}
	public int getCurrentResultState(int state, String letter) {
		// TODO Auto-generated method stub
		int out = states.get(state).getTransition(letter);
		
		if(out != -1)
			return out;
		
		return 0;
	}
}
