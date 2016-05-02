package com.snapstory;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import net.simonvt.menudrawer.MenuDrawer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.snapstory.menu.SlideMenu;
import com.snapstory.util.DataProvider;
import com.snapstory.util.Utility;

public class StoryActivity extends Activity {
	public final static String STORY_UUID_EXTRA = "story_uuid_extra";
	public final static String STORY_NAME_EXTRA = "story_name_extra";
	public final static String PROJECT_UUID_EXTRA = "project_uuid_extra";
	public static final String STORY_TYPE_EXTRA = "story_type_extra";
	// private ViewPager pager;
	private Button submitBtn;
	//private StoryInfoFragment storyInfoFragment;
	Context mContext;
	String uuid,story_name,story_type;
	private DatePickerDialog mDatePicker;
	private SimpleDateFormat dateFormatter;


	static final int REQUEST_IMAGE_CAPTURE = 117;
	static final int REQUEST_SIGNATURE = 1337;
	

	private ImageButton picture;
	private EditText name;
	private RadioGroup genderGroup;
	private String selectedGender = "";
	//private DatePicker birthDayPicker;
	private Button signatureButton;
	private ImageView signatureImageButton;
	private String encodedImage = "";
	private String encodedSignatureString = "";
	private byte[] encodedSignature = new byte[0];
	private MenuDrawer mDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_form);
		mDrawer=SlideMenu.setMenu(this, R.layout.activity_form);

		mContext=this;
		name=(EditText)findViewById(R.id.name);
		submitBtn = (Button) findViewById(R.id.submit);
		
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

		//birthDayPicker = (DatePicker) findViewById(R.id.birthday);

		//System.out.println("birth_create--->"+new Date().getTime());
		//birthDayPicker.setMaxDate(new Date().getTime());

		// storyInfoFragment = StoryInfoFragment.newInstance(getIntent().getStringExtra(STORY_UUID_EXTRA));

		// pager = (ViewPager) findViewById(R.id.pager);
		// pager.setAdapter(new FormViewPagerAdapter(getSupportFragmentManager()));

		picture = (ImageButton) findViewById(R.id.picture);
		picture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent();
			}
		});

		name = (EditText) findViewById(R.id.name);

		genderGroup = (RadioGroup) findViewById(R.id.gender_rg);
		genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.male_rb) {
					selectedGender = "male";
				} else {
					selectedGender = "female";
				}
			}
		});



		signatureButton = (Button) findViewById(R.id.signature_button);
		signatureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(StoryActivity.this, SignatureActivity.class), REQUEST_SIGNATURE);
			}
		});

		signatureImageButton = (ImageView) findViewById(R.id.signature_image_button);
		signatureImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(StoryActivity.this, SignatureActivity.class), REQUEST_SIGNATURE);
			}
		});
		
		((ImageView)findViewById(R.id.MenuBtn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawer.openMenu();
			}
		});
		
		


		// System.out.println("story_uuid--->"+getArguments().getString("story_uuid"));

		/*if(!Strings.isNullOrEmpty(getArguments().getString("story_uuid"))) {
            StoryLoader loader = new StoryLoader(UUID.fromString(getArguments().getString("story_uuid")));
            getActivity().getLoaderManager().initLoader(2, null, loader);
        }*/
		
		Bundle b=getIntent().getExtras();
		if(b!=null)
		{
			System.out.println("b.getString(STORY_NAME_EXTRA)--->"+b.getString(STORY_NAME_EXTRA));
			story_name=b.getString(STORY_NAME_EXTRA);
			
			if(story_name!=null){
				((View)findViewById(R.id.headlayoutText)).setVisibility(View.VISIBLE);
				((TextView)findViewById(R.id.headtextName)).setText(""+b.getString(STORY_NAME_EXTRA));
				((TextView)findViewById(R.id.headtextSchool)).setText(""+AppClass.spUser.getString("project_name", ""));
				//((TextView)findViewById(R.id.textName)).setText(""+b.getString(STORY_NAME_EXTRA));
				//((TextView)findViewById(R.id.textSchool)).setText(""+AppClass.spUser.getString("project_name", ""));
			}
			
			System.out.println("b.getString(STORY_TYPE_EXTRA)--->"+b.getString(STORY_TYPE_EXTRA));
			story_type=b.getString(STORY_TYPE_EXTRA);
			
			if(story_type!=null){
				
				if(story_type.equalsIgnoreCase("person")){					
					((View)findViewById(R.id.personLayout)).setVisibility(View.VISIBLE);					
				}else if(story_type.equalsIgnoreCase("thing")){
					((View)findViewById(R.id.personLayout)).setVisibility(View.GONE);
				}
					
				
			}
			
			
			uuid=b.getString(STORY_UUID_EXTRA);
			if(uuid!=null)
			{
				
			
				//loadData(uuid);
			}
			
		}
		
		
		
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
	
	public void myClickHandler(View v) {
		switch (v.getId()) {
		
			case R.id.txtbirthday:
				Calendar newCalendar = Calendar.getInstance();
				mDatePicker = new DatePickerDialog(this, new OnDateSetListener() {

					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

						
						
						
						

						Calendar newDate = Calendar.getInstance();
						newDate.set(year, monthOfYear, dayOfMonth);
						((TextView)findViewById(R.id.txtbirthday)).setText(dateFormatter.format(newDate.getTime()));
					
						
					}

				},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

				mDatePicker.setTitle("Select Date");
				mDatePicker.show();
				break;
		}
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(StoryActivity.this.getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			picture.setImageBitmap(imageBitmap);

			encodedImage = encodeImage(imageBitmap);

			System.out.println("encodedImage--->"+encodedImage);
		}

		if(requestCode == REQUEST_SIGNATURE && resultCode == Activity.RESULT_OK) {
			Bundle extras = data.getExtras();
			encodedSignature = extras.getByteArray(SignatureActivity.SIGNATURE_EXTRA);

			System.out.println("encodedSignature---->"+encodedSignature);

			Bitmap bmp = BitmapFactory.decodeByteArray(encodedSignature, 0, encodedSignature.length);
			signatureImageButton.setImageBitmap(getResizedBitmap(bmp,100));

			signatureButton.setVisibility(View.GONE);
			signatureImageButton.setVisibility(View.VISIBLE);


			encodedSignatureString=encodeImage(bmp);
			System.out.println("encodedSignatureString--->"+encodedSignatureString);





		}
	}
	
	public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


	public void submit(View v) {

		
		
		String validate=validateUserNamePass();
		
		System.out.println("validate-->"+validate);
		
		if(validate.trim().length()>0)
		{
			Utility.showMyDialog(mContext, validate);
		}else
		{
			if(uuid.equalsIgnoreCase(""))
			{
				UUID uuidRan = UUID.randomUUID();
				 uuid= uuidRan.toString();
				 
				 submit(uuid);
			}else
			{
				submitUpdate(uuid);
			}
			
			
			//if(isValidReport()) {
			System.out.println("ChapterActivity "+uuid);
			
			Intent intent = new Intent(this, ChapterActivity.class);
			intent.putExtra(ChapterActivity.STORY_ID_EXTRA,uuid);
			intent.putExtra(ChapterActivity.STORY_NAME_EXTRA,story_name);
			intent.putExtra(ChapterActivity.PROJECT_ID_EXTRA, getIntent().getLongExtra(PROJECT_UUID_EXTRA, -1));
			startActivity(intent);

		}
		
		
		
		/* } else {
                Toast.makeText(this, "Please enter a name and gender", Toast.LENGTH_SHORT).show();
            }*/

	}

	/* public boolean isValidReport() {
        return name != null && !Strings.isNullOrEmpty(name.getText().toString()) && !Strings.isNullOrEmpty(selectedGender);
    }*/

	public void submit(String storyUUID/*, long projectId*/) {
		/*Story story = new Story(UUID.fromString(storyUUID), name.getText().toString(), selectedGender, projectId);
        //have to subtract 1900 since Date constructor adds 1900
        int birthYear = birthDayPicker.getYear()-1900;

        System.out.println("birth_submit--->"+new Date(birthYear,birthDayPicker.getMonth(), birthDayPicker.getDayOfMonth()));
        story.setBirthDate(new Date(birthYear,birthDayPicker.getMonth(), birthDayPicker.getDayOfMonth()));
        story.setImage(encodedImage);
        story.setSignature(Base64.encodeToString(encodedSignature, Base64.DEFAULT));
        DBHelper.upsertStory(story);*/
		
		
		
		ContentValues values=new ContentValues();
		values.put(DataProvider.STORY_UUID,storyUUID);
		values.put(DataProvider.NAME, name.getText().toString().trim());
		values.put(DataProvider.GENDER, selectedGender);
		values.put(DataProvider.PROJECT_ID,AppClass.spUser.getString("project_id",""));
		values.put(DataProvider.IMAGE, encodedImage);
		values.put(DataProvider.BIRTH_DATE, ""+((EditText)findViewById(R.id.txtbirthday)).getText().toString());
		values.put(DataProvider.SIGNATURE,encodedSignatureString );
		
		getContentResolver().insert(DataProvider.STORY_URI, values);
		
		
		
	}
	
	public void submitUpdate(String storyUUID/*, long projectId*/) {
		/*Story story = new Story(UUID.fromString(storyUUID), name.getText().toString(), selectedGender, projectId);
        //have to subtract 1900 since Date constructor adds 1900
        int birthYear = birthDayPicker.getYear()-1900;

        System.out.println("birth_submit--->"+new Date(birthYear,birthDayPicker.getMonth(), birthDayPicker.getDayOfMonth()));
        story.setBirthDate(new Date(birthYear,birthDayPicker.getMonth(), birthDayPicker.getDayOfMonth()));
        story.setImage(encodedImage);
        story.setSignature(Base64.encodeToString(encodedSignature, Base64.DEFAULT));
        DBHelper.upsertStory(story);*/
		
		
		
		ContentValues values=new ContentValues();
		values.put(DataProvider.STORY_UUID,storyUUID);
		values.put(DataProvider.NAME, name.getText().toString().trim());
		values.put(DataProvider.GENDER, selectedGender);
		values.put(DataProvider.PROJECT_ID,AppClass.spUser.getString("project_id",""));
		values.put(DataProvider.IMAGE, encodedImage);
		values.put(DataProvider.BIRTH_DATE,""+((EditText)findViewById(R.id.txtbirthday)).getText().toString() );
		values.put(DataProvider.SIGNATURE,encodedSignatureString );
		
		getContentResolver().update(DataProvider.STORY_URI, values,
				DataProvider.STORY_UUID + "=?",
				new String[] { storyUUID + "" });
		
		
		
	}

	private String encodeImage(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}
	
	
	public static Bitmap decodeBase64(String input)
	{
	    byte[] decodedBytes = Base64.decode(input, 0);
	    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}

	private void reloadUI(String data) {
		/*data.moveToFirst();
        StoryTable storyTable = storyTable().getApi();
        name.setText(storyTable.name(data));

        if(storyTable.gender(data).equals("male")) {
            genderGroup.check(R.id.male_rb);
        } else {
            genderGroup.check(R.id.female_rb);
        }

        System.out.println("birth bf--->"+storyTable.birthDate(data));
        Date birthDate = storyTable.birthDate(data);

        System.out.println("birth bf--->"+birthDate);

        System.out.println("birth--->"+(birthDate.getYear() + 1900)+"-"+ birthDate.getMonth()+"-"+( birthDate.getDate()));
        birthDayPicker.updateDate(birthDate.getYear() + 1900, birthDate.getMonth(), birthDate.getDate());*/
	}



	/*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

	/*private class FormViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> formFragments = new ArrayList<>();

        public FormViewPagerAdapter(FragmentManager fm) {
            super(fm);

            formFragments.add(storyInfoFragment);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return formFragments.get(position);
        }

        @Override
        public int getCount() {
            return formFragments.size();
        }
    }*/

	/*public void submit(View v) {
        if(pager.getCurrentItem() == 0) {
            String uuid;
            if(getIntent().hasExtra(STORY_UUID_EXTRA)) {
                uuid = getIntent().getStringExtra(STORY_UUID_EXTRA);
            } else {
                uuid = UUID.randomUUID().toString();
            }

            if(storyInfoFragment.isValidReport()) {
                System.out.println("ChapterActivity "+uuid);
                storyInfoFragment.submit(uuid, getIntent().getLongExtra(PROJECT_UUID_EXTRA, -1));
                Intent intent = new Intent(this, ChapterActivity.class);
                intent.putExtra(ChapterActivity.STORY_ID_EXTRA,uuid);
                intent.putExtra(ChapterActivity.PROJECT_ID_EXTRA, getIntent().getLongExtra(PROJECT_UUID_EXTRA, -1));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please enter a name and gender", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
	
	
	
	public String validateUserNamePass()
	{

		String validate="";
		if(!(name.getText().toString().trim().length()>0))
		{
				validate="Please enter name";
			

		}
		
		if(story_type.equalsIgnoreCase("person")){
			if(selectedGender.equalsIgnoreCase(""))
			{
				validate=validate+" \nPlease select gender";
			}
		}

		return validate;

	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if((AppClass.spLogin.getAll().isEmpty()))
		{
			startActivity(new Intent(StoryActivity.this,Login.class));
			finish();	
		}

	}
	
	
	
	
	@SuppressWarnings("deprecation")
	public void loadData(String uuid)
    {
    	try
    	{
    	String projection[] = { DataProvider.ID,DataProvider.STORY_UUID,DataProvider.NAME,DataProvider.GENDER,DataProvider.PROJECT_ID,DataProvider.IMAGE,DataProvider.BIRTH_DATE,DataProvider.SIGNATURE};
    	
    	
        	Cursor cs = getContentResolver().query(DataProvider.STORY_URI,
					projection, DataProvider.STORY_UUID + "=?", new String[] { uuid + "" },
					null);
    			
    		
       
    	
        if(cs.getCount()>0)
        {
        	
        
        if(cs.moveToFirst()) {
    	        
    	            
    	        	int id=cs.getInt(cs.getColumnIndex(DataProvider.ID));
    	        	String STORY_UUID=cs.getString(cs.getColumnIndex(DataProvider.STORY_UUID));
    				String NAME=cs.getString(cs.getColumnIndex(DataProvider.NAME));
    				String GENDER=cs.getString(cs.getColumnIndex(DataProvider.GENDER));
    				String PROJECT_ID=cs.getString(cs.getColumnIndex(DataProvider.PROJECT_ID));
    				String BIRTH_DATE=cs.getString(cs.getColumnIndex(DataProvider.BIRTH_DATE));
    				encodedImage=cs.getString(cs.getColumnIndex(DataProvider.IMAGE));
    				encodedSignatureString=cs.getString(cs.getColumnIndex(DataProvider.SIGNATURE));
    				
    				
    				
    				System.out.println("BIRTH_DATE--->"+BIRTH_DATE);
    				
    				
    				name.setText(NAME);
    				
    				if(GENDER.equalsIgnoreCase("male"))
    				{
    					((RadioButton)genderGroup.findViewById(R.id.male_rb)).setChecked(true);
    					((RadioButton)genderGroup.findViewById(R.id.female_rb)).setChecked(false);
    				}else if(GENDER.equalsIgnoreCase("female"))
    				{
    					((RadioButton)genderGroup.findViewById(R.id.female_rb)).setChecked(true);
    					((RadioButton)genderGroup.findViewById(R.id.male_rb)).setChecked(false);
    				}
    				    				
    				
    				picture.setImageBitmap(decodeBase64(encodedImage));
    				signatureImageButton.setImageBitmap(decodeBase64(encodedSignatureString));
    				signatureButton.setVisibility(View.GONE);
    				signatureImageButton.setVisibility(View.VISIBLE);
    				
    				
    				
    				
    				
    				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
    	            //sdf.applyPattern("EEE, MMM dd HH:mm:ss yyyy z");
    	            Date dt = sdf.parse(BIRTH_DATE);
    	           
    	            System.out.println(dt);
    				
    	            ((EditText)findViewById(R.id.txtbirthday)).setText((1900+dt.getYear())+"-"+ dt.getMonth()+"-"+ dt.getDate());
    	            
    	            //birthDayPicker.updateDate((1900+dt.getYear()), dt.getMonth(), dt.getDate());
    	       
    	    }
        }
        
    		cs.close();
    	}catch (Exception e) {
			// TODO: handle exception
    		
    		System.out.println(e);
		}
           
    }
}
