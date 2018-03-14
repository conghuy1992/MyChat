package com.conghuy.mychat.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
import com.conghuy.mychat.adapters.RoomAdapter;
import com.conghuy.mychat.customUI.DefaultItemAnimatorNoChange;
import com.conghuy.mychat.dto.HttpCallback;
import com.conghuy.mychat.dto.LoginDto;
import com.conghuy.mychat.dto.RoomDto;
import com.conghuy.mychat.dto.RoomDtoDetails;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maidinh on 6/7/2017.
 */

public class FragmentChat extends Fragment {
    public static FragmentChat fm = null;
    private String TAG = "FragmentChat";
    private Context context;


    private ProgressBar prgressBar;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RoomAdapter adapter;
    private List<RoomDtoDetails> roomDtoList;
    private TextView tvNodata;
    private LoginDto loginDto;
    private PrefManager prefManager;

    public FragmentChat() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public FragmentChat(LoginDto loginDto) {
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
        View v = inflater.inflate(R.layout.fragment_chat_layout, container, false);
        fm = this;
        registerFCMReceiver();
        context = getActivity();
        prefManager = new PrefManager(context);
        if (loginDto == null)
            loginDto = new Gson().fromJson(prefManager.get_data_login(), LoginDto.class);


        if (loginDto == null) {
            // rs app
            Const.showDialog(context, Const.getMsg(context, R.string.can_not_get_user_id));
        } else {
            // continue handler view
            handler(v);
        }
        return v;
    }

    void handler(View v) {
        tvNodata = (TextView) v.findViewById(R.id.tvNodata);
        roomDtoList = new ArrayList<>();
        prgressBar = (ProgressBar) v.findViewById(R.id.prgressBar);
        adapter = new RoomAdapter(context, roomDtoList, loginDto);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimatorNoChange());
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        callAPI();
    }

    private void updateLastMsg(RoomDtoDetails fcmDto) {
        Log.d(TAG, "updateLastMsg:");
        int n = roomDtoList.size();
        boolean flag = true;
        /**
         * flag = false: update last msg chat list for position
         * flag = true: add new row chat list
         * */
        for (int i = 0; i < n; i++) {
            RoomDtoDetails roomDtoDetails = roomDtoList.get(i);
            if (roomDtoDetails.chat_room_id == fcmDto.chat_room_id) {

                roomDtoDetails.last_msg = fcmDto.last_msg;
                roomDtoDetails.milliseconds = fcmDto.milliseconds;

                roomDtoList = Const.sort(roomDtoList);
                adapter.notifyDataSetChanged();
                flag = false;
                break;
            }
        }
        if (flag) {
            roomDtoList.add(fcmDto);
            adapter.update(roomDtoList);
            if (tvNodata != null) tvNodata.setVisibility(View.GONE);
        }


    }

    private void registerFCMReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Const.ACTION_RECEIVER_LAST_MSG);
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    private void unregisterFCMReceiver() {
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Const.ACTION_RECEIVER_LAST_MSG)) {
                RoomDtoDetails dto = (RoomDtoDetails) intent.getSerializableExtra(Const.FCM_DATA_NOTIFICATOON);
                if (dto != null) {
                    updateLastMsg(dto);
                }
            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        unregisterFCMReceiver();
        fm = null;
    }

    void callAPI() {
        HttpRequest.getRoom(Const.GET_ROOM, loginDto.getUser_id(), new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "response:" + response);
                prgressBar.setVisibility(View.GONE);
                if (response.startsWith("{")) {
                    RoomDto roomDto = new Gson().fromJson(response, RoomDto.class);
                    if (roomDto.getRoom() != null && roomDto.getRoom().size() > 0 && roomDto.getSuccess() == 1) {
                        tvNodata.setVisibility(View.GONE);
                        roomDtoList = roomDto.getRoom();
                        adapter.update(roomDtoList);
                    } else {
                        // no data
                        tvNodata.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvNodata.setVisibility(View.VISIBLE);
                    Const.showMsg(context, Const.getMsg(context, R.string.unknown_error));
                }
            }

            @Override
            public void onFail(String error) {
                tvNodata.setVisibility(View.VISIBLE);
                prgressBar.setVisibility(View.GONE);
                Log.d(TAG, "response:" + error);
                Const.showDialog(context, error);
            }
        });
    }
}