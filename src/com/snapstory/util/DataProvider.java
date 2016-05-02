package com.snapstory.util;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.snapstory.R;

public class DataProvider extends ContentProvider {
	
	@SuppressLint("SdCardPath") 
	private static String DB_PATH = "/data/data/com.snapstory/databases/";

	private static final UriMatcher uriMatcher;
	public static final Uri PROJECT_URI = Uri.parse("content://com.snapstory.util.DataProvider/projecturi");
	public static final Uri STORY_URI = Uri.parse("content://com.snapstory.util.DataProvider/storyuri");
	public static final Uri CHAPTER_URI = Uri.parse("content://com.snapstory.util.DataProvider/chapteruri");
	public static final Uri QUESTION_URI = Uri.parse("content://com.snapstory.util.DataProvider/questionuri");
	public static final Uri ANSWER_URI = Uri.parse("content://com.snapstory.util.DataProvider/answeruri");
	
	public static final String DATABASE_NAME = "snapstory_db";
	private static final int VERSION_NAME = 1;
	public static SQLiteDatabase myDataBase;

	public static final String TBL_PROJECT = "project";
	public static final String TBL_STORY = "story";
	public static final String TBL_CHAPTER = "chapter";
	public static final String TBL_QUESTION = "question";
	public static final String TBL_ANSWER = "answer";
	
	
	
	//Common
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String IMAGE_URL = "image_url";
	public static final String PROJECT_ID = "project_id";
	
	//project table	
	
	public static final String DESCRIPTION = "description";
	public static final String ORGANIZATION_ID = "organization_id";
	public static final String CITY = "city";
	public static final String LOCATION = "location";
	
	
	
	
	//story table
	public static final String STORY_UUID = "story_uuid";
	public static final String GENDER = "gender";	
	public static final String IMAGE = "image";
	public static final String BIRTH_DATE = "birth_date";
	public static final String SIGNATURE = "signature";
	public static final String REPORT_TYPE = "report_type";
	public static final String REPORT_TEMPLATE_ID = "report_template_id";
	
	//chapter table
	public static final String STORY_ID = "story_id";	
	public static final String CHAPTER_UUID = "chapter_uuid";
	public static final String SYNCED = "synced";
	public static final String LATITUDE = "latitude";	
	public static final String LONGITUDE = "longitude";
	public static final String RECORDED_AT = "recordedAt";
	
	//question table
	public static final String QUESTION = "question";
	public static final String QUESTION_TYPE = "question_type";
	public static final String CHOICES = "choices";
	public static final String TYPE = "type";
	
	
	//answer table
	public static final String CHAPTER_ID = "chapter_id";
	public static final String ANSWER = "answer";
	public static final String ANSWER_UUID = "answer_uuid";
	public static final String QUESTION_ID = "question_id";
	
	
	
	

	private static final int GETALLPROJECT = 1;
	private static final int GETPROJECTBYID = 2;
	private static final int GETALLSTORY = 3;
	private static final int GETSTORYBYID = 4;
	private static final int GETALLCHAPTER = 5;
	private static final int GETCHAPTERBYID = 6;
	private static final int GETALLQUESTION = 7;
	private static final int GETQUESTIONBYID = 8;
	private static final int GETALLANSWER = 9;
	private static final int GETANSWERBYID = 10;
	
	

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		
		uriMatcher.addURI("com.snapstory.util.DataProvider", "projecturi",
				GETALLPROJECT);
		uriMatcher.addURI("com.snapstory.util.DataProvider", "projecturi/#",
				GETPROJECTBYID);	
		
		uriMatcher.addURI("com.snapstory.util.DataProvider", "storyuri",
				GETALLSTORY);
		uriMatcher.addURI("com.snapstory.util.DataProvider", "storyuri/#",
				GETSTORYBYID);
		
		uriMatcher.addURI("com.snapstory.util.DataProvider", "chapteruri",
				GETALLCHAPTER);
		uriMatcher.addURI("com.snapstory.util.DataProvider", "chapteruri/#",
				GETCHAPTERBYID);
		
		uriMatcher.addURI("com.snapstory.util.DataProvider", "questionuri",
				GETALLQUESTION);
		uriMatcher.addURI("com.snapstory.util.DataProvider", "questionuri/#",
				GETQUESTIONBYID);
		
		uriMatcher.addURI("com.snapstory.util.DataProvider", "answeruri",
				GETALLANSWER);
		uriMatcher.addURI("com.snapstory.util.DataProvider", "answeruri/#",
				GETANSWERBYID);
		
		
	}

	@Override
	public boolean onCreate() {
		MyDataBaseHelper myDataHelper = new MyDataBaseHelper(getContext(),
				DATABASE_NAME, null, VERSION_NAME);
		myDataHelper.createDataBase();
		myDataBase = myDataHelper.getWritableDatabase();
		return myDataBase != null ? true : false;
	}

	@Override
	public String getType(Uri paramUri) {
		switch (uriMatcher.match(paramUri)) {
		
		case GETALLPROJECT:		
		case GETALLSTORY:		
		case GETALLCHAPTER:		
		case GETALLQUESTION:		
		case GETALLANSWER:
		
			return "vnd.android.cursor.dir/vnd.test.dataturi";
		case GETPROJECTBYID:
		case GETSTORYBYID:
		case GETCHAPTERBYID:
		case GETQUESTIONBYID:
		case GETANSWERBYID:
			return "vnd.android.cursor.item/vnd.test.datauri";		
		
		default:
			throw new IllegalArgumentException("Invalid URI : " + paramUri);

		}
	}

	@Override
	public Uri insert(Uri paramUri, ContentValues paramContentValues) {
		long rowId = -1;
		switch (uriMatcher.match(paramUri)) {
		
		case GETALLPROJECT:
			rowId = myDataBase.insert(TBL_PROJECT, "projecturi",
					paramContentValues);
			if (rowId > 0) {
				Uri uri = ContentUris.withAppendedId(PROJECT_URI, rowId);
				getContext().getContentResolver().notifyChange(uri, null);
				return uri;
			}
			
		case GETALLSTORY:
			rowId = myDataBase.insert(TBL_STORY, "storyuri",
					paramContentValues);
			if (rowId > 0) {
				Uri uri = ContentUris.withAppendedId(STORY_URI, rowId);
				getContext().getContentResolver().notifyChange(uri, null);
				return uri;
			}
			
		case GETALLCHAPTER:
			rowId = myDataBase.insert(TBL_CHAPTER, "chapteruri",
					paramContentValues);
			if (rowId > 0) {
				Uri uri = ContentUris.withAppendedId(CHAPTER_URI, rowId);
				getContext().getContentResolver().notifyChange(uri, null);
				return uri;
			}
			
		case GETALLQUESTION:
			rowId = myDataBase.insert(TBL_QUESTION, "questionuri",
					paramContentValues);
			if (rowId > 0) {
				Uri uri = ContentUris.withAppendedId(QUESTION_URI, rowId);
				getContext().getContentResolver().notifyChange(uri, null);
				return uri;
			}
			
		case GETALLANSWER:
			rowId = myDataBase.insert(TBL_ANSWER, "answeruri",
					paramContentValues);
			if (rowId > 0) {
				Uri uri = ContentUris.withAppendedId(ANSWER_URI, rowId);
				getContext().getContentResolver().notifyChange(uri, null);
				return uri;
			}			
		
		
		
		}
		throw new IllegalArgumentException("Invalid URI : " + paramUri);
	}

	@Override
	public Cursor query(Uri paramUri, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		switch (uriMatcher.match(paramUri)) {
		
		case GETALLPROJECT:
			queryBuilder.setTables(TBL_PROJECT);
			break;
		case GETPROJECTBYID:
			queryBuilder.setTables(TBL_PROJECT);
			queryBuilder.appendWhere(ID + "="
					+ paramUri.getPathSegments().get(1));
			break;
			
		case GETALLSTORY:
			queryBuilder.setTables(TBL_STORY);
			break;
		case GETSTORYBYID:
			queryBuilder.setTables(TBL_STORY);
			queryBuilder.appendWhere(ID + "="
					+ paramUri.getPathSegments().get(1));
			break;
			
			
		case GETALLCHAPTER:
			queryBuilder.setTables(TBL_CHAPTER);
			break;
		case GETCHAPTERBYID:
			queryBuilder.setTables(TBL_CHAPTER);
			queryBuilder.appendWhere(ID + "="
					+ paramUri.getPathSegments().get(1));
			break;
			
		case GETALLQUESTION:
			queryBuilder.setTables(TBL_QUESTION);
			break;
		case GETQUESTIONBYID:
			queryBuilder.setTables(TBL_QUESTION);
			queryBuilder.appendWhere(ID + "="
					+ paramUri.getPathSegments().get(1));
			break;
			
		case GETALLANSWER:
			queryBuilder.setTables(TBL_ANSWER);
			break;
		case GETANSWERBYID:
			queryBuilder.setTables(TBL_ANSWER);
			queryBuilder.appendWhere(ID + "="
					+ paramUri.getPathSegments().get(1));
			break;			
		
		
		
	
		default:
			break;
		}
		Cursor c = queryBuilder.query(myDataBase, projection, selection,
				selectionArgs, null, null, sort);
		c.setNotificationUri(getContext().getContentResolver(), paramUri);
		return c;
	}

	@Override
	public int update(Uri paramUri, ContentValues paramContentValues,
			String where, String[] whereArgs) {
		int count = 0;
		String segment;
		switch (uriMatcher.match(paramUri)) {
		
		case GETALLPROJECT:
			count = myDataBase.update(TBL_PROJECT, paramContentValues, where,
					whereArgs);
			break;
		case GETPROJECTBYID:
			segment = paramUri.getPathSegments().get(1);
			count = myDataBase.update(TBL_PROJECT, paramContentValues,
					ID
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ")" : ""), whereArgs);
			break;
			
		case GETALLSTORY:
			count = myDataBase.update(TBL_STORY, paramContentValues, where,
					whereArgs);
			break;
		case GETSTORYBYID:
			segment = paramUri.getPathSegments().get(1);
			count = myDataBase.update(TBL_STORY, paramContentValues,
					ID
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ")" : ""), whereArgs);
			break;
			
			
		case GETALLCHAPTER:
			count = myDataBase.update(TBL_CHAPTER, paramContentValues, where,
					whereArgs);
			break;
		case GETCHAPTERBYID:
			segment = paramUri.getPathSegments().get(1);
			count = myDataBase.update(TBL_CHAPTER, paramContentValues,
					ID
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ")" : ""), whereArgs);
			break;
			
			
		case GETALLQUESTION:
			count = myDataBase.update(TBL_QUESTION, paramContentValues, where,
					whereArgs);
			break;
		case GETQUESTIONBYID:
			segment = paramUri.getPathSegments().get(1);
			count = myDataBase.update(TBL_QUESTION, paramContentValues,
					ID
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ")" : ""), whereArgs);
			break;
			
			
		case GETALLANSWER:
			count = myDataBase.update(TBL_ANSWER, paramContentValues, where,
					whereArgs);
			break;
		case GETANSWERBYID:
			segment = paramUri.getPathSegments().get(1);
			count = myDataBase.update(TBL_ANSWER, paramContentValues,
					ID
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ")" : ""), whereArgs);
			break;
			
			
		
		
		default:
			throw new IllegalArgumentException("Invalid URI:" + paramUri);

		}
		getContext().getContentResolver().notifyChange(paramUri, null);
		return count;
	}

	@Override
	public int delete(Uri paramUri, String where, String[] whereArgs) {
		int count = 0;
		String segment;
		switch (uriMatcher.match(paramUri)) {
		
		case GETALLPROJECT:
			count = myDataBase.delete(TBL_PROJECT, where, whereArgs);
			break;
		case GETPROJECTBYID:
			segment = paramUri.getPathSegments().get(1);
			count = myDataBase.delete(TBL_PROJECT,
					ID
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ")" : ""), whereArgs);
			break;
			
			
		case GETALLSTORY:
			count = myDataBase.delete(TBL_STORY, where, whereArgs);
			break;
		case GETSTORYBYID:
			segment = paramUri.getPathSegments().get(1);
			count = myDataBase.delete(TBL_STORY,
					ID
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ")" : ""), whereArgs);
			break;
			
			
		case GETALLCHAPTER:
			count = myDataBase.delete(TBL_CHAPTER, where, whereArgs);
			break;
		case GETCHAPTERBYID:
			segment = paramUri.getPathSegments().get(1);
			count = myDataBase.delete(TBL_CHAPTER,
					ID
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ")" : ""), whereArgs);
			break;
			
			
		case GETALLQUESTION:
			count = myDataBase.delete(TBL_QUESTION, where, whereArgs);
			break;
		case GETQUESTIONBYID:
			segment = paramUri.getPathSegments().get(1);
			count = myDataBase.delete(TBL_QUESTION,
					ID
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ")" : ""), whereArgs);
			break;
			
			
		case GETALLANSWER:
			count = myDataBase.delete(TBL_ANSWER, where, whereArgs);
			break;
		case GETANSWERBYID:
			segment = paramUri.getPathSegments().get(1);
			count = myDataBase.delete(TBL_ANSWER,
					ID
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ")" : ""), whereArgs);
			break;
			
			
		
		
		
			
			default:
			throw new IllegalArgumentException("Invalid URI : " + paramUri);
		}
		getContext().getContentResolver().notifyChange(paramUri, null);
		return count;
	}

	private class MyDataBaseHelper extends SQLiteOpenHelper {
		private Context mContext;

		public MyDataBaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			mContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase paramSQLiteDatabase,
				int oldVersion, int newVersion) {
			try {
				copyDataBaseFiles();
				// getCountryData(paramSQLiteDatabase);
			} catch (IOException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		

		public void createDataBase() {

			boolean dbExist = checkDataBase();

			if (dbExist) {
				// do nothing - database already exist
			} else {

				// By calling this method and empty database will be created
				// into
				// the default system path
				// of your application so we are gonna be able to overwrite that
				// database with our database.
				

				try {
					this.getReadableDatabase();
					copyDataBaseFiles();
					// getCountryData(dataBase);
				} catch (IOException e) {

					Log.e("Exceptiopn", e.getMessage(), e);

				}
			}

		}

		/**
		 * Check if the database already exist to avoid re-copying the file each
		 * time you open the application.
		 * 
		 * @return true if it exists, false if it doesn't
		 */
		private boolean checkDataBase() {

			SQLiteDatabase checkDB = null;

			try {
				String myPath = DB_PATH + DATABASE_NAME;
				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READWRITE);

			} catch (SQLiteException e) {

				// database does't exist yet.

			}

			if (checkDB != null) {

				checkDB.close();

			}

			return checkDB != null ? true : false;
		}

		/**
		 * Copies your database from your local assets-folder to the just
		 * created empty database in the system folder, from where it can be
		 * accessed and handled. This is done by transfering bytestream.
		 * */
		

		private void copyDataBaseFiles() throws IOException {
			InputStream databaseInput = null;
			String outFileName = DB_PATH + DATABASE_NAME;
			OutputStream databaseOutput = new FileOutputStream(outFileName);

			byte[] buffer = new byte[1024];
			int length;
			int db[] = { R.raw.snapstory_db };
			for (int i = 0; i < db.length; i++) {
				databaseInput = mContext.getResources().openRawResource(db[i]);
				while ((length = databaseInput.read(buffer)) > 0) {
					databaseOutput.write(buffer, 0, length);
					databaseOutput.flush();
				}
				databaseInput.close();
			}
			databaseOutput.flush();
			databaseOutput.close();
		}
	}
}
