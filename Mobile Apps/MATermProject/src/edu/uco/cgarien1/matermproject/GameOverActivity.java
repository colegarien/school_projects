// Cole Garien

package edu.uco.cgarien1.matermproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends Activity {

	Button btnQuit;
	Button btnMainMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);

		int bulletsShot = getIntent().getIntExtra(GameScreenActivity.EXTRA_STAT_BULLETS_SHOT, -1);
		int enemyKilled = getIntent().getIntExtra(GameScreenActivity.EXTRA_STAT_ENEMY_KILLED, -1);
		float moneySpent = getIntent().getFloatExtra(GameScreenActivity.EXTRA_STAT_MONEY_SPENT, 0f);
		int towersBought = getIntent().getIntExtra(GameScreenActivity.EXTRA_STAT_TOWERS_BOUGHT, -1);
		int towersSold = getIntent().getIntExtra(GameScreenActivity.EXTRA_STAT_TOWERS_SOLD, -1);
		int trapsBought = getIntent().getIntExtra(GameScreenActivity.EXTRA_STAT_TRAPS_BOUGHT, -1);
		
		TextView statBox = (TextView)findViewById(R.id.stats_txt);
		statBox.setLines(6);
		statBox.setText("Bullets Fired: "+ bulletsShot + "\n" +
						"Enemies Killed: "+ enemyKilled + "\n" +
						"Money Spent: $"+ String.format("%1.2f",moneySpent) + "\n" +
						"Towers Bought: "+ towersBought + "\n" +
						"Towers Sold: "+ towersSold + "\n" +
						"Traps Bought: " + trapsBought);
		
		btnQuit = (Button)findViewById(R.id.btn_gameover_quit);
		btnQuit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// quit the app
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}
		});
		
		btnMainMenu = (Button)findViewById(R.id.btn_gameover_mainmenu);
		btnMainMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// go back to main menu
				Intent intent = new Intent(GameOverActivity.this, MainMenuActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// goto mainMenu
		Intent intent = new Intent(GameOverActivity.this, MainMenuActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}
}
