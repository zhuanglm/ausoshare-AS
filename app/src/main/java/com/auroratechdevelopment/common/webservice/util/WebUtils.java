package com.auroratechdevelopment.common.webservice.util;

import android.util.Log;

import com.auroratechdevelopment.ausoshare.CustomApplication;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.auroratechdevelopment.common.DebugLogUtil;
import com.auroratechdevelopment.common.webservice.WebServiceConstants;

public class WebUtils {
	
	public static String getURI(String contextRoot){
		
		String uri = String.format("%s/%s", WebServiceConstants.WebHost, contextRoot);
		DebugLogUtil.LogD("Request URI >>> " + uri);

		String language = CustomApplication.getInstance().getLanguage().substring(0,2);
		Log.i("Raymond Language",language);

		return uri;
	}
	
	public static Gson getGsonInstance(){
		return new GsonBuilder()
		.serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
	}

}


