package com.song.study.socket;

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by shaohua on 2016/4/23.
 */
public class InetAddressTest {

    static String TAG = "HHSJ的app";

    public static void getInetAddress() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        Log.i(TAG, "手机名：" + address.getHostName());
        Log.i(TAG, "Ip字符串格式：" + address.getHostAddress());
        Log.i(TAG, "IP数组格式：" + Arrays.toString(address.getAddress()));
        Log.i(TAG, "未知：" + address.getCanonicalHostName());
    }
}
