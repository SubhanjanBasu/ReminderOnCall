package com.mycompany.myapp4;


import java.io.FileNotFoundException;
import java.io.IOException;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.*;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.database.Cursor;
import android.content.ContentResolver;
import android.provider.ContactsContract;
import android.widget.TextView;
import android.content.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.nfc.*;
import java.sql.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.database.Cursor;

public class DBAdapter 
{
	
	
	
	static final String KEY_ROWID="id";
	static final String KEY_NAME="name";
	static final String KEY_EMAIL="email";
	static final String Tag="DBAdapter";

	static final String DATABASE_NAME="MyDB";
	static final String DATABASE_TABLE="contacts";
	static final int DATABASE_VERSION=1;

	static final String DATABASE_CREATE="create table contacts(id integer primary key autoincrement," + "name text not null,email text not null);";

	final Context context;
	String phoneNumber;
	DatabaseHelper DBHelper;
	SQLiteDatabase db;

	public DBAdapter(Context ctx)
	{
		this.context=ctx;
		//Toast.makeText(ctx,"this is Databse",Toast.LENGTH_LONG).show();
		DBHelper=new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME,null,DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// TODO: Implement this method
			try
			{
				db.execSQL(DATABASE_CREATE);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			// TODO: Implement this method

			Log.w(Tag,"upgreading database from version" + oldVersion + "to" + newVersion + ",which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}
	}

	public DBAdapter open() throws SQLException
	{
		db=DBHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		DBHelper.close();
	}

	public long insertContact(String name,String email)
	{
		ContentValues initialValues=new ContentValues();
		initialValues.put(KEY_NAME,name);
		initialValues.put(KEY_EMAIL,email);

		return db.insert(DATABASE_TABLE,null,initialValues);
	}

	public boolean deleteContact(long RowId)
	{
		return db.delete(DATABASE_TABLE,KEY_ROWID + "=" + RowId,null)>0;
	}

	public Cursor getAllContacts()
	{
		return db.query(DATABASE_TABLE,new String[] {KEY_ROWID, KEY_NAME, KEY_EMAIL},null,null,null,null,null);

	}

	public Cursor getContact(long RowId) throws SQLException
	{
		Cursor mCursor=db.query(true,DATABASE_TABLE,new String[] {KEY_ROWID,KEY_NAME,KEY_EMAIL},KEY_ROWID + "=" + RowId,null,null,null,null,null);

		if(mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean updateContact(long RowId,String name,String email)
	{
		ContentValues args=new ContentValues();
		args.put(KEY_NAME,name);
		args.put(KEY_EMAIL,email);
		return db.update(DATABASE_TABLE,args,KEY_ROWID + "=" + RowId,null)>0;
	}


}
		
