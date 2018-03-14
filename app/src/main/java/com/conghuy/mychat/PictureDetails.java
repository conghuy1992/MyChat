package com.conghuy.mychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.conghuy.mychat._class.Const;
import com.conghuy.mychat.customUI.ImageViewZoomSupport;

/**
 * Created by Huy on 24/6/2017.
 */

public class PictureDetails extends AppCompatActivity {
    ImageViewZoomSupport iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_details_layout);
        init();

    }

    void init(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Const.getMsg(this, R.string.details));
        }
        iv = (ImageViewZoomSupport) findViewById(R.id.iv);
        Intent intent = getIntent();
        String url = intent.getStringExtra(Const.URL_AV);
        if (url != null && url.length() > 0) Const.setImage(this, iv, Const.ROOT_URL + url);
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
}
