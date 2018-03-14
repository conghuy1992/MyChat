package com.conghuy.mychat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conghuy.mychat.R;
import com.conghuy.mychat._class.Const;
import com.conghuy.mychat.dto.LoginDto;
import com.conghuy.mychat.dto.MessageDtoDetails;
import com.conghuy.mychat.interfaces.Statics;
import com.conghuy.mychat.viewholder.ImageVH;
import com.conghuy.mychat.viewholder.Loading;
import com.conghuy.mychat.viewholder.MsgVH;

import java.util.List;

/**
 * Created by maidinh on 6/6/2017.
 */

public class ChattingAdapter extends RecyclerView.Adapter {
    private String TAG = "ChattingAdapter";
    private Context context;
    private List<MessageDtoDetails> messageDtoList;
    private LoginDto loginDto;

    public void update(List<MessageDtoDetails> messageDtoList) {
        if (messageDtoList != null) {
            this.messageDtoList = messageDtoList;
            this.notifyDataSetChanged();
        }
    }

    public ChattingAdapter(Context context, List<MessageDtoDetails> messageDtoList, LoginDto loginDto) {
        this.context = context;
        this.messageDtoList = messageDtoList;
        this.loginDto = loginDto;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == Statics.ME_MSG) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.me_msg_layout, parent, false);
            vh = new MsgVH(v, context);
        } else if (viewType == Statics.USER_MSG) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.user_msg_layout, parent, false);
            vh = new MsgVH(v, context);
        }

        //
        else if (viewType == Statics.ME_IMAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.me_image_layout, parent, false);
            vh = new ImageVH(v, context);
        } else if (viewType == Statics.USER_IMAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.user_image_layout, parent, false);
            vh = new ImageVH(v, context);
        }

        // default
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.chatting_loading_layout, parent, false);
            vh = new Loading(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageDtoDetails dtoDetails = messageDtoList.get(position);
        if (holder instanceof MsgVH) {
            ((MsgVH) holder).handler(dtoDetails);
        } else if (holder instanceof ImageVH) {
            ((ImageVH) holder).handler(dtoDetails);
        }


    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//        Log.d(TAG,"VIEW_HOLDER"+messageDtoList.get(position).VIEW_HOLDER);
        return messageDtoList.get(position).VIEW_HOLDER;
    }

    @Override
    public int getItemCount() {
        return messageDtoList.size();
    }
}
