package com.snapstory;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

public class AppClass extends Application{
	
	public static AppClass appcontext;
	
	
	
	
	
	//Live Url
	public static String WEB_URL="http://staging-api.snapstory.co/";
	/*public static String WEB_URL_SUBMIT="http://newp.361dm.com/mobile_app/app_handler_response.php";
	public static String WEB_URL_SYNC="http://newp.361dm.com/mobile_app/sync_data.php";
	public static String WEB_URL_SYNC_LR="http://newp.361dm.com/mobile_app/sync_data_lr.php";*/
	
	
	
	public static SharedPreferences spUser;
	public static Editor edUser;
	
	public static SharedPreferences spLogin;
	public static Editor edLogin;
	
	
	static Activity activityContext;
	private ProgressDialog progressDialog;
	
	//public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	//OkHttpClient client = new OkHttpClient();

	/*public String post(String url, String json,String token) throws IOException {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder()
		.url(url)
		.post(body)
		.header("Authorization", "Bearer " + token)
        .header("Content-Type", "applicaton/json")
		.build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}*/
	
	@Override
	public void onCreate() {		
		super.onCreate();
		appcontext = this;
		spUser=getSharedPreferences("USER",MODE_PRIVATE );	
		edUser=spUser.edit();
		
		spLogin=getSharedPreferences("LOGIN",MODE_PRIVATE );	
		edLogin=spLogin.edit();
	}
	
	
	
	
	public boolean isOnline(Context mContext) {
		
		
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo == null) {
			showMyDialog(mContext, "No Network Connection");
			return false;
		} else if (netInfo != null && netInfo.isConnectedOrConnecting()	&& netInfo.isAvailable()) {
			return true;
		} else {
			showMyDialog(mContext, "No Network Connection");
			return false;
		}
		
		
		
	}
	
	public boolean isOnlineWithoutDialgo(Context mContext) {
		
		
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo == null) {
			//showMyDialog(mContext, "No Network Connection");
			return false;
		} else if (netInfo != null && netInfo.isConnectedOrConnecting()	&& netInfo.isAvailable()) {
			return true;
		} else {
			//showMyDialog(mContext, "No Network Connection");
			return false;
		}
		
		
		
	}
	
	public StringBuffer getData(Context context, String url,
			String... httpParameters) {
		StringBuffer sb = null;
		BufferedReader br = null;
		String line = null;
		try {
			br = new BufferedReader(new InputStreamReader(getData(url,httpParameters)));
			sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (ClientProtocolException e) {
			sb = null;
			Log.e(getPackageCodePath(), e.getMessage(), e);
		} catch (URISyntaxException e) {
			sb = null;
			Log.e(getPackageCodePath(), e.getMessage(), e);
		} catch (IOException e) {
			sb = null;
			Log.e(getPackageCodePath(), e.getMessage(), e);
		} catch (Exception e) {
			sb = null;
			Log.e(getPackageCodePath(), e.getMessage(), e);
		} finally {
			try {
				br.close();
				br = null;
			} catch (Exception e) {
				br = null;
			}
		}
		return sb;
	}
	
	public static void showMyDialog(final Context mContext, String message) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setCancelable(true);
		dialog.setMessage(message);
		dialog.setNegativeButton("Ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		dialog.show();
	}
	
	@SuppressWarnings("deprecation")
	public static InputStream getData(String url, String... params)
			throws URISyntaxException, ClientProtocolException, IOException {
		
		HttpResponse response;
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 30000;
		int timeoutSocket = 35000;
		
		if(params[2].equalsIgnoreCase("POST")){
			
			System.out.println("POST");
			HttpPost request = new HttpPost(url);
			request.addHeader("Authorization",params[1]);
			request.addHeader("Content-Type","application/json");
			request.addHeader("Accept", "application/json");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();			
			nvps.add(new BasicNameValuePair("data", params[0]));
			
			
			request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);		
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient client = new DefaultHttpClient(httpParameters);
			response = client.execute(request);
		}else{
			System.out.println("GET");
			HttpUriRequest request = new HttpGet(url);
			request.addHeader("Authorization",params[1]);
			request.addHeader("Content-Type","application/json");
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);		
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient client = new DefaultHttpClient(httpParameters);
			response = client.execute(request);
		}
		
		
		System.out.println("URL--->"+url);
		
		
		
		return response.getEntity().getContent();
	}
	
	
	
	
	
	
	/*@SuppressWarnings("unused")
	public void menuHandler(Activity context, View view) {
		
		activityContext=context;
		switch (view.getId()) {		
		
		case R.id.PopupMenuBtn:
			PopUp.showPopup(context, popup);
			break;
		case R.id.LogoutBtn:
			popup.dismiss();
			context.startActivity(new Intent(context,Login.class));
			context.finish();
			break;
		case R.id.ProfileBtn:
			popup.dismiss();
			context.startActivity(new Intent(context,Profile.class));
			context.finish();
			break;
		case R.id.PopParentlayout:
			popup.dismiss();
			break;
	}
	}*/
	
	public static String toSentenceCase(String inputString) {
	       String result = "";
	       if (inputString.length() == 0) {
	           return result;
	       }
	       char firstChar = inputString.charAt(0);
	       char firstCharToUpperCase = Character.toUpperCase(firstChar);
	       result = result + firstCharToUpperCase;
	       boolean terminalCharacterEncountered = false;
	       char[] terminalCharacters = {'.', '?', '!'};
	       for (int i = 1; i < inputString.length(); i++) {
	           char currentChar = inputString.charAt(i);
	           if (terminalCharacterEncountered) {
	               if (currentChar == ' ') {
	                   result = result + currentChar;
	               } else {
	                   char currentCharToUpperCase = Character.toUpperCase(currentChar);
	                   result = result + currentCharToUpperCase;
	                   terminalCharacterEncountered = false;
	               }
	           } else {
	               char currentCharToLowerCase = Character.toLowerCase(currentChar);
	               result = result + currentCharToLowerCase;
	           }
	           for (int j = 0; j < terminalCharacters.length; j++) {
	               if (currentChar == terminalCharacters[j]) {
	                   terminalCharacterEncountered = true;
	                   break;
	               }
	           }
	       }
	       return result;
	   }
	
	/*static int doSync(Activity mContext){
		activityContext=mContext;
		AlertDialog.Builder altDialog= new AlertDialog.Builder(mContext); 	    	
		altDialog.setTitle("Confirm ...");
        altDialog.setMessage("Are you sure to update the Components?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {	
						dialog.cancel();
						appcontext.new SyncTask().execute();
														
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {					
						dialog.cancel();
					}
				});    	  
    	 altDialog.show();	
    	 
    	 try {
			appcontext.new SyncTask().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    	 return 1;
		
	}*/
	
	/*private class SyncTask extends AsyncTask<Void, Void, String>  {		

		private Looper mLooper;

		@Override
		protected void onPreExecute() {			
			//progressDialog= ProgressDialog.show(activityContext, null,"Synchronizing", true);
			super.onPreExecute();
			if (!AppClass.appcontext.isOnline(activityContext)) {
				this.cancel(true);
				return;
			}
			progressDialog = new ProgressDialog(activityContext);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage("Updating...");			
			progressDialog.show();
		}

		@Override
		protected String doInBackground(Void... args) {
			String result = "Network connection error";
			if (Looper.myLooper() == null) {
				Looper.prepare();
			}
			mLooper = Looper.myLooper();
			result=SyncProcess();		
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (progressDialog != null
					&& progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equalsIgnoreCase("success")) {		
				
				Intent o=new Intent(activityContext,Login.class);
	        	o.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	startActivity(o);
	        	activityContext.finish();
				Toast.makeText(activityContext, "Updated successfully", Toast.LENGTH_LONG).show();
								
			}else if(result.equalsIgnoreCase("no_data")){ 
				Toast.makeText(activityContext, "No records found", Toast.LENGTH_LONG).show();
			}else {
				//result = "Login failed";	
				Toast.makeText(activityContext, "Update failed", Toast.LENGTH_LONG).show();
			}
		}
	}*/
	
	/*private String SyncProcess(){		
		
		String result = "Network connection error";
		try {
			
			Toast.makeText(getApplicationContext(), "Sync", Toast.LENGTH_LONG).show();
		    JSONArray json=SyncUtil.getComponentList(activityContext);
		    
		    if(json.length()==0){
		    	result="no_data";
		    	return result;
		    }
		    
		    JSONObject jsonObj = new JSONObject();
		    //jsonObj.put("action", json);
			jsonObj.put("components", json);
			
			System.out.println("Request : "+ AppClass.WEB_URL_SUBMIT+"?action=offline_submit" + json.toString());
			StringBuffer sb = AppClass.appcontext
					.getData(activityContext, AppClass.WEB_URL_SUBMIT+"?action=offline_submit",json.toString());
			
		
			if (sb == null) {
				return result;
			}
			System.out.println("Response : " + sb.toString());
			jsonObj = new JSONObject(sb.toString());
			result = jsonObj.getString("result");
			System.out.println("result--->"+result);
			if (result.equalsIgnoreCase("success")) {		
				
				Editor ed=AppClass.spCompleted.edit();
				ed.clear();
				ed.commit();
				
				String last_pid=jsonObj.getString("patient_id");
				String last_eid=jsonObj.getString("event_id");
				String last_lid=jsonObj.getString("location_id");
				String last_upid=jsonObj.getString("update_patient_id");
				ContentValues values=new ContentValues();
				//values.put(DataProvider.P_SYNC,1);
				//getContentResolver().update(DataProvider.PATIENT_URI,values, " user_id='"+AppClass.sharedPreferenceLogin.getString("field_worker_id", "0")+"' AND "+DataProvider.P_SYNC+"=0 AND "+DataProvider.ID+"<="+last_pid, null);
				
				
				result="success";
			}
			
		    
			//Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();	
			
		} catch (Exception e) {
			result = "Login Failure";
			Log.e("Exception", e.getMessage(), e);
		}
		return result;
	}*/
	
	/*public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) appcontext.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    } */
	
	
	public static void logOut()
	{
		edUser.clear().commit();
		edLogin.clear().commit();
	}
	
	public static String getLoginB64Auth (String login, String pass) {
		   String source=login+":"+pass;
		   String ret="Basic "+Base64.encodeToString(source.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
		   return ret;
	}
	

}
