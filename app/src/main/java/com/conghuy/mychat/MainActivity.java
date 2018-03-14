package com.conghuy.mychat;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.conghuy.mychat._class.Config;
import com.conghuy.mychat._class.Const;
import com.conghuy.mychat._class.HttpRequest;
import com.conghuy.mychat._class.PrefManager;
import com.conghuy.mychat.adapters.ViewPagerAdapter;
import com.conghuy.mychat.dto.HttpCallback;
import com.conghuy.mychat.dto.LoginDto;
import com.conghuy.mychat.fragments.FragmentChat;
import com.conghuy.mychat.fragments.FragmentProfile;
import com.conghuy.mychat.fragments.FragmentUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {
    private MainActivity in = null;
    private String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Handler myHandler = new Handler();
    private Runnable myRunnable = new Runnable() {
        public void run() {
            mIsExit = false;
        }
    };
    private boolean mIsExit;
    private Context context;
    private LoginDto loginDto;
    private PrefManager prefManager;
    private int[] tabIcons = {
            R.drawable.ic_chat_white_36dp,
            R.drawable.ic_group_white_36dp,
            R.drawable.ic_profile
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    void initView() {
        in = this;

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
//            getSupportActionBar().setElevation(0);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//            getSupportActionBar().setDisplayShowHomeEnabled(false);
//            getSupportActionBar().setTitle(Const.getMsg(context, R.string.login));
        }

        context = this;
        prefManager = new PrefManager(context);
        loginDto = new Gson().fromJson(prefManager.get_data_login(), LoginDto.class);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        FCM();
    }

    private void setupTabIcons() {

//        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabOne.setText("ONE");
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_chat_white_36dp, 0, 0);
//        tabLayout.getTabAt(0).setCustomView(tabOne);
//
//        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabTwo.setText("TWO");
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_group_white_36dp, 0, 0);
//        tabLayout.getTabAt(1).setCustomView(tabTwo);

        int n = tabIcons.length;
        for (int i = 0; i < n; i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentChat(loginDto), "");
        adapter.addFragment(new FragmentUser(loginDto), "");
        adapter.addFragment(new FragmentProfile(in, loginDto), "");
//        adapter.addFragment(new FragmentTab(), "FOUR");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }


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

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.ROOM_NO = 0;
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
//        NotificationUtils.clearNotifications(getApplicationContext());
    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.d(TAG, "Firebase reg id: " + regId);
        // update regId


        if (!TextUtils.isEmpty(regId)) {
//            txtRegId.setText("Firebase Reg Id: " + regId);
            Log.d(TAG, "Firebase Reg Id: " + regId);
            HttpRequest.updateKeyFCM(Const.UPDATE_KEY_FCM, loginDto.getUser_id(), regId, new HttpCallback() {
                @Override
                public void onSuccess(String response) {
                    Log.d(TAG, "updateKeyFCM response:" + response);
                }

                @Override
                public void onFail(String error) {
                    Log.d(TAG, "updateKeyFCM onFail");
                }
            });
        } else {
            Log.d(TAG, "Firebase Reg Id is not received yet!");
        }

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    void FCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    if (message == null) message = "";
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "message:" + message);
                }
            }
        };

        displayFirebaseRegId();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_logout) {
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    private final int MY_PERMISSIONS_REQUEST_CODE = 1;

    public boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void setPermissions() {
        String[] requestPermission = new String[]{
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this, requestPermission, MY_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSIONS_REQUEST_CODE) {
            return;
        }
        boolean isGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }
        if (isGranted) {
            if (FragmentProfile.fm != null)
                FragmentProfile.fm.choosePicture();
        } else {
            Const.showMsg(this, R.string.permission_denied);
        }
    }
}
