package anh.android.seniorapp.control;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	/* Database information */
	private static final int DATABASE_VERSION = 30;
	private static final String DATABASE_NAME = "karaoke";

	/* Information users table */
	private static final String TABLE_USERS = "users";

	/* Login table columns names */
	private static final String KEY_ID = "id_user";
	private static final String KEY_UID = "unique_id";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USERS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_UID + " TEXT, "
				+ KEY_USERNAME + " TEXT UNIQUE, " + KEY_PASSWORD + " TEXT" + ");";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		onCreate(db);
	}

	public void addUser(String uid, String username, String password) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_UID, uid);
		values.put(KEY_USERNAME, username);
		values.put(KEY_PASSWORD, password);
		long id = db.insert(TABLE_USERS, null, values);
		db.close();
		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	public void updateUser(String username, String password, String newPass){
		SQLiteDatabase db = this.getWritableDatabase();
		String selection = KEY_USERNAME + " LIKE ? AND " + KEY_PASSWORD + " LIKE ?";
		String args[] = {username, password};
		ContentValues values = new ContentValues();
		values.put(KEY_PASSWORD, newPass);
		db.update(TABLE_USERS, values, selection, args);
		Log.d(TAG, "Update password db successful");
	}
	
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT * FROM " + TABLE_USERS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("uid", cursor.getString(1));
			user.put("username", cursor.getString(2));
			user.put("password", cursor.getString(3));
		}
		cursor.close();
		db.close();
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
		return user;
	}

	public int getRowCount() {
		String countQuery = "SELECT * FROM " + TABLE_USERS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();
		return rowCount;
	}

	public void deleteUsers(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, null, null);
		db.close();
		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
