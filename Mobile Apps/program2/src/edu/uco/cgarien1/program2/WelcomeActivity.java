// Cole Garien

package edu.uco.cgarien1.program2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

	TextView welcomeText;
	ImageView userImage;
	String username = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		username = getIntent().getStringExtra(LoginActivity.EXTRA_USERNAME);
		
		welcomeText = (TextView)(findViewById(R.id.welcome_label));
		userImage = (ImageView)(findViewById(R.id.user_image));
		
		
		String properName = username.toUpperCase().charAt(0) + username.toLowerCase().substring(1);
		String welcomeMessage = getString(R.string.welcome_message).replace("1", properName);
		
		welcomeText.setText(welcomeMessage);
		if(username.equals("amy"))
			userImage.setImageResource(R.drawable.amy_image);
		else if(username.equals("mark"))
			userImage.setImageResource(R.drawable.mark_image);
		else
			userImage.setImageResource(R.drawable.ic_launcher);
	}
}
