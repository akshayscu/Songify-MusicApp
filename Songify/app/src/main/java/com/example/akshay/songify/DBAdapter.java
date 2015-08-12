package com.example.akshay.songify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBAdapter {

	private static final String TAG = "DBAdapter"; //used for logging database version changes

	// Field Names:
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_ARTIST = "artist";
	public static final String KEY_ALBUM = "album";

	public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_TITLE, KEY_ARTIST, KEY_ALBUM};

	// DataBase info:
	public static final String DATABASE_NAME = "dbSongify";
	public static final String DATABASE_TABLE = "songDetail";
	public static final int DATABASE_VERSION = 2;

	//SQL statement to create database
	private static final String DATABASE_CREATE_SQL =
			"CREATE TABLE " + DATABASE_TABLE
					+ " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_TITLE + " TEXT NOT NULL, " + KEY_ARTIST + " TEXT NOT NULL, "
					+ KEY_ALBUM + " TEXT NOT NULL"
					+ ");";

	private final Context context;
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;


	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}

	// Open the database connection.
	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}

	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}


	public int getCount(){
		 db = myDBHelper.getWritableDatabase();
		String count = "SELECT count(*) FROM "+DATABASE_TABLE;
		Cursor mcursor = db.rawQuery(count, null);
		mcursor.moveToFirst();
		int icount = mcursor.getInt(0);
		return icount;
	}
	// Add a new set of values to be inserted into the database.
	public long insertRow(String title, String artist, String album) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_ARTIST, artist);
		initialValues.put(KEY_ALBUM, album);

		// Insert the data into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(String rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Log.i("check", where);
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) != 0;
	}

	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;
		Cursor c = null;
		c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		ArrayList<String> titleList = null;
		ArrayList<String> artistList = null;
		ArrayList<String> albumList = null;

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");

			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

			// Recreate new database:
			onCreate(_db);
		}

	}
}



