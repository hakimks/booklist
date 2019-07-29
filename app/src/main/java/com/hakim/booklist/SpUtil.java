package com.hakim.booklist;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    private SpUtil(){}

    public static final String PREF_NAME = "BooksPrefferences";
    public static final String POSITION = "position";
    public static final String QUERY = "query";

    public static SharedPreferences getPref(Context context){
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getPreferenceString(Context context, String key){
        return getPref(context).getString(key, "");
    }

    public static int getPreferenceInt(Context context, String key){
        return getPref(context).getInt(key, 0);
    }

    public static void setPreferenceString(Context context, String key, String value){
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setPreferenceInt(Context context, String key, int value){
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

}
