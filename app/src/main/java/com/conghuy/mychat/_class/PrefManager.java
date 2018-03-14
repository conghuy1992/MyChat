package com.conghuy.mychat._class;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by maidinh on 6/7/2017.
 */

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // shared pref mode
    private int PRIVATE_MODE = 0;
    private String KEY_USER_LOGIN = "KEY_USER_LOGIN";
    private String KEY_CB_LOGIN = "KEY_CB_LOGIN";
    private String KEY_USER = "KEY_USER";
    private String KEY_PASS = "KEY_PASS";
    private static final String PREF_NAME = "my_chat";

    public PrefManager(Context context) {
        this._context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void ClearData() {
        editor.clear();
    }

    public String get_data_login() {
        return pref.getString(KEY_USER_LOGIN, "");
    }

    public void set_data_login(String s) {
        editor.putString(KEY_USER_LOGIN, s);
        editor.commit();
    }

    public boolean getKEY_CB_LOGIN() {
        return pref.getBoolean(KEY_CB_LOGIN, false);
    }

    public void setKEY_CB_LOGIN(boolean s) {
        editor.putBoolean(KEY_CB_LOGIN, s);
        editor.commit();
    }

    public String getKEY_USER() {
        return pref.getString(KEY_USER, "");
    }

    public void setKEY_USER(String s) {
        editor.putString(KEY_USER, s);
        editor.commit();
    }

    public String getKEY_PASS() {
        return pref.getString(KEY_PASS, "");
    }

    public void setKEY_PASS(String s) {
        editor.putString(KEY_PASS, s);
        editor.commit();
    }
}
