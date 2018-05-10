package com.song.study.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.song.study.activity.MusicActivity;
import com.song.study.conts.Constant;
import com.song.study.conts.IntentKeywords;
import com.song.study.service.MusicService;

/**
 * @description 监听系统来去电
 **/
public class InOutCallReceiver extends BroadcastReceiver {
    // 用户发送广播
    Intent intent = new Intent(MusicService.ACTION);
    Bundle bundle = new Bundle();
    int data[] = new int[2];
    // 电话管理器
    private TelephonyManager mTelephonyManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 获取电话服务，并监听电话的状态
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (mTelephonyManager.getCallState()) {
            // 通话中
            case TelephonyManager.CALL_STATE_OFFHOOK:
                // do nothings...
                break;
            // 挂断电话，开始播放歌曲
            case TelephonyManager.CALL_STATE_IDLE:
                // 电话响铃时，暂停播放歌曲
            case TelephonyManager.CALL_STATE_RINGING:
                data[0] = Constant.CMD_PLAY;
                data[1] = 0;
                bundle.putIntArray(IntentKeywords.KEY, data);
                // 将命令及数据打包到Intent里
                this.intent.replaceExtras(bundle);
                // 发送广播
                context.sendBroadcast(this.intent);
                break;
            default:
                break;
        }
    }
}
