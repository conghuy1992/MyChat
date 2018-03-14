package com.conghuy.mychat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.conghuy.mychat.ChattingActivity;
import com.conghuy.mychat.R;
import com.conghuy.mychat._class.Const;
import com.conghuy.mychat.dto.LoginDto;

import java.util.List;

/**
 * Created by maidinh on 6/7/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<LoginDto> moviesList;
    private Context context;
    private LoginDto loginDto;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUser;
        public RelativeLayout root;
        public ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            tvUser = (TextView) view.findViewById(R.id.tvUser);
            root = (RelativeLayout) view.findViewById(R.id.root);
            iv=(ImageView)view.findViewById(R.id.iv);
        }

        public void handler(final LoginDto dto, int position) {
            String url = dto.getAvatar();
            if(url!=null&&url.length()>0)Const.setImage(context,iv,Const.ROOT_URL+url);
            else iv.setImageResource(R.drawable.avatar_l);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Const.pictureDetails(context,dto.getAvatar());
                }
            });


            tvUser.setText(dto.getName());
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChattingActivity.class);
                    intent.putExtra(Const.TYPE_CHAT, 1);
                    intent.putExtra(Const.USER_ID, loginDto);
                    intent.putExtra(Const.USER_ID_2, dto);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void update(List<LoginDto> moviesList) {
        if (moviesList != null) {
            this.moviesList = moviesList;
            this.notifyDataSetChanged();
        }

    }

    public UserAdapter(Context context, List<LoginDto> moviesList,LoginDto loginDto) {
        this.context = context;
        this.moviesList = moviesList;
        this.loginDto=loginDto;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_user_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LoginDto movie = moviesList.get(position);
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