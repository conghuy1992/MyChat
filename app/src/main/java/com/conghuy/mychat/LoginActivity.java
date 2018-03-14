package com.conghuy.mychat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.conghuy.mychat._class.Config;
import com.conghuy.mychat._class.Const;
import com.conghuy.mychat._class.HttpRequest;
import com.conghuy.mychat._class.PrefManager;
import com.conghuy.mychat.dto.HttpCallback;
import com.conghuy.mychat.dto.LoginDto;
import com.conghuy.mychat.dto.StatusLogin;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by maidinh on 5/22/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "LoginActivity";
    private EditText edUsername;
    private EditText edPass;
    private TextView btnRegister;
    private TextView btnForgotPassword;
    private Button btnLogin;
    private Button btnFacebook;
    private Button btnGoogle;
    private Context context;
    private ProgressBar prgressBar;
    private CheckBox cbAutoLogin;
    private PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main_layout);
        context = this;
        prefManager = new PrefManager(context);
        initView();
        actionView();

        test();
    }

    void test() {

    }

    private Handler myHandler = new Handler();
    private Runnable myRunnable = new Runnable() {
        public void run() {
            mIsExit = false;
        }
    };
    private boolean mIsExit;

    void onback() {
        if (!isTaskRoot()) {
            super.onBackPressed();
        } else {
            if (mIsExit) {
                super.onBackPressed();
            } else {
                // press 2 times to exit app feature
                this.mIsExit = true;
                Const.showMsg(context, R.string.back_press);
                myHandler.postDelayed(myRunnable, 2000);
            }
        }
    }

    @Override
    public void onBackPressed() {
        onback();
    }

    void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Const.getMsg(this, R.string.login));
        }


        edUsername = (EditText) findViewById(R.id.edUsername);
        edPass = (EditText) findViewById(R.id.edPass);

        btnForgotPassword = (TextView) findViewById(R.id.btnForgotPassword);
        btnRegister = (TextView) findViewById(R.id.btnRegister);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnGoogle = (Button) findViewById(R.id.btnGoogle);

        prgressBar = (ProgressBar) findViewById(R.id.prgressBar);

        cbAutoLogin = (CheckBox) findViewById(R.id.cbAutoLogin);
        boolean flag = prefManager.getKEY_CB_LOGIN();
        cbAutoLogin.setChecked(flag);
        String username = prefManager.getKEY_USER();
        String password = prefManager.getKEY_PASS();
        edUsername.setText(username);
        edPass.setText(password);
        if (flag && username.length() > 0 && password.length() > 0) actionLogin();
    }

    void actionView() {
        btnLogin.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);

        btnForgotPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);



    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefManager.setKEY_CB_LOGIN(cbAutoLogin.isChecked());
        prefManager.setKEY_USER(edUsername.getText().toString().trim());
        prefManager.setKEY_PASS(edPass.getText().toString().trim());
    }

    StatusLogin checkLogin() {
        String pass = edPass.getText().toString();
        if (edUsername.getText().toString().trim().length() == 0)
            return new StatusLogin(Const.MISSING_INPUT_USERNAME, R.string.missing_user);
        if (pass.length() == 0)
            return new StatusLogin(Const.MISSING_INPUT_PASSWORD, R.string.missing_pass);
        return new StatusLogin(Const.REGISTER_SUCCESS, 0);
    }

    void showLoading() {
        prgressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
    }

    void hideLoading() {
        prgressBar.setVisibility(View.GONE);
        btnLogin.setEnabled(true);
    }

    void doLogin() {
        String name = edUsername.getText().toString().trim();
        String pass = edPass.getText().toString();
        HttpRequest.Login(Const.LOGIN, name, pass, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                hideLoading();
                Log.d(TAG, "response:" + response);

                if (response.startsWith("{")) {
                    LoginDto obj = new Gson().fromJson(response, LoginDto.class);
                    int success = obj.getSuccess();
                    if (success == 1) {
                        // add to pref
                        new PrefManager(context).set_data_login(response);

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra(Const.LOGIN_DTO, obj);
                        startActivity(intent);
                        finish();
                    } else {
                        Const.showDialog(context, obj.getMessage());
                    }
                } else {
                    Const.showDialog(context, Const.getMsg(context, R.string.unknown_error));
                }

            }

            @Override
            public void onFail(String error) {
                hideLoading();
                Log.d(TAG, "response:" + error);
                Const.showDialog(context, error);
            }
        });
    }

    void actionLogin() {
        StatusLogin obj = checkLogin();
        if (obj.getStatus() == Const.REGISTER_SUCCESS) {
            // login success
            showLoading();
            doLogin();
        } else {
            Const.showMsg(context, obj.getStringId());
        }
    }

    void actionRegister() {
        startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            actionLogin();
        } else if (v == btnFacebook) {
            Const.showMsg(context, "Facebook");
        } else if (v == btnGoogle) {
            Const.showMsg(context, "Google");
        } else if (v == btnForgotPassword) {
            Const.showMsg(context, "Forgot pass");
        } else if (v == btnRegister) {
            actionRegister();
        }
    }
}
