package com.conghuy.mychat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.conghuy.mychat._class.Const;
import com.conghuy.mychat._class.HttpRequest;
import com.conghuy.mychat._class.PrefManager;
import com.conghuy.mychat.adapters.ChattingAdapter;
import com.conghuy.mychat.adapters.MenuAdapter;
import com.conghuy.mychat.customUI.DefaultItemAnimatorNoChange;
import com.conghuy.mychat.dto.GetRoomIdDto;
import com.conghuy.mychat.dto.HttpCallback;
import com.conghuy.mychat.dto.LoginDto;
import com.conghuy.mychat.dto.MenuChatDto;
import com.conghuy.mychat.dto.MessageDto;
import com.conghuy.mychat.dto.MessageDtoDetails;
import com.conghuy.mychat.dto.RoomDtoDetails;
import com.conghuy.mychat.dto.StatusAPI;
import com.conghuy.mychat.enums.MenuType;
import com.conghuy.mychat.interfaces.Statics;
import com.conghuy.mychat.interfaces.UploadInterfaces;
import com.conghuy.mychat.util.OnVerticalScrollListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;


/**
 * Created by maidinh on 6/6/2017.
 */

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ChattingActivity";
    private ProgressBar prgressBar;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Context context;
    private RoomDtoDetails roomDtoDetails;
    private ChattingAdapter adapter;
    private List<MessageDtoDetails> messageDtoList;
    private TextView tvNodata;
    private LoginDto loginDto;
    private LoginDto loginDto_user;
    private hani.momanii.supernova_emoji_library.Helper.EmojiconEditText edMsg;
    private FrameLayout btnSend;
    private boolean isLoad = true;
    private PrefManager prefManager;
    private FrameLayout btnNewMsg;
    private int TYPE = 0; // 0-> from list chat ; 1-> search room id
    private EmojIconActions emojIcon;
    private RelativeLayout root_view;
    private ImageView emoji_btn, btn_add;

    private RecyclerView menuRecyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuChatDto> menuChatDtos;

    void initMenu() {
        menuChatDtos = new ArrayList<>();
        MenuChatDto menuChatDto = new MenuChatDto(MenuType.FILE, R.drawable.ic_attach_file_black_24dp);
        menuChatDtos.add(menuChatDto);
        menuChatDto = new MenuChatDto(MenuType.PICTURE, R.drawable.ic_image_black_24dp);
        menuChatDtos.add(menuChatDto);
        menuChatDto = new MenuChatDto(MenuType.CAMERA, R.drawable.ic_menu_camera);
        menuChatDtos.add(menuChatDto);

        menuAdapter = new MenuAdapter(context, menuChatDtos);
        menuRecyclerView = (RecyclerView) findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        menuRecyclerView.setAdapter(menuAdapter);
    }

    void initEmoji() {
        btn_add = (ImageView) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        emoji_btn = (ImageView) findViewById(R.id.emoji_btn);
        root_view = (RelativeLayout) findViewById(R.id.root_view);

        // default color
//        emojIcon = new EmojIconActions(this, root_view, edMsg, emoji_btn);

        // custom color
        emojIcon = new EmojIconActions(this, root_view, edMsg, emoji_btn, "#F44336", "#e8e8e8", "#f4f4f4");
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_keyboard_black_24dp, R.drawable.ic_insert_emoticon_black_24dp);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_layout);
        Log.d(TAG, "onCreate");

        Intent intent = getIntent();
        create(intent);
        registerFCMReceiver();
        initMenu();
        initEmoji();

    }

    @Override
    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
        create(intent);
    }

    void create(Intent intent) {
        context = this;
        prefManager = new PrefManager(context);

        TYPE = intent.getIntExtra(Const.TYPE_CHAT, 0);
        Log.d(TAG, "TYPE:" + TYPE);
        loginDto = (LoginDto) intent.getSerializableExtra(Const.USER_ID);
        if (loginDto == null)
            loginDto = new Gson().fromJson(prefManager.get_data_login(), LoginDto.class);

        if (TYPE == 0) fromListChat(intent);
        else fromUserChat(intent);
    }

    void fromListChat(Intent intent) {
        initView(intent);
    }

    void fromUserChat(Intent intent) { // call api get room id
        loginDto_user = (LoginDto) intent.getSerializableExtra(Const.USER_ID_2);
        callApi();
    }

    void initView(Intent intent) {
        roomDtoDetails = (RoomDtoDetails) intent.getSerializableExtra(Const.ROOM_DTO);
        RoomDtoDetails dto = (RoomDtoDetails) intent.getSerializableExtra(Const.FCM_DATA_OPEN_CHAT);
        if (dto != null) {
            roomDtoDetails = dto;
        }

        if (roomDtoDetails == null) {
            Const.showDialog(context, Const.getMsg(context, R.string.can_not_get_room_infor));
        } else {
            init();
        }
    }

    void callApi() {

        HttpRequest.getRoomId(Const.GET_ROOM_ID, loginDto.getUser_id(), loginDto_user.getUser_id(), new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "getRoomId:" + response);
                if (response.startsWith("{")) {
                    roomDtoDetails = new Gson().fromJson(response, RoomDtoDetails.class);
                    if (roomDtoDetails != null && roomDtoDetails.success == 1) {
                        init();
                    } else {
                        // fail
                        Const.showDialog(context, Const.getMsg(context, R.string.can_not_get_room_infor));
                    }
                } else {
                    // fail -> finish
                }
            }

            @Override
            public void onFail(String error) {
                // fail -> finish
            }
        });
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


    void init() {
        if (roomDtoDetails != null)
            Const.cancelAllNotification(context, roomDtoDetails.chat_room_id);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(roomDtoDetails.name);
        }
        edMsg = (hani.momanii.supernova_emoji_library.Helper.EmojiconEditText) findViewById(R.id.edMsg);
        btnSend = (FrameLayout) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        btnNewMsg = (FrameLayout) findViewById(R.id.btnNewMsg);
        btnNewMsg.setOnClickListener(this);

        tvNodata = (TextView) findViewById(R.id.tvNodata);
        messageDtoList = new ArrayList<>();
        adapter = new ChattingAdapter(context, messageDtoList, loginDto);
        prgressBar = (ProgressBar) findViewById(R.id.prgressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimatorNoChange());
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onScrolledUp() {
                super.onScrolledUp();
//                Log.d(TAG, "onScrolledUp");
            }

            @Override
            public void onScrolledDown() {
                super.onScrolledDown();
//                Log.d(TAG, "onScrolledDown");
            }

            @Override
            public void onScrolledToTop() {
                super.onScrolledToTop();
                Log.d(TAG, "onScrolledToTop");
                loadMore();
            }

            @Override
            public void onScrolledToBottom() {
                super.onScrolledToBottom();
                Log.d(TAG, "onScrolledToBottom");
                btnNewMsg.setVisibility(View.GONE);
            }
        });
        callAPI(-1);
    }


    void loadMore() {
        if (messageDtoList != null && messageDtoList.size() > 1) {
            Log.d(TAG, "isLoad:" + isLoad);
            if (isLoad) {
                int index = 0;
                for (MessageDtoDetails obj : messageDtoList) {
                    if (obj.getUser_id() != Statics.TYPE_LOADING) {
                        index = obj.getMessage_id();
                        break;
                    }
                }
                Log.d(TAG, "index:" + index);
                callAPI(index);
            } else {
                removeLoading();
                Log.d(TAG, "no data");
            }
        } else {

        }
    }

    private void removeLoading() {
        if (messageDtoList != null && messageDtoList.size() > 0) {
            if (messageDtoList.get(0).getUser_id() == Statics.TYPE_LOADING) {
                messageDtoList.remove(0);
            }
        }
    }

    private boolean first = true;

    void callAPI(int msgId) {
        Log.d(TAG, "callAPI");
        isLoad = false;
        MyApplication.ROOM_NO = roomDtoDetails.chat_room_id;
        HttpRequest.getMsgOfRoom(Const.GET_MSG_OF_ROOM, msgId, roomDtoDetails.chat_room_id, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                isLoad = true;
                prgressBar.setVisibility(View.GONE);
//                Log.d(TAG, "response:" + response);
                if (response.startsWith("{")) {
                    MessageDto messageDto = new Gson().fromJson(response, MessageDto.class);
                    if (messageDto.getSuccess() == 1) {
                        List<MessageDtoDetails> list = messageDto.getMessageList();
                        if (list != null && list.size() > 0) {
                            tvNodata.setVisibility(View.GONE);

                            for (MessageDtoDetails obj : list) {
                                obj = Const.convertObject(obj, loginDto);
                            }

                            Collections.reverse(list);
//                            for (MessageDtoDetails obj : list) {
//                                Log.d(TAG, "obj:" + obj.getMessage_id() + " - " + obj.getMessage());
//                            }

                            removeLoading();

                            messageDtoList.addAll(0, list);

                            MessageDtoDetails messageDtoDetails = new MessageDtoDetails();
                            messageDtoDetails.setUser_id(Statics.TYPE_LOADING);
                            if (list.size() >= 20) {
                                // add row loading
                                messageDtoList.add(0, messageDtoDetails);

                            }

//                            for (MessageDtoDetails obj : messageDtoList) {
//                                Log.d(TAG,"msgId:"+obj.getMessage_id()+" - "+obj.getMessage());
//                            }
                            adapter.update(messageDtoList);

                            if (!first) {
                                int index = messageDtoList.size() - 20;
                                if (index > 0) {
                                    if (index > 20) index = 20;
                                    layoutManager.scrollToPosition(index);
                                    Log.d(TAG, "messageDtoList index:" + index);
                                }
                            }
                            first = false;

                        } else {
                            isLoad = false;
                            tvNodata.setVisibility(View.VISIBLE);
                            // no data
                        }
                    } else if (messageDto.getSuccess() == 2) {
                        Log.d(TAG, "getSuccess = 2");
                        // no data
                        isLoad = false;
                        if (messageDtoList == null || messageDtoList.size() == 0) {
                            tvNodata.setVisibility(View.VISIBLE);
//                            Const.showMsg(context, messageDto.getMessage());
                        }

                        removeLoading();
                        adapter.notifyDataSetChanged();

                    } else {
                        tvNodata.setVisibility(View.GONE);
                        Const.showMsg(context, messageDto.getMessage());
                    }
                } else {

                    tvNodata.setVisibility(View.GONE);
                    Const.showMsg(context, Const.getMsg(context, R.string.unknown_error));
                }
            }

            @Override
            public void onFail(String error) {
                isLoad = true;
                prgressBar.setVisibility(View.GONE);
                Log.d(TAG, "response:" + error);
                tvNodata.setVisibility(View.GONE);
                Const.showMsg(context, error);
            }
        });
    }

    void actionSend(final String msg, int TYPE) {
        Log.d(TAG, "msg:" + msg);
        edMsg.setText("");
        if (tvNodata != null) if (tvNodata.isShown()) tvNodata.setVisibility(View.GONE);

        final MessageDtoDetails dtoDetails = new MessageDtoDetails();
        dtoDetails.VIEW_HOLDER = Statics.ME_MSG;
        dtoDetails.setSend(false);
        dtoDetails.setMessage(msg);
        dtoDetails.setUser_name(loginDto.getName());
        dtoDetails.setUser_id(loginDto.getUser_id());
        dtoDetails.setChat_room_id(roomDtoDetails.chat_room_id);
        dtoDetails.setMilliseconds(Calendar.getInstance().getTimeInMillis());

        messageDtoList.add(dtoDetails);
        adapter.notifyItemInserted(messageDtoList.size() - 1);
        layoutManager.scrollToPosition(messageDtoList.size() - 1);


        HttpRequest.SendMsg(Const.SEND_MSG, loginDto.getName(), roomDtoDetails.chat_room_id, loginDto.getUser_id(), msg, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "response:" + response);
                if (response.startsWith("{")) {
                    StatusAPI obj = new Gson().fromJson(response, StatusAPI.class);
                    int success = obj.getSuccess();
                    if (success == 1) {
                        // go to chat
//                            Const.showMsg(context, obj.getMessage());
//                            dtoDetails.setSend(true);
                        dtoDetails.setSend(true);
                        dtoDetails.setMessage_id(obj.getMessage_id());
                        dtoDetails.setMilliseconds(obj.getMilliseconds());
                        adapter.notifyDataSetChanged();

                        // sendBroadcast to update last msg of list chat room

                        RoomDtoDetails dto = new RoomDtoDetails();
                        dto.chat_room_id = roomDtoDetails.chat_room_id;
                        dto.last_msg = msg;
                        dto.milliseconds = obj.getMilliseconds();

                        Intent intentLastMsg = new Intent(Const.ACTION_RECEIVER_LAST_MSG);
                        intentLastMsg.putExtra(Const.FCM_DATA_NOTIFICATOON, dto);
                        sendBroadcast(intentLastMsg);

                    } else {
                        Const.showMsg(context, Const.getMsg(context, R.string.can_not_send_this_msg));
                    }
                } else {
                    Const.showMsg(context, Const.getMsg(context, R.string.unknown_error));
                }
            }

            @Override
            public void onFail(String error) {
                Log.d(TAG, "response:" + error);
                Const.showMsg(context, error);
            }
        }, TYPE);
    }

    public void addMsgFromFCM(RoomDtoDetails dto) {
        Log.d(TAG, "addMsgFromFCM");
        if (dto.chat_room_id != roomDtoDetails.chat_room_id || dto.user_id_sent == loginDto.getUser_id())
            return;

        Log.d(TAG, "addMsgFromFCM: 2");

        MessageDtoDetails dtoDetails = new MessageDtoDetails();
        dtoDetails.setSend(true);
        dtoDetails.setMessage(dto.last_msg);
        dtoDetails.setMessage_id(dto.message_id);
        dtoDetails.setChat_room_id(dtoDetails.getChat_room_id());
        dtoDetails.setUser_id(dto.user_id_sent);
        dtoDetails.setMilliseconds(dto.milliseconds);
        dtoDetails.setAvatar(dto.avatar);
        dtoDetails.type = dto.type;

        dtoDetails = Const.convertObject(dtoDetails, loginDto);
        messageDtoList.add(dtoDetails);
        adapter.notifyItemInserted(messageDtoList.size() - 1);

        if (layoutManager.findLastCompletelyVisibleItemPosition() == messageDtoList.size() - 2) {
            int b = messageDtoList.size() - 1;
            Log.d(TAG, "b:" + b);
            if (b >= 0) layoutManager.scrollToPosition(b);
        } else {
            btnNewMsg.setVisibility(View.VISIBLE);
        }

    }

    public int roomId() {
        Log.d(TAG, "roomDtoDetails.getChat_room_id():" + roomDtoDetails.chat_room_id);
        return roomDtoDetails.chat_room_id;
    }

    private void receiveNotification(Intent intent) {
        Log.d(TAG, "receiveNotification");
        RoomDtoDetails dto = (RoomDtoDetails) intent.getSerializableExtra(Const.FCM_DATA_NOTIFICATOON);
        if (dto != null) {
            addMsgFromFCM(dto);
        }
    }

    void file() {
        Const.showMsg(context, "FILE");
    }

    public void choosePicture() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, Statics.PICK_IMAGE_REQUEST);
    }

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
        ActivityCompat.requestPermissions(this, requestPermission, Statics.MY_PERMISSIONS_REQUEST_CODE);
    }

    void picture() {
        if (checkPermissions()) choosePicture();
        else setPermissions();
    }

    void camera() {
        Const.showMsg(context, "CAMERA");
    }

    private void handlerMenu(Intent intent) {
        MenuChatDto menuChatDto = (MenuChatDto) intent.getSerializableExtra(Const.MENU_TYPE);
        switch (menuChatDto.menuType) {
            case FILE:
                file();
                break;
            case PICTURE:
                picture();
                break;
            case CAMERA:
                camera();
                break;
        }
    }

    private BroadcastReceiver mReceiverNewAssignTask = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Const.ACTION_RECEIVER_NOTIFICATION)) {
                receiveNotification(intent);
            } else if (intent.getAction().equals(Const.ACTION_MENU)) {
                handlerMenu(intent);
            }
        }
    };

    private void registerFCMReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Const.ACTION_RECEIVER_NOTIFICATION);
        filter.addAction(Const.ACTION_MENU);
        registerReceiver(mReceiverNewAssignTask, filter);
    }

    private void unregisterFCMReceiver() {
        unregisterReceiver(mReceiverNewAssignTask);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterFCMReceiver();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Statics.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Log.d(TAG, "uri:" + uri);
            String path = Const.getRealPathFromURI(context, uri);
            Log.d(TAG, "path:" + path);
            if (path != null && path.length() > 0 && !path.startsWith("http")) {
                new HttpRequest().new Upload(Const.UpLoadFILE, path, new UploadInterfaces() {
                    @Override
                    public void onProgressUpdate(int values) {

                    }

                    @Override
                    public void onSuccess(StatusAPI statusAPI) {
                        String msg = statusAPI.url;
                        if (msg == null || msg.length() == 0) return;
                        actionSend(msg, Statics.TYPE_PICTURE);
                    }

                    @Override
                    public void onFail() {

                    }
                }).execute();

//                listPath.add(path);
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//                    int bm = Const.getDimenInPx(context, R.dimen.iv_width);
//                    bitmap = bitmap.createScaledBitmap(bitmap, bm, bm, false);
//
//                    ImageView iv = new ImageView(this);
//                    iv.setImageBitmap(bitmap);
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins(Const.getDimenInPx(context, R.dimen.iv_margin_left), 0, 0, 0);
//                    iv.setLayoutParams(layoutParams);
//                    layout_iv.addView(iv, 0);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
//        else if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
//            if (uri != null) {
//                try {
//                    String path = Const.getPathFromURI(uri, this);
//                    Log.d(TAG, "path:" + path);
//                    listPath.add(path);
//                    Bitmap bitmap = Const.createScaleBitmap(path, 100);
//                    int bm = Const.getDimenInPx(context, R.dimen.iv_width);
//                    bitmap = bitmap.createScaledBitmap(bitmap, bm, bm, false);
//                    ImageView iv = new ImageView(this);
//                    iv.setImageBitmap(bitmap);
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins(Const.getDimenInPx(context, R.dimen.iv_margin_left), 0, 0, 0);
//                    iv.setLayoutParams(layoutParams);
//                    layout_iv.addView(iv, 0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnSend) {
            final String msg = edMsg.getText().toString().trim();
            if (msg == null || msg.length() == 0) return;
            actionSend(msg, Statics.TYPE_MESSAGE);
        } else if (v == btnNewMsg) {
            btnNewMsg.setVisibility(View.GONE);
            int a = messageDtoList.size() - 1;
            if (a > 0) layoutManager.scrollToPosition(a);
        } else if (v == btn_add) {
            if (menuRecyclerView.isShown()) {
                btn_add.animate().rotation(90);
                menuRecyclerView.setVisibility(View.GONE);
            } else {
                btn_add.animate().rotation(45);
                menuRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}
