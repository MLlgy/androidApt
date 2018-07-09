package com.mk.aline.aptlib;

import android.app.Activity;

import com.mk.aline.apt.ProxyInfo;

import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Creater: liguoying
 * Time: 2018/7/9 0009 11:30
 */
public class AptLibInit {

    private static final Map<Class<?>, AbstractInjector<Object>> INJECTOR_MAP = new LinkedHashMap<Class<?>, AbstractInjector<Object>>();
    private static final long INTERNAL_TIME = 2000;

    public static void androidApt(View mView) {
        androidApt(mView, INTERNAL_TIME);
    }

    public static void androidApt(Activity mActivity) {
        androidApt(mActivity, INTERNAL_TIME);
    }

    public static void androidApt(Activity mActivity, long internalTime) {
        AbstractInjector<Object> mInjector = findInject(mActivity);
        mInjector.inject(Finder.ACTIVITY, mActivity, mActivity);
        mInjector.setIntervalTime(internalTime);
    }

    public static void androidApt(View mView, long internalTime) {
        AbstractInjector<Object> mInjector = findInject(mView);
        mInjector.inject(Finder.VIEW, mView, mView);
        mInjector.setIntervalTime(internalTime);
    }

    private static AbstractInjector<Object> findInject(Object mActivity) {
        Class<?> clazz = mActivity.getClass();
        AbstractInjector<Object> mInjector = INJECTOR_MAP.get(clazz);
        if (mInjector == null) {
            try {
                Class injectClazz = Class.forName(clazz.getName() + "$$" + ProxyInfo.PROXY);
                mInjector = (AbstractInjector<Object>) injectClazz.newInstance();
                INJECTOR_MAP.put(clazz, mInjector);
            } catch (Exception mE) {
                mE.printStackTrace();
            }
        }
        return mInjector;
    }
}
