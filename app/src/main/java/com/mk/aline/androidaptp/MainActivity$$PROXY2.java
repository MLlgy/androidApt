// Generate code from APT.Do not modify!
package com.mk.aline.androidaptp;

import android.view.View;

import com.mk.aline.aptlib.Finder;
import com.mk.aline.aptlib.AbstractInjector;

public class MainActivity$$PROXY2<T extends MainActivity> implements AbstractInjector<T> {
    public long intervalTime;

    @Override
    public void setIntervalTime(long time) {
        intervalTime = time;
    }

    @Override
    public void inject(final Finder finder, final T target, Object source) {
        View view;
        view = finder.findViewById(source, 2131165218);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                long time = 0L;

                @Override
                public void onClick(View v) {
                    long temp = System.currentTimeMillis();
                    if (temp - time >= intervalTime) {
                        time = temp;
                        target.once();
                    }
                }
            });
        }
        view = finder.findViewById(source, 2131165219);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                long time = 0L;

                @Override
                public void onClick(View v) {
                    long temp = System.currentTimeMillis();
                    if (temp - time >= intervalTime) {
                        time = temp;
                        target.onceMe(v);
                    }
                }
            });
        }
    }

}
