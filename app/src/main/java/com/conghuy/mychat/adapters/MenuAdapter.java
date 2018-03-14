package com.conghuy.mychat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.conghuy.mychat.R;
import com.conghuy.mychat._class.Const;
import com.conghuy.mychat.dto.MenuChatDto;

import java.util.List;

/**
 * Created by maidinh on 8/11/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private Context context;
    private List<MenuChatDto> menuChatDtos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //        public TextView title, year, genre;
        public ImageView iv;
        public FrameLayout root;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.iv);
            root = (FrameLayout) view.findViewById(R.id.root);
//            year = (TextView) view.findViewById(R.id.year);
        }

        public void handler(final MenuChatDto menuChatDto) {
            iv.setImageResource(menuChatDto.icon);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Const.ACTION_MENU);
                    intent.putExtra(Const.MENU_TYPE, menuChatDto);
                    context.sendBroadcast(intent);
                }
            });
        }
    }


    public MenuAdapter(Context context, List<MenuChatDto> menuChatDtos) {
        this.context = context;
        this.menuChatDtos = menuChatDtos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_chat_row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MenuChatDto menuChatDto = menuChatDtos.get(position);
        holder.handler(menuChatDto);
    }

    @Override
    public int getItemCount() {
        return menuChatDtos.size();
    }
}