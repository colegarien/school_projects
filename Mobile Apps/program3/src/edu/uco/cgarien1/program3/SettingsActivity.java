// Cole Garien

package edu.uco.cgarien1.program3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {
	private static final String[] ALPHABETS = new String[] {
        "ab", "abc", "abcdef","abcdefghijklmnopqrstuvwxyz", "wxyz"
    };

	private DFA dfa;
	
	private AutoCompleteTextView alphabet_text;
	private EditText number_states;
	private ToggleButton toggle_null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		// get the dfa
		dfa = (DFA)(getIntent().getExtras().getParcelable(MainActivity.EXTRA_DFA));
		
		alphabet_text = (AutoCompleteTextView)(findViewById(R.id.alphabet_text));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ALPHABETS);
		alphabet_text.setAdapter(adapter);
		alphabet_text.setText(dfa.getAlphabet());
		
		number_states = (EditText)(findViewById(R.id.number_states));
		number_states.setText((String)(""+dfa.getNumberOfStates()));
		
		toggle_null = (ToggleButton)(findViewById(R.id.toggle_null));
		toggle_null.setChecked(dfa.getAcceptNull());
		toggle_null.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        dfa.setAcceptNull(isChecked);
		    }
		});
	}
	
	@Override
	public void onBackPressed() {
		String tempAlphabet = alphabet_text.getText().toString().trim().toLowerCase();
		String tempStateNumber = number_states.getText().toString().trim();
		
	    dfa.setAlphabet(tempAlphabet);
	    if(tempStateNumber.isEmpty())
	    	dfa.setNumberOfStates(0);
	    else
	    	dfa.setNumberOfStates(Integer.parseInt(tempStateNumber));
	    
		setExtraResult();
		
		super.onBackPressed();
	}

	private void setExtraResult(){
		Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_RETURN_DFA, (Parcelable)dfa);
        setResult(RESULT_OK, data);
	}
}
