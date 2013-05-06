package il.ac.huji.example.broadcastreceiversexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Recevier extends BroadcastReceiver{

	static final String ACTION_CALL_FILTERED = "il.ac.huji.action.call_filtered";

	private String prohib_num = "0540123456";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction();
		
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			Toast.makeText(context, "Receiver Started...", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (action.equalsIgnoreCase(Intent.ACTION_NEW_OUTGOING_CALL)) {
			
			Intent reply_intent;
			
			if(intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER).equals(prohib_num)) {
				Toast.makeText(context, "Blocking call to: " + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER), Toast.LENGTH_SHORT).show();
				reply_intent = new Intent(Recevier.ACTION_CALL_FILTERED);
				reply_intent.putExtra(Intent.EXTRA_PHONE_NUMBER, intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
				context.sendBroadcast(reply_intent);
				setResultData(null);
			}
		}
		
		if (action.equals(ACTION_CALL_FILTERED)) {
			Toast.makeText(context, "Call Blocked : " + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER), Toast.LENGTH_SHORT).show();
			return;
		}
	}	
}
