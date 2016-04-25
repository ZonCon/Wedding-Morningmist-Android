package com.megotechnologies.wedding_morningmist;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.megotechnologies.wedding_morningmist.interfaces.ZCFragmentLifecycle;
import com.megotechnologies.wedding_morningmist.interfaces.ZCRunnable;

public class FragmentMenu extends FragmentMeta implements ZCFragmentLifecycle, ZCRunnable {

	TextView tvLabShare, tvLabPolicy, tvVersion, tvLabAlerts, tvLabRsvp;
	LinearLayout llContainer;

	int opcode = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		activity.lastCreatedActivity = MainActivity.SCREEN_ACCOUNT;

		v =  inflater.inflate(R.layout.fragment_menu, container, false);

		storeClassVariables();
		initUIHandles();
		initUIListeners();
		formatUI();

		return v;
	}

	@Override
	public void storeClassVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initUIHandles() {
		// TODO Auto-generated method stub

		llContainer = (LinearLayout)v.findViewById(R.id.ll_container);
		tvLabShare = (TextView)v.findViewById(R.id.tv_share);
		tvLabPolicy = (TextView)v.findViewById(R.id.tv_policies);
		tvLabAlerts = (TextView)v.findViewById(R.id.tv_alerts);
		tvLabRsvp = (TextView)v.findViewById(R.id.tv_rsvp);
		tvVersion = (TextView)v.findViewById(R.id.tv_version);

	}

	@Override
	public void initUIListeners() {
		// TODO Auto-generated method stub

		tvLabShare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, MainActivity.SHARE_SUBJECT);
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, MainActivity.SHARE_CONTENT + MainActivity.MARKET_URL_PREFIX_2 + activity.context.getPackageName());
				startActivity(Intent.createChooser(sharingIntent, "Share This App"));

			}

		});

		tvLabPolicy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				activity.loadPolicy();

			}

		});

		tvLabAlerts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				activity.loadAlerts();


			}
		});

	}

	@Override
	public void formatUI() {
		// TODO Auto-generated method stub
		try {

			PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			String version = pInfo.versionName;
			tvVersion.setText("Version " + version);
			tvVersion.setTypeface(activity.tf);
			tvVersion.setPadding(MainActivity.SPACING, MainActivity.SPACING*2, MainActivity.SPACING, 0);
			tvVersion.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE - 3));

		} catch (PackageManager.NameNotFoundException e) {

		}

		tvLabShare.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
		tvLabShare.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
		tvLabShare.setGravity(Gravity.LEFT);

		tvLabPolicy.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
		tvLabPolicy.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
		tvLabPolicy.setGravity(Gravity.LEFT);

		tvLabAlerts.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
		tvLabAlerts.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
		tvLabAlerts.setGravity(Gravity.LEFT);

		tvLabRsvp.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
		tvLabRsvp.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
		tvLabRsvp.setGravity(Gravity.LEFT);

		activity.app.ENABLE_SYNC = false;
		activity.app.PREVENT_CLOSE_AND_SYNC = true;

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		activity.app.ENABLE_SYNC = true;
		activity.app.PREVENT_CLOSE_AND_SYNC = false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub


	}

	@Override
	public void setRunFlag(Boolean value) {
		// TODO Auto-generated method stub

	}


}
