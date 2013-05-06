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

	private BroadcastReceiver receiver;
	private boolean enabled = false;
	static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Resources res = getResources();
		final Button toggleBtn = (Button) findViewById(R.id.toggleBtn);

		toggleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(enabled){
					toggleBtn.setText(res.getString(R.string.enable));
					disableReceiver();
				}
				else{
					toggleBtn.setText(res.getString(R.string.disable));
					enableReciever();
				}
			}
		});


		receiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {	

				String action = intent.getAction();

				if (action.equals(ACTION_SMS_RECEIVED)){

					Bundle extras = intent.getExtras(); 
					String messages = "";

					if ( extras != null ) {

						// Get received SMS array
						Object[] smsExtra = (Object[]) extras.get( "pdus" );

						for ( int i = 0; i < smsExtra.length; ++i ) {

							SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);

							String body = sms.getMessageBody().toString();
							String address = sms.getOriginatingAddress();

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

		Toast.makeText(this, "SMS Receiver Enabled.", Toast.LENGTH_SHORT).show();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_SMS_RECEIVED);
		registerReceiver(receiver , filter);
		enabled = true;

	}

	public void disableReceiver(){
		Toast.makeText(this, "SMS Receiver Disabled.", Toast.LENGTH_SHORT).show();
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
