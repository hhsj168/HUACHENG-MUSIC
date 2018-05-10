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
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {
            Cursor cursor1 = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            new AlertDialog.Builder(context).setMessage("正在扫描存储卡...").show();
            count1 = cursor1.getCount();
        } else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
            Cursor cursor2 = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            count2 = cursor2.getCount();
            if (count >= 0) {
                Toast.makeText(context, "共增加" + count + "首歌曲", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "共减少" + count + "首歌曲", Toast.LENGTH_LONG).show();
            }
        }
    }
}
