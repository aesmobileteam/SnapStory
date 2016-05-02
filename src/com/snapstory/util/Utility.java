package com.snapstory.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Utility {
	
	public static final String APP_DIRECTORY = "/sdcard/SnapStory";
	
	public static  Context mcon;
	
	public static int FLAG=0;
	
	
	

	
	
	
	//public static final String BNI_URL = "http://php55.development.local/dev150330_bni/ver1/bni/index.php/api/bni_master/";
	
	
	public static InputStream getData(String url, String... params)
			throws URISyntaxException, ClientProtocolException, IOException {
		HttpPost request = new HttpPost(url);
		HttpParams httpParameters = new BasicHttpParams();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("data", params[0]));
		request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		int timeoutConnection = 30000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 35000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpClient client = new DefaultHttpClient(httpParameters);
		HttpResponse response = client.execute(request);
		return response.getEntity().getContent();
	}
	
	

	@SuppressWarnings("deprecation")
	public static boolean isValidSession() {
		Date begin, end, current;
		begin = new Date();
		current = new Date();
		end = new Date();
		begin.setHours(9);
		begin.setMinutes(00);
		end.setHours(19);
		end.setMinutes(00);
		if (current.after(begin) && current.before(end))
			return true;
		return false;
	}
	
	public static InputStream postFile(String url, String... param)
			throws ClientProtocolException, IOException {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);

		for (int i = 1; i < param.length; i++) {
			File file = new File(param[i]);
			System.out.println(param[i] + " " + file.exists());

			if (file.exists()) {
				FileBody bin = new FileBody(file);
				reqEntity.addPart("attachment" + i, bin);
			}
		}
		reqEntity.addPart("data", new StringBody(param[0]));
		post.setEntity(reqEntity);
		HttpResponse response = client.execute(post);
		return response.getEntity().getContent();
	}
	
	public static String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
	
	
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	

	public static void closeCursor(Cursor c) {
		try {
			if (c != null) {
				c.close();
				c = null;
			}
		} catch (Exception e) {
			Log.e("Exception", e.getMessage(), e);
		}
	}
	

	public static void showMyDialog(final Context mContext, String message) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setCancelable(true);
		dialog.setMessage(message);
		dialog.setNegativeButton("Ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialog.show();
	}

	
	public static void closeDataBase(SQLiteDatabase db) {
		try {
			if (db != null) {
				db.close();
				db = null;
			}
		} catch (Exception e) {
			Log.e("Exception", e.getMessage(), e);
		}
	}
	public static boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	

}