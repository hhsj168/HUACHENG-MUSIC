package com.song.study.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.song.study.R;
import com.song.study.base.BaseActivity;
import com.song.study.musicobject.Music;
import com.song.study.conts.Constant;
import com.song.study.view.LrcView;
import com.song.study.musicutil.MusicUtil;
import com.song.study.service.MusicService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顾修忠
 * @category 播放音乐的界面
 */
public class MusicActivity extends BaseActivity implements OnClickListener,
        SensorEventListener {

    public static final String KEY = "CMD";
    private static final String TAG = "MusicActivity";
    // 摇晃的速度，越小灵敏度越高
    private static final int HOLD_SPEED = 2400;
    public static TextView mTextView_music_name, mTextView_music_singer;
    public static LrcView mTextView_show_lrc;
    public static ImageView mImageView_music_play;
    public static SeekBar mSeekBar;
    public static TextView mTextView_music_start_time,
            mTextView_music_end_time;
    // 用户发送广播
    Intent intent = new Intent(MusicService.ACTION);
    Bundle bundle = new Bundle();
    int data[] = new int[2];
    int flag_RE = -1;
    private ImageView mImageView_music_loop, mImageView_music_random,
            mImageView_music_previous, mImageView_music_next;
    private FrameLayout mRelativeLayout;
    private List<Music> musics = new ArrayList<Music>();
    private boolean random_model_open = false;
    private int count_click = 0;
    // 传感器管理器
    private SensorManager mSensorManager;
    private Sensor acc_sensor;// 加速度传感器
    /* 结束时三轴加速度x、y、z的值 */
    private float last_x, last_y = 4.5f, last_z = 9.8f;
    private float x, y, z;
    private long last_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //广告
        if (Constant.D)
            Log.e(TAG, "onCreate()");
        // 设置布局文件，显示界面
        setContentView(R.layout.playmusic);
        intitView();

        // 启动播放服务
        this.startService(new Intent(this, MusicService.class));

        // 获得传感器管理对象
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 获取加速度传感器
        acc_sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 注册加速度传感器
        mSensorManager.registerListener(this, acc_sensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (flag_RE == 3) {
            return;
        }
        if (Constant.D)
            Log.e(TAG, "onStart()");
        mImageView_music_play.setImageResource(R.drawable.ic_media_pause);
        int flag = getIntent().getIntExtra("FLAG", 0);
        if (Constant.D)
            Log.e(TAG, "------FLAG---" + flag);

        if (flag == Constant.FLAG_ALL) {
            musics.clear();
            musics = MusicUtil.getAllMusics(getApplicationContext());
            data[0] = Constant.CMD_PLAY_ALBUM_Default;
            data[1] = 0;
            bundle.putIntArray(KEY, data);
            // 将命令及数据打包到Intent里
            intent.putExtras(bundle);
            // 发送广播更改专辑
            this.sendBroadcast(intent);
        } else if (flag == Constant.FLAG_PART) {
            String albumname = getIntent().getStringExtra("albumname");
            if (Constant.D)
                Log.e(TAG, "-----------" + albumname);
            musics.clear();
            musics = MusicUtil.getMusicInOneAlum(getApplicationContext(),
                    albumname);
            data[0] = Constant.CMD_PLAY_ALBUM_SPEC;
            data[1] = 0;
            bundle.putIntArray(KEY, data);
            bundle.putString("albumname", albumname);
            // 将命令及数据打包到Intent里
            intent.putExtras(bundle);
            // 发送广播更改专辑
            this.sendBroadcast(intent);
        } else if (flag == Constant.FLAG_RECENT) {
            musics.clear();
            musics = MusicService.recentMusics;
            data[0] = Constant.CMD_PLAY_ALBUM_RENCENT;
            data[1] = 0;
            bundle.putIntArray(KEY, data);
            // 将命令及数据打包到Intent里
            intent.putExtras(bundle);
            // 发送广播更改专辑
            this.sendBroadcast(intent);
        }
        if (Constant.D)
            Log.e(TAG, "--musics.size()---" + musics.size());
        if (musics.size() == 0) {
            return;
        }
        int index = getIntent().getIntExtra("index", 0);
        Music m = musics.get(index);
        mTextView_music_name.setText(m.getTitle());
        mTextView_music_singer.setText(m.getSinger());
        mTextView_music_end_time.setText(MusicUtil.formatTime(m.getTime()));

        bundle.clear();
        data[0] = Constant.CMD_PLAY_SPEC;
        data[1] = 0;
        bundle.putIntArray(KEY, data);
        bundle.putInt("index", index);
        // 将命令及数据打包到Intent里
        intent.putExtras(bundle);
        // 发送广播
        this.sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (Constant.D)
            Log.e(TAG, "onResume()");
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("setting",
                Activity.MODE_PRIVATE);
        boolean b = preferences.getBoolean("LRC_IS_SHOW", true);
        if (b) {
            data[0] = Constant.CMD_SHOW_LRC;
        } else {
            data[0] = Constant.CMD_NOT_SHOW_LRC;
        }
        bundle.clear();
        data[1] = 0;
        bundle.putIntArray(KEY, data);
        // 将命令及数据打包到Intent里
        intent.putExtras(bundle);
        // 发送广播
        this.sendBroadcast(intent);

        //判断是否显示桌面歌词
        b = preferences.getBoolean("SetDeskTopLrc", true);
        if (b) {
            data[0] = Constant.CMD_SHOW_DESKTOP_LRC;
        } else {
            data[0] = Constant.CMD_NOT_SHOW_DESKTOP_LRC;
        }
        bundle.clear();
        data[1] = 0;
        bundle.putIntArray(KEY, data);
        // 将命令及数据打包到Intent里
        intent.putExtras(bundle);
        // 发送广播
        this.sendBroadcast(intent);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if (Constant.D)
            Log.e(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (Constant.D)
            Log.e(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        if (Constant.D)
            Log.e(TAG, "onRestart()");
        super.onRestart();
        flag_RE = 3;
    }

    /**
     * 初始化组件,设置监听器
     */
    private void intitView() {
        mTextView_music_name = (TextView) findViewById(R.id.music_name);
        mTextView_music_singer = (TextView) findViewById(R.id.music_singer);
        mTextView_music_start_time = (TextView) findViewById(R.id.music_start_time);
        mTextView_music_end_time = (TextView) findViewById(R.id.music_end_time);
        mTextView_show_lrc = (LrcView) findViewById(R.id.show_lrc);

        mImageView_music_loop = (ImageView) findViewById(R.id.music_play_loop);
        mImageView_music_random = (ImageView) findViewById(R.id.music_play_random);
        mImageView_music_previous = (ImageView) findViewById(R.id.music_previous);
        mImageView_music_play = (ImageView) findViewById(R.id.music_play);
        mImageView_music_next = (ImageView) findViewById(R.id.music_next);
        mRelativeLayout = (FrameLayout) this.findViewById(R.id.relative_layout_id);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar_id);
        mSeekBar.setOnSeekBarChangeListener(MusicService.seekBarChangeListenerFactory());

        // 为各组件添加响应的监听器
        mImageView_music_loop.setOnClickListener(this);
        mImageView_music_random.setOnClickListener(this);
        mImageView_music_previous.setOnClickListener(this);
        mImageView_music_play.setOnClickListener(this);
        mImageView_music_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 将bundle清空，填充新的内容
        // 由于Activity与Receiver之间通信，主要是传递哪个按钮被按下了，当前使用什么播放模式
        // 以及播放列表中哪首歌被选择了， 所以在发送的广播中，使用两个整数代表命令与数据
        // data[0] 表示CMD命令，命令内容见前面定义的"广播命令"
        // data[1] 表示传递数据，如果命令不含有参数，则为0
        bundle.clear();
        switch (v.getId()) {
            case R.id.music_play:
                data[0] = Constant.CMD_PLAY;
                data[1] = 0;
                bundle.putIntArray(KEY, data);
                break;
            case R.id.music_next:
                data[0] = Constant.CMD_NEXT;
                data[1] = 0;
                bundle.putIntArray(KEY, data);
                break;
            case R.id.music_previous:
                data[0] = Constant.CMD_PREV;
                data[1] = 0;
                bundle.putIntArray(KEY, data);
                break;
            case R.id.music_play_loop:
                String msg = "";
                count_click++;
                if (count_click == 1) {
                    // 进入无限循环模式
                    data[0] = Constant.CMD_LOOP;
                    data[1] = Constant.CMD_LOOP;
                    mImageView_music_loop
                            .setImageResource(R.drawable.play_loop_sel);
                    msg = "重复播放所有歌曲";
                } else if (count_click == 2) {
                    // 进入单曲循环模式
                    data[0] = Constant.CMD_SINGLE;
                    data[1] = Constant.CMD_SINGLE;
                    mImageView_music_loop
                            .setImageResource(R.drawable.play_loop_spec);
                    msg = "重复播放当前歌曲";
                } else if (count_click == 3) {
                    // 恢复默认,进入一遍播放完后就暂停模式
                    data[0] = Constant.CMD_ORDER_BY_ORDER;
                    data[1] = Constant.CMD_ORDER_BY_ORDER;
                    count_click = 0;
                    mImageView_music_loop.setImageResource(R.drawable.play_loop);
                    msg = "重复播放已经关闭";
                }
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                bundle.putIntArray(KEY, data);
                break;
            // 随机播放
            case R.id.music_play_random:
                String s = "";
                if (random_model_open == false) {
                    data[0] = Constant.CMD_RANDOM;
                    data[1] = Constant.CMD_RANDOM;
                    mImageView_music_random.setImageResource(R.drawable.play_random_sel);
                    s = "随机播放已经打开";
                    random_model_open = true;
                } else {
                    data[0] = Constant.CMD_ORDER_BY_ORDER;
                    data[1] = Constant.CMD_ORDER_BY_ORDER;
                    mImageView_music_random.setImageResource(R.drawable.play_random);
                    s = "随机播放已经关闭";
                    random_model_open = false;
                }
                bundle.putIntArray(KEY, data);
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        // 将命令及数据打包到Intent里
        intent.replaceExtras(bundle);
        // 发送广播
        this.sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constant.D)
            Log.e(TAG, "onDestroy()");
        // 解除加速度传感器
        // mSensorManager.unregisterListener(this);
        //广告：以下方法将用于释放SDK占用的系统资源
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mRelativeLayout.setBackgroundResource(R.drawable.listbg1);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mRelativeLayout.setBackgroundResource(R.drawable.listbg);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_ring:
                showSetRingDialog();
                break;
            case R.id.set_more:
                startActivity(new Intent(this, AppPreferenceActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.set_more_app:
                //来打开广告墙
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置手机各种铃声的对话框
     */
    private void showSetRingDialog() {

        new AlertDialog.Builder(this)
                .setTitle(getResources()
                        .getString(R.string.set_ring_dialog_title))
                .setIcon(R.drawable.info)
                .setSingleChoiceItems(R.array.set_ring_dialog_msg, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                                setVoice(which);
                            }
                        }).show();
    }

    /**
     * 获取歌曲的(绝对路径)
     */
    private String getPath() {
        String path = null;
        String title = MusicActivity.mTextView_music_name.getText().toString();
        // 根据
        Cursor cursor = this.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.TITLE + "=?", new String[]{title},
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        }
        cursor.close();
        return path;
    }

    /**
     * @param id 想要设置为哪种铃声的标示：
     */
    private void setVoice(int id) {
        String path = getPath();
        ContentValues cv = new ContentValues();
        Uri newUri = null;
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(path);

        if (Constant.D)
            Log.e(TAG, "---setRing---" + uri.toString());
        // 查询音乐文件在媒体库是否存在
        Cursor cursor = this.getContentResolver().query(uri, null,
                MediaStore.MediaColumns.DATA + "=?", new String[]{path},
                null);
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            String _id = cursor.getString(0);
            switch (id) {
                case Constant.RINGTONE:
                    cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                    cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                    cv.put(MediaStore.Audio.Media.IS_ALARM, false);
                    cv.put(MediaStore.Audio.Media.IS_MUSIC, false);
                    break;
                case Constant.NOTIFICATION:
                    cv.put(MediaStore.Audio.Media.IS_RINGTONE, false);
                    cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
                    cv.put(MediaStore.Audio.Media.IS_ALARM, false);
                    cv.put(MediaStore.Audio.Media.IS_MUSIC, false);
                    break;

                case Constant.ALARM:
                    cv.put(MediaStore.Audio.Media.IS_RINGTONE, false);
                    cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                    cv.put(MediaStore.Audio.Media.IS_ALARM, true);
                    cv.put(MediaStore.Audio.Media.IS_MUSIC, false);
                    break;
                case Constant.ALL:
                    cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                    cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
                    cv.put(MediaStore.Audio.Media.IS_ALARM, true);
                    cv.put(MediaStore.Audio.Media.IS_MUSIC, false);
                    break;
                default:
                    break;
            }

            // 把需要设为铃声的歌曲更新铃声库
            getContentResolver().update(uri, cv,
                    MediaStore.MediaColumns.DATA + "=?", new String[]{path});
            newUri = ContentUris.withAppendedId(uri, Long.valueOf(_id));
            // 下一步为关键代码：
            String title = MusicActivity.mTextView_music_name.getText()
                    .toString();
            switch (id) {
                case Constant.RINGTONE:
                    RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, newUri);
                    Toast.makeText(MusicActivity.this, "已经将   " + title + " 设置为手机铃声", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.NOTIFICATION:
                    RingtoneManager.setActualDefaultRingtoneUri(this,
                            RingtoneManager.TYPE_NOTIFICATION, newUri);
                    Toast.makeText(MusicActivity.this, "已经将   " + title + " 设置为手机通知铃声", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.ALARM:
                    RingtoneManager.setActualDefaultRingtoneUri(this,
                            RingtoneManager.TYPE_ALARM, newUri);
                    Toast.makeText(MusicActivity.this,
                            "已经将   " + title + " 设置为手机闹铃铃声", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case Constant.ALL:
                    RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALL, newUri);
                    Toast.makeText(MusicActivity.this, "已经将   " + title + " 设置为手机所有所有", Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
            cursor.close();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 处理精准度改变
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
		/* 手机晃动，当前时间 */
        long current_time = java.lang.System.currentTimeMillis();
        long diffTime = (current_time - last_time);
        /* 如果持续的时间超过100毫秒，就计算晃动幅度 */
        if (diffTime > 100) {
            last_time = current_time;
            x = event.values[SensorManager.DATA_X];
            y = event.values[SensorManager.DATA_Y];
            z = event.values[SensorManager.DATA_Z];
            float speed = 10000
                    * Math.abs(x + y + z - last_x - last_y - last_z) / diffTime;
            // Log.e(TAG, "speed=" + speed);
            /* 检测到摇晃后执行 */
            if (speed > HOLD_SPEED) {
                if (Constant.D)
                    Log.e(TAG, "》》》》》》》》》》》》》next了》》》》》》》》》》》》》》》");
                // 进行换歌操作
                data[0] = Constant.CMD_NEXT;
                data[1] = 0;
                bundle.putIntArray(KEY, data);
                // 将命令及数据打包到Intent里
                intent.replaceExtras(bundle);
                // 发送广播
                this.sendBroadcast(intent);
            }
        }
        /* 重新开始检测 */
        last_x = x;
        last_y = y;
        last_z = z;
    }
}