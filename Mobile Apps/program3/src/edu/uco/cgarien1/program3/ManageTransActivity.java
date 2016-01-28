// Cole Garien

package edu.uco.cgarien1.program3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ManageTransActivity extends Activity {

	private DFA dfa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_trans);
		
		// get the dfa
		dfa = (DFA)(getIntent().getExtras().getParcelable(MainActivity.EXTRA_DFA));
		
		fillTransGroup();
	}
	
	public void fillTransGroup(){
		LinearLayout grp_trans = (LinearLayout)(findViewById(R.id.grp_state_trans));
		
		String alphabet = dfa.getAlphabet();
		
		Integer[] states = new Integer[dfa.getNumberOfStates()];
		for(int i = 0; i < states.length; i++) { states[i] = i; }
		
        for (int i = 0; i < dfa.getNumberOfStates(); i++) {
        	for(int j = 0; j <alphabet.length(); j++){
        		int ddId = (i*alphabet.length()+j);
        		LinearLayout row = new LinearLayout(this);

        		TextView txtLabel = new TextView(this);
        		txtLabel.setId(ddId*dfa.getNumberOfStates());
        		txtLabel.setText("S" + i + ", " + alphabet.charAt(j));
        		txtLabel.setTextColor(getResources().getColor(R.color.label_text));
        		txtLabel.setTextSize(getResources().getDimension(R.dimen.text_medium));
        		
        		Spinner spinner = new Spinner(this);
        		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, states);
        		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        		spinner.setAdapter(adapter);
        		
        		spinner.setId(ddId);
        		spinner.setSelection(dfa.getCurrentResultState(i,""+alphabet.charAt(j)));
        		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
						// TODO Auto-generated method stub
						String alphabet = dfa.getAlphabet();
						int curState = parent.getId()/alphabet.length();
						int letterIndex = parent.getId()-(curState+curState);
						String curLetter = alphabet.substring(letterIndex, letterIndex+1);
						
						dfa.setStateTransition(curState, curLetter, pos);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						
					}

        		});
        		
	            row.addView(txtLabel);
	            row.addView(spinner);
	            grp_trans.addView(row);
        	}
        }
	}
	
	@Override
	public void onBackPressed() {
		setExtraResult();
		
		super.onBackPressed();
	}

	private void setExtraResult(){
		Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_RETURN_DFA, (Parcelable)dfa);
        setResult(RESULT_OK, data);
	}
}
