package il.ac.huji.example.broadcastreceiversexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.Toast;

public class Recevier extends BroadcastReceiver{

	// name of custom action
	static final String ACTION_CALL_FILTERED = "il.ac.huji.action.call_filtered";

	// A prohibited phone number.
	private String prohib_num = "0540123456";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		// get resources
		Resources res = context.getResources();
		
		// get action name
		String action = intent.getAction();
		
		//  CASE 1 : Android system boot completed
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			
			// notify user that Phone filter receiver has started
			Toast.makeText(context, res.getString(R.string.boot_comp_str), Toast.LENGTH_LONG).show();
			return;
		}
		
		// CASE 2: Device is issuing an outgoing call
		if (action.equalsIgnoreCase(Intent.ACTION_NEW_OUTGOING_CALL)) {

			// outgoing call's number is prohibited - 
			// notify user and broadcast ACTION_CALL_FILTERED
			if(intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER).equals(prohib_num)) {
				
				// notify user that a call is being blocked
				Toast.makeText(context, res.getString(R.string.blocking_call_str) + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER), Toast.LENGTH_SHORT).show();
				
				// create new intent with ACTION_CALL_FILTERED action
				Intent reply_intent = new Intent(Recevier.ACTION_CALL_FILTERED);
				
				// set appropriate extra
				reply_intent.putExtra(Intent.EXTRA_PHONE_NUMBER, intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
				
				// broadcast intent
				context.sendBroadcast(reply_intent);
				
				// set the actual number for the outgoing call - setting null will abort the call.
				setResultData(null);
			}
			return;
		}
		
		// CASE 3: outgoing call was blocked
		if (action.equals(ACTION_CALL_FILTERED)) {
			
			// notify user.
			Toast.makeText(context, res.getString(R.string.call_blocked_str) + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER), Toast.LENGTH_SHORT).show();
			return;
		}
	}	
}
