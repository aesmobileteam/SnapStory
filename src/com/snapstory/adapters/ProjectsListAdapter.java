package com.snapstory.adapters;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.snapstory.AppClass;
import com.snapstory.R;
import com.snapstory.util.DataProvider;
import com.snapstory.util.ImageLoader;

public class ProjectsListAdapter extends CursorAdapter{

    /*private Context context;
    private Retriever retriever;

    public ProjectsListAdapter(Context context, Retriever retriever) {
        this.context = context;
        this.retriever = retriever;
    }

    @Override
    public int getCount() {
        return retriever == null ? 0 : retriever.getCount();
    }

    @Override
    public Object getItem(int position) {
        retriever.move(position);
        return projectTable().getApi().id(retriever);
    }

    @Override
    public long getItemId(int position) {
        return retriever == null || getCount() <= position ? -1L : position;
    }

    public long getProjectId(int position) {
        retriever.moveToPosition(position);
        return retriever == null ? -1 : projectTable().getApi().id(retriever);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (retriever == null || !retriever.moveToPosition(position)) {
            return null;
        }

        ProjectTable projectTable = projectTable().getApi();

        return new ViewBuilder().name(projectTable.name(retriever), projectTable.imageUrl(retriever), projectTable.description(retriever))
                .build(context);
    }

    private class ViewBuilder {
        private String name;
        private String projectImgUrl;
        private String description;

        public ViewBuilder name(String name, String projectImgUrl, String description) {
            this.name=name;
            this.projectImgUrl = projectImgUrl;
            this.description = description;
            return this;
        }

        public View build(Context context) {
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.project_list_item, null);
            ((TextView)view.findViewById(R.id.project_name)).setText(name);
            ((TextView)view.findViewById(R.id.description)).setText(description);
            ImageView projectImageView = (ImageView) view.findViewById(R.id.project_image);
            Glide.with(context).load(projectImgUrl).centerCrop().placeholder(R.drawable.image_one).into(projectImageView);
            return view;
        }
    }*/
	
	private Context context;	
	Cursor cs;
	ImageLoader imageLoader;

	@SuppressWarnings("deprecation")
	public ProjectsListAdapter(Context context, Cursor c) {
		super(context, c);
		this.context=context;
		cs = c;
		imageLoader = new ImageLoader(context);
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
			convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.project_list_item, null);
			holder = new ViewHolder();
			holder.txtname=((TextView)convertView.findViewById(R.id.project_name));
			holder.txtdescription=((TextView)convertView.findViewById(R.id.description));
			holder.imgproject = (ImageView) convertView.findViewById(R.id.project_image);
           // Glide.with(context).load(projectImgUrl).centerCrop().placeholder(R.drawable.image_one).into(projectImageView);
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
		
		holder.txtname.setText(cs.getString(cs.getColumnIndex(DataProvider.NAME)));
		holder.txtdescription.setText(cs.getString(cs.getColumnIndex(DataProvider.DESCRIPTION)).replace("null", ""));
		
		System.out.println("image url-->"+cs.getString(cs.getColumnIndex(DataProvider.IMAGE_URL)));
		imageLoader.DisplayImage(cs.getString(cs.getColumnIndex(DataProvider.IMAGE_URL)), holder.imgproject);
		
		//holder.name.setText(cs.getString(cs.getColumnIndex(DataProvider.NAME)));
		/*String temp_date="";
		try{
			DateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
			Date date11 = dateFormat.parse(cs.getString(cs.getColumnIndex(DataProvider.TIME)));
			SimpleDateFormat Format = new SimpleDateFormat("hh:mm");
			temp_date = Format.format(date11);
		}catch(Exception e){
			
		}*/
		
		
		
		/*holder.time.setText(temp_date);
		holder.message.setText(cs.getString(cs.getColumnIndex(DataProvider.MESSAGE)));
		int flag=cs.getInt(cs.getColumnIndex(DataProvider.MSG_FLAG));
		System.out.println("flag-------->>>>"+flag+"        "+cs.getString(cs.getColumnIndex(DataProvider.MSG_FLAG)));
		if(flag==1){
			holder.name.setTypeface(null, Typeface.BOLD);
			holder.name.setTextSize(18);
			holder.time.setTypeface(null, Typeface.BOLD);
			holder.time.setTextSize(18);
			holder.message.setTypeface(null, Typeface.BOLD);
			holder.message.setTextSize(18);
		}else{
			holder.name.setTypeface(null, Typeface.NORMAL);
			holder.name.setTextSize(14);
			holder.time.setTypeface(null, Typeface.NORMAL);
			holder.time.setTextSize(14);
			holder.message.setTypeface(null, Typeface.NORMAL);
			holder.message.setTextSize(14);
		}*/
		/*
		if(cs.getString(cs.getColumnIndex(DataProvider.NAME)).equalsIgnoreCase(sharedPreference.getString("USER_NAME", ""))){
			holder.imgTop.setBackgroundResource(R.drawable.orange_bg_top);
			holder.imgBot.setBackgroundResource(R.drawable.orange_bg_bot);
			holder.layCenter.setBackgroundColor(Color.parseColor("#fdebdb"));			
		}else{
			holder.imgTop.setBackgroundResource(R.drawable.white_bg_top);
			holder.imgBot.setBackgroundResource(R.drawable.white_bg_bot);
			holder.layCenter.setBackgroundColor(Color.parseColor("#ffffff"));	
		}*/
		
		/*if(cs.getString(cs.getColumnIndex(DataProvider.NAME)).equalsIgnoreCase("NAVA"))
			holder.layListItem.setBackgroundResource(R.drawable.list_bk);
		else
			holder.layListItem.setBackgroundResource(R.drawable.list_bk_w);*/
		
		/*holder.chkMessage.setClickable(true);
        holder.chkMessage.setOnClickListener(new View.OnClickListener(){
               public void onClick(View view)
               {
            	   	 //getContentResolver().delete(DataProvider.LOGIN_URI, DataProvider.ID,cs.getString(cs.getColumnIndex(DataProvider.ID)));
            	   	 getContentResolver().delete(DataProvider.LOGIN_URI,DataProvider.ID + "=?",new String[] { cs.getString(cs.getColumnIndex(DataProvider.ID)) + "" });
            	   	 notifyDataSetChanged();
               }
        });*/
		//holder.name.setText(cs.getString(cs.getColumnIndex(DataProvider.NAME)));
		//System.out.println("ID->"+cs.getString(cs.getColumnIndex(DataProvider.ID)));	
		
		
		
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


	static class ViewHolder {
		TextView txtname;
		TextView txtdescription;
		ImageView imgproject;
		
	}


	public long getProjectId(int position) {
		cs.moveToPosition(position);
		AppClass.edUser.putString("project_name", ""+cs.getString(cs.getColumnIndex(DataProvider.NAME)));
		AppClass.edUser.putString("project_location", cs.getString(cs.getColumnIndex(DataProvider.CITY))+","+cs.getString(cs.getColumnIndex(DataProvider.LOCATION)));
        return cs.getInt(cs.getColumnIndex(DataProvider.PROJECT_ID));
	}
	

}
