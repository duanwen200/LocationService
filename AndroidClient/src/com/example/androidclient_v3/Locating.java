package com.example.androidclient_v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class Locating extends Service {
    private MapID previousMap;
   
    static public boolean WorkDone;//为true是表示没有工作在做，否则表示有，有同步的的作用。。。。
    
    private  int counter;
    private LocatingBinder locatingbinder;

    //判断binder已经ok
    private boolean isBund;
    //
    private Timer WifiScanTimer=null;
    private CommunicationPOST CommunicationPost;
	// use a handler to deal with the received data (send the handler messages from other
	// threads)
	private final Handler handler = new Handler(){
		@Override
		public void handleMessage(final Message msg) {
			String bundleResult = msg.getData().getString("Response");	

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
//////////////////////
	public class LocatingBinder extends Binder
	{	
	  public int getCounter(){
		  return counter;
	  }
	}
    
	
	@Override 
	public void onCreate(){
		super.onCreate();
		Log.i("Locating","onCreate");
		
		CommunicationPost=new CommunicationPOST(handler);
		
		previousMap=new MapID();
		WorkDone=true;
		isBund=false;
		locatingbinder=new LocatingBinder();
		
		if(WifiScanTimer==null)
			WifiScanTimer=new Timer();
		
		WifiScanTimer.schedule(new TimerTask(){   
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(WorkDone){
					Intent intent=new Intent(Locating.this,WifiScan.class);
					intent.putExtra("Sample_CNT",2);
					intent.putExtra("class","Locating");
					getApplicationContext().bindService(intent,conn,Service.BIND_AUTO_CREATE);
					WorkDone=false;
					while(!isBund);
					while(!wifiscanbinder.isDone());
					Bundle data=wifiscanbinder.getData();	
					//发送结果给服务器（数据还有待包装）
					CommunicationPost.performPost("URL","Body");
					getApplicationContext().unbindService(conn);
					WorkDone=true;
					isBund=false;						
	             }
				 
			}
	    	 
	     },0,3000);
	
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		  Log.i("Logcating","onBind");
		Bundle MapData=intent.getExtras();
		previousMap.buildingID=MapData.getString("buildingID");
		previousMap.floorID=MapData.getString("floorID");
		return locatingbinder;
	}
	@Override   
	public boolean onUnbind(Intent intent){
		
		 Log.i("Logcating","onUnbind");
		if(isBund)getApplicationContext().unbindService(conn);
		  isBund=false;
		    WifiScanTimer.cancel();
			WifiScanTimer.purge();
			WifiScanTimer=null;    
		    return true;
	}
   @Override
   public void onDestroy(){
	   super.onDestroy();
	  Log.i("Logcating","onDestroy");
   }
	
	
}
