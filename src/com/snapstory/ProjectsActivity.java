package com.snapstory;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import net.simonvt.menudrawer.MenuDrawer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.snapstory.adapters.ProjectsListAdapter;
import com.snapstory.menu.SlideMenu;
import com.snapstory.util.DataProvider;

public class ProjectsActivity extends Activity {
    public static final String USER_NAME_EXTRA = "user_name_extra";

    private ListView projectsListView;
    public static final int PROJECT_LOADER_ID = 1337;
    private ProjectsListAdapter adapter;
    private MenuDrawer mDrawer;
    private ProgressDialog dlgProgress;
	private Activity mContext;
	private String SORT_ORDER = DataProvider.NAME+ " COLLATE LOCALIZED ASC";

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.projects);
        mDrawer=SlideMenu.setMenu(this, R.layout.projects);
        mContext=this;
        projectsListView = (ListView)findViewById(R.id.projects_list);
        
        
    /*    ContentValues cv=new ContentValues();
        
        cv.put(DataProvider.NAME, "PROJ1");
        cv.put(DataProvider.DESCRIPTION, "PROJ DESC");
        cv.put(DataProvider.IMAGE_URL, "http://snapstory-staging.s3.amazonaws.com/projects/school.jpg");
        
        getContentResolver().insert(DataProvider.PROJECT_URI, cv);*/

        	
        	if (AppClass.appcontext.isOnlineWithoutDialgo(mContext)) {
        		new ProjectTask().execute();
			}else{
				adapter = new ProjectsListAdapter(ProjectsActivity.this, managedQuery(DataProvider.PROJECT_URI, null, null, null, SORT_ORDER));
		        projectsListView.setAdapter(adapter);
			}
        	
			
		
        
        
        projectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long projectId = adapter.getProjectId(position);
                System.out.println("project_id-->"+projectId);
                AppClass.edUser.putString("project_id", ""+projectId).commit();
                Intent intent = new Intent(ProjectsActivity.this, StoriesActivity.class);
                //intent.putExtra(StoriesActivity.PROJECT_ID_EXTRA, projectId);
                startActivity(intent);
            }
        });
        
        
        
        
        ((ImageView)findViewById(R.id.MenuBtn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawer.openMenu();
			}
		});
        

        /*TextView nameTV = (TextView)findViewById(R.id.textViewWelcome);

        String welcomeString = getString(R.string.welcome_message);
        if(getIntent().hasExtra(USER_NAME_EXTRA)) {
            welcomeString = welcomeString + ", " + getIntent().getStringExtra(USER_NAME_EXTRA);
        } else {
            welcomeString = welcomeString + "!";
        }

        nameTV.setText(welcomeString);
*/
        /*Call<ProjectsResponseWrapper> call = SnapStoryServiceGenerator.getSnapStory(this).getProjects();
        call.enqueue(new Callback<ProjectsResponseWrapper>() {
            @Override
            public void onResponse(Call<ProjectsResponseWrapper> call, Response<ProjectsResponseWrapper> response) {
                ProjectsResponseWrapper wrapper = response.body();
                if (wrapper != null) {
                    DBHelper.upsertProjects(wrapper.getData());
                    getLoaderManager().initLoader(PROJECT_LOADER_ID, null, new ProjectListLoader());
                }
            }

            @Override
            public void onFailure(Call<ProjectsResponseWrapper> call, Throwable t) {
                getLoaderManager().initLoader(PROJECT_LOADER_ID, null, new ProjectListLoader());
            }
        });*/


    }

    @Override
    protected void onResume() {
        super.onResume();
       /* Intent storySyncIntent = new Intent(this, StorySyncService.class);
        startService(storySyncIntent);*/
        
        
        if((AppClass.spLogin.getAll().isEmpty()))
		{
			startActivity(new Intent(ProjectsActivity.this,Login.class));
			finish();	
		}
    }
    
    
    
    

    /*private class ProjectListLoader implements LoaderManager.LoaderCallbacks<FSCursor> {

        @Override
        public Loader<FSCursor> onCreateLoader(int id, Bundle args) {
            return new FSCursorLoader<>(ProjectsActivity.this, projectTable());
        }

        @Override
        public void onLoadFinished(Loader<FSCursor> loader, FSCursor data) {
            adapter = new ProjectsListAdapter(ProjectsActivity.this, data);
            projectsListView.setAdapter(adapter);
        }

        @Override
        public void onLoaderReset(Loader<FSCursor> loader) {

        }
    }*/
    
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
    
    private class ProjectTask extends AsyncTask<Void, Void, String> {

		private Looper mLooper;		
		String msg="";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			ProjectsActivity.this.dlgProgress = new ProgressDialog(mContext);
			ProjectsActivity.this.dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			ProjectsActivity.this.dlgProgress.setCanceledOnTouchOutside(false);
			ProjectsActivity.this.dlgProgress.setMessage("Please wait...");
			ProjectsActivity.this.dlgProgress.show();

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
					.getData(mContext, AppClass.WEB_URL+"projects","","Bearer "+AppClass.spLogin.getString("token", ""),"");

				//http://staging-api.snapstory.co/projects
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
					            Cursor allrows = mydb.rawQuery("DELETE FROM " + DataProvider.TBL_PROJECT, null);
					            System.out.println("COUNT : " + allrows.getCount());				        
					            allrows.close();
					            
					            /*Cursor allrows1 = mydb.rawQuery("DELETE FROM " + DataProvider.TBL_QUESTION, null);
					            System.out.println("COUNT : " + allrows1.getCount());				        
					            allrows1.close();*/
					            
					            mydb.close();					            
					            
					            
					            
					            
						 }catch(Exception e)
						 {
							 e.printStackTrace();
						 }
					}
					
					for(int i=0;i<jsonArray.length();i++){
						JSONObject projectObj=jsonArray.getJSONObject(i);
						
						System.out.println("ID--->"+projectObj.getInt("id"));
						
						ContentValues cv=new ContentValues();
						cv.put(DataProvider.PROJECT_ID, projectObj.getInt("id"));				        
				        cv.put(DataProvider.NAME, projectObj.getString("name"));
				        cv.put(DataProvider.DESCRIPTION, projectObj.getString("description"));
				        cv.put(DataProvider.IMAGE_URL, projectObj.getString("imageUrl"));
				        cv.put(DataProvider.CITY, projectObj.getString("city"));
				        cv.put(DataProvider.LOCATION, projectObj.getString("location"));
				        
				        getContentResolver().insert(DataProvider.PROJECT_URI, cv);
				        
				        
				        /*JSONArray questionArray=projectObj.getJSONArray("Questions");	
				        
				        for(int j=0;j<questionArray.length();j++){
							JSONObject questionObj=questionArray.getJSONObject(j);
							
							
				        
					        ContentValues cv1=new ContentValues();				        
					        cv1.put(DataProvider.QUESTION, questionObj.getString("question"));		
					        cv1.put(DataProvider.PROJECT_ID, questionObj.getString("projectId"));				 
					        
					        
					        
					        
					        String answer="";
					        
					        if(questionObj.getString("questionType").equalsIgnoreCase("multiple")){		
					        	JSONArray answerArray=questionObj.getJSONArray("answers");
					        	for(int k=0;k<answerArray.length();k++){
					        		if(k!=0)
					        			answer=answer+"/";
									answer=answer+answerArray.getString(k);				        
					        	}		
					        	
					        }
					        cv1.put(DataProvider.CHOICES, answer);	
					        getContentResolver().insert(DataProvider.QUESTION_URI, cv1);
				        
				        }*/
						
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
			if (ProjectsActivity.this.dlgProgress != null
					&& ProjectsActivity.this.dlgProgress.isShowing()) {
				ProjectsActivity.this.dlgProgress.dismiss();
			}
			if (result.equalsIgnoreCase("success")) {					


				/*AppClass.edLogin.putBoolean("isLogin", true).commit();
				startActivity(new Intent(Login.this,ProjectsActivity.class));
				finish();*/

			}
			
			
			adapter = new ProjectsListAdapter(ProjectsActivity.this, managedQuery(DataProvider.PROJECT_URI, null, null, null, SORT_ORDER));
	        projectsListView.setAdapter(adapter);
		}	


	}
}
