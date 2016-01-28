// Cole Garien

package edu.uco.cgarien1.program6;

import edu.uco.cgarien1.program6.Department;
import edu.uco.cgarien1.program6.DepartmentDialogFragment.PickDepartmentListener;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends Activity implements PickDepartmentListener{

	// Notification ID to allow for future updates
	private static final int NOTIFICATION_ID = 1;
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;
	RemoteViews mContentView = new RemoteViews(
			"edu.uco.cgarien1.program6",
			R.layout.department_notification);

	private Button btnDept;

	private Department[] departments = {
			new Department("Department of Biology","tel:4059745017","http://biology.uco.edu"),
			new Department("Department of Chemistry","tel:4059742000","http://www.uco.edu/cms/chemistry/index.asp"),
			new Department("Department of Computer Science","tel:4059745717","http://cs.uco.edu/Home4/"),
			new Department("Department of Engineering and Physics","tel:4059742000","http://www.uco.edu/cms/engineering/index.asp"),
			new Department("Funeral Service Department","tel:4059745001","http://www.uco.edu/cms/funeral/index.asp"),
			new Department("Department of Mathematics and Statistics","tel:4059745012","http://www.math.uco.edu/"),
			new Department("Department of Nursing","tel:4059742000","http://www.uco.edu/cms/nursing/index.asp")};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnDept = (Button)findViewById(R.id.btn_dept);
		btnDept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DepartmentDialogFragment d = new DepartmentDialogFragment();
				d.show(getFragmentManager(), "departments");
			}
		});
		
	}

	@Override
	public void onPickDepartmentClick(int itemIndex, DialogFragment dialog) {
		mNotificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(departments[itemIndex].getUrl()));
		mContentIntent = PendingIntent.getActivity(getApplicationContext(), 0, mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		mContentView.setTextViewText(R.id.title, departments[itemIndex].getName());

		Notification.Builder notificationBuilder = new Notification.Builder(
				getApplicationContext())
			.setTicker("Program6 Notification!")
			.setSmallIcon(android.R.drawable.stat_sys_warning)
			.setAutoCancel(true)
			.setContentIntent(mContentIntent)
			.setContent(mContentView);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
	}
}
