// Cole Garien

package edu.uco.cgarien1.matermproject;

import edu.uco.cgarien1.matermproject.InstructionsDialogFragment.InstructionsDialogListener;
import edu.uco.cgarien1.matermproject.LevelSelectDialogFragment.LevelSelectDialogListener;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity implements InstructionsDialogListener, LevelSelectDialogListener{
	public static final String EXTRA_LEVEL_ID ="thetowerdefense.level.identification";
	public static final String EXTRA_TILESET_ID ="thetowerdefense.tileset.identification";
	
	private Button btnPlay;
	private Button btnInstructions;
	private Button btnQuit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		btnPlay = (Button)findViewById(R.id.btn_play);
		btnPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// open stage select
				LevelSelectDialogFragment d = new LevelSelectDialogFragment();
				d.show(getFragmentManager(), "levels");
			}
		});
		btnInstructions = (Button)findViewById(R.id.btn_instructions);
		btnInstructions.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// show instructions
				InstructionsDialogFragment d = new InstructionsDialogFragment();
				d.show(getFragmentManager(), "instructions");
			}
		});
		btnQuit = (Button)findViewById(R.id.btn_quit);
		btnQuit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// quit the app
				onBackPressed();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}
	
	@Override
	public void onLevelSelected(int levelId, int tilesetId, DialogFragment dialog) {
		// send level data to gamescreen
		Intent i = new Intent(MainMenuActivity.this, GameScreenActivity.class);
		i.putExtra(EXTRA_TILESET_ID,tilesetId);
		i.putExtra(EXTRA_LEVEL_ID,levelId);
		startActivity(i);
	}
}
