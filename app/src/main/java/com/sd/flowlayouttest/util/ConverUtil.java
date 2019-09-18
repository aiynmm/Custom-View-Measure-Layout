package com.sd.flowlayouttest.util;

import android.content.res.Resources;

public class ConverUtil {

    public static float dp2px(float dpValue) {
        return dpValue * Resources.getSystem().getDisplayMetrics().density;
    }

    public static float px2dp(float pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }
}
