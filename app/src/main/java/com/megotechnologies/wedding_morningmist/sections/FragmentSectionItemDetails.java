package com.megotechnologies.wedding_morningmist.sections;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.TypedValue;
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
import com.megotechnologies.wedding_morningmist.ZoomImageActivity;
import com.megotechnologies.wedding_morningmist.interfaces.ZCFragmentLifecycle;
import com.megotechnologies.wedding_morningmist.interfaces.ZCRunnable;
import com.megotechnologies.wedding_morningmist.utilities.ImageProcessingFunctions;
import com.megotechnologies.wedding_morningmist.utilities.MLog;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FragmentSectionItemDetails extends FragmentMeta implements ZCFragmentLifecycle, ZCRunnable {

    public int idStream, idItem;

    Thread thPictDownload;
    int IV_ID_PREFIX = 4000;
    LinearLayout llContainer;

    LinearLayout llShare;
    TextView tvShare;

    String zoomableStr = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        v = inflater.inflate(R.layout.fragment_section_itemdetails, container, false);

        storeClassVariables();
        initUIHandles();
        initUIListeners();
        formatUI();

        MLog.log("Listing " + idStream + "," + idItem);

        try {
            loadFromLocalDB();
        } catch (IllegalStateException e) {

        }

        return v;
    }

    @Override
    public void storeClassVariables() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initUIHandles() {
        // TODO Auto-generated method stub
        llContainer = (LinearLayout) v.findViewById(R.id.ll_container);
    }

    @Override
    public void initUIListeners() {
        // TODO Auto-generated method stub

    }

    @Override
    public void formatUI() {
        // TODO Auto-generated method stub
        activity.app.ENABLE_SYNC = false;
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
        activity.app.ENABLE_SYNC = true;
    }

    void loadFromLocalDB() {


        String streamName = "";

        ArrayList<HashMap<String, String>> recordsItems = null;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_STREAM);
        map.put(MainActivity.DB_COL_SRV_ID, String.valueOf(idStream));
        String _idStream = null;
        if (dbC.isOpen()) {
            dbC.isAvailale();
            _idStream = dbC.retrieveId(map);
            recordsItems = dbC.retrieveRecords(map);
        }
        HashMap<String, String> mapStream = recordsItems.get(0);
        streamName = mapStream.get(MainActivity.DB_COL_NAME);

        map = new HashMap<String, String>();
        map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_ITEM);
        map.put(MainActivity.DB_COL_FOREIGN_KEY, _idStream);
        map.put(MainActivity.DB_COL_SRV_ID, String.valueOf(idItem));
        recordsItems = null;
        if (dbC.isOpen()) {
            dbC.isAvailale();
            recordsItems = dbC.retrieveRecords(map);
        }

        llContainer.removeAllViews();
        llContainer.setBackgroundColor(getResources().getColor(R.color.white));

        for (int i = 0; i < recordsItems.size(); i++) {

            map = recordsItems.get(i);
            String _idItem = map.get(MainActivity.DB_COL_ID);
            final String title = map.get(MainActivity.DB_COL_TITLE);
            final String subtitle = map.get(MainActivity.DB_COL_SUB);
            String content = map.get(MainActivity.DB_COL_CONTENT);
            Long ts = Long.parseLong(map.get(MainActivity.DB_COL_TIMESTAMP));
            Date tsDate = new java.util.Date(ts);
            String timestamp = new SimpleDateFormat("dd/MM/yyyy hh:mma").format(tsDate);

            // Get handles for pictures, urls, locations, contacts & attachments

            map = new HashMap<String, String>();
            map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_PICTURE);
            map.put(MainActivity.DB_COL_FOREIGN_KEY, _idItem);
            ArrayList<HashMap<String, String>> recordsPictures = null;
            if (dbC.isOpen()) {
                dbC.isAvailale();
                recordsPictures = dbC.retrieveRecords(map);
            }

            map = new HashMap<String, String>();
            map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_URL);
            map.put(MainActivity.DB_COL_FOREIGN_KEY, _idItem);
            ArrayList<HashMap<String, String>> recordsUrls = null;
            if (dbC.isOpen()) {
                dbC.isAvailale();
                recordsUrls = dbC.retrieveRecords(map);
            }

            map = new HashMap<String, String>();
            map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_ATTACHMENT);
            map.put(MainActivity.DB_COL_FOREIGN_KEY, _idItem);
            ArrayList<HashMap<String, String>> recordsAttachments = null;
            if (dbC.isOpen()) {
                dbC.isAvailale();
                recordsAttachments = dbC.retrieveRecords(map);
            }

            map = new HashMap<String, String>();
            map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_LOCATION);
            map.put(MainActivity.DB_COL_FOREIGN_KEY, _idItem);
            ArrayList<HashMap<String, String>> recordsLocations = null;
            if (dbC.isOpen()) {
                dbC.isAvailale();
                recordsLocations = dbC.retrieveRecords(map);
            }

            map = new HashMap<String, String>();
            map.put(MainActivity.DB_COL_TYPE, MainActivity.DB_RECORD_TYPE_CONTACT);
            map.put(MainActivity.DB_COL_FOREIGN_KEY, _idItem);
            ArrayList<HashMap<String, String>> recordsContacts = null;
            if (dbC.isOpen()) {
                dbC.isAvailale();
                recordsContacts = dbC.retrieveRecords(map);
            }

            int imgHeight = ((MainActivity.SCREEN_WIDTH * 3) / 4);
            int imgMargin = (MainActivity.SCREEN_WIDTH / 16);
            int imgWidth = (activity.SCREEN_WIDTH - imgMargin * 2);

            int headHeight = (int) ((activity.SCREEN_HEIGHT / 10));

            TextView tvHead = new TextView(activity.context);
            tvHead.setText(title.toUpperCase());
            RelativeLayout.LayoutParams paramsLLHead = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tvHead.setLayoutParams(paramsLLHead);
            tvHead.setTextColor(getResources().getColor(R.color.text_color));
            tvHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (MainActivity.TEXT_SIZE_TILE));
            tvHead.setPadding(imgMargin, imgMargin + headHeight / 10, imgMargin, 0);
            tvHead.setEllipsize(TextUtils.TruncateAt.END);
            tvHead.setGravity(Gravity.CENTER);
            llContainer.addView(tvHead);

            ImageView ivProduct = new ImageView(activity.context);

            if (recordsPictures.size() > 0) {

                LinearLayout lProductC = new LinearLayout(activity.context);
                LinearLayout.LayoutParams paramsProductC = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsProductC.setMargins(0, imgMargin, 0, 0);
                lProductC.setLayoutParams(paramsProductC);
                lProductC.setOrientation(LinearLayout.VERTICAL);
                llContainer.addView(lProductC);

                RelativeLayout rBorderC = new RelativeLayout(activity.context);
                LinearLayout.LayoutParams paramsBorderC = new LinearLayout.LayoutParams(imgWidth, imgHeight);
                paramsBorderC.setMargins(imgMargin, 0, imgMargin, 0);
                rBorderC.setLayoutParams(paramsBorderC);
                rBorderC.setBackgroundColor(getResources().getColor(R.color.tile_color));
                lProductC.addView(rBorderC);

                RelativeLayout.LayoutParams paramsIvProduct = null;
                paramsIvProduct = new RelativeLayout.LayoutParams(imgWidth - 2, imgHeight - 2);
                paramsIvProduct.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                paramsIvProduct.setMargins(1, 1, 1, 1);
                ivProduct.setPadding(2, 2, 2, 2);
                ivProduct.setLayoutParams(paramsIvProduct);
                ivProduct.setId(IV_ID_PREFIX + i);
                ivProduct.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivProduct.setImageDrawable(getResources().getDrawable(R.drawable.cover));
                try {
                    ivProduct.setCropToPadding(true);
                } catch (NoSuchMethodError e) {

                }
                rBorderC.addView(ivProduct);

            }

            TextView tvDesc = new TextView(activity.context);
            tvDesc.setTextColor(getResources().getColor(R.color.text_color));
            tvDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
            tvDesc.setText(subtitle);
            tvDesc.setPadding(imgMargin, imgMargin, imgMargin, 0);
            tvDesc.setLineSpacing(0.0f, 1.2f);
            llContainer.addView(tvDesc);

            String picture = "";

            if (recordsPictures.size() > 0) {

                map = recordsPictures.get(0);
                picture = map.get(MainActivity.DB_COL_PATH_ORIG);

                if (picture.length() > 0) {

                    MLog.log("Picture = " + picture);

                    // Create the container linear layout for the picture
                    if (activity.checkIfExistsInExternalStorage(picture)) {

                        final String filePath = MainActivity.STORAGE_PATH + "/" + picture;
                        Bitmap bmp = (new ImageProcessingFunctions()).decodeSampledBitmapFromFile(filePath, MainActivity.IMG_DETAIL_MAX_SIZE, MainActivity.IMG_DETAIL_MAX_SIZE);
                        Message msg = new Message();
                        msg.obj = bmp;
                        msg.what = (IV_ID_PREFIX + i);
                        MLog.log("Sending message to " + msg.what);
                        threadHandler.sendMessage(msg);

                        ivProduct.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub

                                activity.app.ISGOINGOUTOFAPP = true;
                                Intent intent = new Intent(activity, ZoomImageActivity.class);
                                intent.putExtra(ZoomImageActivity.IMAGE_INTENT_KEY, filePath);
                                intent.putExtra(ZoomImageActivity.IMAGE_INTENT_TRYOUT, false);
                                intent.putExtra(ZoomImageActivity.IMAGE_INTENT_ZOOMABLE, zoomableStr);
                                startActivity(intent);

                            }

                        });

                    } else {

                        thPictDownload = new Thread(this);
                        thPictDownload.setName(picture + ";" + (IV_ID_PREFIX + i));
                        thPictDownload.start();

                    }

                }

            }

            llShare = new LinearLayout(activity.context);
            llShare.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams llShareParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llShare.setLayoutParams(llShareParams);
            llContainer.addView(llShare);

            tvShare = new TextView(activity.context);
            LinearLayout.LayoutParams tvShareParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tvShare.setLayoutParams(tvShareParams);
            tvShare.setTextColor(getResources().getColor(R.color.text_color));
            tvShare.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TITLE));
            tvShare.setText(Character.toString(MainActivity.FONT_CHAR_SHARE));
            tvShare.setPadding(imgMargin, MainActivity.SPACING, 0, MainActivity.SPACING);
            tvShare.setTypeface(activity.tf);
            llShare.addView(tvShare);

            String iconStr = "";

            if (recordsUrls.size() > 0) {

                iconStr += (" " + Character.toString(MainActivity.FONT_CHAR_URL));

            }

            if (recordsContacts.size() > 0) {

                iconStr += (" " + Character.toString(MainActivity.FONT_CHAR_CONTACT));

            }

            if (recordsLocations.size() > 0) {

                iconStr += (" " + Character.toString(MainActivity.FONT_CHAR_LOCATION));

            }

            if (recordsAttachments.size() > 0) {

                iconStr += (" " + Character.toString(MainActivity.FONT_CHAR_ATTACHMENT));

            }

            // Insert indicators

            TextView tvRightIndicator = null;
            if (iconStr.length() > 0) {

                tvRightIndicator = new TextView(activity.context);
                tvRightIndicator.setText(iconStr);
                tvRightIndicator.setTextColor(getResources().getColor(R.color.text_color));
                tvRightIndicator.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
                tvRightIndicator.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                tvRightIndicator.setEllipsize(TextUtils.TruncateAt.END);
                tvRightIndicator.setSingleLine();
                tvRightIndicator.setTypeface(activity.tf);
                tvRightIndicator.setGravity(Gravity.RIGHT);
                llContainer.addView(tvRightIndicator);

            }

            content = content.replace("<br />", "\n");
            TextView tvContent = new TextView(activity.context);
            tvContent.setTextColor(getResources().getColor(R.color.text_color));
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
            tvContent.setText(timestamp + " - " + content);
            tvContent.setPadding(imgMargin, MainActivity.SPACING, imgMargin, MainActivity.SPACING * 2);
            tvContent.setLineSpacing(0.0f, 1.5f);
            llContainer.addView(tvContent);

            final String picUrl = picture;
            final String contentStr = content;
            llShare.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    activity.app.ISGOINGOUTOFAPP = true;

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
                    String msg = "";
                    msg += (title + "\n\n");
                    msg += (subtitle + "\n\n");
                    if (picUrl.length() > 0) {
                        msg += ("Picture: " + MainActivity.UPLOADS + "/" + picUrl + "\n\n");
                    }
                    msg += (contentStr + "\n\n");
                    msg += (MainActivity.SHARE_ITEM_POSTFIX + "\n\n");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
                    startActivity(Intent.createChooser(sharingIntent, title));

                }

            });

            for (int j = 0; j < recordsUrls.size(); j++) {

                map = recordsUrls.get(j);

                String url = map.get(MainActivity.DB_COL_URL);
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                final String link = url;
                LinearLayout llButton = new LinearLayout(activity.context);
                llButton.setOrientation(LinearLayout.HORIZONTAL);
                llButton.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams llButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llButtonParams.setMargins(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                llButton.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.dark_shadow_background));
                llButton.setLayoutParams(llButtonParams);
                llButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        activity.app.ISGOINGOUTOFAPP = true;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(browserIntent);

                    }

                });

                TextView tvIcon = new TextView(activity.context);
                LinearLayout.LayoutParams tvIconParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvIconParams.weight = 1;
                tvIcon.setLayoutParams(tvIconParams);
                tvIcon.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                tvIcon.setTextColor(getResources().getColor(R.color.white));
                tvIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
                tvIcon.setText(Character.toString(MainActivity.FONT_CHAR_URL));
                tvIcon.setTypeface(activity.tf);
                tvIcon.setGravity(Gravity.CENTER);
                llButton.addView(tvIcon);

                TextView tvText = new TextView(activity.context);
                LinearLayout.LayoutParams tvTextParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvTextParams.weight = 6;
                tvText.setLayoutParams(tvTextParams);
                tvText.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                tvText.setTextColor(getResources().getColor(R.color.white));
                tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
                tvText.setText(map.get(MainActivity.DB_COL_CAPTION));
                tvText.setGravity(Gravity.CENTER);
                tvText.setLines(2);
                llButton.addView(tvText);

                llContainer.addView(llButton);

            }

            for (int j = 0; j < recordsLocations.size(); j++) {

                map = recordsLocations.get(j);

                final String location = MainActivity.MAPS_PREFIX + map.get(MainActivity.DB_COL_LOCATION).replaceAll(" ", "");
                LinearLayout llButton = new LinearLayout(activity.context);
                llButton.setOrientation(LinearLayout.HORIZONTAL);
                llButton.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams llButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llButtonParams.setMargins(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                llButton.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.dark_shadow_background));
                llButton.setLayoutParams(llButtonParams);
                llButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        activity.app.ISGOINGOUTOFAPP = true;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(location));
                        startActivity(browserIntent);

                    }

                });

                TextView tvIcon = new TextView(activity.context);
                LinearLayout.LayoutParams tvIconParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvIconParams.weight = 1;
                tvIcon.setLayoutParams(tvIconParams);
                tvIcon.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                tvIcon.setTextColor(getResources().getColor(R.color.white));
                tvIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
                tvIcon.setText(Character.toString(MainActivity.FONT_CHAR_LOCATION));
                tvIcon.setTypeface(activity.tf);
                tvIcon.setGravity(Gravity.CENTER);
                llButton.addView(tvIcon);


                TextView tvText = new TextView(activity.context);
                LinearLayout.LayoutParams tvTextParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvTextParams.weight = 6;
                tvText.setLayoutParams(tvTextParams);
                tvText.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                tvText.setTextColor(getResources().getColor(R.color.white));
                tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
                tvText.setText(map.get(MainActivity.DB_COL_CAPTION));
                tvText.setGravity(Gravity.CENTER);
                tvText.setLines(2);
                llButton.addView(tvText);

                llContainer.addView(llButton);

            }

            for (int j = 0; j < recordsContacts.size(); j++) {

                map = recordsContacts.get(j);

                final String name = map.get(MainActivity.DB_COL_NAME);
                final String email = map.get(MainActivity.DB_COL_EMAIL);
                final String phone = map.get(MainActivity.DB_COL_PHONE);

                LinearLayout llButton = new LinearLayout(activity.context);
                llButton.setOrientation(LinearLayout.HORIZONTAL);
                llButton.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams llButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llButtonParams.setMargins(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                llButton.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.dark_shadow_background));
                llButton.setLayoutParams(llButtonParams);
                llButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        activity.app.ISGOINGOUTOFAPP = true;

                        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email)
                                .putExtra(ContactsContract.Intents.Insert.NAME, name)
                                .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                                .putExtra(ContactsContract.Intents.Insert.PHONE, phone)
                                .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                        startActivity(intent);

                    }

                });

                TextView tvIcon = new TextView(activity.context);
                LinearLayout.LayoutParams tvIconParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvIconParams.weight = 1;
                tvIcon.setLayoutParams(tvIconParams);
                tvIcon.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                tvIcon.setTextColor(getResources().getColor(R.color.white));
                tvIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
                tvIcon.setText(Character.toString(MainActivity.FONT_CHAR_CONTACT));
                tvIcon.setTypeface(activity.tf);
                tvIcon.setGravity(Gravity.CENTER);
                llButton.addView(tvIcon);

                TextView tvText = new TextView(activity.context);
                LinearLayout.LayoutParams tvTextParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvTextParams.weight = 6;
                tvText.setLayoutParams(tvTextParams);
                tvText.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                tvText.setTextColor(getResources().getColor(R.color.white));
                tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
                tvText.setText(map.get(MainActivity.DB_COL_NAME));
                tvText.setGravity(Gravity.CENTER);
                tvText.setLines(2);
                llButton.addView(tvText);

                llContainer.addView(llButton);


            }


            for (int j = 0; j < recordsAttachments.size(); j++) {

                map = recordsAttachments.get(j);

                String url = map.get(MainActivity.DB_COL_URL);
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                final String link = url;
                LinearLayout llButton = new LinearLayout(activity.context);
                llButton.setOrientation(LinearLayout.HORIZONTAL);
                llButton.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams llButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llButtonParams.setMargins(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                llButton.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.dark_shadow_background));
                llButton.setLayoutParams(llButtonParams);
                llButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        activity.app.ISGOINGOUTOFAPP = true;

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(browserIntent);

                    }

                });

                TextView tvIcon = new TextView(activity.context);
                LinearLayout.LayoutParams tvIconParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvIconParams.weight = 1;
                tvIcon.setLayoutParams(tvIconParams);
                tvIcon.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                tvIcon.setTextColor(getResources().getColor(R.color.white));
                tvIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
                tvIcon.setText(Character.toString(MainActivity.FONT_CHAR_ATTACHMENT));
                tvIcon.setTypeface(activity.tf);
                tvIcon.setGravity(Gravity.CENTER);
                llButton.addView(tvIcon);

                TextView tvText = new TextView(activity.context);
                LinearLayout.LayoutParams tvTextParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                tvTextParams.weight = 6;
                tvText.setLayoutParams(tvTextParams);
                tvText.setPadding(MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING, MainActivity.SPACING);
                tvText.setTextColor(getResources().getColor(R.color.white));
                tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (MainActivity.TEXT_SIZE_TILE));
                tvText.setText(map.get(MainActivity.DB_COL_CAPTION));
                tvText.setGravity(Gravity.CENTER);
                tvText.setLines(2);
                llButton.addView(tvText);

                zoomableStr = link;

            }

        }


    }

    protected Handler displayHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {

                case 2:

                    loadFromLocalDB();

                    break;

                default:
                    break;
            }


        }

    };

    protected Handler threadHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            if (msg.what == -1) {


            } else {

                Bitmap bmp = (Bitmap) msg.obj;
                if (bmp != null) {

                    MLog.log("Displaying = " + msg.what);

                    ImageView iv = (ImageView) v.findViewById(msg.what);
                    iv.setImageBitmap(bmp);

                } else {

                    ImageView iv = (ImageView) v.findViewById(msg.what);
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.cover));

                }

            }

        }

    };

    @Override
    public void run() {
        // TODO Auto-generated method stub
        Looper.prepare();

        Thread t = Thread.currentThread();


        String tName = t.getName();
        String[] strArr = tName.split(";");

        String url = MainActivity.UPLOADS + "/" + strArr[0];
        Bitmap bmp = (new ImageProcessingFunctions()).decodeSampledBitmapFromStream(url, MainActivity.IMG_DETAIL_MAX_SIZE, MainActivity.IMG_DETAIL_MAX_SIZE);
        if (strArr.length > 1) {

            int id = Integer.parseInt(strArr[1]);
            Message msg = new Message();
            msg.obj = bmp;
            msg.what = id;
            MLog.log("Sending message to " + id);
            threadHandler.sendMessage(msg);

        } else {

            Message msg = new Message();
            msg.what = -1;
            MLog.log("Sending message to " + -1);
            threadHandler.sendMessage(msg);

        }

        String filePath = MainActivity.STORAGE_PATH + "/" + strArr[0];
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        try {

            FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setRunFlag(Boolean value) {
        // TODO Auto-generated method stub

    }

}
