package com.example.androidclient_v3;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class FingerDialog {
	private Context context1;
	private MapView PView;
	private EditText building_edit;
	private EditText floor_edit;
	
   public FingerDialog(Context context,MapView mapview,View layout ){
	   context1=context;
	   PView=mapview;
	   building_edit=(EditText)layout.findViewById(R.id.buildingID);
	   floor_edit=(EditText)layout.findViewById(R.id.floorID);
	   
	    new Thread(){
			 public void run(){
				//暂停Locating.Service
	  			while(!Locating.WorkDone){};
				Locating.WorkDone=false;	
	  			Intent intent_locating=new Intent("LocationService.StopLocating");
	  			context1.sendBroadcast(intent_locating);
			 }
		 }.start();
		 
	   building_edit.setText(PView.mapId.buildingID);
	   floor_edit.setText(PView.mapId.floorID);
	   
	   AlertDialog.Builder builder = new Builder(context);
  	   builder.setMessage("确定FingerPrints?");
  	   builder.setTitle("提示"); 
  	   builder.setView(layout);
  	   builder.setPositiveButton("确定", new  DialogInterface.OnClickListener(){
  		 @Override
  		public void onClick(DialogInterface dialog, int which) {
  			
  			//启动FingerPrintsSurvey.Service
  			Intent intent_finger=new Intent(context1,FingerPrintsSurvey.class);
  			intent_finger.putExtra("X",PView.touchX);
  			intent_finger.putExtra("Y",PView.touchY);
  			intent_finger.putExtra("buildingID", building_edit.getText().toString());
  			intent_finger.putExtra("floorID", floor_edit.getText().toString());
  			context1.startService(intent_finger);
  			 
  			 dialog.dismiss();
  			 PView.touchX=-1;
  		  	 PView.touchY=-1;
  		  	 PView.postInvalidate();
  			
  			 }
  	 });
  	 
  	 builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
  		public void onClick(DialogInterface dialog, int which) {
  			 Toast.makeText(context1, "取消", Toast.LENGTH_SHORT).show();
		     //restart Locating.Service
  			 Intent intent_reStartLocating=new Intent("LocationService.ReStartLocating");
		     context1.sendBroadcast(intent_reStartLocating);
		     
  			 PView.touchX=-1;
  		  	 PView.touchY=-1;
  		  	 PView.postInvalidate();
 			 dialog.dismiss();   
 			 }
  	 });
	  Dialog dialog=builder.create();
  	  dialog.show();
   }
}
