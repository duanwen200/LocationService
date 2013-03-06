package com.example.androidclient_v3;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class FingerPrintsSurvey extends Service {

    private boolean isBund;
    private Bundle data;
    static public boolean isFingerPrintsOk=true;
    private CommunicationPOST CommunicationPost;
	// use a handler to deal with the received data (send the handler messages from other
	// threads)
	private final Handler handler = new Handler(){
		@Override
		public void handleMessage(final Message msg) {
			String bundleResult = msg.getData().getString("Response");	
			//完成任务后重新开启Locating.Service
	
			Log.i("FingerPrintsSurvey",bundleResult);
		}
	};
//////////////////////////////////////////////////////////////////////////////
//////////
	private WifiScan.WifiScanBinder wifiscanbinder;
	private ServiceConnection conn=new ServiceConnection(){	
	    @Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
		// TODO Auto-generated method stub
		    wifiscanbinder=(WifiScan.WifiScanBinder)service; 
		    isBund=true;        
		   }
	    @Override
	   public void onServiceDisconnected(ComponentName name) {
	         // TODO Auto-generated method stub	
	          Log.i("Locating","disconnection");
	       }
	};
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
   
	@Override
	public void onCreate(){
		super.onCreate();
		isBund=false;
		isFingerPrintsOk=true;
		CommunicationPost=new CommunicationPOST(handler);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		int X=intent.getIntExtra("X", -1);
		int Y=intent.getIntExtra("Y",-1);
		String buildingID=intent.getStringExtra("buildingID");
		String floorID=intent.getStringExtra("floorID");
		Log.i("FingerPrintsSurvey:X",String.valueOf(X));
		Log.i("FingerPrintsSurvey:Y",String.valueOf(Y));
		Log.i("FingerPrintsSurvey:buildingID",buildingID);
		Log.i("FingerPrintsSurvey:floorID",floorID);
		isBund=false;
	 
	 new Thread(){
		 public void run(){
			 	 isFingerPrintsOk=false; 
			     while(!LocationService.isStopped ){}
			     Log.i("FingerPrintsSurvey","isStoped");
			 
				Intent intentWifi=new Intent(FingerPrintsSurvey.this,WifiScan.class);
				intentWifi.putExtra("Sample_CNT",1);
				intentWifi.putExtra("class", "FingerSurvey");
				bindService(intentWifi,conn,Service.BIND_AUTO_CREATE);
				while(!isBund){}
				while(!wifiscanbinder.isDone());
				data=wifiscanbinder.getData();  
				unbindService(conn);
				isBund=false;
				Intent intent_reStartLocating=new Intent("LocationService.ReStartLocating");
				sendBroadcast(intent_reStartLocating);
				isFingerPrintsOk=true;
				//发送结果给服务器（数据还有待包装）	
				Log.i("FingerPrintsSurvey","Before Send data");
				CommunicationPost.performPost("URL","FingerPrintsSurvey");
				Intent intent_response=new Intent();
				intent_response.setAction("AndroidClient.FingerPrints_Ok");
				sendBroadcast(intent_response);
			
		 }
	 }.start();
	  
	
		//CommunicationPost.performPost("URL","Body");
		//FingerPrintsSurvey.this.stopSelf();
				
		return START_STICKY;
		
	}
}
