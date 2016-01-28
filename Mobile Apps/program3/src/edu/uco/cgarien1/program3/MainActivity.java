// Cole Garien

package edu.uco.cgarien1.program3;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String EXTRA_DFA = "program3.dfa";
	public static final String EXTRA_RETURN_DFA = "program3.settings_dfa";

	
	private DFA dfa = new DFA();
	
	private Button btnSettings;
	private Button btnStates;
	private Button btnTestInput;
	private Switch swchReset;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		dfa.setNumberOfStates(3);
		dfa.setAlphabet("ab");
		dfa.setStartState(0);
		ArrayList<Integer> tempEnds = new ArrayList<Integer>();
		tempEnds.add(0);
		tempEnds.add(1);
		dfa.setEndStates(tempEnds);
		dfa.setStateTransition(0, "a", 0);
		dfa.setStateTransition(0, "b", 1);
		
		dfa.setStateTransition(1, "a", 2);
		dfa.setStateTransition(1, "b", 1);
		
		dfa.setStateTransition(2, "a", 0);
		dfa.setStateTransition(2, "b", 1);
		 */
		
		btnSettings = (Button)(findViewById(R.id.btn_settings));
		btnSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// goto the settings activity (pass dfa for modification)
				Intent i = new Intent(MainActivity.this, SettingsActivity.class);
				i.putExtra(EXTRA_DFA, (Parcelable)dfa);
				startActivityForResult(i, 1);
			}
		});
		
		btnStates = (Button)(findViewById(R.id.btn_states));
		btnStates.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dfa.getNumberOfStates()>0){
					// goto the state manage activity (pass dfa for modification)
					Intent i = new Intent(MainActivity.this, ManageStateActivity.class);
					i.putExtra(EXTRA_DFA, (Parcelable)dfa);
					startActivityForResult(i, 2);
				}else{
					Toast.makeText(MainActivity.this, "Atleast one state required,\nCheck settings", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnTestInput = (Button)(findViewById(R.id.btn_test));
		btnTestInput.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dfa.getNumberOfStates()>0){
					// goto the test activity (pass dfa for modification)
					Intent i = new Intent(MainActivity.this, TestInputActivity.class);
					i.putExtra(EXTRA_DFA, (Parcelable)dfa);
					
					// test input does not modify the dfa so no results expected
					startActivity(i);
				}else{
					Toast.makeText(MainActivity.this, "Atleast one state required,\nCheck settings", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		swchReset = (Switch)(findViewById(R.id.swch_reset));
		swchReset.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		            // The toggle is enabled reset dfa and this switch
		        	dfa = new DFA();
		        	swchReset.setChecked(false);
		        	Toast.makeText(MainActivity.this, "DFA reset to Defaults", Toast.LENGTH_SHORT).show();
		        } else {
		            // The toggle is disabled
		        }
		    }
		});
	}
	

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode==RESULT_OK){
	    	// update the dfa
	    	dfa = (DFA)(data.getExtras().getParcelable(EXTRA_RETURN_DFA));
    	}
    }
}
