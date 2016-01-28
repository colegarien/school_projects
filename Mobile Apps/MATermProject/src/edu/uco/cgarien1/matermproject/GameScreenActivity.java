// Cole Garien

package edu.uco.cgarien1.matermproject;

import java.util.Date;

import edu.uco.cgarien1.matermproject.Tile.TileType;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GameScreenActivity extends Activity{
	public static final String EXTRA_STAT_ENEMY_KILLED ="thetowerdefense.stat.enemyskilled";
	public static final String EXTRA_STAT_MONEY_SPENT = "thetowerdefense.stat.moneyspent";
	public static final String EXTRA_STAT_TOWERS_BOUGHT="thetowerdefense.stat.towersbought";
	public static final String EXTRA_STAT_TOWERS_SOLD = "thetowerdefense.stat.towersold";
	public static final String EXTRA_STAT_TRAPS_BOUGHT ="thetowerdefense.stat.trapsbought";
	public static final String EXTRA_STAT_BULLETS_SHOT ="thetowerdefense.stat.bulletsshot";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);

		int tilesetId = getIntent().getIntExtra(MainMenuActivity.EXTRA_TILESET_ID, R.drawable.tileset_debug);
		int levelId = getIntent().getIntExtra(MainMenuActivity.EXTRA_LEVEL_ID, R.drawable.level_0000);
		
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.frame);
		final GameSurfaceView gameView = new GameSurfaceView(getApplicationContext(), levelId, tilesetId,(RelativeLayout)findViewById(R.id.buy_menu));
		
		relativeLayout.addView(gameView,0);
		
	}
	
	// used to unify units for all screens
	public static float toPixel(int dp){
		float px = 0;
		px = dp * Resources.getSystem().getDisplayMetrics().density;
		return px;
	}
	
	private class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener {
		private final DisplayMetrics mDisplay;
		private final int mDisplayWidth, mDisplayHeight;
		private final SurfaceHolder mSurfaceHolder;
		private final Paint mPainter = new Paint();
		private Thread mDrawingThread;
		
		private Level level;
		private GestureDetector mDetector;
		private PointF mapPos = new PointF(0f,0f);
		
		private RelativeLayout buyMenu;
		private Button btnSell;
		private ListView towerBuyList;
		private ListView obstacleBuyList;
		private ListView upgradeBuyList;
		

		private BuyMenuListItem buy_towers_data[] = new BuyMenuListItem[]
	            {
	                new BuyMenuListItem("Small Tower", 100.00f, "weak tower"),
	                new BuyMenuListItem("Fast Tower", 150.00f, "shoot fast"),
	                new BuyMenuListItem("Medium Tower", 200.00f, "okay tower"),
	                new BuyMenuListItem("Power Tower", 300.00f, "strong damaage"),
	                new BuyMenuListItem("DEMO Tower", 0.00f, "Tower for DEMO")
	            };
		private BuyMenuListItem buy_obstacle_data[] = new BuyMenuListItem[]
	            {
	                new BuyMenuListItem("Slowing Trap", 150.00f, "Slows the enemy"),
	                new BuyMenuListItem("Damage Trap", 200.00f, "Hurts the enemy")
	            };
		
		public GameSurfaceView(Context context, int levelId, int tilesetId, RelativeLayout buyMenu) {
			super(context);
		
			// general info about the display; size, density, font scaling, etc
			mDisplay = new DisplayMetrics();
			GameScreenActivity.this.getWindowManager().getDefaultDisplay()
					.getMetrics(mDisplay);
			mDisplayWidth = mDisplay.widthPixels;
			mDisplayHeight = mDisplay.heightPixels;
		
			mPainter.setAntiAlias(true);
		
			mSurfaceHolder = getHolder();
			mSurfaceHolder.addCallback(this);

			// to prevent problems with loaded resources
			Options ops = new Options();
			ops.inScaled=false;

			// set tileset
			TileSetSingleton.setBitmap(BitmapFactory.decodeResource(getResources(), tilesetId,ops));
			// create level
			level = new Level(BitmapFactory.decodeResource(getResources(), levelId,ops), mPainter);

			mDetector = new GestureDetector(GameScreenActivity.this, this);
			this.buyMenu=buyMenu;
			this.hideBuyMenu();
			this.buyMenu.findViewById(R.id.btn_buy_close).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					hideBuyMenu();
					level.setSelection(false);
				}
			});
			
			towerBuyList = (ListView) this.buyMenu.findViewById(R.id.list_buy_tower);
			BuyMenuListItemAdapter adapter = new BuyMenuListItemAdapter(GameScreenActivity.this, R.layout.buy_menu_list_item, buy_towers_data);
			towerBuyList.setAdapter(adapter);
			towerBuyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> list, View view,int position, long id) {
					hideBuyMenu();
					level.addTower(position,buy_towers_data[position].price);
					level.setSelection(false);
				}
			});
			obstacleBuyList = (ListView) this.buyMenu.findViewById(R.id.list_buy_obstacle);
			adapter = new BuyMenuListItemAdapter(GameScreenActivity.this, R.layout.buy_menu_list_item, buy_obstacle_data);
			obstacleBuyList.setAdapter(adapter);
			obstacleBuyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> list, View view,int position, long id) {
					hideBuyMenu();
					level.addObstacle(position,buy_obstacle_data[position].price);
					level.setSelection(false);
				}
			});
			upgradeBuyList = (ListView) this.buyMenu.findViewById(R.id.list_buy_upgrade);
			//adapter = new BuyMenuListItemAdapter(GameScreenActivity.this, R.layout.buy_menu_list_item, buy_upgrade_data);
			//upgradeBuyList.setAdapter(adapter);
			upgradeBuyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> list, View view,int position, long id) {
					hideBuyMenu();
					level.upgradeTower();
					level.setSelection(false);
				}
			});
			btnSell = (Button) this.buyMenu.findViewById(R.id.btn_buy_sell);
			btnSell.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					hideBuyMenu();
					level.sellTower();
					level.setSelection(false);
				}
			});
		}
		
		private void hideBuyMenu() {
			level.setSelection(false);
			buyMenu.setVisibility(View.GONE);
		}
		private void showBuyMenu() {
			buyMenu.setVisibility(View.GONE);
			towerBuyList.setVisibility(View.GONE);
			obstacleBuyList.setVisibility(View.GONE);
			upgradeBuyList.setVisibility(View.GONE);
			btnSell.setVisibility(View.GONE);
			buyMenu.bringToFront();
			buyMenu.requestFocus();
			buyMenu.invalidate();
			this.invalidate();
			buyMenu.setVisibility(View.VISIBLE);
			switch(level.getSelectionBox().getSelectionType()){
				case Empty:
					towerBuyList.setVisibility(View.VISIBLE);
					break;
				case Path:
					obstacleBuyList.setVisibility(View.VISIBLE);
					break;
				case Tower:
					BuyMenuListItem buy_upgrade_data[] = new BuyMenuListItem[]{new BuyMenuListItem("UPGRADE TOWER", level.towerList.get(level.getSelectionBox().getTowerIndex()).getUpgradeCost(), "upgrade range, rate, and power")};
					upgradeBuyList.setAdapter(new BuyMenuListItemAdapter(GameScreenActivity.this, R.layout.buy_menu_list_item, buy_upgrade_data));
					upgradeBuyList.setVisibility(View.VISIBLE);
					btnSell.setVisibility(View.VISIBLE);
					break;
				default:
					hideBuyMenu();
					break;
			}
		}
		
		private void Update() {
			// update objects here
			level.update();
			
			// need to gameover?
			if(level.isGameOver()){
				if (null != mDrawingThread)
					mDrawingThread.interrupt();
				Intent intent = new Intent(GameScreenActivity.this, GameOverActivity.class);
				intent.putExtras(level.getStats());
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}
		private void Draw(Canvas canvas) {
			canvas.drawColor(Color.DKGRAY);
			// real draw
			Bitmap tempBitmap = Bitmap.createBitmap(level.getSize().x, level.getSize().y,Config.ARGB_8888);
			Canvas tempCanvas = new Canvas(tempBitmap);
			//level.draw(canvas, mPainter);
			level.draw(tempCanvas, mPainter);
				
			mPainter.setColor(Color.WHITE);
			canvas.drawBitmap(tempBitmap,level.getRect(), new RectF(mapPos.x,mapPos.y,mapPos.x+level.getSize().x,mapPos.y+level.getSize().y), mPainter);

			mPainter.setColor(Color.LTGRAY);
			mPainter.setTextSize(toPixel(22));
			canvas.drawText(" "+String.format("%2.2f",level.enemyPool.curIntermission/1000)+"       Money: $"+String.format("%1.2f", level.wallet)+"      Life: "+(level.allowedEnemies - level.enemyPool.getLived()), 0, toPixel(22), mPainter);
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,int height){
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
				        	// lock the frame rate at about 30 fps
				            Thread.sleep(Math.max(10,33+passedTime));
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

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			//Toast.makeText(GameScreenActivity.this, "onTouchEvent", Toast.LENGTH_SHORT).show();
			this.mDetector.onTouchEvent(event);
			// Be sure to call the superclass implementation
			// possible error here
			return true;
			//return super.onTouchEvent(event);
		}
		@Override
		public boolean onDown(MotionEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void onLongPress(MotionEvent event) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			//Toast.makeText(GameScreenActivity.this, "onScroll("+distanceX+","+distanceY+")", Toast.LENGTH_SHORT).show();
			mapPos.x -= distanceX;
			mapPos.y -= distanceY;
			hideBuyMenu();
			return true;
		}
		@Override
		public void onShowPress(MotionEvent event) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean onSingleTapUp(MotionEvent event) {
			// TODO Auto-generated method stub
			//Toast.makeText(GameScreenActivity.this, "Tap Detected: ("+event.getX()+", "+event.getY()+")", Toast.LENGTH_SHORT).show();
			boolean openMenu = level.makeSelection(event.getX()-mapPos.x, event.getY()-mapPos.y);
			if(openMenu)
				showBuyMenu();
			else
				hideBuyMenu();
			
			return true;
		}
		

	    
	    // for listing buy-able items
	    private class BuyMenuListItem {
	        public String title,detail;
	        public float price;
	        
	        public BuyMenuListItem(String title,float price, String detail) {
	            super();
	            this.title = title;
	            this.price = price;
	            this.detail = detail;
	        }
	    }
	    private class BuyMenuListItemAdapter extends ArrayAdapter<BuyMenuListItem>{

	        Context context;
	        int layoutResourceId;   
	        BuyMenuListItem data[] = null;
	        
	        public BuyMenuListItemAdapter(Context context, int layoutResourceId, BuyMenuListItem[] data) {
	            super(context, layoutResourceId, data);
	            this.layoutResourceId = layoutResourceId;
	            this.context = context;
	            this.data = data;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            View row = convertView;
	            BuyMenuListItemHolder holder = null;
	            
	            if(row == null)
	            {
	                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	                row = inflater.inflate(layoutResourceId, parent, false);
	                
	                holder = new BuyMenuListItemHolder();
	                holder.txtTitle = (TextView)row.findViewById(R.id.txtBuyTitle);
	                holder.txtPrice = (TextView)row.findViewById(R.id.txtBuyPrice);
	                holder.txtDetail = (TextView)row.findViewById(R.id.txtBuyDetail);
	                
	                row.setTag(holder);
	            }
	            else
	            {
	                holder = (BuyMenuListItemHolder)row.getTag();
	            }
	            
	            BuyMenuListItem item = data[position];
	            holder.txtTitle.setText(item.title);
	            holder.txtDetail.setText(item.detail);
	            holder.txtPrice.setText("$"+String.format("%1.2f", item.price));
	           
	            return row;
	        }
	        
	        private class BuyMenuListItemHolder
	        {
	            TextView txtTitle;
	            TextView txtPrice;
	            TextView txtDetail;
	        }
	    }
	    
	}

}
