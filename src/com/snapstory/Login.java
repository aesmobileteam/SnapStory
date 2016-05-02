package com.snapstory;

import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.snapstory.util.Utility;


public class Login extends Activity {

	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private ProgressDialog dlgProgress;
	private Activity mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mContext=this;
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		mPasswordView=(EditText)findViewById(R.id.password);

		
		
		
		
		
		((Button)findViewById(R.id.email_sign_in_button)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				String validate=validateUserNamePass();
				
				System.out.println("validate-->"+validate);
				
				if(validate.trim().length()>0)
				{
					Utility.showMyDialog(mContext, validate);
				}else
				{
					new LoginTask().execute();

				}


			}
		});

		







	}


	public String validateUserNamePass()
	{

		String validate="";
		if(mEmailView.getText().toString().trim().length()>0)
		{

			if(Utility.isValidEmail(mEmailView.getText().toString())==false)
			{
				validate="Please enter valid email address";
			}

		}else
		{
			validate="Please enter email address";
		}

		if(!(mPasswordView.getText().toString().trim().length()>0))
		{
			validate=validate+" \nPlease enter password";
		}

		return validate;

	}

	

	

	

	private class LoginTask extends AsyncTask<Void, Void, String> {

		private Looper mLooper;		
		String msg="";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!AppClass.appcontext.isOnline(mContext)) {
				this.cancel(true);
				return;
			}
			Login.this.dlgProgress = new ProgressDialog(mContext);
			Login.this.dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			Login.this.dlgProgress.setCanceledOnTouchOutside(false);
			Login.this.dlgProgress.setMessage("Logging in...");
			Login.this.dlgProgress.show();

		}


		@Override
		protected String doInBackground(Void... paramArrayOfParams) {			
			String result = "Network connection error";
			try {
				if (Looper.myLooper() == null) {
					Looper.prepare();
				}
				mLooper = Looper.myLooper();
				

				String tockenLogin=AppClass.getLoginB64Auth(mEmailView.getText().toString().trim(), mPasswordView.getText().toString().trim());
				System.out.println("tockenLogin--->"+tockenLogin);
				System.out.println("Request : "+AppClass.WEB_URL+"login");
				StringBuffer sb = AppClass.appcontext
					.getData(mContext, AppClass.WEB_URL+"login","",tockenLogin,"");


				if (sb == null) {
					return result;
				}
				System.out.println("Response : " + sb.toString());
				
				if(sb.toString().equalsIgnoreCase("Unauthorized"))
				{
					result="Unauthorized User";	
				}else
				{
				
				JSONObject jsonObj = new JSONObject(sb.toString());
				AppClass.edUser.putString("email",jsonObj.getString("email") );
				AppClass.edUser.putString("id",jsonObj.getString("id") );
				AppClass.edUser.putString("createdAt",jsonObj.getString("createdAt") );
				AppClass.edUser.putString("role",jsonObj.getString("role") );
				AppClass.edUser.putString("organizationId",jsonObj.getString("organizationId") );
				AppClass.edUser.putString("name", jsonObj.getString("name"));
				AppClass.edUser.commit();
				
				AppClass.edLogin.putString("token",jsonObj.getString("token"));
				
				
				result="success";
				}
			} catch (Exception e) {
				result = "Network connection error";
				Log.e("Exception", e.getMessage(), e);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (Login.this.dlgProgress != null
					&& Login.this.dlgProgress.isShowing()) {
				Login.this.dlgProgress.dismiss();
			}
			if (result.equalsIgnoreCase("success")) {					


				
				Toast.makeText(mContext,"Login Success", Toast.LENGTH_SHORT).show();
				AppClass.edLogin.putBoolean("isLogin", true).commit();
				startActivity(new Intent(Login.this,ProjectsActivity.class));
				finish();

			}else
			{
				Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
			}
		}	


	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if(!(AppClass.spLogin.getAll().isEmpty()))
		{
			startActivity(new Intent(Login.this,ProjectsActivity.class));
			finish();	
		}
	}

}
