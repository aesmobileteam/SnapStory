package com.snapstory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.simonvt.menudrawer.MenuDrawer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.snapstory.adapters.QuestionListAdapter;
import com.snapstory.menu.SlideMenu;
import com.snapstory.util.DataProvider;
import com.snapstory.util.GPSTracker;
import com.snapstory.util.ShanListView;

public class ChapterActivity extends Activity {
    public final static String PROJECT_ID_EXTRA = "project_id_extra";
    public final static String STORY_ID_EXTRA = "story_id_extra";
    public final static String STORY_NAME_EXTRA = "story_name_extra";
    //private Chapter submittedChapter;
    QuestionListAdapter adapter;
    ShanListView questionListView;
    Button submitButton;
    String storyUUID,projectUUID,story_name;
    private MenuDrawer mDrawer;
    private GPSTracker gps;
    private Activity mContext;

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.chapter);
        mDrawer=SlideMenu.setMenu(this, R.layout.chapter);
        mContext=this;

        questionListView = (ShanListView)findViewById(R.id.question_list);
        
        /*ContentValues cv=new ContentValues();
        
        cv.put(DataProvider.QUESTION, "Which is your favourate vehicle?");
        
        cv.put(DataProvider.CHOICES, "Car/Bus/Van/Bike/Walk");
        
        getContentResolver().insert(DataProvider.QUESTION_URI, cv);*/
        projectUUID = AppClass.spUser.getString("project_id", "");
        storyUUID = getIntent().getStringExtra(STORY_ID_EXTRA);
        
        Bundle b=getIntent().getExtras();
		if(b!=null)
		{
			System.out.println("b.getString(STORY_NAME_EXTRA)--->"+b.getString(STORY_NAME_EXTRA));
			story_name=b.getString(STORY_NAME_EXTRA);
			
			if(story_name!=null){
        
		        ((View)findViewById(R.id.headlayoutText)).setVisibility(View.VISIBLE);
				((TextView)findViewById(R.id.headtextName)).setText(""+b.getString(STORY_NAME_EXTRA));
				((TextView)findViewById(R.id.headtextSchool)).setText(""+AppClass.spUser.getString("project_name", ""));
				
			}
		}
        
        adapter = new QuestionListAdapter(ChapterActivity.this, managedQuery(DataProvider.QUESTION_URI, null,
        		DataProvider.PROJECT_ID+"='"+projectUUID+"' and "+DataProvider.STORY_ID+"='"+storyUUID+"'", null, null),getAnswersForChapter());
        questionListView.setAdapter(adapter);
        
        ((ScrollView)findViewById(R.id.Scrollreport)).smoothScrollTo(0,0);

        submitButton = (Button)findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswers();

            }
        });
        
        ((ImageView)findViewById(R.id.MenuBtn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawer.openMenu();
			}
		});

        
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

    private void submitAnswers() {
       // submittedChapter = new Chapter();
        
    	double latitude=0,longitude=0;
        
        try {
			
			gps = new GPSTracker(mContext);
			
			if(gps.canGetLocation()){
                 
                 latitude = gps.getLatitude();
                 longitude = gps.getLongitude();  
                 System.out.println("Your Location is - \nLat: " + latitude + "\nLong: " + longitude); 			 
 				 
 				 
             }else{
                 gps.showSettingsAlert();
             }		
				
		} catch (Exception e) {
			Log.e("Exception", e.getMessage(), e);
		}	
      
        UUID chapterUUID = UUID.randomUUID();
        System.out.println("chapterID-->"+chapterUUID);
        
        ContentValues cv=new ContentValues();
        
        cv.put(DataProvider.CHAPTER_UUID, ""+chapterUUID);
        cv.put(DataProvider.STORY_ID, ""+storyUUID);        
        cv.put(DataProvider.SYNCED, "false");
        cv.put(DataProvider.LATITUDE, latitude);
        cv.put(DataProvider.LONGITUDE, longitude);
        cv.put(DataProvider.RECORDED_AT, new Date().toString());
        
        getContentResolver().insert(DataProvider.CHAPTER_URI, cv);
        updateAnswers(chapterUUID);
        Intent intent = new Intent(this, StorySyncService.class);
        startService(intent);

        Intent projectsIntent = new Intent(this, ProjectsActivity.class);
        startActivity(projectsIntent);
    }
    
    public void updateAnswers(UUID chapterUUID) {
       // List<Answer> answers = new ArrayList<>();
        for(int i=0; i<adapter.getCount(); i++) {
            String answer = adapter.getAnswers().get(i);
            
            ContentValues cv=new ContentValues();
            
            cv.put(DataProvider.ANSWER, answer);
            cv.put(DataProvider.ANSWER_UUID, ""+UUID.randomUUID());        
            cv.put(DataProvider.CHAPTER_ID, ""+chapterUUID);
           // cv.put(DataProvider.QUESTION_ID, adapter.getQuestionID(i) );
            
            getContentResolver().insert(DataProvider.ANSWER_URI, cv);
        }

       // return answers;
    }

   /* public List<Answer> getAnswers(UUID chapterUUID) {
        List<Answer> answers = new ArrayList<>();
        for(int i=0; i<adapter.getCount(); i++) {
            String answer = adapter.getAnswers().get(i);
            answers.add(new Answer(answer, chapterUUID, UUID.randomUUID(), adapter.getQuestionID(i)));
        }

        return answers;
    }*/

    private List<String> getAnswersForChapter() {

        //System.out.println("chapterID Load-->"+chapterUUID);
        List<String> answers = new ArrayList<String>();
        
        Cursor cs = getContentResolver().query(DataProvider.QUESTION_URI,null,
  				null, null, null);
        
        int size=cs.getCount();
        //Retriever answerData = answerTable().find().byChapterId(chapterUUID.toString()).andFinally().get();
        /*if(answerData != null && answerData.moveToFirst()) {
            do {
                System.out.println("answer-->"+answerTable().getApi().answer(answerData));
                answers.add(answerTable().getApi().answer(answerData));
            } while (answerData.moveToNext());
            answerData.close();
        }*/
        for(int i=0;i<size;i++)
        	answers.add("Bus");
        return answers;
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        
        if((AppClass.spLogin.getAll().isEmpty()))
		{
			startActivity(new Intent(ChapterActivity.this,Login.class));
			finish();	
		}else
		{
			
		
        
        long projectId = -1;

        if(getIntent().hasExtra(PROJECT_ID_EXTRA)) {
            projectId = getIntent().getLongExtra(PROJECT_ID_EXTRA, -1);
        }
        
		}
       // getLoaderManager().initLoader(12, null, new QuestionListLoader(projectId));
    }


    /*private class QuestionListLoader implements LoaderManager.LoaderCallbacks<FSCursor> {
        long projectId;
        UUID chapterUUID = UUID.randomUUID();
        List<String> answers = new ArrayList<>();
        String chapterid;

        public QuestionListLoader(long projectId) {
            this.projectId = projectId;
        }

        @Override
        public Loader<FSCursor> onCreateLoader(int id, Bundle args) {

            Retriever chapterID = chapterTable().find().byStoryId(storyUUID.toString()).andFinally().get();
            if(chapterID != null && chapterID.moveToFirst()) {
                do {
                    System.out.println("chapter id-->" + chapterTable().getApi().uuid(chapterID));
                    chapterid=chapterTable().getApi().uuid(chapterID);
                } while (chapterID.moveToNext());
                chapterID.close();
            }
            if(chapterid!=null)
                answers=getAnswersForChapter(UUID.fromString(chapterid));
            //String chapter=chapterTable().getApi().uuid(chapterID);
            return new FSCursorLoader<>(ChapterActivity.this, questionTable().find().byProjectId(projectId).andFinally());

        }

        @Override
        public void onLoadFinished(Loader<FSCursor> loader, FSCursor data) {
            adapter = new QuestionListAdapter(ChapterActivity.this, data,answers,storyUUID);
            questionListView.setAdapter(adapter);
        }

        @Override
        public void onLoaderReset(Loader<FSCursor> loader) {

        }
    }*/
}
