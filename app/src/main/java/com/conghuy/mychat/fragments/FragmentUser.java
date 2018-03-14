package com.conghuy.mychat.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.conghuy.mychat.R;
import com.conghuy.mychat._class.Const;
import com.conghuy.mychat._class.HttpRequest;
import com.conghuy.mychat._class.PrefManager;
import com.conghuy.mychat.adapters.UserAdapter;
import com.conghuy.mychat.customUI.DefaultItemAnimatorNoChange;
import com.conghuy.mychat.dto.HttpCallback;
import com.conghuy.mychat.dto.ListUser;
import com.conghuy.mychat.dto.LoginDto;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by maidinh on 6/7/2017.
 */

public class FragmentUser extends Fragment {
    private String TAG="FragmentUser";
    private LoginDto loginDto;
    private PrefManager prefManager;
    private Context context;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProgressBar prgressBar;
    private TextView tvNodata;
    private UserAdapter adapter;

    public FragmentUser() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public FragmentUser(LoginDto loginDto) {
        // Required empty public constructor
        this.loginDto = loginDto;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_layout, container, false);
        context = getActivity();
        prefManager = new PrefManager(context);
        if (loginDto == null)
            loginDto = new Gson().fromJson(prefManager.get_data_login(), LoginDto.class);
        init(v);
        callApi();
        return v;
    }

    void init(View v) {
        adapter = new UserAdapter(context, new ArrayList<LoginDto>(),loginDto);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimatorNoChange());
        layoutManager = new LinearLayoutManager(context);
        prgressBar = (ProgressBar) v.findViewById(R.id.prgressBar);
        tvNodata = (TextView) v.findViewById(R.id.tvNodata);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    void callApi() {
        HttpRequest.GetAllUser(Const.GET_ALL_USER, loginDto.getUser_id(), new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG,"response:"+response);
                prgressBar.setVisibility(View.GONE);
                if (response.startsWith("{")) {
                    ListUser listUser = new Gson().fromJson(response, ListUser.class);
                    if (listUser != null && listUser.getSuccess() == 1 && listUser.getUser() != null && listUser.getUser().size() > 0) {
                        // success
                        adapter.update(listUser.getUser());
                    } else {
                        // nodata
                        tvNodata.setVisibility(View.VISIBLE);
                    }
                } else {

                }
            }

            @Override
            public void onFail(String error) {
                prgressBar.setVisibility(View.GONE);
            }
        });
    }

}