package com.example.travelapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
//    public static final String PREFERENCES_NAME = "main_data";
    //    public static final String PREFERENCES_NAME = "complete_data";
    //    public static final String PREFERENCES_NAME = "user_data";

    private static final String DEFAULT_VALUE_STRING = " ";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;

    private static SharedPreferences getPreferences(String PREFERENCES_NAME, Context context) {

        return context.getSharedPreferences(PREFERENCES_NAME, context.MODE_PRIVATE);

    }

    private static SharedPreferences getNewPreferences(Context context) {

        return context.getSharedPreferences("complete_data", context.MODE_PRIVATE);

    }

    // String 값 저장
    public static void setString(String PREFERENCES_NAME, Context context, String key, String value){
        SharedPreferences prefs = getPreferences(PREFERENCES_NAME, context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setNewString(Context context, String key, String value){
        SharedPreferences prefs = getNewPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String PREFERENCES_NAME, Context context, String key) {

        SharedPreferences prefs = getPreferences(PREFERENCES_NAME, context);

        boolean value = prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN);

        return value;

    }


    // String 값 로드
    public static String getString(String PREFERENCES_NAME, Context context, String key){
        SharedPreferences prefs = getPreferences(PREFERENCES_NAME, context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        return value;
    }

    public static String getNewString(Context context, String key){
        SharedPreferences prefs = getNewPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        return value;
    }



    // 키 값 삭제
    public static void removeKey(String PREFERENCES_NAME, Context context, String key){
        SharedPreferences prefs = getPreferences(PREFERENCES_NAME, context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void newRemoveKey(Context context, String key){
        SharedPreferences prefs = getNewPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
    }

    // 모든 저장 데이터 삭제
    public static void clear(String PREFERENCES_NAME, Context context){
        SharedPreferences prefs = getPreferences(PREFERENCES_NAME, context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public static void newClear(Context context){
        SharedPreferences prefs = getNewPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
