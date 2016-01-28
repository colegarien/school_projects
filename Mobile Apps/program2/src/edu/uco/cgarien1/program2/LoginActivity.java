// Cole Garien

package edu.uco.cgarien1.program2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	public static final String EXTRA_USERNAME = "program2.USERNAME";

	EditText userId;
	EditText userPassword;
	TextView errorBox;
	Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		userId = (EditText)(findViewById(R.id.id_input));
		userPassword = (EditText)(findViewById(R.id.password_input));
		errorBox = (TextView)(findViewById(R.id.error_label));
		
		loginButton = (Button)(findViewById(R.id.login_button));
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = userId.getText().toString();
				String password = userPassword.getText().toString();
				
				// verify the id and password are correct
				if((username.equals("amy") && password.equals("123")) 
						|| (username.equals("mark") && password.equals("456"))){
					// empty error box (may be empty already)
					errorBox.setText("");
					errorBox.setVisibility(View.INVISIBLE);
					
					// clear username and password
					userId.setText("");
					userPassword.setText("");
					
					// load welcome activity
					Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
					i.putExtra(EXTRA_USERNAME, username);
					startActivity(i);
					
				}else{
					errorBox.setText(R.string.login_failed_text);
					errorBox.setVisibility(View.VISIBLE);
				}
			}
		});
	}
}
