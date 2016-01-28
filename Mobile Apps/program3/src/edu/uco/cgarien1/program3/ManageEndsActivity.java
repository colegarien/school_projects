// Cole Garien

package edu.uco.cgarien1.program3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class ManageEndsActivity extends Activity {

	private DFA dfa;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_ends);
		
		// get the dfa
		dfa = (DFA)(getIntent().getExtras().getParcelable(MainActivity.EXTRA_DFA));
		
		fillCheckGroup();
	}
	
	public void fillCheckGroup(){
		LinearLayout chk_grp_ends = (LinearLayout)(findViewById(R.id.grp_end_state));
		
        for (int i = 0; i < dfa.getNumberOfStates(); i++) {
            CheckBox chkbtn = new CheckBox(this);
            chkbtn.setId(i);
            chkbtn.setChecked(dfa.isEndState(i));
            chkbtn.setText("State " + i);
            chkbtn.setTextColor(getResources().getColor(R.color.input_text));
            chkbtn.setTextSize(getResources().getDimension(R.dimen.text_medium));
            chkbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					dfa.setEndStates(buttonView.getId(), isChecked);
				}
			});
            chk_grp_ends.addView(chkbtn);
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
