package com.appchef.movier.SharedPreferenceValues;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private SharedPreferencesManager() {
    }

    private static SharedPreferences getInstance() {
        if (sharedPreferences == null) {
            sharedPreferences = BaseApplication.getInstance().getSharedPreferences(Constants.TINDER_APP, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    static String getUserToken() {
        if (sharedPreferences == null)
            sharedPreferences = getInstance();
        return sharedPreferences.getString(Constants.ACCESS_TOKEN, null);
    }

    static void setUserToken(String userToken) {
        if (sharedPreferences == null)
            sharedPreferences = getInstance();
        editor = sharedPreferences.edit();
        editor.remove(Constants.ACCESS_TOKEN);
        editor.putString(Constants.ACCESS_TOKEN, userToken);
        editor.apply();
    }
}
