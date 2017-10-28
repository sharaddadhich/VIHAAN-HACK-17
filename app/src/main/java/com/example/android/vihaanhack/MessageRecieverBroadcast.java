package com.example.android.vihaanhack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by sharaddadhich on 28/10/17.
 */

public class MessageRecieverBroadcast extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();
    String TAG = "BroadcastReciverError";

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "onReceive: i  m in broadcast");
            final Bundle bundle = intent.getExtras();

            try{
                if(bundle!=null)
                {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    Log.d(TAG, "onReceive: " + pdusObj);

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);


                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        if(message.contains("Found Team Hope"))
                        {
                            Intent gotomain =  new Intent();
                            gotomain.putExtra("msg",message);
                            gotomain.setClassName("com.example.android.vihaanhack","com.example.android.vihaanhack.Activities.MapActivity");
                            gotomain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(gotomain);
                        }

                    }

                }
            }catch (Exception e)
            {
                Log.d(TAG, "onReceive: " + e);
            }

        }
}
