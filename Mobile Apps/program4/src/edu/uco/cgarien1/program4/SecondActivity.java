// Cole Garien

package edu.uco.cgarien1.program4;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SecondActivity extends Activity {
	
	private Spinner spin_line;
	private Spinner spin_back;

	private int initialLen = 128;
	private float factor = 1.5f;
	private int lineColor = Color.BLACK;
	private int backColor = Color.YELLOW;
	private boolean divide = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		initialLen = getIntent().getIntExtra(MainActivity.EXTRA_LENGTH, 128);
		factor = getIntent().getFloatExtra(MainActivity.EXTRA_FACTOR, 1.5f);
		lineColor = getIntent().getIntExtra(MainActivity.EXTRA_LINECOLOR, Color.BLACK);
		backColor = getIntent().getIntExtra(MainActivity.EXTRA_BACKCOLOR,Color.YELLOW);
		divide = getIntent().getBooleanExtra(MainActivity.EXTRA_DIVIDE,true);
		
		spin_line = (Spinner)(findViewById(R.id.spinner_line_color));
		spin_back = (Spinner)(findViewById(R.id.spinner_back_color));
        // create an ArrayAdapter for the spinner
        ArrayAdapter<CharSequence> adapter =
        		ArrayAdapter.createFromResource(getApplicationContext(),
        				R.array.array_colors,
        				android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spin_line.setAdapter(adapter);
        spin_back.setAdapter(adapter);
        
        // intilize selected value
        for(int i = 0; i < adapter.getCount(); i++){
        	int curColor = SecondActivity.this.getResources().getColor(
					SecondActivity.this.getResources().getIdentifier(
							adapter.getItem(i).toString(), "color", SecondActivity.this.getPackageName()));
        	if(curColor == backColor)
        		spin_back.setSelection(i);
        	if(curColor == lineColor)
        		spin_line.setSelection(i);
        }
        
        spin_line.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				//  item selected
				lineColor = SecondActivity.this.getResources().getColor(
						SecondActivity.this.getResources().getIdentifier(
								parent.getItemAtPosition(pos).toString(), "color", SecondActivity.this.getPackageName()));
			}
			@Override
        	public void onNothingSelected(AdapterView<?> parent) {
        	}
		});
        spin_back.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				//  item selected
				backColor = SecondActivity.this.getResources().getColor(
						SecondActivity.this.getResources().getIdentifier(
								parent.getItemAtPosition(pos).toString(), "color", SecondActivity.this.getPackageName()));
			}
			@Override
        	public void onNothingSelected(AdapterView<?> parent) {
        	}
		});
        
		//intialize contorls
        ((RadioButton)findViewById(R.id.rad_divide)).setChecked(divide);
        ((RadioButton)findViewById(R.id.rad_subtract)).setChecked(!divide);
        
        ((EditText)findViewById(R.id.init_len_input)).setText(""+initialLen);
        ((EditText)findViewById(R.id.factor_input)).setText(""+factor);
        
		
	}
	
	@Override
	public void onBackPressed() {
		initialLen = Integer.parseInt(((EditText)findViewById(R.id.init_len_input)).getText().toString());
		factor = Float.parseFloat(((EditText)findViewById(R.id.factor_input)).getText().toString());
		divide = ((RadioButton)findViewById(R.id.rad_divide)).isChecked();
		
		setExtraResult();
		//Toast.makeText(SecondActivity.this, "line: "+lineColor+"\nback: "+backColor, Toast.LENGTH_SHORT).show();
		
		super.onBackPressed();
	}
	
	private void setExtraResult(){
		Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_LENGTH, initialLen);
        data.putExtra(MainActivity.EXTRA_FACTOR, factor);
        data.putExtra(MainActivity.EXTRA_LINECOLOR, lineColor);
        data.putExtra(MainActivity.EXTRA_BACKCOLOR, backColor);
        data.putExtra(MainActivity.EXTRA_DIVIDE,divide);
        setResult(RESULT_OK, data);
	}
}
