package com.megotechnologies.wedding_morningmist.utilities;

import android.util.Log;

import com.megotechnologies.wedding_morningmist.MainActivity;

public class MLog {
	
	public static void log(String str) {
		
		if(MainActivity.LOG) {
			
			Log.i("mego", str);
			
		}
		
	}
	

}
