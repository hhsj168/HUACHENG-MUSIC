package com.song.study.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.song.study.MessageEvent;
import com.song.study.MyApplication;
import com.song.study.R;
import com.song.study.activity.AboutAuthor;
import com.song.study.conts.IntentKeywords;
import com.song.study.musicobject.Music;
import com.song.study.conts.Constant;
import com.song.study.musicutil.LrcProcess;
import com.song.study.musicutil.LrcProcess.LrcContent;
import com.song.study.view.LrcView;
import com.song.study.musicutil.MusicUtil;
import com.song.study.view.MyFloatView;
import com.song.study.receiver.InOutCallReceiver;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 后台服务类，用于播放音乐 处理接收到的广播信息，和进进度条的线程处理
 *
 * @author Kevin Song
 */
public class MusicService extends Service {

    // 自定义广播接收器的ACTION
    public static final String ACTION = "MUSIC";
    private static final String TAG = "MusicService";
    private static final int UPDATE_UI = 0X1;
    public static List<Music> recentMusics = new ArrayList<Music>();
    private static MediaPlayer mediaPlayer;
    // 处理歌词的类
    public LrcProcess mLrcProcess;
    // 显示歌词的组件
    public LrcView mLrcView;
    private BroadcastReceiver receiver = null;
    private InOutCallReceiver receiver_inoutcall = null;
    private List<Music> musics /* = MusicListFrament.musics */;
    // 当前播放的下标
    private int current_index = -1;
    // 当前播放的下标
    private int current_index_playMode = Constant.CMD_ORDER_BY_ORDER;
    // 进度条线程
    private MyThread thread;
    private WindowManager wm = null;
    private MyFloatView vFloatView = null;

    // 任务栏中的信息通知管理器
    private NotificationManager mNotificationManager;
    private boolean isShowDestTop = true;
    // 创建对象
    private List<LrcContent> lrcList = new ArrayList<LrcContent>();
    // 初始化歌词检索值
    private int index = 0;
    // 初始化歌曲播放时间的变量
    private int CurrentTime = 0;
    // 初始化歌曲总时间的变量
    private int CountTime = 0;
    /**
     * 处理线程发过来的信息
     */
    private final Handler mHandler = new MyHandler(this);

    private MessageEvent updateUiEvent;

    private static MusicService musicService;

    public static final MusicService getInstance() {
        return musicService;
    }

    private void updateUi(Message msg) {
        if (updateUiEvent == null) {
            updateUiEvent = new MessageEvent("update_ui");

        }
        int[] positions = new int[2];
        positions[0] = mediaPlayer.getCurrentPosition();
        positions[1] = mediaPlayer.getDuration();
        updateUiEvent.setData(positions);
        notifyMusic(updateUiEvent);

//        int pos = mediaPlayer.getCurrentPosition() * MusicActivity.mSeekBar.getMax() / mediaPlayer.getDuration();
//        MusicActivity.mSeekBar.setProgress(pos);
//        MusicActivity.mTextView_music_start_time.setText(MusicUtil.formatTime(mediaPlayer.getCurrentPosition()));
    }


    /**
     * 添加到最近播放列表中
     */
    public static void addRecentList(Music m) {
        if (!recentMusics.contains(m)) {
            recentMusics.add(m);
        } else {

        }
    }


    @Override
    public void onCreate() {
        // 默认加载所有的歌曲
        musics = MusicUtil.getAllMusics(getApplicationContext());
        // 定制广播接收器
        receiver = receiverFactory();
        // 注册广播接收器
        this.registerReceiver(receiver, new IntentFilter(ACTION));
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        filter.addAction("android.intent.action.PHONE_STATE");
        // 注册来去电广播，(没有用常驻型的注册方式)
        this.registerReceiver(receiver_inoutcall, filter);
        // 创建MediaPlayer对象
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置监听器，监听当歌曲播放完时要做...
        mediaPlayer.setOnCompletionListener(oncompleteion_listener);

        // /////////////////////// 初始化歌词配置 /////////////////////// //
        mLrcProcess = new LrcProcess();

        // 1:获取NotificationManager对象,负责“发出”与“取消” Notification。
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 创建悬浮窗口
        createView();
        super.onCreate();
        musicService = this;
    }


    /**
     * 显示设置Notification
     **/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(String tickerText, String contentTitle, String contentText, int id, int resId, int defaults) {
        Intent intent = new Intent(this, AboutAuthor.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        Notification notification = builder
                .setSmallIcon(resId)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(contentIntent)
                .setDefaults(defaults)
                .setTicker(tickerText)
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(id, notification);

    }

    /**
     * 判断是否显示歌词
     */
    private boolean isShowLrc() {
        SharedPreferences preferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        return preferences.getBoolean("LRC_IS_SHOW", true);
    }

    private static MessageEvent seekbarChangeEvent;

    // SeekBar的监听器
    public static OnSeekBarChangeListener seekBarChangeListenerFactory() {
        return new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                if (seekbarChangeEvent == null) {
                    seekbarChangeEvent = new MessageEvent("seekbar_change");
                }
                seekbarChangeEvent.setData(arg1);
                MusicService.notifyMusic(seekbarChangeEvent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                mediaPlayer.seekTo(arg0.getProgress() * mediaPlayer.getDuration() / arg0.getMax());
            }
        };
    }

    private BroadcastReceiver receiverFactory() {

        return new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                int[] data = intent.getExtras().getIntArray(IntentKeywords.KEY);
                current_index = intent.getIntExtra("index", current_index);
                if (Constant.D)
                    Log.e(TAG, "current_index=" + current_index);
                switch (data[0]) {
                    case Constant.CMD_PLAY_SPEC:
                        loadSong();
                        break;
                    case Constant.CMD_PLAY:
                        play();
                        break;
                    case Constant.CMD_NEXT:
                        next();
                        break;
                    case Constant.CMD_PREV:
                        previous();
                        break;
                    case Constant.CMD_SINGLE:
                        setCurrent_index_playMode(data[1]);
                        break;
                    case Constant.CMD_LOOP:
                        setCurrent_index_playMode(data[1]);
                        break;
                    case Constant.CMD_ORDER_BY_ORDER:
                        setCurrent_index_playMode(data[1]);
                        break;
                    case Constant.CMD_RANDOM:
                        setCurrent_index_playMode(data[1]);
                        break;
                /* 删除正在播放的歌曲 */
                    case Constant.CMD_DEL:
                        // 清空，重新加载
                        musics.clear();
                        musics = MusicUtil.getAllMusics(getApplicationContext());
                        if (Constant.D)
                            Log.e("onreceiver(", "musics.size//" + musics.size());
                        if (Constant.D)
                            Log.e("onreceiver(", "musics.size//" + current_index);
                        next();
                        break;
                /* 当播放的是某个专辑中歌曲时 */
                    case Constant.CMD_PLAY_ALBUM_SPEC:
                        if (Constant.D)
                            Log.e(TAG, "---CMD_PLAY_SPEC_CHANGE_LIST--");
                        // 清空，重新加载
                        musics.clear();
                        String albumname = intent.getStringExtra("albumname");
                        if (Constant.D)
                            Log.e(TAG, "==albumname=" + albumname);
                        musics = MusicUtil.getMusicInOneAlum(getApplicationContext(), albumname);
                        break;
                /* 当播放的是默认专辑中歌曲时 */
                    case Constant.CMD_PLAY_ALBUM_Default:
                        if (Constant.D)
                            Log.e(TAG, "---CMD_PLAY_ALBUM_Default--");
                        // 清空，重新加载
                        musics.clear();
                        musics = MusicUtil.getAllMusics(getApplicationContext());
                        break;
                    // 播放最近的播放过的歌曲
                    case Constant.CMD_PLAY_ALBUM_RENCENT:
                        if (Constant.D)
                            Log.e(TAG, "---CMD_PLAY_ALBUM_RENCENT--");
                        // 清空，重新加载
                        musics.clear();
                        if (!recentMusics.isEmpty()) {
                            musics = recentMusics;
                        } else {
                            musics = MusicUtil.getAllMusics(getApplicationContext());
                        }

                        if (Constant.D)
                            Log.e(TAG, "----recentMusics.size()==" + recentMusics.size());
                        break;
                    // 显示歌词
                    case Constant.CMD_SHOW_LRC:
                        playLRC(current_index);
                        break;
                    // 不显示歌词
                    case Constant.CMD_NOT_SHOW_LRC:

                        MessageEvent event = new MessageEvent("hide_lrc");
                        notifyMusic(event);

//                        MusicActivity.mTextView_show_lrc.SetIndex(-1);
//                        MusicActivity.mTextView_show_lrc.invalidate();
                        // 移除歌词线程mRunnable
                        mHandler.removeCallbacks(mRunnable);
                        break;
                    // 显示桌面歌词
                    case Constant.CMD_SHOW_DESKTOP_LRC:
                        isShowDestTop = true;
                        break;
                    // 不显示桌面歌词
                    case Constant.CMD_NOT_SHOW_DESKTOP_LRC:
                        isShowDestTop = false;
                        vFloatView.SetIndex(-1);
                        vFloatView.invalidate();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 播放歌曲
     */
    private void play() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            // 清除所有的Notification
            mNotificationManager.cancelAll();
            //悬浮窗不可见
            vFloatView.setVisibility(View.GONE);
            MessageEvent event = new MessageEvent("pause_song");
            event.setData(current_index);
            EventBus.getDefault().post(event);
        } else {
            mediaPlayer.start();
            showNotification("正在播放", "正在播放歌曲", musics.get(current_index)
                            .getTitle(), R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                    Notification.DEFAULT_VIBRATE);
            //悬浮窗可见
            vFloatView.setVisibility(View.VISIBLE);
            MessageEvent event = new MessageEvent("play_song");
            event.setData(current_index);
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 加载歌曲
     **/
    private void loadSong() {
        mediaPlayer.reset();
        mNotificationManager.cancelAll();
        showNotification("正在播放", "正在播放歌曲",
                musics.get(current_index).getTitle(), R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, Notification.DEFAULT_VIBRATE);
        try {
            mediaPlayer.setDataSource(musics.get(current_index).getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();
            addRecentList(musics.get(current_index));
            if (thread != null) {
                thread.stopThread();
            }
            // 更新进度条
            thread = new MyThread();
            thread.start();
            // 同步歌词
            if (isShowLrc()) {
                playLRC(current_index);
            }
            MessageEvent event = new MessageEvent("load_song");
            event.setData(current_index);
            EventBus.getDefault().post(event);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 歌词同步处理类,找出下句歌词的index
     */
    public int LrcIndex() {
        if (mediaPlayer.isPlaying()) {
            // 获得歌曲播放在哪的时间
            CurrentTime = mediaPlayer.getCurrentPosition();
            // 获得歌曲总时间长度
            CountTime = mediaPlayer.getDuration();
        }
        if (CurrentTime < CountTime) {
            for (int i = 0; i < lrcList.size(); i++) {
                if (i < lrcList.size() - 1) {
                    if (CurrentTime < lrcList.get(i).getLrc_time() && i == 0) {
                        index = i;
                    }
                    if (CurrentTime > lrcList.get(i).getLrc_time()
                            && CurrentTime < lrcList.get(i + 1).getLrc_time()) {
                        index = i;
                    }
                }
                if (i == lrcList.size() - 1
                        && CurrentTime > lrcList.get(i).getLrc_time()) {
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * 读取歌词
     */
    public void playLRC(int index) {
        // 读取歌词文件
        mLrcProcess.readLRC(musics.get(index).getUrl());
        // 传回处理后的歌词文件
        lrcList = mLrcProcess.getLrcContent();
        if (!lrcList.isEmpty()) {
            MessageEvent event = new MessageEvent("play_lrc");
            event.setData(lrcList);
            notifyMusic(event);
//            MusicActivity.mTextView_show_lrc.setSentenceEntities(lrcList);
            this.vFloatView.setSentenceEntities(lrcList);
            // 启动线程
            mHandler.post(mRunnable);
        }
    }

    /***
     * 创建悬浮窗口
     */
    private void createView() {
        vFloatView = new MyFloatView(getApplicationContext());
        // 获取WindowManager
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        // 设置LayoutParams(全局变量）相关参数
        WindowManager.LayoutParams wmParams = ((MyApplication) getApplication()).getMywmParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        // 系统提示类型,重要
        wmParams.format = 1;
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE; // 设置window type
        // 不能抢占聚焦点
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制
        wmParams.alpha = 1.0f;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;
        // 设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 显示myFloatView


        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                wm.addView(vFloatView, wmParams);
            }
        }

    }

    /**
     * 根据当前的播放模式 播放下一首
     */
    private void next() {
        switch (getCurrent_index_playMode()) {
            case Constant.CMD_RANDOM:
                randomplay();
                break;
            case Constant.CMD_ORDER_BY_ORDER:
                if (++current_index >= musics.size()) {
                    current_index = 0;
                    loadSong_order_by_order();
                    return;
                }
                break;
            case Constant.CMD_PLAY_SPEC:
            case Constant.CMD_LOOP:
                if (++current_index >= musics.size()) {
                    current_index = 0;
                }
                break;
            case Constant.CMD_SINGLE:
                break;
            default:
                break;
        }
        loadSong();
    }

    /**
     * 根据当前的播放模式 播放上一首
     */
    private void previous() {
        switch (getCurrent_index_playMode()) {
            case Constant.CMD_RANDOM:
                randomplay();
                break;
            case Constant.CMD_ORDER_BY_ORDER:
                if (--current_index <= 0) {
                    current_index = musics.size() - 1;
                    loadSong_order_by_order();
                    return;
                }
                break;
            case Constant.CMD_LOOP:
                if (--current_index <= 0) {
                    current_index = musics.size() - 1;
                }
                break;
            case Constant.CMD_SINGLE:
                break;
            default:
                break;
        }
        loadSong();
    }

    /**
     * 处理单次循环播放所有的歌曲
     */
    private void loadSong_order_by_order() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musics.get(current_index).getUrl());
            mediaPlayer.prepare();
            addRecentList(musics.get(current_index));
            if (isShowLrc()) {
                playLRC(current_index);
            }
            MessageEvent event = new MessageEvent("load_song_order_by_order");
            notifyMusic(event);
//            MusicActivity.mImageView_music_play.setImageResource(R.drawable.ic_media_play);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 产生随机下标
     */
    private void randomplay() {
        current_index = new Random().nextInt(musics.size());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 解除广播接收器
        unregisterReceiver(receiver);
        unregisterReceiver(receiver_inoutcall);
        if (thread != null) {
            thread.stopThread();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mHandler.removeCallbacks(mRunnable);
        mNotificationManager.cancelAll();
        // 删除组件销毁悬浮窗口
        wm.removeView(vFloatView);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 获取当前歌曲的播放模式
     **/
    public int getCurrent_index_playMode() {
        return current_index_playMode;
    }

    /**
     * 设置当前歌曲的播放模式
     *
     * @param current_index_playMode
     **/
    public void setCurrent_index_playMode(int current_index_playMode) {
        this.current_index_playMode = current_index_playMode;
    }

    public int getIndex() {
        return current_index;
    }


    // 歌词滚动线程
    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {

            MessageEvent event = new MessageEvent("scroll_lrc");
            event.setData(LrcIndex());
            notifyMusic(event);

//            // 播放界面的显示歌词
//            MusicActivity.mTextView_show_lrc.SetIndex(LrcIndex());
//            MusicActivity.mTextView_show_lrc.invalidate();


            // 桌面歌词
            if (isShowDestTop) {
                vFloatView.SetIndex(LrcIndex());
                vFloatView.invalidate();
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };
    // 音乐播放完成监听事件
    OnCompletionListener oncompleteion_listener = new OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer palPlayer) {
            // 停止播放进度线程
            if (thread != null) {
                thread.stopThread();
            }
            mHandler.removeCallbacks(mRunnable);


            notifyMusic(new MessageEvent("stop_play"));

//            MusicActivity.mTextView_show_lrc.setText("");
            vFloatView.setText("");
            next();
        }

    };


    // 线程内部类，用异步线程 Handler更新 主线程(UI线程)
    private class MyThread extends Thread {
        // 线程关闭的条件
        private boolean isStop = false;

        @Override
        public void run() {
            super.run();
            while (!isStop) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = mHandler.obtainMessage(UPDATE_UI);
                mHandler.sendMessage(msg);
            }
        }

        // 线程停止
        public void stopThread() {
            isStop = true;
        }
    }


    private static class MyHandler extends Handler {

        private WeakReference<MusicService> service;

        public MyHandler(MusicService service) {
            this.service = new WeakReference<MusicService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            if (service.get() != null) {
                service.get().updateUi(msg);
            }
        }
    }

    static List<Notificationer> mNotificationList;

    public interface Notificationer {
        void notifyMusic(MessageEvent event);
    }

    public static void notifyMusic(MessageEvent event) {
        if (mNotificationList != null && mNotificationList.size() > 0) {
            for (Notificationer notificationer : mNotificationList) {
                notificationer.notifyMusic(event);
            }
        }
    }

    public static void addListener(Notificationer notificationer) {
        if (mNotificationList == null) {
            mNotificationList = new ArrayList<>();
        }
        if (!mNotificationList.contains(notificationer)) {
            mNotificationList.add(notificationer);
        }
    }


}
