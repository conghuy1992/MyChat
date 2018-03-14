package com.conghuy.mychat.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.conghuy.mychat.ChattingActivity;
import com.conghuy.mychat.PictureDetails;
import com.conghuy.mychat.R;
import com.conghuy.mychat._class.Const;
import com.conghuy.mychat.dto.LoginDto;
import com.conghuy.mychat.dto.RoomDto;
import com.conghuy.mychat.dto.RoomDtoDetails;

import java.util.List;

/**
 * Created by maidinh on 6/6/2017.
 */

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {
    private String TAG="RoomAdapter";
    private List<RoomDtoDetails> moviesList;
    private Context context;
    private LoginDto loginDto;

    public void updateLastMsg() {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvLastMsg, tvTime;
        public RelativeLayout root;
        public ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvLastMsg = (TextView) view.findViewById(R.id.tvLastMsg);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            root = (RelativeLayout) view.findViewById(R.id.root);
            iv = (ImageView) view.findViewById(R.id.iv);
        }

        public void handler(final RoomDtoDetails dto, int position) {
            String url = dto.avatar;
            if (url != null && url.length() > 0) Const.setImage(context, iv, Const.ROOT_URL + url);
            else iv.setImageResource(R.drawable.avatar_l);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Const.pictureDetails(context, dto.avatar);
                }
            });

            tvTitle.setText(dto.name);
            tvLastMsg.setText(dto.last_msg);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChattingActivity.class);
                    intent.putExtra(Const.TYPE_CHAT, 0);
                    intent.putExtra(Const.ROOM_DTO, dto);
                    intent.putExtra(Const.USER_ID, loginDto);
                    context.startActivity(intent);
                }
            });

            tvTime.setText(Const.showTime(dto.milliseconds, Const.DATE_FORMAT_YYYY_MM_DD_H_M));

        }
    }

    public void update(List<RoomDtoDetails> moviesList) {
        Log.d(TAG,"update");
        if (moviesList != null) {
            this.moviesList = Const.sort(moviesList);
            this.notifyDataSetChanged();
        }

    }

    public RoomAdapter(Context context, List<RoomDtoDetails> moviesList, LoginDto loginDto) {
        this.context = context;
        this.moviesList = moviesList;
        this.loginDto = loginDto;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_room_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RoomDtoDetails movie = moviesList.get(position);
        holder.handler(movie, position);
//        holder.title.setText(movie.getTitle());
//        holder.genre.setText(movie.getGenre());
//        holder.year.setText(movie.getYear());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}