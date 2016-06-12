package com.mycompany.myapp4;

import java.io.FileNotFoundException;
import java.io.IOException;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.*;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.widget.SearchView;

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
import java.security.*;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity
{

	TextView txt,ph,hd;
	Button btn;
	EditText mEdit,me;
	String a,b,rem;
	int ii,take=5;
	Intent i;
	String phoneNumber;
	DBAdapter db=new DBAdapter(this);

	SearchView searchView1;
	

    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		TelephonyManager telephonyManager=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		hd=(TextView) findViewById(R.id.main2);
		me=(EditText)findViewById(R.id.email);
		me.setText("+91");
		mEdit=(EditText)findViewById(R.id.name);
		ph=(TextView)findViewById(R.id.ph);
		btn=(Button)findViewById(R.id.btn);
		searchView1 = (SearchView) findViewById(R.id.searchView);
		txt=(TextView) findViewById(R.id.txt1);
		setupSearchView();
		PhoneStateListener callStateListener=new PhoneStateListener()
		{
			public void onCallStateChanged(int state,String incomingNumber)
			{
				db.open();
				String number=incomingNumber;
				Cursor c=db.getAllContacts();
				
				if(state==TelephonyManager.CALL_STATE_RINGING)
				{
					if(c.moveToFirst())
					{
						do
						{
							if(c.getString(2).equals(number))
							{
								Toast.makeText(getApplicationContext(),"You Have A reminder Please check..\nReminder:"+c.getString(1),Toast.LENGTH_LONG).show();
								NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
								Notify("Reminder On Call",c.getString(1));
								rem=c.getString(1);
								ii=Integer.parseInt(c.getString(0));
							}
							else
							{
								;
							}
						}
						while(c.moveToNext());
					}
					
					
				}

				if(state==TelephonyManager.CALL_STATE_OFFHOOK)
				{
					
				}
				if(state==TelephonyManager.CALL_STATE_IDLE)
				{
					if(rem!=null)
					{
						Toast.makeText(getApplicationContext(),"You Have A reminder from last caller please check it will Be Deleted autometically...\nReminder:"+rem,Toast.LENGTH_LONG).show();
						NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			            Notify("Reminder On Call",rem);
						db.deleteContact(ii);
						rem=null;
					}
				}
				db.close();
			}
		};
		telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	
	
	
	//menu work start

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		super.onCreateOptionsMenu(menu);
		createMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		return MenuChoice(item);
	}

	public void createMenu(Menu menu)
	{
		MenuItem mnu1=menu.add(0,0,0,"Set Reminder");
		{
			mnu1.setIcon(R.drawable.rem);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		MenuItem mnu2=menu.add(0,1,1,"Upcoming Reminder");
		{
			mnu2.setIcon(R.drawable.upcm);
			mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		MenuItem mnu3=menu.add(0,2,2,"How to Use");
		{
			mnu3.setIcon(R.drawable.how);
			mnu3.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		MenuItem mnu4=menu.add(0,3,3,"About Us");
		{
			mnu4.setIcon(R.drawable.abc);
			mnu4.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

	}

	private boolean MenuChoice(MenuItem item)
	{
		switch(item.getItemId())
		{
			case 0:
				Toast.makeText(this, "Set Reminder", Toast.LENGTH_LONG).show();
				hd.setText("Set Reminder",TextView.BufferType.EDITABLE);
				me.setVisibility(View.VISIBLE);
				mEdit.setVisibility(View.VISIBLE);
				btn.setVisibility(View.VISIBLE);
				ph.setVisibility(View.VISIBLE);
				searchView1.setVisibility(View.VISIBLE);
				me.setText("+91");
				txt.setText("Reminder",TextView.BufferType.EDITABLE);
				ph.setText("Phone Number",TextView.BufferType.EDITABLE);
				break;

			case 1:
				Toast.makeText(this, "Upcomming Reminder", Toast.LENGTH_LONG).show();
				me.setVisibility(View.GONE);
				mEdit.setVisibility(View.GONE);
				btn.setVisibility(View.GONE);
				ph.setVisibility(View.GONE);
				searchView1.setVisibility(View.GONE);
				hd.setText("Upcomming Reminder",TextView.BufferType.EDITABLE);
				txt.setText("",TextView.BufferType.EDITABLE);
				getallcont();
				break;

				
			case 2:
			    Toast.makeText(this,"How To Use",Toast.LENGTH_LONG).show();
				AlertDialog ad1=new AlertDialog.Builder(this).create();
				ad1.setCancelable(false);
				ad1.setTitle("How to Use");
			    ad1.setMessage("1)At home page,first search for the contact or Manually Enter the phone Number\n\n2)Set Reminder For it and Submit.\n\n3)Now sit back and Relax whenever you Get a Call from That number a Notification Will Come To Remind you\n\n4)In case of Outgoing Call You only get a message at bottom(As your phone is On your hand :p )\n\n5)Reminder will be deleted after Showing\n\n6)Get your full list of Reminder from 'Upcoming Reminder' Option...");
				ad1.setButton("Ok",new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog,int which)
						{
							dialog.dismiss();
						}
					});
				ad1.show();
				break;
				
			case 3:
				Toast.makeText(this,"About Us\n(Vertion 1.0.0)",Toast.LENGTH_LONG).show();
				
				AlertDialog ad=new AlertDialog.Builder(this).create();
				ad.setCancelable(false);
				ad.setTitle("About us\n(Vertion 1.0.0)");
			    ad.setMessage("It was 1st sept,2015.the same Routined morning with a cup of tea in our hand.I was Discussing about our Final Year Project with sourav.\nIt was the third cup(I guess) when suddenly an idea popped up through our Conversation.\nAnd the output is now in your hand.\nA warm 'HI' to our all user...Enjoy the app\n\nThanking you\nSubhanjan Basu\nEmail:Subhanjan.basu12@Gmail.com\n\n\nSourav Paul\nEmail:Sourav412p@gmail.com");
				ad.setButton("Ok",new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog,int which)
						{
							dialog.dismiss();
						}
					});
					ad.show();
				break;
		}
		return false;
	}
	
	
	
	
	//get all values
	
	public void DisplayContact(Cursor c)
	{
		a=txt.getText().toString();
		txt.setText(a+"Reminder:"+c.getString(1)+"\nPhone Number="+c.getString(2)+"\n\n",TextView.BufferType.EDITABLE);
		   
	}
	
	public void getallcont()
	{
		db.open();

		Cursor c=db.getAllContacts();
		if(c.moveToFirst())
		{
			do
			{
				DisplayContact(c);

			}
			while(c.moveToNext());
		}
		db.close();
		
	}
	
	
	
	

	


	//DATABASE ENTRY
	
	
	public void onClick(View view)
	{
		
	
		a=mEdit.getText().toString();
		b=me.getText().toString();


	
		db.open();
		
		if(a.equals("") || b.equals("") || b.equals("+91"))
		{
			Toast.makeText(this,"Reminder/Phone Number Can not be Empty...",Toast.LENGTH_LONG).show();
		}
		else
		{
			long id=db.insertContact(a,b);
			Toast.makeText(this,"Reminder Updated\n"+"Reminder:" + a +"\n"+"Phone Number:" + b,Toast.LENGTH_LONG).show();
		}
		
		mEdit.setText("",TextView.BufferType.EDITABLE);
		me.setText("+91",TextView.BufferType.EDITABLE);
		
		
		db.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	//NOTIFICATION WORKS FROM HERE
	
	
	
	
	
	
	
	
	@SuppressWarnings("deprecation")
	public void Notify(String notificationTitle,String notificationMessage)
	{
		NotificationManager notificationManeger=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		@SuppressWarnings("deprecation")
			Notification notification=new Notification(R.drawable.icon,"New Message",System.currentTimeMillis());
		Intent notificationIntent=new Intent(this,MainActivity.class);
		PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
		notification.setLatestEventInfo(MainActivity.this,notificationTitle,notificationMessage,pendingIntent);
		notificationManeger.notify(9999,notification);
	}
	
	
	
	
	//THIS PART IS FOR THE SEARCHVIEW....

	
	private void setupSearchView() {
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		final SearchView searchView = (SearchView) findViewById(R.id.searchView);
		SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
		searchView.setSearchableInfo(searchableInfo);
	}
	@Override
	protected void onNewIntent(Intent in) {
		if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(in.getAction())) {
			String displayName = getDisplayNameForContact(in);
			me.setText(displayName,TextView.BufferType.EDITABLE);;
			}
	}
	private String getDisplayNameForContact(Intent in) {
		Cursor phoneCursor = getContentResolver().query(in.getData(), null, null, null, null);
		String phoneno="0";
		phoneCursor.moveToFirst();
		String id=phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.Contacts._ID));

		Cursor pcur=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",new String[] {id},null);
		while(pcur.moveToNext())
		{
			phoneno=pcur.getString(pcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		}
		int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		String name = phoneCursor.getString(idDisplayName);
		phoneCursor.close();
		return phoneno;
	}
	
	
	
	//BROADCAST

	public static class OutgoingCallBroadcastReceiver extends BroadcastReceiver
	{

		String phoneNumber;
		@Override
		public void onReceive(Context context, Intent intent)
		{
			phoneNumber=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			DBAdapter db1=new DBAdapter(context);
			
			db1.open();
			Cursor c=db1.getAllContacts();
			if(c.moveToFirst())
			{
				do
				{
					if(c.getString(2).equals(phoneNumber))
					{
						Toast.makeText(context,"You Have A reminder Please check..\nReminder:"+c.getString(1),Toast.LENGTH_LONG).show();
						db1.deleteContact(Integer.parseInt(c.getString(0)));
					}
					else
					{
						;
					}
				}
				while(c.moveToNext());
			}
			db1.close();
		}
	}



	
}		

