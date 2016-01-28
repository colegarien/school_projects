// Cole Garien

package edu.uco.cgarien1.program4;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.ZoomControls;

public class MainActivity extends Activity {
	public static final String EXTRA_LENGTH = "program4.LENGTH",
							   EXTRA_FACTOR="program4.FACTOR",
							   EXTRA_LINECOLOR="program4.LINECOLOR",
							   EXTRA_BACKCOLOR="program4.BACKCOLOR",
							   EXTRA_DIVIDE="program4.DIVIDE";
	
	private ImageView imgView;
	private Button btnGenerate;
	private Button btnSettings;
	private SeekBar leftBar;
	private SeekBar rightBar;
	private ZoomControls lineWidthControls;
	
	private int intialLen = 128;
	private float intialAng = -(float)Math.PI/2f;
	private float leftAng = -0.1f;
	private float rightAng = 0.1f;
	private int minLen = 1;
	private float factor = 1.5f;
	private int lineColor = Color.BLACK;
	private int backColor = Color.YELLOW;
	private int lineWidth = 1;
	private Paint linePaint = new Paint();
	private boolean divide = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imgView = (ImageView)(findViewById(R.id.surfTree));
		
		btnGenerate = (Button)(findViewById(R.id.btnGenerate));
		btnGenerate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				drawFractal();
			}
		});
		
		btnSettings = (Button)(findViewById(R.id.btnSettings));
		btnSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainActivity.this, SecondActivity.class);
				
				i.putExtra(EXTRA_LENGTH, intialLen);
                i.putExtra(EXTRA_FACTOR, factor);
				i.putExtra(EXTRA_LINECOLOR, lineColor);
                i.putExtra(EXTRA_BACKCOLOR, backColor);
				i.putExtra(EXTRA_DIVIDE,divide);
                
                startActivityForResult(i, 1);
			}
		});
		
		leftBar = (SeekBar)(findViewById(R.id.barLeftAngle));
		leftBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// not used
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// not used
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// turn progress into a number from -(PI/2) to (PI/2)
				float angle =  (float)progress/(float)seekBar.getMax();
				angle= (angle*2f - 1f) * (float)(Math.PI/2f);
				
				leftAng = angle;
			}
		});
		
		rightBar = (SeekBar)(findViewById(R.id.barRightAngle));
		rightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// not used
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// not used
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// turn progress into a number from -(PI/2) to (PI/2)
				float angle =  (float)progress/(float)seekBar.getMax();
				angle= (angle*2f - 1f) * (float)(Math.PI/2f);
				
				// Note that the right bar has been flipped
				rightAng = -angle;
			}
		});
		
		lineWidthControls = (ZoomControls)(findViewById(R.id.lineWidthControls));
		lineWidthControls.setOnZoomInClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(lineWidth + 1 <= 10){
					lineWidth+=1;
					linePaint.setStrokeWidth(toPixel(lineWidth));
				}
			}
		});
		lineWidthControls.setOnZoomOutClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(lineWidth-1 >= 1){
					lineWidth-=1;
					linePaint.setStrokeWidth(toPixel(lineWidth));
				}
			}
		});
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		leftBar.setProgress((int)(  ( (leftAng +(Math.PI/2f)) /Math.PI )*leftBar.getMax()));
		// right bar is upside down so rightAng is inverted for calculation
		rightBar.setProgress((int)( ( (-rightAng+(Math.PI/2f)) /Math.PI )*rightBar.getMax()));
		
		// imageview loaded and can be drawn on now
		drawFractal();
	}
	
	public void drawFractal(){
		
		Bitmap toDraw = Bitmap.createBitmap(imgView.getWidth(),imgView.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(toDraw);
		canvas.drawColor(backColor);
		
		drawFractal(imgView.getWidth()/2,imgView.getHeight(),(int)toPixel(intialLen),intialAng,canvas);
		
		imgView.setImageBitmap(toDraw);
	}
	
	public void drawFractal(int x, int y, int len, float angle, Canvas c){
		int endX = (int)(x+Math.cos(angle)*len);
		int endY = (int)(y+Math.sin(angle)*len);
		c.drawLine(x, y, endX, endY, linePaint);
		if(len > minLen){
			// if divide then divide, else subtract
			int nextLen = (divide ? (int)(len/factor) : (int)(len-factor));
			drawFractal(endX, endY, nextLen,angle+leftAng,c);
			drawFractal(endX, endY, nextLen,angle+rightAng,c);
		}
	}
	
	// makes units used (for drawn stuff) look consistent
	public static float toPixel(int dp){
		float px = dp * Resources.getSystem().getDisplayMetrics().density;
		return px;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode == RESULT_OK){
    		intialLen = data.getIntExtra(EXTRA_LENGTH, 128);
    		factor = data.getFloatExtra(EXTRA_FACTOR, 1.5f);
    		lineColor = data.getIntExtra(EXTRA_LINECOLOR, Color.BLACK);
    		backColor = data.getIntExtra(EXTRA_BACKCOLOR,Color.YELLOW);
    		divide = data.getBooleanExtra(EXTRA_DIVIDE,true);

    		linePaint.setColor(lineColor);
    		
			drawFractal();
    	}
    }
}
