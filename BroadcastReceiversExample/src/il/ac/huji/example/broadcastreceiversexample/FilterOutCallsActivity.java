package il.ac.huji.example.broadcastreceiversexample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class FilterOutCallsActivity extends Activity{

	// A dynamic broadcast receiver.
	private BroadcastReceiver receiver;
	
	// indicates if receiver is registered or not
	private boolean enabled = false;
	
	// SMS_RECEIVED action name
	static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	
	// hold the data_name associated with the SMS message data in the extra's Bundle
	// (stands for "protocol data unit" which describes a packet of SMS message for example)
	static final String SMS_DATA = "pdus";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Resources res = getResources();
		final Button toggleBtn = (Button) findViewById(R.id.toggleBtn);

		// create the toggle button functionality
		toggleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// receiver is registered
				if(enabled){
					
					// toggle button's text
					toggleBtn.setText(res.getString(R.string.enable));
					
					//unregister receiver
					disableReceiver();
				}
				// receiver isn't registered
				else{
					
					//toggle button's text
					toggleBtn.setText(res.getString(R.string.disable));
					
					//register receiver
					enableReciever();
				}
			}
		});

		// implement the SMS BroadcastReceiver
		receiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {	

				String action = intent.getAction();

				// SMS received
				if (action.equals(ACTION_SMS_RECEIVED)){

					// retrieve data
					Bundle extras = intent.getExtras(); 
					String messages = "";

					if ( extras != null ) {

						// Get received SMS array
						Object[] smsExtra = (Object[]) extras.get( SMS_DATA );

						for ( int i = 0; i < smsExtra.length; ++i ) {

							// Create SMSMesage object from raw pdu
							SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
							
							// get message body & origin
							String body = sms.getMessageBody().toString();
							String address = sms.getOriginatingAddress();
							
							// append to the messages string
							messages += "SMS from " + address + " :\n";                    
							messages += body + "\n";
						}
						// Display SMS message
						Toast.makeText( context, messages, Toast.LENGTH_SHORT ).show();
					}
				}	
			}

		};
	}


	public void enableReciever(){
		
		Resources res = getResources();
		
		Toast.makeText(this, res.getString(R.string.sms_receiver_enabled), Toast.LENGTH_SHORT).show();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_SMS_RECEIVED);
		
		registerReceiver(receiver , filter);
		
		enabled = true;

	}

	public void disableReceiver(){
		
		Resources res = getResources();
		Toast.makeText(this, res.getString(R.string.sms_receiver_disabled), Toast.LENGTH_SHORT).show();
		
		unregisterReceiver(receiver);
		
		enabled = false;
	}	

	@Override
	protected void onResume() {
		super.onResume();
		if(!enabled) enableReciever();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(enabled) disableReceiver();
	}

}
