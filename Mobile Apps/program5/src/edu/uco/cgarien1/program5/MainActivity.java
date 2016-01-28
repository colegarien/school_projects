// Cole Garien

package edu.uco.cgarien1.program5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ListView listview;
	
	private ActionMode.Callback actionModeCallback;
	private ActionMode actionMode;
	
	private Department[] departments = {
			new Department("Department of Biology","tel:4059745017","http://biology.uco.edu"),
			new Department("Department of Chemistry","tel:4059742000","http://www.uco.edu/cms/chemistry/index.asp"),
			new Department("Department of Computer Science","tel:4059745717","http://cs.uco.edu/Home4/"),
			new Department("Department of Engineering and Physics","tel:4059742000","http://www.uco.edu/cms/engineering/index.asp"),
			new Department("Funeral Service Department","tel:4059745001","http://www.uco.edu/cms/funeral/index.asp"),
			new Department("Department of Mathematics and Statistics","tel:4059745012","http://www.math.uco.edu/"),
			new Department("Department of Nursing","tel:4059742000","http://www.uco.edu/cms/nursing/index.asp")};
	
	private String numberToCall = "";
	private String urlToNavigate = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		actionModeCallback = new ActionModeCallback();
		
		listview = (ListView) findViewById(R.id.listview);
		ArrayAdapter<Department> adapter = new ArrayAdapter<Department>(getApplicationContext(),
				R.layout.list_item, departments);
		listview.setAdapter(adapter);
	
		listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				if (actionMode != null) {
					return false;
				}
				
				numberToCall = departments[pos].getNumber();
				urlToNavigate = departments[pos].getUrl();
				
				// Start the Contextual Action Bar using the ActionMode.Callback
				actionMode = MainActivity.this.startActionMode(actionModeCallback);
				return true;
			}
		
		});
	}
	
	
	private class ActionModeCallback implements ActionMode.Callback {

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.menu_call:
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse(numberToCall));
				startActivity(intent);
				mode.finish();
				return true;
			case R.id.menu_web:
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToNavigate));
				startActivity(browserIntent);
				mode.finish();
				return true;
			default:
				return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			actionMode = null;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.context_menu, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	
}

