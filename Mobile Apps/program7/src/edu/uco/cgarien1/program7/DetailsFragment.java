// Cole Garien

package edu.uco.cgarien1.program7;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

	private TextView nameView = null;
	private TextView emailView = null;
	private TextView phoneView = null;
	private TextView websiteView = null;
	private int mCurrIdx = -1;
	private int mDetailArrayLen;

	private String urlToNavigate = "";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		nameView = (TextView) getActivity().findViewById(R.id.name_view);
		emailView = (TextView) getActivity().findViewById(R.id.email_view);
		phoneView = (TextView) getActivity().findViewById(R.id.phone_view);
		websiteView = (TextView) getActivity().findViewById(R.id.website_view);
		websiteView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!urlToNavigate.isEmpty()){
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToNavigate));
					startActivity(browserIntent);
				}else{
					// a contact has yet to be selected
				}
			}
		});
		
		mDetailArrayLen = MainActivity.contacts.length;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.details_fragment, container, false);
	}

	public int getShownIndex() {
		return mCurrIdx;
	}

	public void showDetailsAtIndex(int newIndex) {
		if (newIndex < 0 || newIndex >= mDetailArrayLen)
			return;
		mCurrIdx = newIndex;
		nameView.setText(MainActivity.contacts[mCurrIdx].getFirstName()+" "+MainActivity.contacts[mCurrIdx].getLastName());
		emailView.setText(MainActivity.contacts[mCurrIdx].getEmail());
		phoneView.setText(MainActivity.contacts[mCurrIdx].getNumber());
		websiteView.setText(MainActivity.contacts[mCurrIdx].getUrl());
		
		urlToNavigate = MainActivity.contacts[mCurrIdx].getUrl();
	}

}
