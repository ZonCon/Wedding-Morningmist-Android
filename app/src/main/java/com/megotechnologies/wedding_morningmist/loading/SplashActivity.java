package com.megotechnologies.wedding_morningmist.loading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.megotechnologies.wedding_morningmist.MainActivity;
import com.megotechnologies.wedding_morningmist.R;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity{
	
	TextView tvZonCon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);

		if(getIntent().getExtras() != null) {

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					HashMap<String, String> map = (HashMap<String, String>)getIntent().getExtras().get("new-message");
					intent.putExtra("new-message", map);
					startActivity(intent);
					finish();

				}
			}, MainActivity.SPLASH_DURATION);



		} else {

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(intent);
					finish();

				}
			}, MainActivity.SPLASH_DURATION);

		}

		tvZonCon = (TextView)findViewById(R.id.tv_zoncon);
		tvZonCon.setShadowLayer(2, 1, 1, getResources().getColor(R.color.black));
		tvZonCon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		tvZonCon.setVisibility(RelativeLayout.GONE);
		
	}

}
