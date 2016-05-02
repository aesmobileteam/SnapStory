package com.snapstory;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;


public class StorySyncBroadcast extends BroadcastReceiver{

	
	private static Context mainContext;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		mainContext=context;		
		System.out.println("broadcast started");
		if(intent.equals("android.net.conn.CONNECTIVITY_CHANGE")){
			System.out.println("network broadcast");			
		}
		
		Intent intent1 = new Intent(mainContext, StorySyncService.class);
		mainContext.startService(intent1);
		
	}
	
	
	
}
