package com.example.androidclient_v3;

import java.util.Timer;
import java.util.TimerTask;

import com.example.androidclient_v3.Locating.LocatingBinder;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Activity {
    
	MapView mapview;
	private MapID currentMap;
	//用于标识Locating.Service是bind状态还是unbind状态
	boolean isBund;
	static public boolean isStopped;
	//标识是否已经绑定Locating.Service
    private Timer LocatingTimer=null;
    private BroadcastReceiver StopLocatingReceiver=new BroadcastReceiver(){
 
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
		    Log.i("LocationgService.StopLocatingReceiver","StopLocatingReceiver");
			StopLocating();
			
		}
    	
    };
    private BroadcastReceiver ReStartLocatingReceiver=new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			StartLocating();
			Log.i("LocationgService.ReStart","ReStart");
		}
    	
    };

//////////////////////////////////////////////////////////////////////////////
//////////
	private LocatingBinder locatingbinder;
	private ServiceConnection conn=new ServiceConnection(){	
	@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
		// TODO Auto-generated method stub
		 locatingbinder=(Locating.LocatingBinder)service;
		 isBund=true;	
		}
	@Override
		public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub	
         isBund=false;
        Log.i("LocationService:onServiceConnection","DisConnected");
	  }
	};
 	//临时数据
	protected int LocationX=0;
	protected int LocationY=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_service);
       
       mapview=(MapView)findViewById(R.id.map);

        currentMap=new MapID();
        ////For experiments
            currentMap.buildingID="E4";
            currentMap.floorID="2"; 
            mapview.mapId.buildingID=currentMap.buildingID;
            mapview.mapId.floorID=currentMap.floorID;
       ///
        Log.i("LocationServie","onCreate");
    }
    
    @Override
     protected void onResume(){
    	super.onResume();
    	IntentFilter filter_stop = new IntentFilter();
    	filter_stop.addAction("LocationService.StopLocating");
    	registerReceiver(StopLocatingReceiver,filter_stop);
    	IntentFilter filter_restart=new IntentFilter();
    	filter_restart.addAction("LocationService.ReStartLocating");
    	registerReceiver(ReStartLocatingReceiver,filter_restart);
    	StartLocating();
    }
    
    private void StartLocating(){
    	isBund=false;
    	isStopped=false;
    	Intent intent=new Intent(LocationService.this,Locating.class);
    	Bundle map=new Bundle();
    	map.putString("buildingID",currentMap.buildingID);
    	map.putString("floorID", currentMap.floorID);
    	intent.putExtra("MapID", map);
    	getApplicationContext().bindService(intent,conn,Service.BIND_AUTO_CREATE);    

       //在新线程中调用Locating.Service,3s一次  
    	
    	if(LocatingTimer==null){
    		LocatingTimer=new Timer();
    	}
       LocatingTimer.schedule(new TimerTask(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
		if(isBund){
		      if(LocationX<mapview.ViewWidth && LocationY<mapview.ViewHeight){
		    	  mapview.locationX=LocationX;
		    	  mapview.locationY=LocationY;
		    	  LocationX+=50;
		    	  LocationY+=50;
		    	  mapview.postInvalidate();
		      }
		      else{
		    	  LocationX=0;
		    	  LocationY=0;
		    	  mapview.locationX=LocationX;
		    	  mapview.locationY=LocationY;
		    	  mapview.postInvalidate();
		      }
		}
		}    	   
       }, 0,1000);
    }
    
    private void StopLocating(){  	
    /*	new Thread(){
    		public void run(){
    			while(!Locating.WorkDone){};
			    	LocatingTimer.cancel(); 
			    	LocatingTimer=null; 
			    	unbindService(conn);
			    	isStopped=true;
    		}
    	}.start();
    	*/
    	if(isBund==true)getApplicationContext().unbindService(conn);
    	isBund=false;

    	isStopped=true;
    }
   @Override
    protected void onPause(){
	   super.onPause();
    	StopLocating();
    	unregisterReceiver(StopLocatingReceiver);
    	unregisterReceiver(ReStartLocatingReceiver);
    	Log.i("LocationService","onPause");
    }

}
