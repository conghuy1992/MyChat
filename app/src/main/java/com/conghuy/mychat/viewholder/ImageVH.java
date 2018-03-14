package com.conghuy.mychat.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.conghuy.mychat.R;
import com.conghuy.mychat._class.Const;
import com.conghuy.mychat.dto.MessageDtoDetails;

/**
 * Created by maidinh on 8/11/2017.
 */

public class ImageVH extends RecyclerView.ViewHolder {
    private ImageView iv;
    private Context context;

    public ImageVH(View v, Context context) {
        super(v);
        this.context = context;
        iv = (ImageView) v.findViewById(R.id.iv);
    }

    public void handler(MessageDtoDetails dto) {
        String url = "";
        try {
            url = Const.ROOT_URL + dto.message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Const.setImageChatting(context, iv, url);
    }
}
