package com.conghuy.mychat.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.conghuy.mychat.R;
import com.conghuy.mychat._class.Const;
import com.conghuy.mychat.dto.MessageDtoDetails;

/**
 * Created by maidinh on 8/10/2017.
 */

public class MsgVH extends RecyclerView.ViewHolder {
    public TextView tvMsg, tvTime;
    public ImageView iv;
    public ImageView ivIsSend;
    public Context context;

    public MsgVH(View v, Context context) {
        super(v);
        this.context = context;
        tvMsg = (TextView) v.findViewById(R.id.tvMsg);
        tvTime = (TextView) v.findViewById(R.id.tvTime);
        iv = (ImageView) v.findViewById(R.id.iv);
        ivIsSend = (ImageView) v.findViewById(R.id.ivIsSend);
    }

    public void handler(MessageDtoDetails dtoDetails) {
        if (iv != null) {
            if (dtoDetails.getAvatar() != null && dtoDetails.getAvatar().length() > 0)
                Const.setImage(context, iv, Const.ROOT_URL + dtoDetails.getAvatar());
            else iv.setImageResource(R.drawable.avatar_l);
        }

        tvMsg.setText(dtoDetails.getMessage());
        String time = "";
        try {
            time = Const.showTime(dtoDetails.getMilliseconds(), Const.DATE_FORMAT_YYYY_MM_DD_H_M);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvTime.setText(time);

        if (ivIsSend != null) {
            if (dtoDetails.isSend()) ivIsSend.setVisibility(View.GONE);
            else ivIsSend.setVisibility(View.VISIBLE);
        }
    }
}
