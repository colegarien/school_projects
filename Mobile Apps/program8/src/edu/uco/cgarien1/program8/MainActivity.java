// Cole Garien

package edu.uco.cgarien1.program8;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.frame);
		final MySurfaceView bubbleView = new MySurfaceView(getApplicationContext());

		relativeLayout.addView(bubbleView);
	}
	
	
	private class MySurfaceView extends SurfaceView implements
	SurfaceHolder.Callback {
		private final DisplayMetrics mDisplay;
		private final int mDisplayWidth, mDisplayHeight;
		private final SurfaceHolder mSurfaceHolder;
		private final Paint mPainter = new Paint();
		private Thread mDrawingThread;
		
		private static final int NUM_OBJ = 10;
		private static final int OBJ_RAD = 15;
		private static final int OBJ_PAD = OBJ_RAD+5;
		private static final float MAX_SPD = 1.5f;
		
		private ArrayList<MyShape> shapes = new ArrayList<MyShape>();
		
		public MySurfaceView(Context context) {
			super(context);
		
			// general info about the display; size, density, font scaling, etc
			mDisplay = new DisplayMetrics();
			MainActivity.this.getWindowManager().getDefaultDisplay()
					.getMetrics(mDisplay);
			mDisplayWidth = mDisplay.widthPixels;
			mDisplayHeight = mDisplay.heightPixels;
		
			mPainter.setAntiAlias(true);
		
			mSurfaceHolder = getHolder();
			mSurfaceHolder.addCallback(this);
			
			Rect screenBounds = new Rect(0,0,mDisplayWidth,mDisplayHeight-73);
			for(int i  = 0; i < NUM_OBJ; i+=2){
				shapes.add(new MySquare(screenBounds.left + OBJ_PAD + (float)(Math.random()*(screenBounds.right-(OBJ_PAD*2))),
						screenBounds.top+OBJ_PAD+i*((screenBounds.height())/(NUM_OBJ)),
						OBJ_RAD,
						(int)(MAX_SPD*Math.pow(-1,(int)(Math.random()*2))),
						(int)(MAX_SPD*Math.pow(-1,(int)(Math.random()*2))),
						Color.rgb((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256)),
						screenBounds));
				shapes.add(new MyCircle(screenBounds.left + OBJ_PAD + (float)(Math.random()*(screenBounds.right-(OBJ_PAD*2))),
						screenBounds.top+OBJ_PAD+(i+1)*((screenBounds.height())/(NUM_OBJ)),
						OBJ_RAD,
						(int)(MAX_SPD*Math.pow(-1,(int)(Math.random()*2))),
						(int)(MAX_SPD*Math.pow(-1,(int)(Math.random()*2))),
						Color.rgb((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256)),
						screenBounds));
			}
		}
		private void Update() {
			// update objects here
			// **should combine collision checks with movement
			for(int i = 0; i<shapes.size(); i++){
				// move the shape
				shapes.get(i).Update();
				
				// needs to be optimized
				for(int j = 0; j <shapes.size(); j++)
					if(j!=i)
						MyShape.Collision(shapes.get(i), shapes.get(j));
			}
		}
		private void Draw(Canvas canvas) {
			canvas.drawColor(Color.BLACK);
			
			// draw objects here
			for(int i = 0; i<shapes.size(); i++){
				shapes.get(i).Draw(canvas, mPainter);
			}
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mDrawingThread = new Thread(new Runnable() {
				public void run() {
					Canvas canvas = null;
					while (!Thread.currentThread().isInterrupted()) {
						// log time that this frame starts
						Date time = new Date();
					    long passedTime = time.getTime();
						
						Update();
						canvas = mSurfaceHolder.lockCanvas();
						if (null != canvas) {
							Draw(canvas);
							mSurfaceHolder.unlockCanvasAndPost(canvas);
						}
						
						// find time after frame is finished
						time = new Date();
				        // find time difference (this will be negative)
				        passedTime-=time.getTime();
				        try {
				        	// lock the frame rate at about 20 fps
				            Thread.sleep(Math.max(10,50+passedTime));
				        } catch (InterruptedException e) {
				            // something un-explainable went wrong
				        }
					}
				}
			});
			mDrawingThread.start();
		}
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (null != mDrawingThread)
				mDrawingThread.interrupt();
		}
		
	}
}
