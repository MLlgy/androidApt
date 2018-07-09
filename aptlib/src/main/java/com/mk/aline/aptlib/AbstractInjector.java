package com.mk.aline.aptlib;

/**
 * Creater: liguoying
 * Time: 2018/7/9 0009 14:07
 */
public interface AbstractInjector<T> {

    void inject(Finder mFinder,T target, Object source);

    void setIntervalTime(long mIntervalTime);
}
