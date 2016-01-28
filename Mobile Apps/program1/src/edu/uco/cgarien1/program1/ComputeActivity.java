// Cole Garien

package edu.uco.cgarien1.program1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ComputeActivity extends Activity {
	public static final String EXTRA_NUMBER_1 = "program1.NUMBER1";
	public static final String EXTRA_NUMBER_2 = "program1.NUMBER2";
	public static final String EXTRA_RESULT = "program1.RESULT";
	
	EditText textNumber1;
	EditText textNumber2;
	TextView textResult;
	Button buttonAdd;
	Button buttonMultiply;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compute);
		
		textNumber1 = (EditText)findViewById(R.id.number1_text);
		textNumber2 = (EditText)findViewById(R.id.number2_text);
		textResult = (TextView)findViewById(R.id.result_text);
		
		buttonAdd = (Button)findViewById(R.id.add_button);
		buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	// start AddActivity
                Intent i = new Intent(ComputeActivity.this, AddActivity.class);
                
                String number1 = textNumber1.getText().toString();
                String number2 = textNumber2.getText().toString();
                
                if(number1.isEmpty()){
                	// number1 is empty!
            		displayWarning(R.string.number_1_warning);
                }else if(number2.isEmpty()){
                	// number2 is empty!
            		displayWarning(R.string.number_2_warning);
                }else{
	                i.putExtra(EXTRA_NUMBER_1, number1);
	                i.putExtra(EXTRA_NUMBER_2, number2);
	                
	                startActivityForResult(i, 0);
                }
            }
        });
		
		buttonMultiply = (Button)findViewById(R.id.multiply_button);
		buttonMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	// start MultiplyActivity
                Intent i = new Intent(ComputeActivity.this, MultiplyActivity.class);
                
                String number1 = textNumber1.getText().toString();
                String number2 = textNumber2.getText().toString();
                
                if(number1.isEmpty()){
                	// number1 is empty!
            		displayWarning(R.string.number_1_warning);
                }else if(number2.isEmpty()){
                	// number2 is empty!
            		displayWarning(R.string.number_2_warning);
                }else{
	                i.putExtra(EXTRA_NUMBER_1, number1);
	                i.putExtra(EXTRA_NUMBER_2, number2);
	                
	                startActivityForResult(i, 1);
                }
            }
        });
	}
	
	// message is int id from value in strings.xml
	private void displayWarning(int message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode == RESULT_OK)
    		textResult.setText( data.getStringExtra(EXTRA_RESULT) );
    	else {
    		// result was not returned!
    		displayWarning(R.string.nothing_returned);
    	}
    }

}
