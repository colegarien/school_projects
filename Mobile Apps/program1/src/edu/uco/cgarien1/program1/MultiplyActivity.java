// Cole Garien

package edu.uco.cgarien1.program1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MultiplyActivity extends Activity {

	TextView textNumber1;
	TextView textNumber2;
	TextView textResult;
	Button buttonComputeMultiply;
	
	private void setExtraResult(){
		Intent data = new Intent();
        data.putExtra(ComputeActivity.EXTRA_RESULT, textResult.getText().toString());
        setResult(RESULT_OK, data);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multiply);
		
		textNumber1 = (TextView)findViewById(R.id.number1_text);
		textNumber1.setText(getIntent().getStringExtra(ComputeActivity.EXTRA_NUMBER_1));
		
		textNumber2 = (TextView)findViewById(R.id.number2_text);
		textNumber2.setText(getIntent().getStringExtra(ComputeActivity.EXTRA_NUMBER_2));
		
		textResult = (TextView)findViewById(R.id.result_text);
		
		buttonComputeMultiply = (Button)findViewById(R.id.compute_multiply_button);
		buttonComputeMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	float result = ( Float.parseFloat(textNumber1.getText().toString())
            				   * Float.parseFloat(textNumber2.getText().toString()) );
            	
            	// empty string concat so result will parse to a string
            	textResult.setText(""+result);
            	setExtraResult();
            }
        });
	}
}
