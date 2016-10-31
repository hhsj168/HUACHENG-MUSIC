package com.song.study.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;

public class ScanSdReceiver extends BroadcastReceiver {
    private int count1;
    private int count2;
    private int count;

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        String action = arg1.getAction();
        if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {
            Cursor cursor1 = arg0.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            new AlertDialog.Builder(arg0).setMessage("正在扫描存储卡...").show();
            count1 = cursor1.getCount();
        } else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
            Cursor cursor2 = arg0.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            count2 = cursor2.getCount();
            if (count >= 0) {
                Toast.makeText(arg0, "共增加" + count + "首歌曲", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(arg0, "共减少" + count + "首歌曲", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
