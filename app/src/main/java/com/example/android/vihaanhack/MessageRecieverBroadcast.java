package com.example.android.vihaanhack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sharaddadhich on 28/10/17.
 */

public class MessageRecieverBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        final Bundle bundle = intent.getExtras();

        try{
            if(bundle!=null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);


                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    if(message.contains("Found Team Hope"))
                    {
                        Toast.makeText(context, "Recived msg", Toast.LENGTH_SHORT).show();
                        Intent gotomain =  new Intent();
                        gotomain.setClassName("com.example.android.vihaanhack","com.example.android.vihaanhack.Activities.MapActivity");
                        gotomain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(gotomain);
                    }

                }

            }
        }catch (Exception e)
        {
            Log.d("12312345", "onReceive: " + e);
        }



    }
}
