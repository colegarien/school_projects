// Cole Garien

package edu.uco.cgarien1.program3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TestInputActivity extends Activity {

	private DFA dfa;
	
	private ImageButton btnRunTest;
	private TextView testOutput;
	private EditText testInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_input);
		
		// get the dfa
		dfa = (DFA)(getIntent().getExtras().getParcelable(MainActivity.EXTRA_DFA));
		
		testOutput = (TextView)(findViewById(R.id.test_output));
		testInput = (EditText)(findViewById(R.id.test_input));

		btnRunTest = (ImageButton)(findViewById(R.id.btn_run_test));
		btnRunTest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// get test inputs from the input box if any
				String[] testInputs = testInput.getText().toString().split("\n");
				if(testInputs.length == 0)
					Toast.makeText(TestInputActivity.this, "No inputs!", Toast.LENGTH_SHORT).show();
				else{
					String output = "";
					for(int i = 0; i< testInputs.length; i++){
						output+= dfa.proccessInput(testInputs[i]);
						output+="\n";
					}
					testOutput.setText(output);
					testInput.setText("");
				}
				
			}
		});
	}
}
