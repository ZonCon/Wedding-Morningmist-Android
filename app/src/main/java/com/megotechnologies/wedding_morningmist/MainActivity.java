package com.megotechnologies.wedding_morningmist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.megotechnologies.wedding_morningmist.alerts.FragmentAlertItemsList;
import com.megotechnologies.wedding_morningmist.db.DbConnection;
import com.megotechnologies.wedding_morningmist.interfaces.ZCActivityLifecycle;
import com.megotechnologies.wedding_morningmist.interfaces.ZCRunnable;
import com.megotechnologies.wedding_morningmist.loading.FragmentLoading;
import com.megotechnologies.wedding_morningmist.policy.FragmentPolicyItemsList;
import com.megotechnologies.wedding_morningmist.sections.FragmentSectionItemsList;
import com.megotechnologies.wedding_morningmist.sections.FragmentSectionsCategories;
import com.megotechnologies.wedding_morningmist.utilities.MLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity implements ZCActivityLifecycle, ZCRunnable {

	public static Boolean LOG = true;

	// Project
	
	public static String PID = "18";
	
	// API
	
	public static String API = "http://www.zoncon.com/v6_1/index.php/projects/";
	public static String API_STREAMS = API + "get_public_streams";
	public static String API_INDI_STREAMS = API + "get_public_individual_stream";
	public static String API_NOTIFICATIONS = API + "get_public_notification";
	public static String UPLOADS = "http://www.zoncon.com/v6_1/uploads";

	// Fonts

	public static char FONT_CHAR_URL = 0xe70a;
	public static char FONT_CHAR_CONTACT = 0xe708;
	public static char FONT_CHAR_LOCATION = 0xe706;
	public static char FONT_CHAR_ATTACHMENT = 0xe70b;
	public static char FONT_CHAR_SHARE = 0xe713;
	public static char FONT_CHAR_LIKED = 0xe71c;


	// Maps

	public static String MAPS_PREFIX = "http://maps.google.com/?q=";

	// External Storage

	public static String STORAGE_PATH = "";

	// Screen

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static int SPACING = 20;

	// Pictures

	public static int IMG_TH_MAX_SIZE = 200;
	public static int IMG_DETAIL_MAX_SIZE = 800;

	// Text size
	
	public static char TEXT_SIZE_TILE = 12;
	public static char TEXT_SIZE_BUTTON = 17;
	public static char TEXT_SIZE_TITLE = 16;
	
	// DB
	
	public static String DB_TABLE = "records";
	public static String DB_NAME = "zoncon_conference_retronight.db";
	public static int DB_VERSION = 5;
	
	// DB Columns
	
	public static String DB_COL_ID = "_id";
	public static String DB_COL_SRV_ID = "server_id";
	public static String DB_COL_TYPE = "type";
	public static String DB_COL_TITLE = "title";
	public static String DB_COL_SUB = "subtitle";
	public static String DB_COL_CONTENT = "content";
	public static String DB_COL_TIMESTAMP = "timestamp";
	public static String DB_COL_STOCK = "stock";
	public static String DB_COL_PRICE = "price";
	public static String DB_COL_EXTRA_1 = "extra1";
	public static String DB_COL_EXTRA_2 = "extra2";
	public static String DB_COL_EXTRA_3 = "extra3";
	public static String DB_COL_EXTRA_4 = "extra4";
	public static String DB_COL_EXTRA_5 = "extra5";
	public static String DB_COL_EXTRA_6 = "extra6";
	public static String DB_COL_EXTRA_7 = "extra7";
	public static String DB_COL_EXTRA_8 = "extra8";
	public static String DB_COL_EXTRA_9 = "extra9";
	public static String DB_COL_EXTRA_10 = "extra10";
	public static String DB_COL_BOOKING = "bookingPrice";
	public static String DB_COL_DISCOUNT = "discount";
	public static String DB_COL_SKU = "sku";
	public static String DB_COL_SIZE = "size";
	public static String DB_COL_WEIGHT = "weight";
	public static String DB_COL_NAME = "name";
	public static String DB_COL_CAPTION = "caption";
	public static String DB_COL_URL = "url";
	public static String DB_COL_LOCATION = "location";
	public static String DB_COL_EMAIL = "email";
	public static String DB_COL_PHONE = "phone";
	public static String DB_COL_PATH_ORIG = "path_original";
	public static String DB_COL_PATH_PROC = "path_processed";
	public static String DB_COL_PATH_TH = "path_thumbnail";
	public static String DB_COL_CART_ITEM_STREAM_SRV_ID = "cart_item_stream_server_id";
	public static String DB_COL_CART_ITEM_SRV_ID = "cart_item_server_id";
	public static String DB_COL_CART_ITEM_QUANTITY = "cart_item_quantity";
	public static String DB_COL_CART_COUPON_CODE = "cart_coupon_code";
	public static String DB_COL_CART_CART_ISOPEN = "cart_isopen";
	public static String DB_COL_FOREIGN_KEY = "foreign_key";
	public static String[] DB_ALL_COL = {DB_COL_ID, DB_COL_SRV_ID, DB_COL_TYPE, DB_COL_TITLE, DB_COL_SUB, DB_COL_CONTENT, DB_COL_TIMESTAMP, DB_COL_STOCK, DB_COL_PRICE, DB_COL_EXTRA_1, DB_COL_EXTRA_2, DB_COL_EXTRA_3, DB_COL_EXTRA_4, DB_COL_EXTRA_5, DB_COL_EXTRA_6, DB_COL_EXTRA_7, DB_COL_EXTRA_8, DB_COL_EXTRA_9, DB_COL_EXTRA_10, DB_COL_BOOKING, DB_COL_DISCOUNT, DB_COL_SKU, DB_COL_SIZE, DB_COL_WEIGHT, DB_COL_NAME, DB_COL_CAPTION, DB_COL_URL, DB_COL_LOCATION, DB_COL_EMAIL, DB_COL_PHONE, DB_COL_PATH_ORIG, DB_COL_PATH_PROC, DB_COL_PATH_TH, DB_COL_CART_ITEM_STREAM_SRV_ID, DB_COL_CART_ITEM_SRV_ID, DB_COL_CART_ITEM_QUANTITY, DB_COL_CART_COUPON_CODE, DB_COL_CART_CART_ISOPEN, DB_COL_FOREIGN_KEY};
	
	// DB Record Types
	
	public static String DB_RECORD_TYPE_STREAM = "RECORD_STREAM";
	public static String DB_RECORD_TYPE_ITEM = "RECORD_ITEM";
	public static String DB_RECORD_TYPE_PICTURE = "RECORD_PICTURE";
	public static String DB_RECORD_TYPE_ATTACHMENT = "RECORD_ATTACHMENT";
	public static String DB_RECORD_TYPE_URL = "RECORD_URL";
	public static String DB_RECORD_TYPE_LOCATION = "RECORD_LOCATION";
	public static String DB_RECORD_TYPE_CONTACT = "RECORD_CONTACT";
	public static String DB_RECORD_TYPE_CART = "RECORD_CART";
	public static String DB_RECORD_TYPE_CART_ITEM = "RECORD_CART_ITEM";
	public static String DB_RECORD_TYPE_DISCOUNT = "RECORD_DISCOUNT";
	public static String DB_RECORD_TYPE_COUPON = "RECORD_COUPON";
	public static String DB_RECORD_TYPE_TAX_1 = "RECORD_TAX_1";
	public static String DB_RECORD_TYPE_TAX_2 = "RECORD_TAX_2";
	public static String DB_RECORD_TYPE_MESSAGESTREAM_PUSH = "MESSAGE_PUSH";
	public static String DB_RECORD_VALUE_CART_OPEN = "yes";
	

	// Threads

	public static String TH_NAME_LOAD_MORE = "load_more";
	public static String TH_NAME_LOAD_MORE_CHECKER = "load_more_checker";
	
	// Thread Constants
	
	public static String TH_STATE_TERM = "TERMINATED";
	public static int TH_CHECKER_DURATION = 200;
	public static int TH_SPLASH_MIN_DURATION = 3000;
	
	// Messages

	public static String MSG_VERSION_UPDATE = "Please update your app to the latest version from the Play Store! If you haven't yet received a software update, please wait for a few hours until it becomes available.";
	public static String MSG_OK = "OK";
	public static String MSG_PRODUCT_NOT_AVAILABLE = "This section contains no items!";

	// Splash

	public static int SPLASH_DURATION = 3000;

	// Loading

	public static int LOADING_PIC_COUNT = 3;

	// Cart

	public static long TIMEOUT_CART_CLEAR = 86400000;

	// Market

	public static String MARKET_URL_PREFIX_1 = "market://details?id=";
	public static String MARKET_URL_PREFIX_2 = "http://play.google.com/store/apps/details?id=";

	// Market

	public static String SHARE_SUBJECT = "ZonCon Conference App";
	public static String SHARE_CONTENT = "Thank you for downoading the ZonCon Conference mobile app!";

	// Notifications

	public static int NOTIF_SLEEP_TIME = 300000;

	// Load More

	public static String LOAD_MORE_CAPTION = "LOAD MORE";
	public static String LOAD_MORE_CAPTION_LOADING = "Loading...";
	public static String LOAD_MORE_CAPTION_END = "REACHED THE END";

	// Body

	public static int BODY_TOP_MARGIN_DP = 40;
	public static int BODY_BOTTOM_MARGIN_DP = 0;


	public static String SHARE_ITEM_POSTFIX = "Brought to you by ZonCon Conference!";

	public static String SCREEN_SECTION_CATEGORIES = "SectionCategories";
	public static String SCREEN_SECTION_ITEM_LIST = "SectionItemList";
	public static String SCREEN_SECTION = "SectionItemDetails";
	public static String SCREEN_ALERT_ITEM_LIST = "AlertItemList";
	public static String SCREEN_ALERT_ITEM_DETAILS = "AlertItemDetails";
	public static String SCREEN_POLICY_ITEM_LIST = "PolicyItemList";
	public static String SCREEN_POLICY_ITEM_DETAILS = "PolicyItemDetails";
	public static String SCREEN_LOADING = "Splash";
	public static String SCREEN_STREAMS = "Stream";
	public static String SCREEN_ACCOUNT = "Account";

	public static String STREAM_NAME_VERSIONS = "Versions";

	public static int NUM_INIT_STREAMS = 3;

	public static final String MyPREFERENCES = "MyPrefs" ;

	public ZonConApplication app;

	public static ProgressDialog pageLoad;

	public Context context;
	public SharedPreferences sharedPreferences;
	public DbConnection dbC;
	public FragmentManager fragMgr;

	protected LinearLayout llHead;
	public ImageView ivHeadTitle, ivHeadMenu, ivBg;
	protected FrameLayout flBody;

	public Typeface tf;

	protected Boolean RUN_FLAG = false;
	public Boolean IS_CLICKED = false;
	public Boolean IS_CLICKABLE_FRAME = true;
	public Boolean IS_CLICKABLE_FRAGMENT = true;
	public String myCountryId = "1", myStateId = "13", myCityId = "147";
	public String lastCreatedActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
			// call something for API Level 11+

			try {

				display.getSize(size);
				SCREEN_WIDTH = size.x;
				SCREEN_HEIGHT = size.y;

			} catch (java.lang.NoSuchMethodError ignore) { // Older device
				SCREEN_WIDTH = display.getWidth();
				SCREEN_HEIGHT = display.getHeight();
			}

		} else {

			SCREEN_WIDTH = display.getWidth();
			SCREEN_HEIGHT = display.getHeight();

		}

		String packName = getApplicationContext().getPackageName();
		STORAGE_PATH = Environment.getExternalStorageDirectory() + "/" + packName;
		File dir = new File(STORAGE_PATH);
		if (!dir.exists()) {
			dir.mkdir();
		}

		storeContext();
		storeClassVariables();
		initUIHandles();
		formatUI();
		initUIListeners();

		app.context = MainActivity.this;

		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		MLog.log("Resuming..." + app.stateOfLifeCycle);
		MLog.log("Resuming..." + app.wasInBackground + "");
		MLog.log("Resuming..." + app.ENABLE_SYNC + "");

		if(!app.PREVENT_CLOSE_AND_SYNC && !app.ISGOINGOUTOFAPP) {

			loadSplash();

		} else {

			// Do Nothing

		}

		if(app.ISGOINGOUTOFAPP) {

			app.ISGOINGOUTOFAPP = false;

		}

		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();

	}

	public void loadSplash() {

		try {

			app.ENABLE_SYNC = true;
			app.PREVENT_CLOSE_AND_SYNC = false;

			fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction fragmentTransaction;

			fragmentTransaction = fragMgr.beginTransaction();
			FragmentLoading fragmentLoc = new FragmentLoading();
			fragmentLoc.isBegin = true;
			fragmentTransaction.add(R.id.ll_body, fragmentLoc, MainActivity.SCREEN_LOADING)
					.addToBackStack(MainActivity.SCREEN_LOADING)
					.commit();

		} catch (IllegalStateException e) {

		}

	}

	public void loadShop() {

		try {

			app.PREVENT_CLOSE_AND_SYNC = false;

			fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction fragmentTransaction;

			fragmentTransaction = fragMgr.beginTransaction();
			FragmentSectionsCategories fragmentLoc = new FragmentSectionsCategories();
			fragmentTransaction.add(R.id.ll_body, fragmentLoc, MainActivity.SCREEN_SECTION_CATEGORIES)
					.addToBackStack(MainActivity.SCREEN_SECTION_CATEGORIES)
					.commit();


			if(getIntent().getExtras() != null) {

				HashMap<String, String> map = (HashMap<String, String>)getIntent().getExtras().get("new-message");
				String idStream = map.get(MainActivity.DB_COL_SRV_ID);

				map = new HashMap<String, String>();
				map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_STREAM);
				map.put(MainActivity.DB_COL_SRV_ID, idStream);
				ArrayList<HashMap<String, String>> records =  null;
				if(dbC.isOpen()) {
					dbC.isAvailale();
					records =  dbC.retrieveRecords(map);
				}

				if(records.size() > 0) {

					map = records.get(0);
					String name = map.get(MainActivity.DB_COL_NAME);

					if(name.toLowerCase().contains("alert")) {

						loadAlerts();

					} else {


						FragmentSectionItemsList frag = new FragmentSectionItemsList();
						frag.idStream = Integer.parseInt(idStream);
						fragMgr.beginTransaction()
								.add((flBody).getId(), frag, MainActivity.SCREEN_SECTION_ITEM_LIST)
								.addToBackStack(MainActivity.SCREEN_SECTION_ITEM_LIST)
								.commit();

					}

				}

			}

		} catch(IllegalStateException e) {

		}

	}

	public void loadAlerts() {

		try {

			app.PREVENT_CLOSE_AND_SYNC = false;

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_STREAM);
			ArrayList<HashMap<String, String>> records =  null;
			if(dbC.isOpen()) {
				dbC.isAvailale();
				records =  dbC.retrieveRecords(map);
			}

			if(records.size() > MainActivity.NUM_INIT_STREAMS) {

				for(int i = 0; i < records.size(); i++) {

					map = records.get(i);
					String name = map.get(MainActivity.DB_COL_NAME);

					if(name.toLowerCase().contains("alert")) {

						//fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
						FragmentAlertItemsList fragment = new FragmentAlertItemsList();
						fragment.idStream = Integer.parseInt(map.get(MainActivity.DB_COL_SRV_ID));
						fragMgr.beginTransaction()
								.add(R.id.ll_body, fragment, MainActivity.SCREEN_ALERT_ITEM_LIST)
								.addToBackStack(MainActivity.SCREEN_ALERT_ITEM_LIST)
								.commit();

						break;

					}

				}

			}

		} catch (IllegalStateException e) {

		}

	}

	public void loadPolicy() {

		try {

			app.PREVENT_CLOSE_AND_SYNC = false;

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_STREAM);
			ArrayList<HashMap<String, String>> records =  null;
			if(dbC.isOpen()) {
				dbC.isAvailale();
				records =  dbC.retrieveRecords(map);
			}

			if(records.size() > MainActivity.NUM_INIT_STREAMS) {

				for(int i = 0; i < records.size(); i++) {

					map = records.get(i);
					String name = map.get(MainActivity.DB_COL_NAME);

					if(name.toLowerCase().contains("terms")) {

						//fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
						FragmentPolicyItemsList fragment = new FragmentPolicyItemsList();
						fragment.idStream = Integer.parseInt(map.get(MainActivity.DB_COL_SRV_ID));
						fragMgr.beginTransaction()
								.add(R.id.ll_body, fragment, MainActivity.SCREEN_POLICY_ITEM_LIST)
								.addToBackStack(MainActivity.SCREEN_POLICY_ITEM_LIST)
								.commit();

						break;

					}

				}

			}

		} catch (IllegalStateException e) {

		}

	}

	public void loadMenu() {

		try {

			app.PREVENT_CLOSE_AND_SYNC = true;

			fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction fragmentTransaction;

			fragmentTransaction = fragMgr.beginTransaction();
			FragmentMenu fragmentLoc = new FragmentMenu();
			fragmentTransaction.add(R.id.ll_body, fragmentLoc, MainActivity.SCREEN_ACCOUNT)
					.addToBackStack(MainActivity.SCREEN_ACCOUNT)
					.commit();

		} catch (IllegalStateException e) {

		}

	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		int backStackEntryCount = fragMgr.getBackStackEntryCount();
		MLog.log("Back Pressed = " + lastCreatedActivity);
		MLog.log("Back Stack Entry = " + backStackEntryCount);

		if(app.APP_EXIT_ON_BACK) {
			super.onBackPressed();
			finish();
		} else {

			if(backStackEntryCount == 1){

				String fragmentTag = fragMgr.getBackStackEntryAt(fragMgr.getBackStackEntryCount() - 1).getName();
				MLog.log("Back Fragment found " + fragmentTag);
				if(fragmentTag.equals(MainActivity.SCREEN_SECTION_CATEGORIES)) {
					super.onBackPressed();
					finish();
				} else {
					loadShop();
				}

			} else {

				MLog.log("Pressing Back");
				super.onBackPressed();

			}

		}
		IS_CLICKABLE_FRAGMENT = true;

	}

	public int dpToPixels(int dp) {
		float density = getApplicationContext().getResources().getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}

	float pixelsToSp(Context context, float px) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		//Log.d(TAG, "Scaled Density " + scaledDensity + "");
		return px/scaledDensity;
	}

	float spToPixels(Context context, float px) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		//Log.d(TAG, "Scaled Density " + scaledDensity + "");
		return px*scaledDensity;
	}

	public Boolean checkIfExistsInExternalStorage(String fileName) {

		String filePath = STORAGE_PATH + "/" + fileName;
		File file = new File(filePath);
		if(file.exists()) {

			if(file.length() > 0) {
				return file.exists();
			} else {

				file.delete();
				return false;

			}

		}

		return file.exists();

	}

	@Override
	public void storeContext() {
		// TODO Auto-generated method stub
		context = MainActivity.this;
		app = (ZonConApplication)getApplicationContext();
	}

	@Override
	public void initUIListeners() {
		// TODO Auto-generated method stub

		ivHeadMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!IS_CLICKED) {

					IS_CLICKED = true;
					loadMenu();

				}
			}

		});

	}

	@Override
	public void initUIHandles() {
		// TODO Auto-generated method stub
		llHead = (LinearLayout)findViewById(R.id.ll_head);
		ivHeadTitle = (ImageView)findViewById(R.id.iv_head_title);
		ivHeadMenu = (ImageView)findViewById(R.id.iv_head_menu);
		ivBg = (ImageView)findViewById(R.id.img_bg);

		flBody = (FrameLayout)findViewById(R.id.ll_body);

		tf = Typeface.createFromAsset(getAssets(), "icomoon.ttf");

	}

	@Override
	public void formatUI() {
		// TODO Auto-generated method stub

		if(isTablet(getApplicationContext())) {

			TEXT_SIZE_TILE = (char)(18);
			TEXT_SIZE_TITLE = (char)(22);

		}

	}

	@Override
	public void storeClassVariables() {
		// TODO Auto-generated method stub
		dbC = app.conn;
		fragMgr = getFragmentManager();

	}

	public Handler handlerLoading = new Handler() {

		public void handleMessage(android.os.Message msg) {

			if(msg.what == 0) {

				hideLoading();

			} else {

				showLoading();

			}

		};

	};

	public void hideHeaderFooter() {

		llHead.setVisibility(RelativeLayout.GONE);
		RelativeLayout.LayoutParams rParams = (RelativeLayout.LayoutParams) flBody.getLayoutParams();
		rParams.setMargins(0, 0, 0, 0);


	}

	public void showHeaderFooter() {

		llHead.setVisibility(RelativeLayout.VISIBLE);
		RelativeLayout.LayoutParams rParams = (RelativeLayout.LayoutParams) flBody.getLayoutParams();
		rParams.setMargins(0, dpToPixels(MainActivity.BODY_TOP_MARGIN_DP), 0, dpToPixels(MainActivity.BODY_BOTTOM_MARGIN_DP));


	}

	public void showLoading() {

		pageLoad = new ProgressDialog(MainActivity.this);
		pageLoad.setTitle("Loading...");
		pageLoad.setMessage("Please wait");
		pageLoad.setCanceledOnTouchOutside(false);
		pageLoad.show();

	}

	public void hideLoading() {

		pageLoad.dismiss();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRunFlag(Boolean value) {
		// TODO Auto-generated method stub
		RUN_FLAG = value;
	}

	protected Handler threadHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

		}

	};

	public boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		MLog.log("IS TABLET = " + (xlarge || large));
		return (xlarge || large);
	}

	public Boolean checkVersionCompat() {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_STREAM);
		map.put(MainActivity.DB_COL_NAME, MainActivity.STREAM_NAME_VERSIONS);

		ArrayList<HashMap<String, String>> records = null;
		if(dbC.isOpen()) {
			dbC.isAvailale();
			records = dbC.retrieveRecords(map);
		}

		MLog.log("Version Records=" + records.size());

		if(records.size() > 0) {

			map = records.get(0);
			String streamKey = map.get(MainActivity.DB_COL_ID);

			MLog.log("Version Records key =" + streamKey);

			map = new HashMap<String, String>();
			map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_ITEM);
			map.put(MainActivity.DB_COL_FOREIGN_KEY, streamKey);
			if(dbC.isOpen()) {
				dbC.isAvailale();
				records = dbC.retrieveRecords(map);
			}

			MLog.log("Version Records Items key =" + records.size());

			if(records.size() > 0) {

				map = records.get(0);
				String versionWeb = map.get(MainActivity.DB_COL_TITLE);
				MLog.log("Web Version = " + versionWeb);
				try {

					String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
					Double dVersionWeb = Double.valueOf(versionWeb);
					Double dVersion = Double.valueOf(version);
					if(dVersion >= dVersionWeb) {

						return true;

					} else {

						return false;

					}

				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			} 
		}


		return false;

		//activity.getFragmentManager().beginTransaction().remove(FragmentLoading.this).commit();

	}

}
