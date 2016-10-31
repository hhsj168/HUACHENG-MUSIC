package com.song.study.musicutil;

/**
 * 因为在Tabhost中的Act切换到其它的Act(不是TabHost中的)
 * 直接在finish()或startActivity后直接调用overridePendingTransition()无效，所以.....
 */
public class AnimCommon {

    public static int in = 0;
    public static int out = 0;

    public static void set(int a, int b) {
        in = a;
        out = b;
    }

    public static void clear() {
        in = 0;
        out = 0;
    }
}
