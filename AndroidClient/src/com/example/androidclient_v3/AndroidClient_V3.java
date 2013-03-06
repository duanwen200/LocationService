package com.example.androidclient_v3;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class AndroidClient_V3 extends Activity {
    
	Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_client__v3);
        start=(Button)findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent intent_service=new Intent("com.example.androidclient_v3.service");
				Intent intent_service=new Intent(AndroidClient_V3.this,LocationService.class);
				AndroidClient_V3.this.startActivity(intent_service);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_android_client__v3, menu);
        return true;
    }
    
}
