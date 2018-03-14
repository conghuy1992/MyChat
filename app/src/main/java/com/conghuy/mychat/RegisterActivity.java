package com.conghuy.mychat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.conghuy.mychat._class.Const;
import com.conghuy.mychat._class.HttpRequest;
import com.conghuy.mychat.dto.HttpCallback;
import com.conghuy.mychat.dto.StatusAPI;
import com.conghuy.mychat.dto.StatusLogin;
import com.google.gson.Gson;


/**
 * Created by maidinh on 5/22/2017.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout btnDisplayPass;
    private FrameLayout btnDisplayConfirmPass;
    private EditText edPass;
    private EditText edConfirmPass;
    private EditText edUsername;
    private EditText edEmail;
    private boolean isDisplayPass = true;
    private boolean isDisplayConfirmPass = true;
    private Button btnRegister;
    private Context context;
    private String TAG = "RegisterActivity";
    private ProgressBar prgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main_layout);
        context = this;
        initView();
        actionView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

    void initView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setTitle(Const.getMsg(this, R.string.register));
        }

        btnDisplayPass = (FrameLayout) findViewById(R.id.btnDisplayPass);
        btnDisplayConfirmPass = (FrameLayout) findViewById(R.id.btnDisplayConfirmPass);

        edUsername = (EditText) findViewById(R.id.edUsername);
        edEmail = (EditText) findViewById(R.id.edEmail);
        edPass = (EditText) findViewById(R.id.edPass);
        edConfirmPass = (EditText) findViewById(R.id.edConfirmPass);

        btnRegister = (Button) findViewById(R.id.btnRegister);

        prgressBar = (ProgressBar) findViewById(R.id.prgressBar);

    }

    void actionView() {
        btnDisplayPass.setOnClickListener(this);
        btnDisplayConfirmPass.setOnClickListener(this);
        btnRegister.setOnClickListener(this);


    }

    void displayPass() {
        if (isDisplayPass) {
            edPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isDisplayPass = false;
        } else {
            edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isDisplayPass = true;
        }
    }

    void displayConfirmPass() {
        if (isDisplayConfirmPass) {
            edConfirmPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isDisplayConfirmPass = false;
        } else {
            edConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isDisplayConfirmPass = true;
        }
    }


    StatusLogin checkLogin() {
        String pass = edPass.getText().toString();
        String confirmPass = edConfirmPass.getText().toString();
        if (edUsername.getText().toString().trim().length() == 0)
            return new StatusLogin(Const.MISSING_INPUT_USERNAME, R.string.missing_user);
        if (edEmail.getText().toString().trim().length() == 0)
            return new StatusLogin(Const.MISSING_INPUT_EMAIL, R.string.missing_email);
        if (pass.length() == 0)
            return new StatusLogin(Const.MISSING_INPUT_PASSWORD, R.string.missing_pass);
        if (confirmPass.length() == 0)
            return new StatusLogin(Const.MISSING_INPUT_CONFIRM_PASSWORD, R.string.missing_confirm_pass);
        if (!pass.equals(confirmPass))
            return new StatusLogin(Const.MISSING_INPUT_CONFIRM_PASS_DIFFERRENT, R.string.missing_confirm_pass_incorrect);
        return new StatusLogin(Const.REGISTER_SUCCESS, 0);
    }

    void showLoading() {
        prgressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);
    }

    void hideLoading() {
        prgressBar.setVisibility(View.GONE);
        btnRegister.setEnabled(true);
    }

    void actionRegister() {
        StatusLogin obj = checkLogin();
        if (obj.getStatus() == Const.REGISTER_SUCCESS) {
            // reegister success
            callAPI();
        } else {
            hideLoading();
            Const.showMsg(context, obj.getStringId());
        }
    }

    void callAPI() {
        String name = edUsername.getText().toString().trim();
        String pass = edPass.getText().toString();
        String email = edEmail.getText().toString().trim();
        HttpRequest.Register(Const.REGISTER, name, pass, email, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                hideLoading();
                Log.d(TAG, "response:" + response);

                if (response.startsWith("{")) {
                    StatusAPI obj = new Gson().fromJson(response, StatusAPI.class);
                    int success = obj.getSuccess();
                    if (success == 1) {
                        // go to chat
//                    Const.showDialog(context, obj.getMessage());
                        Const.showMsg(context, obj.getMessage());

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


    @Override
    public void onClick(View v) {
        if (v == btnDisplayPass) {
            displayPass();
        } else if (v == btnDisplayConfirmPass) {
            displayConfirmPass();
        } else if (v == btnRegister) {
            showLoading();
            actionRegister();
        }
    }
}
