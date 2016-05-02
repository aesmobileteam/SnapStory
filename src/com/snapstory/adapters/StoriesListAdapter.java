package com.snapstory.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.snapstory.R;
import com.snapstory.StoriesActivity;
import com.snapstory.StoryActivity;
import com.snapstory.util.DataProvider;

public class StoriesListAdapter extends CursorAdapter {
    /*private Context context;
    private Retriever retriever;
    private AddUpdateListener listener;

    public interface AddUpdateListener {
        void onAddButtonClicked(String StoryId);
    }

    public StoriesListAdapter(Context context, Retriever retriever, AddUpdateListener listener) {
        this.context = context;
        this.retriever = retriever;
        this.listener = listener;
    }

    public String getStoryUUID(int position) {
        retriever.moveToPosition(position);
        return retriever == null ? "" : storyTable().getApi().uuid(retriever);
    }

    @Override
    public int getCount() {
        return retriever == null ? 0 : retriever.getCount();
    }

    @Override
    public Object getItem(int position) {
        retriever.move(position);
        return storyTable().getApi().id(retriever);
    }

    @Override
    public long getItemId(int position) {
        return retriever == null || getCount() <= position ? -1L : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (retriever == null || !retriever.moveToPosition(position)) {
            return null;
        }

        StoryTable storyTable = storyTable().getApi();

        return new ViewBuilder().name(storyTable.name(retriever))
                .build(context, position, listener);
    }

    private class ViewBuilder {
        private String name;

        public ViewBuilder name(String name) {
            this.name=name;
            return this;
        }

        public View build(Context context, final int position, final AddUpdateListener listener) {
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.story_list_item, null);
            ((TextView)view.findViewById(R.id.project_name)).setText(name);
            (view.findViewById(R.id.add_update)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddButtonClicked(getStoryUUID(position));
                }
            });
            return view;
        }
    }*/
	
	private Context context;	
	Cursor cs;

	@SuppressWarnings("deprecation")
	public StoriesListAdapter(Context context, Cursor c) {
		super(context, c);
		this.context=context;
		cs = c;
		
	}
	
	public int getCount() {			
		return super.getCount();
	}

	public Object getItem(int position) {		
		return null;
	}

	public long getItemId(int position) {			
		return super.getItemId(position);
	}

	public View getView(final int position, View convertView,
			ViewGroup parent) {		
		ViewHolder holder;
		if (convertView == null) {
			convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.story_list_item, null);
			holder = new ViewHolder();
			holder.name = ((TextView)convertView.findViewById(R.id.project_name));
			holder.add_update=((Button)convertView.findViewById(R.id.add_update));
			
			/*holder.name = (TextView) convertView.findViewById(R.id.txtName);
			holder.time = (TextView) convertView.findViewById(R.id.txtTime);
			holder.message = (TextView) convertView.findViewById(R.id.txtMessage);
			holder.imgTop = (ImageView)convertView.findViewById(R.id.imgTop);
			holder.imgBot = (ImageView)convertView.findViewById(R.id.imgBot);
			holder.layCenter = (View) convertView.findViewById(R.id.layCenter);		
			holder.layListItem = (View) convertView.findViewById(R.id.layListItem);*/
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		cs.moveToPosition(position);	
		
	
		holder.name.setText(cs.getString(cs.getColumnIndex(DataProvider.NAME)));
		
		holder.add_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	cs.moveToPosition(position);	
                //listener.onAddButtonClicked(getStoryUUID(position));
            	Intent intent = new Intent(context, StoryActivity.class);
                intent.putExtra(StoryActivity.STORY_UUID_EXTRA, cs.getString(cs.getColumnIndex(DataProvider.STORY_UUID)));
                intent.putExtra(StoryActivity.STORY_NAME_EXTRA, cs.getString(cs.getColumnIndex(DataProvider.NAME)));
                intent.putExtra(StoryActivity.STORY_TYPE_EXTRA, cs.getString(cs.getColumnIndex(DataProvider.REPORT_TYPE)));
                context.startActivity(intent);
            }
        });
		
		
	   return convertView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

    private class ViewHolder {
        TextView name;
        Button add_update;
    }
}
