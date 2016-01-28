// Cole Garien

package edu.uco.cgarien1.program3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ManageStartsActivity extends Activity {

	private DFA dfa;
	
	private RadioGroup rad_grp_starts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_starts);
		
		// get the dfa
		dfa = (DFA)(getIntent().getExtras().getParcelable(MainActivity.EXTRA_DFA));
		
		rad_grp_starts = (RadioGroup) findViewById(R.id.grp_start_state);
		fillRadioGroup();
		rad_grp_starts.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				for(int i = 0; i < dfa.getNumberOfStates(); i++)
					if(i==checkedId)
						dfa.setStartState(checkedId);
				
			}
		});
		
	}
	
	public void fillRadioGroup(){
            for (int i = 0; i < dfa.getNumberOfStates(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(i);
                rdbtn.setText("State " + i);
                rdbtn.setTextColor(getResources().getColor(R.color.input_text));
                rdbtn.setTextSize(getResources().getDimension(R.dimen.text_medium));
                rad_grp_starts.addView(rdbtn);
            }
            rad_grp_starts.check(dfa.getStartState());
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
