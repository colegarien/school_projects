// Cole Garien

package edu.uco.cgarien1.matermproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class LevelSelectDialogFragment extends DialogFragment {
	private int tilesetId;
	public interface LevelSelectDialogListener {
		public void onLevelSelected(int levelId, int tilesetId, DialogFragment dialog);
	}
	
	LevelSelectDialogListener listener;
    // possibly temporary
	private LevelListItem levelitem_data[] = new LevelListItem[]
            {
            	new LevelListItem(R.drawable.level_0000, "Level 000"),
                new LevelListItem(R.drawable.level_0001, "Level 001"),
                new LevelListItem(R.drawable.level_0002, "Level 002"),
                new LevelListItem(R.drawable.path_demo, "Path Demo"),
                new LevelListItem(R.drawable.error_map, "**Error Map**")
            };
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // initialize the tileset to simple set
        tilesetId = R.drawable.tileset_simple;
        
        try {
            listener = (LevelSelectDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LevelSelectDialogFragment");
        }
    }
    
    @SuppressLint("InflateParams")
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LevelListItemAdapter adapter = new LevelListItemAdapter(getActivity(), R.layout.level_list_item, levelitem_data);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.level_select_layout, null);
        
        // selecting tileset
        RadioGroup radGroup = (RadioGroup)view.findViewById(R.id.tileset_radios);
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radgroup, int checkedId) {
				if(checkedId == R.id.rad_simple)
					tilesetId = R.drawable.tileset_simple;
				else
					tilesetId = R.drawable.tileset_debug;
			}
		});
        
        builder.setView(view)
        	   .setTitle(getActivity().getString(R.string.title_level_dialog))
               .setPositiveButton(R.string.text_close, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   LevelSelectDialogFragment.this.getDialog().cancel();
                   }
               })
               .setAdapter(adapter, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                	   // The 'which' is index in list, so send level icon id to listener
                	   listener.onLevelSelected(levelitem_data[which].icon, tilesetId, LevelSelectDialogFragment.this);
	               }
               });
        return builder.create();
    }
    
    
    // for listing levels
    private class LevelListItem {
        public int icon;
        public String title;
       
        public LevelListItem(int icon, String title) {
            super();
            this.icon = icon;
            this.title = title;
        }
    }
    private class LevelListItemAdapter extends ArrayAdapter<LevelListItem>{

        Context context;
        int layoutResourceId;   
        LevelListItem data[] = null;
       
        public LevelListItemAdapter(Context context, int layoutResourceId, LevelListItem[] data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            LevelListItemHolder holder = null;
           
            if(row == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
               
                holder = new LevelListItemHolder();
                holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
                holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
               
                row.setTag(holder);
            }
            else
            {
                holder = (LevelListItemHolder)row.getTag();
            }
           
            LevelListItem item = data[position];
            holder.txtTitle.setText(item.title);
            holder.imgIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), item.icon));
           
            return row;
        }
       
        private class LevelListItemHolder
        {
            ImageView imgIcon;
            TextView txtTitle;
        }
    }
}
