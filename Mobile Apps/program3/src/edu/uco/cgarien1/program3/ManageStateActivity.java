// Cole Garien

package edu.uco.cgarien1.program3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ManageStateActivity extends Activity {

	private DFA dfa;
	
	private Button btn_starts;
	private Button btn_ends;
	private Button btn_trans;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_state);
		
		// get the dfa
		dfa = (DFA)(getIntent().getExtras().getParcelable(MainActivity.EXTRA_DFA));
		
		btn_starts = (Button)(findViewById(R.id.btn_mng_starts));
		btn_starts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// setting start statess
				Intent i = new Intent(ManageStateActivity.this, ManageStartsActivity.class);
				i.putExtra(MainActivity.EXTRA_DFA, (Parcelable)dfa);
				startActivityForResult(i, 4);
				
			}
		});
		
		btn_ends = (Button)(findViewById(R.id.btn_mng_ends));
		btn_ends.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// settign end states
				Intent i = new Intent(ManageStateActivity.this, ManageEndsActivity.class);
				i.putExtra(MainActivity.EXTRA_DFA, (Parcelable)dfa);
				startActivityForResult(i, 5);
				
			}
		});
		
		btn_trans = (Button)(findViewById(R.id.btn_mng_trans));
		btn_trans.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// setting/modifying transitions
				Intent i = new Intent(ManageStateActivity.this, ManageTransActivity.class);
				i.putExtra(MainActivity.EXTRA_DFA, (Parcelable)dfa);
				startActivityForResult(i, 6);
			}
		});
	}
	
	private void setExtraResult(){
		Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_RETURN_DFA, (Parcelable)dfa);
        setResult(RESULT_OK, data);
	}
	

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode==RESULT_OK){
	    	// update the dfa
	    	dfa = (DFA)(data.getExtras().getParcelable(MainActivity.EXTRA_RETURN_DFA));
	    	setExtraResult();
    	}
    }
}
