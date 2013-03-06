package com.example.androidclient_v3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
//
public class FingerPrintsResponse extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "AndroidClient.FingerPrints_Ok", Toast.LENGTH_SHORT).show();
	}

}
