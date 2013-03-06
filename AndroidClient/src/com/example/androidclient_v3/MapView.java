package com.example.androidclient_v3;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MapView extends View implements OnGestureListener{
     
	private Bitmap bitmap;
	private Context context1;
	public int touchX;
	public int touchY;
	public float locationX;
	public float locationY;
	public int ViewWidth;
	public int ViewHeight;
	public MapID mapId;
	private GestureDetector detector;
	
	
	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		context1=context;
		// TODO Auto-generated constructor stub
		//bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.pic);
		bitmap=((BitmapDrawable) context.getResources().getDrawable(R.drawable.pic3)).getBitmap();
		touchX=-1;
		touchY=-1;
		locationX=-1f;
		locationY=-1f;
		ViewWidth=bitmap.getWidth();
		ViewHeight=bitmap.getHeight();
		mapId=new MapID();
		setBackgroundColor(Color.BLUE);
		setFocusable(true);
	    detector=new GestureDetector(this);
	}
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		//Log.i("MyView","onDraw");
		
		
		if(touchX>=0){
			touchX=((int)(touchX/100))*100;
			touchY=((int)(touchY/100))*100;
			
			Paint paint=new Paint();
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(3);
			paint.setColor(Color.RED);
			canvas.drawBitmap(bitmap, 0, 0,null);	
			canvas.drawRect(new Rect(touchX,touchY,touchX+100,touchY+100), paint);
		}else{
			 canvas.drawBitmap(bitmap, 0, 0,null);
			 Paint paint=new Paint();
			 paint.setColor(Color.GREEN);
			 canvas.drawCircle(locationX, locationY, 10, paint);
		}
			
			
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
	
		return detector.onTouchEvent(event);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredHeight = measureHeight(heightMeasureSpec);
		int measuredWidth = measureWidth(widthMeasureSpec);
         
		setMeasuredDimension( measuredWidth,measuredHeight);
	  
	}
      
	private int measureHeight(int heightSpec){
		int height;
		int specMode = MeasureSpec.getMode(heightSpec);
		int specSize = MeasureSpec.getSize(heightSpec);
		 if(specMode==MeasureSpec.EXACTLY){
			height=specSize;
		
		 }else{
			 height=bitmap.getHeight();
			 if(specMode==MeasureSpec.AT_MOST){
				 height=Math.min(height, specSize);		
			 }
		 }
		 return height;
	}
	
	private int measureWidth(int widthSpec){
		int width;
		int specMode = MeasureSpec.getMode(widthSpec);
		int specSize = MeasureSpec.getSize(widthSpec);
		 if(specMode==MeasureSpec.EXACTLY){
			width=specSize;
		
		 }else{
			 width=bitmap.getWidth();
			 if(specMode==MeasureSpec.AT_MOST){
				 width=Math.min(width, specSize);
				
			 }
		 }
		 return width;
	}
	@Override
	public boolean onDown(MotionEvent event) {
		// TODO Auto-generated method stub
        Log.i("Gesture","onDown");
		return true;
	}
	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		 Log.i("Gesture","onFling");
		return false;
	}
	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
    	//Dialog                     
		   LayoutInflater inflater = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		   View layout = inflater.inflate(R.layout.finger_dialog, (ViewGroup)findViewById(R.id.dialog));
    	if(FingerPrintsSurvey.isFingerPrintsOk){
    		FingerDialog info=new FingerDialog(context1,MapView.this,layout);      
    	}
    	Log.i("Gesture","onLongPress");
		
	}
	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		touchX=-1;
    	touchY=-1;
    	invalidate(); 
		 Log.i("Gesture","onScroll");
		return false;
	}
	@Override
	public void onShowPress(MotionEvent event) {
		// TODO Auto-generated method stub
		touchX=(int)event.getX();
        touchY=(int)event.getY();
        invalidate(); 
  
		Log.i("Gesture","onShowPress");
	
	}
	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		touchX=-1;
    	touchY=-1;
    	invalidate(); 
    	Log.i(" onSingleTapUp"," onSingleTapUp");
		return true;
	}
}
