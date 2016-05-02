package com.snapstory;

import android.app.IntentService;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import com.snapstory.util.DataProvider;




public class StorySyncService extends IntentService{
	
	private ArrayList<String> storySyncList;

    public StorySyncService() {
        super("StorySyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	
    	try {
    		
    	storySyncList=new ArrayList<String>();
    	Cursor cs = getContentResolver().query(DataProvider.STORY_URI,null,null,null, null);
        //Retriever data = chapterTable().joinStoryTable(FSJoin.Type.INNER).find().byNotSynced().andFinally().get();
    	JSONArray storyArray=new JSONArray();
    	
    	System.out.println("stories count------------->" + cs.getCount());
  		cs.moveToFirst();
  		
  		//if (cs.moveToFirst()) {
  		do {
  			
  			JSONObject storyObj=new JSONObject();      	      
  			storyObj.put("birthDate",cs.getString(cs.getColumnIndex(DataProvider.BIRTH_DATE)));
  			storyObj.put("gender",cs.getString(cs.getColumnIndex(DataProvider.GENDER)));
  			storyObj.put("image",cs.getString(cs.getColumnIndex(DataProvider.IMAGE)));
  			storyObj.put("projectId",AppClass.spUser.getString("project_id",""));
  			storyObj.put("name",cs.getString(cs.getColumnIndex(DataProvider.NAME)));
  			storyObj.put("uuid",cs.getString(cs.getColumnIndex(DataProvider.STORY_UUID)));
  			storyObj.put("reportTemplateId",cs.getString(cs.getColumnIndex(DataProvider.REPORT_TEMPLATE_ID)));
  			storyObj.put("reportType",cs.getString(cs.getColumnIndex(DataProvider.REPORT_TYPE)));
  			storyObj.put("updates",getChapterforStory(cs.getString(cs.getColumnIndex(DataProvider.STORY_UUID))));
  			storySyncList.add(cs.getString(cs.getColumnIndex(DataProvider.STORY_UUID)));
  			if(getChapterforStory(cs.getString(cs.getColumnIndex(DataProvider.STORY_UUID))).length()>0)
  			storyArray.put(storyObj);
  		} while (cs.moveToNext());
  		
  		storysync(storyArray);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }


    private JSONArray getChapterforStory(String storyUUID) {
    	
    	JSONArray chapterArray=new JSONArray();
    	try {
        	Cursor cs = getContentResolver().query(DataProvider.CHAPTER_URI,null,DataProvider.STORY_ID+"='"+storyUUID+"' and "
        			+DataProvider.SYNCED+"!=1",null, null);
            //Retriever data = chapterTable().joinStoryTable(FSJoin.Type.INNER).find().byNotSynced().andFinally().get();
        	
        	
        	System.out.println("chapter count------------->" + cs.getCount());
        	if(cs.getCount()>0){
      		cs.moveToFirst();
      		
      		//if (cs.moveToFirst()) {
      		do {
      			
      			JSONObject chapterObj=new JSONObject();      	      
      			chapterObj.put("uuid",cs.getString(cs.getColumnIndex(DataProvider.CHAPTER_UUID)));
      			chapterObj.put("latitude",cs.getString(cs.getColumnIndex(DataProvider.LATITUDE)));
      			chapterObj.put("longitude",cs.getString(cs.getColumnIndex(DataProvider.LONGITUDE)));
      			chapterObj.put("recordedAt",cs.getString(cs.getColumnIndex(DataProvider.RECORDED_AT)));
      			//chapterObj.put("storyUUID",cs.getString(cs.getColumnIndex(DataProvider.STORY_ID)));
      			//chapterObj.put("isSyncd",cs.getString(cs.getColumnIndex(DataProvider.SYNCED)));
      			
      			chapterObj.put("answers",getAnswersforChapter(cs.getString(cs.getColumnIndex(DataProvider.CHAPTER_UUID))));
      			if(getAnswersforChapter(cs.getString(cs.getColumnIndex(DataProvider.CHAPTER_UUID))).length()>0)
      				chapterArray.put(chapterObj);
      		} while (cs.moveToNext());
      		
        	}
      		
        	}catch(Exception e){
        		e.printStackTrace();
        	}
    	return chapterArray;
	}

	private JSONArray getAnswersforChapter(String chapterUUID) {
		JSONArray answerArray=new JSONArray();
    	try {
        	Cursor cs = getContentResolver().query(DataProvider.ANSWER_URI,null,DataProvider.CHAPTER_ID+"='"+chapterUUID+"'",null, null);
            //Retriever data = chapterTable().joinStoryTable(FSJoin.Type.INNER).find().byNotSynced().andFinally().get();
        	
        	
        	System.out.println("answers count------------->" + cs.getCount());
        	
        	if(cs.getCount()>0){
      		cs.moveToFirst();
      		
      		//if (cs.moveToFirst()) {
      		do {
      			
      			JSONObject answerObj=new JSONObject();      	      
      			//answerObj.put("chapterUUID",cs.getString(cs.getColumnIndex(DataProvider.CHAPTER_ID)));
      			//answerObj.put("uuid",cs.getString(cs.getColumnIndex(DataProvider.ANSWER_UUID)));
      			answerObj.put("response",cs.getString(cs.getColumnIndex(DataProvider.ANSWER)));
      			
      			//int qID=0;
      		/*	if(cs.getString(cs.getColumnIndex(DataProvider.QUESTION_ID)).length()>0)
      				qID=Integer.parseInt(cs.getColumnIndex(DataProvider.QUESTION_ID));*/
      			answerObj.put("questionId",cs.getPosition());
      			
      			answerArray.put(answerObj);
      		} while (cs.moveToNext());
      		
        	}
      		
        	}catch(Exception e){
        		e.printStackTrace();
        	}
    	return answerArray;
	}
	
	
	private String storysync(JSONArray storyArray){
		
		String result="";
		try {
			
			
			
			JSONObject jsonObj=new JSONObject();
			
			/*{
			    "data": [{
			        "uuid": "82347842309",
			        "name": "Jonathan Spies",
			        "reportTemplateId": "sadf987d09s87",
			        "reportType": "person",
			        "gender": "male",
			        "birthDate": "2000-03-20",
			        "image": "base64encoded blob",
			        "updates": [{
			            "uuid": "456745234534",
			            "latitude": "90.54645",
			            "longitude": "90.456",
			            "recordedAt": "2016-02-01 04:56:00",
			            "answers": [{
			                "questionId": 2,
			                "response": "I like goats"
			            }]
			        }]
			    }]
			}*/
			
			jsonObj.put("data", storyArray);

			System.out.println("Request : " + jsonObj.toString());
			StringBuffer sb = AppClass.appcontext
					.getData(this, AppClass.WEB_URL+"reports/batch",jsonObj.toString(),"Bearer "+AppClass.spLogin.getString("token", ""),"POST");
			
			//String sb = AppClass.appcontext.post(AppClass.WEB_URL+"reports/batch", jsonObj.toString(), AppClass.spLogin.getString("token", ""));

			if (sb == null) {
				return result;
			}
			System.out.println("Response : " + sb.toString());
			jsonObj = new JSONObject(sb.toString());
			
			System.out.println("data-->"+jsonObj.getJSONObject("data"));
			JSONObject data=jsonObj.getJSONObject("data");
			if(data.getString("uuid")!=null){
				for(int i=0;i<storySyncList.size();i++){
					ContentValues cvNew=new ContentValues();
					cvNew.put(DataProvider.SYNCED, 1);
					getContentResolver().update(DataProvider.CHAPTER_URI, cvNew,DataProvider.STORY_ID+"='"+storySyncList.get(i)+"'" , null);
				}
			}
			result="success";
			//result = jsonObj.getString("status");
			//System.out.println("result--->"+result);
			/*if (result.equalsIgnoreCase("success")) {	
				result="success";				
				
			}*/

		} catch (Exception e) {
			result = "Network connection error";
			Log.e("Exception", e.getMessage(), e);
		}
		return result;
	}
	
	

	
}
