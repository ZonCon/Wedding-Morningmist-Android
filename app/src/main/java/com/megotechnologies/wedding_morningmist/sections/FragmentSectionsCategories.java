package com.megotechnologies.wedding_morningmist.sections;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.megotechnologies.wedding_morningmist.FragmentMeta;
import com.megotechnologies.wedding_morningmist.MainActivity;
import com.megotechnologies.wedding_morningmist.R;
import com.megotechnologies.wedding_morningmist.interfaces.ZCFragmentLifecycle;
import com.megotechnologies.wedding_morningmist.interfaces.ZCRunnable;
import com.megotechnologies.wedding_morningmist.utilities.ImageProcessingFunctions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentSectionsCategories extends FragmentMeta implements ZCFragmentLifecycle, ZCRunnable {

	int IV_ID_PREFIX = 4000;

	Boolean RUN_FLAG = false;
	Thread thPictDownload;

	LinearLayout llContainer;

	Timer timer;

	int bannerCounter = 0, bannerMax = 0;
	ArrayList<HashMap<String, String>> recordsBannerItems;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v =  inflater.inflate(R.layout.fragment_section_categories, container, false);
		activity.lastCreatedActivity = MainActivity.SCREEN_SECTION_CATEGORIES;
		activity.showHeaderFooter();

		storeClassVariables();
		initUIHandles();
		initUIListeners();
		formatUI();
		
		try {
			loadFromLocalDB();
		} catch (IllegalStateException e) {
			
		}

		return v;

	}

	Handler timerHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			bannerCounter++;
			if(bannerCounter == bannerMax) {
				bannerCounter = 0;
			}

			HashMap<String, String> mapItem = recordsBannerItems.get(bannerCounter);
			String _idItem = mapItem.get(MainActivity.DB_COL_ID);
			bannerMax = recordsBannerItems.size();

			HashMap<String, String> mapPictures = new HashMap<String, String>();
			mapPictures.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_PICTURE);
			mapPictures.put(MainActivity.DB_COL_FOREIGN_KEY, _idItem);
			ArrayList<HashMap<String, String>> recordsPictures = null;
			if (dbC.isOpen()) {
				dbC.isAvailale();
				recordsPictures = dbC.retrieveRecords(mapPictures);
			}

			String picture = "";
			if (recordsPictures.size() > 0) {
				mapPictures = recordsPictures.get(0);
				picture = mapPictures.get(MainActivity.DB_COL_PATH_ORIG);
			}

			if (picture.length() > 0) {

				if (activity.checkIfExistsInExternalStorage(picture)) {

					try {
						String filePath = MainActivity.STORAGE_PATH + "/" + picture;
						Bitmap bmp = (new ImageProcessingFunctions()).decodeSampledBitmapFromFile(filePath, MainActivity.IMG_DETAIL_MAX_SIZE, MainActivity.IMG_DETAIL_MAX_SIZE);
						Message msg1 = new Message();
						msg1.obj = bmp;
						msg1.what = (IV_ID_PREFIX + MainActivity.NUM_INIT_STREAMS);
						threadHandler.sendMessage(msg1);
					} catch (OutOfMemoryError e) {

					}

				} else {

					thPictDownload = new Thread(FragmentSectionsCategories.this);
					thPictDownload.setName(picture + ";" + (IV_ID_PREFIX + MainActivity.NUM_INIT_STREAMS));
					thPictDownload.start();

				}

			} else {

				Message msg1 = new Message();
				msg1.obj = null;
				msg1.what = (IV_ID_PREFIX + MainActivity.NUM_INIT_STREAMS);
				threadHandler.sendMessage(msg1);

			}

		}
	};

	TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {

			timerHandler.sendEmptyMessage(0);

		}
	};

	void loadFromLocalDB() {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_STREAM);

		ArrayList<HashMap<String, String>> records = null;
		if(dbC.isOpen()) {
			dbC.isAvailale();
			records = dbC.retrieveRecords(map);
		}

		if(records.size() < MainActivity.NUM_INIT_STREAMS) {
			return;
		}

		llContainer.removeAllViews();
		llContainer.setPadding(0, 0, 0, 2);

		RelativeLayout rlContainer = new RelativeLayout(activity.context);
		LinearLayout.LayoutParams paramsLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		rlContainer.setLayoutParams(paramsLL);
		llContainer.addView(rlContainer);

		LinearLayout llSections = new LinearLayout(activity.context);
		RelativeLayout.LayoutParams paramsRl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsRl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		llSections.setOrientation(LinearLayout.VERTICAL);
		llSections.setLayoutParams(paramsRl);
		rlContainer.addView(llSections);

		for(int i = MainActivity.NUM_INIT_STREAMS; i < records.size();) {

			if(i == MainActivity.NUM_INIT_STREAMS) {

				map = records.get(i);

				//Last min test case
				if (map == null) {
					break;
				}

				final int idStreamLeft = Integer.parseInt(map.get(MainActivity.DB_COL_SRV_ID));

				HashMap<String, String> mapItems = new HashMap<String, String>();
				mapItems.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_ITEM);
				mapItems.put(MainActivity.DB_COL_FOREIGN_KEY, map.get(MainActivity.DB_COL_ID));
				String _idItem = null;
				if (dbC.isOpen()) {
					dbC.isAvailale();
					_idItem = dbC.retrieveId(mapItems);
					recordsBannerItems = dbC.retrieveRecords(mapItems);
					if (recordsBannerItems.size() > 0) {
						HashMap<String, String> mapItem = recordsBannerItems.get(0);
						bannerMax = recordsBannerItems.size();
						bannerCounter = 0;

						timer = new Timer();
						timer.schedule(timerTask, 0, 5000);

					}
				}

				HashMap<String, String> mapPictures = new HashMap<String, String>();
				mapPictures.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_PICTURE);
				mapPictures.put(MainActivity.DB_COL_FOREIGN_KEY, _idItem);
				ArrayList<HashMap<String, String>> recordsPictures = null;
				if (dbC.isOpen()) {
					dbC.isAvailale();
					recordsPictures = dbC.retrieveRecords(mapPictures);
				}

				String picture = "";
				if (recordsPictures.size() > 0) {
					mapPictures = recordsPictures.get(0);
					picture = mapPictures.get(MainActivity.DB_COL_PATH_ORIG);
				}

				if (picture.length() > 0) {

					if (activity.checkIfExistsInExternalStorage(picture)) {

						try {

							String filePath = MainActivity.STORAGE_PATH + "/" + picture;
							Bitmap bmp = (new ImageProcessingFunctions()).decodeSampledBitmapFromFile(filePath, MainActivity.IMG_DETAIL_MAX_SIZE, MainActivity.IMG_DETAIL_MAX_SIZE);
							Message msg = new Message();
							msg.obj = bmp;
							threadHandler.sendMessage(msg);

						} catch (OutOfMemoryError e) {

						}

					} else {

						thPictDownload = new Thread(this);
						thPictDownload.setName(picture + ";" + (IV_ID_PREFIX + i));
						thPictDownload.start();

					}

				} else {

					Message msg = new Message();
					msg.obj = null;
					//msg.what = (IV_ID_PREFIX + i);
					threadHandler.sendMessage(msg);

				}
				i++;

			} else {

				LinearLayout llRowC = new LinearLayout(activity.context);
				llRowC.setOrientation(LinearLayout.HORIZONTAL);
				paramsLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				paramsLL.bottomMargin = 2;
				llRowC.setLayoutParams(paramsLL);
				llSections.addView(llRowC);

				for (int j = i; j < records.size() && j < i + 3; j++) {

					map = records.get(j);

					//Last min test case
					if (map == null) {
						break;
					}

					final int idStreamLeft = Integer.parseInt(map.get(MainActivity.DB_COL_SRV_ID));

					int rowHeight = (int) (MainActivity.SCREEN_WIDTH / 8);

					RelativeLayout rlRow = new RelativeLayout(activity.context);
					paramsLL = new LinearLayout.LayoutParams(0, rowHeight);
					paramsLL.weight = 2;
					if (j % 3 == 1) {
						paramsLL.setMargins(2, 0, 1, 0);
					} else if (j % 3 == 2) {
						paramsLL.setMargins(1, 0, 1, 0);
					}else {
						paramsLL.setMargins(1, 0, 2, 0);
					}
					rlRow.setLayoutParams(paramsLL);
					rlRow.setBackgroundColor(activity.getResources().getColor(R.color.tile_bg));
					llRowC.addView(rlRow);
					rlRow.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							if (!activity.IS_CLICKED) {

								activity.IS_CLICKED = true;

								FragmentSectionItemsList frag = new FragmentSectionItemsList();
								frag.idStream = idStreamLeft;
								activity.fragMgr.beginTransaction()
										.add(((ViewGroup) getView().getParent()).getId(), frag, MainActivity.SCREEN_SECTION_ITEM_LIST)
										.addToBackStack(MainActivity.SCREEN_SECTION_ITEM_LIST)
										.commit();

							}


						}

					});

					TextView tv = new TextView(activity.context);
					RelativeLayout.LayoutParams rParamsTv = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
					rParamsTv.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
					tv.setLayoutParams(rParamsTv);
					tv.setGravity(Gravity.CENTER);
					tv.setText(map.get(MainActivity.DB_COL_NAME).toUpperCase());
					tv.setTextColor(getResources().getColor(R.color.text_color));
					tv.setTextSize(10);
					tv.setLineSpacing(0.0f, 1.2f);
					rlRow.addView(tv);

				}

				i+=3;
			}

		}

	}
	
	@Override
	public void initUIHandles() {
		// TODO Auto-generated method stub

		llContainer = (LinearLayout)v.findViewById(R.id.ll_container);
		
	}

	@Override
	public void formatUI() {
		// TODO Auto-generated method stub
		activity.app.ENABLE_SYNC = true;
	}

	@Override
	public void storeClassVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initUIListeners() {
		// TODO Auto-generated method stub

	}

	protected Handler threadHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			try {

				Bitmap bmp = (Bitmap) msg.obj;
				if (bmp != null) {

					ImageView iv = (ImageView) v.findViewById(msg.what);
					activity.ivBg.setImageBitmap(bmp);

				} else {

					ImageView iv = (ImageView) v.findViewById(msg.what);
					activity.ivBg.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));

				}

			} catch (IllegalStateException e) {

			}


		}

	};

	@Override
	public void run() {
		// TODO Auto-generated method stub

		Looper.prepare();

		//activity.handlerLoading.sendEmptyMessage(1);

		Thread t = Thread.currentThread();
		String tName = t.getName();
		String[] strArr = tName.split(";");

		String url = MainActivity.UPLOADS + "/" + strArr[0];
		int id = Integer.parseInt(strArr[1]);
		Bitmap bmp = (new ImageProcessingFunctions()).decodeSampledBitmapFromStream(url, MainActivity.IMG_DETAIL_MAX_SIZE, MainActivity.IMG_DETAIL_MAX_SIZE);
		Message msg = new Message();
		msg.obj = bmp;
		msg.what = id;
		threadHandler.sendMessage(msg);

		String filePath = MainActivity.STORAGE_PATH + "/" + strArr[0];
		File file = new File(filePath);
		if(file.exists()) {
			file.delete();
		}

		try {

			FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		//activity.handlerLoading.sendEmptyMessage(0);

	}


	@Override
	public void setRunFlag(Boolean value) {
		// TODO Auto-generated method stub
		RUN_FLAG = value;
	}



}
