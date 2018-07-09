package com.mk.aline.aptlib;

import android.app.Activity;
import android.view.View;

/**
 * Creater: liguoying
 * Time: 2018/7/9 0009 14:00
 */
public enum Finder {

    VIEW {
        @Override
        public View findViewById(Object source, int id) {
            return ((View) source).findViewById(id);
        }
    },
    ACTIVITY {
        @Override
        public View findViewById(Object source, int id) {
            return ((Activity) source).findViewById(id);
        }
    };

    public abstract View findViewById(Object source, int id);
}
