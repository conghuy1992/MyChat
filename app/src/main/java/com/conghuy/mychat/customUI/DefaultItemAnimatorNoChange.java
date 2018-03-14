package com.conghuy.mychat.customUI;

import android.support.v7.widget.DefaultItemAnimator;

/**
 * Created by maidinh on 16/9/2016.
 */
public class DefaultItemAnimatorNoChange extends DefaultItemAnimator {
    public DefaultItemAnimatorNoChange() {
        setSupportsChangeAnimations(false);
    }
}