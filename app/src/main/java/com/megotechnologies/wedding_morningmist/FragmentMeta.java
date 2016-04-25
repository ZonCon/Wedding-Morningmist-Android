package com.megotechnologies.wedding_morningmist;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.megotechnologies.wedding_morningmist.db.DbConnection;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentMeta extends Fragment{

	protected View v;
	protected MainActivity activity;
	protected DbConnection dbC;
	protected Boolean LOCATION_SELECTED = false;
	protected Boolean IS_SIGNEDIN = false;
	protected Boolean IS_CLICKED = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (MainActivity)getActivity();
		dbC = activity.app.conn;

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				activity.IS_CLICKED = false;		
			}
			
		}, 1000);
		
		if(activity.app.APP_EXIT_ON_BACK) {
			activity.app.APP_EXIT_ON_BACK = false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IS_CLICKED = false;
	}


}
