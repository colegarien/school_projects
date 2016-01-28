// Cole Garien

package edu.uco.cgarien1.matermproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class InstructionsDialogFragment extends DialogFragment {
	public interface InstructionsDialogListener {}
	
	InstructionsDialogListener listener;
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (InstructionsDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement InstructionsDialogFragment");
        }
    }
    
    @SuppressLint("InflateParams")
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.instructions_dialog_layout, null);

        builder.setView(view)
               .setPositiveButton(R.string.text_close, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       InstructionsDialogFragment.this.getDialog().cancel();
                   }
               });      
        return builder.create();
    }
}
