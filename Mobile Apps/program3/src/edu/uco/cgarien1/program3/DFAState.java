// Cole Garien

package edu.uco.cgarien1.program3;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class DFAState implements Parcelable{
	// format of a transition is: letter,resultState
	private ArrayList<String> transitions = new ArrayList<String>();
	
	public DFAState(){
		transitions= new ArrayList<String>();
	}
	
	// Parcelling part
	public DFAState(Parcel in){
		//transitions= (ArrayList<String>) in.readSerializable();
		in.readList(transitions, String.class.getClassLoader());
    }
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flag) {
		// TODO Auto-generated method stub
		//out.writeSerializable(transitions);
		out.writeList(transitions);
	}
	
	public void initTransitions(String alphabet){
		// defaults all transitions to goto state 0
		transitions.clear();
		for(int i = 0; i < alphabet.length(); i++)
			transitions.add(alphabet.charAt(i) + ",0");
	}
	
	// changes transition
	public void modTransition(String letter, int state){
		for(int i = 0; i<transitions.size(); i++){
			String[] trans = transitions.get(i).split(",");
			if(trans[0].equals(letter)){
				transitions.set(i, trans[0]+","+state);
				return; // done
			}
		}
	}
	
	// get resulting state due to input
	public int getTransition(String in){
		int out = -1;

		for(int i = 0; i<transitions.size(); i++){
			String[] trans = transitions.get(i).split(",");
			if(trans[0].equals(in)){
				return Integer.parseInt(trans[1]);
			}
		}
		
		return out;
	}
	
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DFAState createFromParcel(Parcel in) {
            return new DFAState(in); 
        }
        public DFAState[] newArray(int size) {
            return new DFAState[size];
        }
    };

}
