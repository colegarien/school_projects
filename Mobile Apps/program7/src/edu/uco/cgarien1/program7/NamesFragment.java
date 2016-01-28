// Cole Garien

package edu.uco.cgarien1.program7;

import java.util.Comparator;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NamesFragment extends ListFragment{
	private ListSelectionListener mListener = null;
	
	public interface ListSelectionListener{
		public void onListSelection(int index);
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		getListView().setItemChecked(pos, true);
		mListener.onListSelection(pos);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (ListSelectionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
		}
	}
	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);

		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		ArrayAdapter<Contact> adapter= new ArrayAdapter<Contact>(getActivity(), R.layout.name_item, MainActivity.contacts);
		adapter.sort(new Comparator<Contact>() {
			public int compare(Contact a, Contact b) {
		        return String.CASE_INSENSITIVE_ORDER.compare(a.getLastName(), b.getLastName());
		    }
		});
		setListAdapter(adapter);
	}

}
