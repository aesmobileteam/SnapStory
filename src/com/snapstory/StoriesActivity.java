package com.snapstory;

import net.simonvt.menudrawer.MenuDrawer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.snapstory.adapters.ProjectsListAdapter;
import com.snapstory.adapters.StoriesListAdapter;
import com.snapstory.menu.SlideMenu;
import com.snapstory.util.DataProvider;
import com.snapstory.util.ShanListView;

public class StoriesActivity extends Activity  {
    private final static int STORIES_LOADER_ID = 117;
    private final static int PROJECT_LOADER_ID = 1313;
    public static final String PROJECT_ID_EXTRA = "project_id";

    private ShanListView storiesListView;
    private Button newStoryButton;
    private StoriesListAdapter adapter;
    private Context context;
    private Dialog popDialog;
   // private FSCursor data;
    View layoutSetting;
    String sId;
    private ProgressDialog dlgProgress;
	private Activity mContext;
	private MenuDrawer mDrawer;
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_stories);
        mDrawer=SlideMenu.setMenu(this, R.layout.activity_stories);
        mContext=this;
        storiesListView = (ShanListView)findViewById(R.id.stories_list);
        ((ScrollView)findViewById(R.id.ScrollStory)).smoothScrollTo(0,0);
        
       /* ContentValues cv=new ContentValues();
        
        cv.put(DataProvider.NAME, "Story1");
        
        getContentResolver().insert(DataProvider.STORY_URI, cv);*/
        
        
        ((TextView)findViewById(R.id.projectName)).setText(""+AppClass.spUser.getString("project_name", ""));
        ((TextView)findViewById(R.id.projectLocation)).setText(""+AppClass.spUser.getString("project_location", ""));
        

    	if (AppClass.appcontext.isOnlineWithoutDialgo(mContext)) {
    		new StoriesTask().execute();
		}else{
			   adapter = new StoriesListAdapter(StoriesActivity.this, managedQuery(DataProvider.STORY_URI, null, DataProvider.PROJECT_ID+"='"+AppClass.spUser.getString("project_id", "0")+"'", null, null));
		        storiesListView.setAdapter(adapter);
		}

        newStoryButton = (Button)findViewById(R.id.create_story);
        newStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewStory();
            }
        });

        context=this;

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutSetting = layoutInflater.inflate(R.layout.popup_view, null);
        popDialog= new Dialog(context);
        popDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popDialog.setContentView(layoutSetting);
        popDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popDialog.setCanceledOnTouchOutside(true);
        popDialog.setCancelable(true);

        ((TextView)layoutSetting.findViewById(R.id.textView2)).setMovementMethod(new ScrollingMovementMethod());

        ((ImageView)layoutSetting.findViewById(R.id.buttonClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popDialog.isShowing()) {
                    popDialog.dismiss();
                }
            }
        });
        
        ((ImageView)findViewById(R.id.MenuBtn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawer.openMenu();
			}
		});

        ((Button)layoutSetting.findViewById(R.id.buttonCreateProject)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popDialog.isShowing())
                {
                    popDialog.dismiss();
                }

                Intent intent = new Intent(StoriesActivity.this, StoryActivity.class);
                intent.putExtra(StoryActivity.STORY_UUID_EXTRA, sId);
                intent.putExtra(StoryActivity.PROJECT_UUID_EXTRA, getIntent().getLongExtra(PROJECT_ID_EXTRA, -1));
                startActivity(intent);
            }
        });

        storiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String uuid = adapter.getStoryUUID(position);
            	String uuid = null;
                Intent intent = new Intent(StoriesActivity.this, StoryActivity.class);
                intent.putExtra(StoryActivity.STORY_UUID_EXTRA, uuid);
                startActivity(intent);
            }
        });

        final long projectId = getIntent().getLongExtra(PROJECT_ID_EXTRA, -1);
        //getLoaderManager().initLoader(STORIES_LOADER_ID, null, new StoryListLoader(projectId));

        /*if(projectId != -1) {
            Call<StoriesResponseWrappter> call = SnapStoryServiceGenerator.getSnapStory(this).getStoriesForProject(projectId);
            call.enqueue(new Callback<StoriesResponseWrappter>() {
                @Override
                public void onResponse(Call<StoriesResponseWrappter> call, Response<StoriesResponseWrappter> response) {
                    DBHelper.upsertStories(response.body().getData());
                    getLoaderManager().initLoader(STORIES_LOADER_ID, null, new StoryListLoader(projectId));
                }

                @Override
                public void onFailure(Call<StoriesResponseWrappter> call, Throwable t) {
                    getLoaderManager().initLoader(STORIES_LOADER_ID, null, new StoryListLoader(projectId));

                }
            });
        }*/

    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	  final int drawerState = mDrawer.getDrawerState();
          if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
              mDrawer.closeMenu();
              return;
          }else
          {
              
              finish();
          }
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        
        if((AppClass.spLogin.getAll().isEmpty()))
		{
			startActivity(new Intent(StoriesActivity.this,Login.class));
			finish();	
		}else
		{
        
        long projectId = -1;
        if(getIntent().hasExtra(PROJECT_ID_EXTRA)) {
            projectId=getIntent().getLongExtra(PROJECT_ID_EXTRA, -1);
        }
		}
        //getLoaderManager().initLoader(PROJECT_LOADER_ID, null, new ProjectLoader(projectId));
    }

    private void createNewStory() {
        Intent intent = new Intent(StoriesActivity.this, StoryActivity.class);
//        intent.putExtra(StoryActivity.PROJECT_UUID_EXTRA, getIntent().getLongExtra(PROJECT_ID_EXTRA, -1));
        startActivity(intent);
    }

    public void onAddButtonClicked(String storyId) {

        sId=storyId;

        Intent intent = new Intent(StoriesActivity.this, StoryActivity.class);
        intent.putExtra(StoryActivity.STORY_UUID_EXTRA, sId);
        intent.putExtra(StoryActivity.PROJECT_UUID_EXTRA, getIntent().getLongExtra(PROJECT_ID_EXTRA, -1));
        startActivity(intent);
    }

    /*private class StoryListLoader implements LoaderManager.LoaderCallbacks<FSCursor> {
        long projectId;

        public StoryListLoader(long projectId) {
            this.projectId = projectId;
        }

        @Override
        public Loader<FSCursor> onCreateLoader(int id, Bundle args) {
            return new FSCursorLoader<>(StoriesActivity.this, storyTable().find().byProjectId(projectId).andFinally());
        }

        @Override
        public void onLoadFinished(Loader<FSCursor> loader, FSCursor data) {
            adapter = new StoriesListAdapter(StoriesActivity.this, data, StoriesActivity.this);
            storiesListView.setAdapter(adapter);
        }

        @Override
        public void onLoaderReset(Loader<FSCursor> loader) {

        }
    }*/

    /*private class ProjectLoader implements LoaderManager.LoaderCallbacks<FSCursor> {
        long projectId;

        public ProjectLoader(long projectId) {
            this.projectId = projectId;
        }

        @Override
        public Loader<FSCursor> onCreateLoader(int id, Bundle args) {
            return new FSCursorLoader<>(StoriesActivity.this, projectTable().find().byId(projectId).andFinally());
        }

        @Override
        public void onLoadFinished(Loader<FSCursor> loader, FSCursor data) {
            if(data != null && data.moveToFirst()) {
                ((TextView)findViewById(R.id.projectName)).setText(projectTable().getApi().name(data));
                ((TextView)findViewById(R.id.projectDescription)).setText(projectTable().getApi().description(data));
            }
        }

        @Override
        public void onLoaderReset(Loader<FSCursor> loader) {

        }
    }*/
    
    private class StoriesTask extends AsyncTask<Void, Void, String> {

		private Looper mLooper;		
		String msg="";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			StoriesActivity.this.dlgProgress = new ProgressDialog(mContext);
			StoriesActivity.this.dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			StoriesActivity.this.dlgProgress.setCanceledOnTouchOutside(false);
			StoriesActivity.this.dlgProgress.setMessage("Please wait...");
			StoriesActivity.this.dlgProgress.show();

		}


		@Override
		protected String doInBackground(Void... paramArrayOfParams) {			
			String result = "Network connection error";
			try {
				if (Looper.myLooper() == null) {
					Looper.prepare();
				}
				mLooper = Looper.myLooper();
				JSONObject jsonObj = new JSONObject();
				//	jsonObj.put("device_id", deviceId);

				//System.out.println("Request : " + jsonObj.toString());
				StringBuffer sb = AppClass.appcontext
					.getData(mContext, AppClass.WEB_URL+"projects/"+AppClass.spUser.getString("project_id", "0")+"/report_templates","","Bearer "+AppClass.spLogin.getString("token", ""),"");


				if (sb == null) {
					return result;
				}
				System.out.println("Response : " + sb.toString());
				jsonObj = new JSONObject(sb.toString());

				//String st="{\"result\":\"success\",\"learner_details\":{\"learner_id\":\"1897\",\"name\":\"Ramesh B\",\"gender\":\"Male\",\"dob\":\"1976-06-18\",\"mobile\":\"9842231700\",\"designation\":\"CEO\",\"experience\":\"NIL\",\"education\":\"Master in Computer Applications\",\"office_location\":\"GN Mills \",\"city_town\":\"Coimbatore\",\"region_state\":\"Tamil Nadu\",\"country\":\"India\",\"learner_image\":\"http:\\/\\/php55.development.local\\/dev150908_361dm\\/ver1\\/imeta\\/learner_photo\\/1897_tn.jpg\"},\"guided_path\":[{\"completed_status\":\"Completed\",\"lr_id\":\"623\",\"lr_name\":\"ARRIVING IN THE NEW CITY\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"624\",\"lr_name\":\"FIRST DAY AT WORK\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"625\",\"lr_name\":\"SUNDAY LUNCH\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"626\",\"lr_name\":\"FIRST REVIEW\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"687\",\"lr_name\":\"BUSINESS CONVENTION\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"688\",\"lr_name\":\"PRE-PROJECT INTERVIEW\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"689\",\"lr_name\":\"REAL-TIME PROJECT\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"690\",\"lr_name\":\"PROJECT COMPLETION\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"}],\"product_id\":\"83\",\"user_path\":{\"Communications Module\":[{\"completed_status\":\"Completed\",\"lr_id\":\"623\",\"lr_name\":\"ARRIVING IN THE NEW CITY\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"624\",\"lr_name\":\"FIRST DAY AT WORK\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"625\",\"lr_name\":\"SUNDAY LUNCH\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"626\",\"lr_name\":\"FIRST REVIEW\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"687\",\"lr_name\":\"BUSINESS CONVENTION\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"688\",\"lr_name\":\"PRE-PROJECT INTERVIEW\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"689\",\"lr_name\":\"REAL-TIME PROJECT\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"690\",\"lr_name\":\"PROJECT COMPLETION\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"}]},\"total_score\":\"127.5\",\"overall_score\":\"217\",\"score\":\"(127.5\\/217)=58.8%\"}";

				//jsonObj = new JSONObject(st);
				//				result = jsonObj.getString("result");
				//result="success";
				//System.out.println("result--->"+result);
				//if (result.equalsIgnoreCase("success")) {	
					
					JSONArray jsonArray=jsonObj.getJSONArray("data");
					
					if(jsonArray.length()>0){
						SQLiteDatabase mydb;
						 try {
					            mydb = mContext.openOrCreateDatabase(DataProvider.DATABASE_NAME, Context.MODE_PRIVATE, null);
					            Cursor allrows = mydb.rawQuery("DELETE FROM " + DataProvider.TBL_STORY + " where "+DataProvider.PROJECT_ID+"='"+AppClass.spUser.getString("project_id", "0")+"'", null);
					            System.out.println("COUNT : " + allrows.getCount());				        
					            allrows.close();
					            
					            Cursor allrows1 = mydb.rawQuery("DELETE FROM " + DataProvider.TBL_QUESTION+ " where "+DataProvider.PROJECT_ID+"='"+AppClass.spUser.getString("project_id", "0")+"'", null);
					            System.out.println("COUNT : " + allrows1.getCount());				        
					            allrows1.close();
					            
					            /*Cursor allrows1 = mydb.rawQuery("DELETE FROM " + DataProvider.TBL_CHAPTER, null);
					            System.out.println("COUNT : " + allrows1.getCount());				        
					            allrows1.close();
					            
					            Cursor allrows2 = mydb.rawQuery("DELETE FROM " + DataProvider.TBL_ANSWER, null);
					            System.out.println("COUNT : " + allrows2.getCount());				        
					            allrows2.close();*/
					            
					            mydb.close();					            
					            
					            
					            
					            
						 }catch(Exception e)
						 {
							 e.printStackTrace();
						 }
					}
					
					for(int i=0;i<jsonArray.length();i++){
						JSONObject storyObj=jsonArray.getJSONObject(i);
						
						//System.out.println("ID--->"+storyObj.getInt("id"));
						
						ContentValues cv=new ContentValues();
						cv.put(DataProvider.STORY_UUID, storyObj.getString("_id"));		
						String storyUUID=storyObj.getString("_id");
				        cv.put(DataProvider.NAME, storyObj.getString("name"));
				       // cv.put(DataProvider.GENDER, storyObj.getString("gender"));
				        cv.put(DataProvider.PROJECT_ID, storyObj.getInt("projectId"));
				        String projectId=storyObj.getString("projectId");
				        cv.put(DataProvider.REPORT_TYPE, storyObj.getString("reportType"));
				        cv.put(DataProvider.REPORT_TEMPLATE_ID, storyObj.getString("_id"));
				        
				        getContentResolver().insert(DataProvider.STORY_URI, cv);
				        
				        
				        JSONArray phasesArray=storyObj.getJSONArray("phases");	
				        
				        for(int j=0;j<phasesArray.length();j++){
							JSONObject phaseObj=phasesArray.getJSONObject(j);
							
							
				        
					        /*ContentValues cv1=new ContentValues();				        
					        cv1.put(DataProvider.CHAPTER_UUID, chapterObj.getString("uuid"));		
					        cv1.put(DataProvider.STORY_ID, chapterObj.getString("storyUUID"));	
					        cv1.put(DataProvider.SYNCED, chapterObj.getString("isSyncd"));	
					        getContentResolver().insert(DataProvider.CHAPTER_URI, cv1);*/
					        
					        JSONArray questionArray=phaseObj.getJSONArray("questions");	
					        
					        for(int k=0;k<questionArray.length();k++){
								JSONObject questionObj=questionArray.getJSONObject(k);
								
								ContentValues cv2=new ContentValues();				        
						        cv2.put(DataProvider.QUESTION, questionObj.getString("question"));		
						        cv2.put(DataProvider.PROJECT_ID, projectId);
						        cv2.put(DataProvider.STORY_ID, storyUUID);
						        //cv2.put(DataProvider.QUESTION_TYPE, projectId);
						        if(questionObj.getString("type").equalsIgnoreCase("dropdown")){		
						        	cv2.put(DataProvider.CHOICES, questionObj.getString("answers").replace(",", "/"));							        	
						        }
						        getContentResolver().insert(DataProvider.QUESTION_URI, cv2);
						        
						        
						        
						
					        
					        }
					        
					
				        
				        }
						
					//}
					
					
					
					result="success";
				}
			} catch (Exception e) {
				result = "Network connection error";
				Log.e("Exception", e.getMessage(), e);
			}
			return result;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (StoriesActivity.this.dlgProgress != null
					&& StoriesActivity.this.dlgProgress.isShowing()) {
				StoriesActivity.this.dlgProgress.dismiss();
			}
			if (result.equalsIgnoreCase("success")) {					


				/*AppClass.edLogin.putBoolean("isLogin", true).commit();
				startActivity(new Intent(Login.this,ProjectsActivity.class));
				finish();*/

			}
			
			
			adapter = new StoriesListAdapter(StoriesActivity.this, managedQuery(DataProvider.STORY_URI, null, 
					DataProvider.PROJECT_ID+"='"+AppClass.spUser.getString("project_id", "0")+"'", null, null));
			storiesListView.setAdapter(adapter);
		}	


	}
    
    /*private class StoriesTask1 extends AsyncTask<Void, Void, String> {

		private Looper mLooper;		
		String msg="";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			StoriesActivity.this.dlgProgress = new ProgressDialog(mContext);
			StoriesActivity.this.dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			StoriesActivity.this.dlgProgress.setCanceledOnTouchOutside(false);
			StoriesActivity.this.dlgProgress.setMessage("Please wait...");
			StoriesActivity.this.dlgProgress.show();

		}


		@Override
		protected String doInBackground(Void... paramArrayOfParams) {			
			String result = "Network connection error";
			try {
				if (Looper.myLooper() == null) {
					Looper.prepare();
				}
				mLooper = Looper.myLooper();
				JSONObject jsonObj = new JSONObject();
				//	jsonObj.put("device_id", deviceId);

				//System.out.println("Request : " + jsonObj.toString());
				StringBuffer sb = AppClass.appcontext
					.getData(mContext, AppClass.WEB_URL+"projects/"+AppClass.spUser.getString("project_id", "0")+"/stories","","Bearer "+AppClass.spLogin.getString("token", ""),"");


				if (sb == null) {
					return result;
				}
				System.out.println("Response : " + sb.toString());
				jsonObj = new JSONObject(sb.toString());

				//String st="{\"result\":\"success\",\"learner_details\":{\"learner_id\":\"1897\",\"name\":\"Ramesh B\",\"gender\":\"Male\",\"dob\":\"1976-06-18\",\"mobile\":\"9842231700\",\"designation\":\"CEO\",\"experience\":\"NIL\",\"education\":\"Master in Computer Applications\",\"office_location\":\"GN Mills \",\"city_town\":\"Coimbatore\",\"region_state\":\"Tamil Nadu\",\"country\":\"India\",\"learner_image\":\"http:\\/\\/php55.development.local\\/dev150908_361dm\\/ver1\\/imeta\\/learner_photo\\/1897_tn.jpg\"},\"guided_path\":[{\"completed_status\":\"Completed\",\"lr_id\":\"623\",\"lr_name\":\"ARRIVING IN THE NEW CITY\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"624\",\"lr_name\":\"FIRST DAY AT WORK\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"625\",\"lr_name\":\"SUNDAY LUNCH\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"626\",\"lr_name\":\"FIRST REVIEW\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"687\",\"lr_name\":\"BUSINESS CONVENTION\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"688\",\"lr_name\":\"PRE-PROJECT INTERVIEW\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"689\",\"lr_name\":\"REAL-TIME PROJECT\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"690\",\"lr_name\":\"PROJECT COMPLETION\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"}],\"product_id\":\"83\",\"user_path\":{\"Communications Module\":[{\"completed_status\":\"Completed\",\"lr_id\":\"623\",\"lr_name\":\"ARRIVING IN THE NEW CITY\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"624\",\"lr_name\":\"FIRST DAY AT WORK\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"625\",\"lr_name\":\"SUNDAY LUNCH\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"626\",\"lr_name\":\"FIRST REVIEW\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"Completed\",\"lr_id\":\"687\",\"lr_name\":\"BUSINESS CONVENTION\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"688\",\"lr_name\":\"PRE-PROJECT INTERVIEW\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"689\",\"lr_name\":\"REAL-TIME PROJECT\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"},{\"completed_status\":\"In-Progress\",\"lr_id\":\"690\",\"lr_name\":\"PROJECT COMPLETION\",\"module_name\":\"Communications Module\",\"module_id\":\"12\"}]},\"total_score\":\"127.5\",\"overall_score\":\"217\",\"score\":\"(127.5\\/217)=58.8%\"}";

				//jsonObj = new JSONObject(st);
				//				result = jsonObj.getString("result");
				//result="success";
				//System.out.println("result--->"+result);
				//if (result.equalsIgnoreCase("success")) {	
					
					JSONArray jsonArray=jsonObj.getJSONArray("data");
					
					if(jsonArray.length()>0){
						SQLiteDatabase mydb;
						 try {
					            mydb = mContext.openOrCreateDatabase(DataProvider.DATABASE_NAME, Context.MODE_PRIVATE, null);
					            Cursor allrows = mydb.rawQuery("DELETE FROM " + DataProvider.TBL_STORY, null);
					            System.out.println("COUNT : " + allrows.getCount());				        
					            allrows.close();
					            
					            Cursor allrows1 = mydb.rawQuery("DELETE FROM " + DataProvider.TBL_CHAPTER, null);
					            System.out.println("COUNT : " + allrows1.getCount());				        
					            allrows1.close();
					            
					            Cursor allrows2 = mydb.rawQuery("DELETE FROM " + DataProvider.TBL_ANSWER, null);
					            System.out.println("COUNT : " + allrows2.getCount());				        
					            allrows2.close();
					            
					            mydb.close();					            
					            
					            
					            
					            
						 }catch(Exception e)
						 {
							 e.printStackTrace();
						 }
					}
					
					for(int i=0;i<jsonArray.length();i++){
						JSONObject storyObj=jsonArray.getJSONObject(i);
						
						System.out.println("ID--->"+storyObj.getInt("id"));
						
						ContentValues cv=new ContentValues();
						cv.put(DataProvider.STORY_UUID, storyObj.getInt("uuid"));				        
				        cv.put(DataProvider.NAME, storyObj.getString("name"));
				        cv.put(DataProvider.GENDER, storyObj.getString("gender"));
				        cv.put(DataProvider.PROJECT_ID, storyObj.getString("projectId"));
				        cv.put(DataProvider.IMAGE_URL, storyObj.getString("imageUrl"));
				        
				        getContentResolver().insert(DataProvider.STORY_URI, cv);
				        
				        
				        JSONArray chapterArray=storyObj.getJSONArray("chapters");	
				        
				        for(int j=0;j<chapterArray.length();j++){
							JSONObject chapterObj=chapterArray.getJSONObject(j);
							
							
				        
					        ContentValues cv1=new ContentValues();				        
					        cv1.put(DataProvider.CHAPTER_UUID, chapterObj.getString("uuid"));		
					        cv1.put(DataProvider.STORY_ID, chapterObj.getString("storyUUID"));	
					        cv1.put(DataProvider.SYNCED, chapterObj.getString("isSyncd"));	
					        getContentResolver().insert(DataProvider.CHAPTER_URI, cv1);
					        
					        JSONArray answerArray=storyObj.getJSONArray("answers");	
					        
					        for(int k=0;k<answerArray.length();k++){
								JSONObject answerObj=answerArray.getJSONObject(j);
								
								
					        
						        ContentValues cv2=new ContentValues();				        
						        cv2.put(DataProvider.CHAPTER_ID, answerObj.getString("chapterUUID"));		
						        cv2.put(DataProvider.ANSWER_UUID, answerObj.getString("uuid"));	
						        cv2.put(DataProvider.ANSWER, answerObj.getString("response"));
						        cv2.put(DataProvider.QUESTION_ID, answerObj.getString("questionId"));
						        getContentResolver().insert(DataProvider.ANSWER_URI, cv2);
						        
						        
						        
						
					        
					        }
					        
					
				        
				        }
						
					//}
					
					
					
					result="success";
				}
			} catch (Exception e) {
				result = "Network connection error";
				Log.e("Exception", e.getMessage(), e);
			}
			return result;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (StoriesActivity.this.dlgProgress != null
					&& StoriesActivity.this.dlgProgress.isShowing()) {
				StoriesActivity.this.dlgProgress.dismiss();
			}
			if (result.equalsIgnoreCase("success")) {					


				AppClass.edLogin.putBoolean("isLogin", true).commit();
				startActivity(new Intent(Login.this,ProjectsActivity.class));
				finish();

			}
			
			
			adapter = new StoriesListAdapter(StoriesActivity.this, managedQuery(DataProvider.STORY_URI, null, null, null, null));
			storiesListView.setAdapter(adapter);
		}	


	}*/
}
