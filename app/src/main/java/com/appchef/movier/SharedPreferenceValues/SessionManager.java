package com.appchef.movier.SharedPreferenceValues;

import android.text.TextUtils;

public class SessionManager {

    private static String accessToken;

    public static String getUserToken() {
        if (TextUtils.isEmpty(accessToken)) {
            accessToken = SharedPreferencesManager.getUserToken();
        }
        return accessToken;
    }

    public static void setUserToken(String userToken) {
        SessionManager.accessToken = userToken;
        SharedPreferencesManager.setUserToken(userToken);
    }
}
