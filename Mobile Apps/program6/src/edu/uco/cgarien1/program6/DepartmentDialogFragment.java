// Cole Garien

package edu.uco.cgarien1.program6;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DepartmentDialogFragment extends DialogFragment {
	
	public interface PickDepartmentListener {
		public void onPickDepartmentClick(int itemIndex, DialogFragment dialog);
	}
	
	PickDepartmentListener listener;
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (PickDepartmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement PickDepartmentListener");
        }
    }
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dlg_dept).setItems(R.array.department_array, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
            	   listener.onPickDepartmentClick(which, DepartmentDialogFragment.this);
               }
        });
        return builder.create();
    }
}
