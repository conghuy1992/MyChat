package com.conghuy.mychat.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.conghuy.mychat.LoginActivity;
import com.conghuy.mychat.MainActivity;
import com.conghuy.mychat.R;
import com.conghuy.mychat._class.Const;
import com.conghuy.mychat._class.PrefManager;
import com.conghuy.mychat._class.UpLoadFile;
import com.conghuy.mychat.dto.LoginDto;
import com.conghuy.mychat.dto.StatusAPI;
import com.conghuy.mychat.interfaces.UploadInterfaces;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by maidinh on 6/23/2017.
 */

public class FragmentProfile extends Fragment implements View.OnClickListener {
    public static FragmentProfile fm = null;
    private ImageView ivAvatar, ivPicture;
    private String TAG = "FragmentProfile";
    private MainActivity in;
    private LoginDto loginDto;
    private ProgressBar progressBar;
    private TextView tvName;
    private Button btnLogout;

    public FragmentProfile() {
    }

    @SuppressLint("ValidFragment")
    public FragmentProfile(MainActivity in, LoginDto loginDto) {
        this.in = in;
        this.loginDto = loginDto;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        fm = this;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_layout, container, false);
        init(v);
        return v;
    }

    void init(View v) {
        btnLogout = (Button) v.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        ivAvatar = (ImageView) v.findViewById(R.id.ivAvatar);
        ivAvatar.setOnClickListener(this);
        String url = loginDto.getAvatar();
        if (url != null && url.length() > 0) {
            String fullUrl = Const.ROOT_URL + url;
//            Log.d(TAG,"fullUrl:"+fullUrl);
            Const.setImage(getActivity(), ivAvatar, fullUrl);
        }


        ivPicture = (ImageView) v.findViewById(R.id.ivPicture);
        ivPicture.setOnClickListener(this);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        tvName = (TextView) v.findViewById(R.id.tvName);
        if (loginDto != null) tvName.setText(loginDto.getName());

    }

    private int PICK_IMAGE_REQUEST = 1;

    public void choosePicture() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, Const.getMsg(getActivity(), R.string.app_name) + " Select Picture"), PICK_IMAGE_REQUEST);


        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    void showProgress() {
        ivPicture.setEnabled(false);
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgress() {
        ivPicture.setEnabled(true);
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Log.d(TAG, "uri:" + uri);
            String path = Const.getRealPathFromURI(getActivity(), uri);
            Log.d(TAG, "path:" + path);
            Log.d(TAG, "URL:" + Const.UpLoadImage);
//            new UpLoadFile.UploadFileToServer(path, Const.UpLoadImage).execute();

            showProgress();
            new UpLoadFile.UploadFileAsync(path, Const.UpLoadImage, loginDto.getUser_id(), new UploadInterfaces() {
                @Override
                public void onProgressUpdate(int values) {

                }

                @Override
                public void onSuccess(StatusAPI statusAPI) {
                    hideProgress();
                }

                @Override
                public void onFail() {
                    hideProgress();
                    ivAvatar.setImageResource(R.drawable.avatar_l);
                }
            }).execute();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                ivAvatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void logout() {
        new PrefManager(getActivity()).setKEY_CB_LOGIN(false);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        if (v == ivPicture) {
            if (in.checkPermissions()) choosePicture();
            else in.setPermissions();
        } else if (v == ivAvatar) {
            Const.pictureDetails(getActivity(), loginDto.getAvatar());
        } else if (v == btnLogout) {
            logout();
        }
    }
}
