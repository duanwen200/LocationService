package com.example.androidclient_v3;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class WifiScan extends Service {
   private	Bundle data;
   private int Sample_CNT;
   private int data_cnt;
///////////to store  BSSID and RSSI in each measure
   private ArrayList<String> BSSID=new ArrayList<String>(); 
   private ArrayList<Integer> RSSI=new ArrayList<Integer>();
   
   ///Wifi related
	 private WifiManager mainWifi;
	 private WifiReceiver receiver;
	 private List<ScanResult> listWifi;
   //标识上一个Scan是否结束
	 private boolean receiving;
   //标识工作任务已完成
	 private boolean isDone;
   //onBind()返回
	 private Binder scanbinder=new WifiScanBinder();
     @Override
	 public void onCreate(){
       super.onCreate();
	   data_cnt=0;
	   receiver=new WifiReceiver();
	   data=new Bundle();
	   mainWifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
	   if(!mainWifi.isWifiEnabled())
		   mainWifi.setWifiEnabled(true);
   }
 	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
 	//	Log.i("WifiScan","onBind  "+intent.getStringExtra("class"));
 		registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)); 
 		data_cnt=0;
 		isDone=false;
 		receiving=false;
 		if(data.isEmpty()==false){
 		       data.clear();
 	    	   RSSI.clear();
 	    	   BSSID.clear();

 		}
 		Sample_CNT=intent.getIntExtra("Sample_CNT", -1);
 		new Thread(){
 			public void run(){
 				while(data_cnt<Sample_CNT){
 	 	    		data_cnt++;
 	 				mainWifi.startScan(); 				
 	 				receiving=true;
 	 				
 	 				while(receiving);
 	 	       }
 			}
 		}.start();
 	
		return scanbinder;
	}
 	
	public class WifiScanBinder extends Binder
	{
		public boolean isDone(){
			return isDone;
		}
		public Bundle getData(){
			return data;
		}
	}
     @Override
     public boolean onUnbind(Intent intent){
    	// Log.i("WifiScan","onUnbind  "+intent.getStringExtra("class"));
    	 return true;
     }
	 @Override
	 public void onDestroy(){
		 super.onDestroy();
		
		 unregisterReceiver(receiver);
	 }
	 //WifiReceiver
	    class WifiReceiver extends BroadcastReceiver{
	    	@Override  
	        public void onReceive(Context context, Intent intent) {
		
				listWifi=mainWifi.getScanResults();
	            //to record whether one BSSID in the array was detected in this scan
				boolean[] detected=new boolean[BSSID.size()];
	             for(int i=0;i<BSSID.size();i++){
	            	 detected[i]=false;
	             }
	             //add the results of the new scan in the BSSID and RSSI Array.
				for(int i=0;i<listWifi.size();i++){
	               if(BSSID.contains(listWifi.get(i).BSSID)){
	            	   int index=BSSID.indexOf(listWifi.get(i).BSSID);
	            	   int tmp_level=RSSI.get(index).intValue();
	            	   RSSI.set(index,tmp_level+listWifi.get(i).level);
	            	   detected[index]=true;
	               }else{
	            	   BSSID.add(listWifi.get(i).BSSID);
	            	   RSSI.add((data_cnt-1)*(-120)+listWifi.get(i).level);
	               }
				}
				// if the BSSID　that has been in the array but not found in the latest scan , add -120 to the RSSI
				for(int i=0;i<detected.length;i++){
					if(!detected[i]){
						int tmp_level=RSSI.get(i).intValue();
						RSSI.set(i,tmp_level-120);
					}
				}
		
				if(data_cnt==Sample_CNT){
				    String[] RSSI_A=new String[RSSI.size()];
				     for(int i=0;i<RSSI.size();i++){
				    	 RSSI_A[i]=String.valueOf(RSSI.get(i)/Sample_CNT);
				     }   
				    
				     String[] BSSID_A=new String[BSSID.size()];
				    	for(int i=0;i<BSSID.size();i++){
				    		BSSID_A[i]=BSSID.get(i);
				    	}    	 
	                 data.putString("bearing","270");
	                 data.putStringArray("RSSI", RSSI_A);
	                 data.putStringArray("BSSID", BSSID_A);
	                 isDone=true;   
	              
				}
				receiving=false;
				
	       }
	    }


}
