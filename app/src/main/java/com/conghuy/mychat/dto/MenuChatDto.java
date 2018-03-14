package com.conghuy.mychat.dto;

import com.conghuy.mychat.enums.MenuType;

import java.io.Serializable;

/**
 * Created by maidinh on 8/11/2017.
 */

public class MenuChatDto implements Serializable {
    public MenuType menuType;
    public int icon;

    public MenuChatDto(MenuType menuType, int icon) {
        this.menuType = menuType;
        this.icon = icon;
    }
}
