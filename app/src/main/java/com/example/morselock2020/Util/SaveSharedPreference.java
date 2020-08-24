package com.example.morselock2020.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_FIRST = "first";
    static final String PREF_MORSE = "morse_code";
    static final String PREF_PW = "pw";
    static final String PREF_TIME = "press_time";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    //-- set --//
    public static void setIsFirst(Context context, boolean isFirst) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(PREF_FIRST, isFirst);
        editor.apply();
    }

    public static void setMorseCode(Context context, final String morse) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_MORSE, morse);
        editor.apply();
    }

    public static void setPW(Context context, final String pw) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_PW, pw);
        editor.apply();
    }

    public static void setPressTime(Context context, float time) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putFloat(PREF_TIME, time);
        editor.apply();
    }

    //-- get --//
    public static boolean getPrefFirst(Context context) {
        return getSharedPreferences(context).getBoolean(PREF_FIRST, true);
    }

    public static String getPrefMorse(Context context) {
        return getSharedPreferences(context).getString(PREF_MORSE, "");
    }

    public static float getPrefTime(Context context) {
        return getSharedPreferences(context).getFloat(PREF_TIME, 0);
    }

    // 모든 정보 삭제
    public static void Clear(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }
}
