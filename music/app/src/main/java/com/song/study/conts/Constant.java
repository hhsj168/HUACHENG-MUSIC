package com.song.study.conts;

/**
 * 用到的常量
 *
 * @author 顾修忠
 */
public interface Constant {

    /**
     * 指令
     */
    interface PlayCommond {
        int CMD_PLAY = 0X0000;// 播放命令
        int CMD_PAUSE = 0X0001;//暂停
        int CMD_PLAY_SPEC = 0X0002; // 播放指定歌曲
        int CMD_LAST = 0X0003; // 上一首命令
        int CMD_NEXT = 0X0004; // 下一首命令}
    }

    interface PlayMode {
        int MODE_SINGLE_ONCE = 0X0005;//单曲一次
        int MODE_SINGLE_LOOP = 0X0006; // 单曲循环模式
        int MODE_ALL_ONCE = 0X0007;//全部一次
        int MODE_ALL_LOOP = 0X0008; // 无限循环模式
        int MODE_RANDOM = 0X0009; // 随机播放模式
    }

    interface LRCCommond {
        int CMD_PLAY_ALBUM_Default = 0X0010; // 默认专辑的命令
        int CMD_PLAY_ALBUM_SPEC = 0X0011; // 改变专辑的命令
        int CMD_PLAY_ALBUM_RENCENT = 0X0012;// 播放最近播放的歌曲
        int CMD_SHOW_LRC = 0X0013;// 显示歌词
        int CMD_NOT_SHOW_LRC = 0X0014;// 不显示歌词
        int CMD_SHOW_DESKTOP_LRC = 0X0015;//显示桌面歌词
        int CMD_NOT_SHOW_DESKTOP_LRC = 0X0016;//不显示桌面歌词
    }

    interface FileCommond {
        int CMD_DEL = 0X0009;// 从手机中删除正在播放的歌曲
    }


    // 广播命令
    public static final int CMD_PLAY = 0;// 播放命令
    public static final int CMD_PLAY_SPEC = 2; // 播放指定歌曲
    public static final int CMD_PREV = 3; // 上一首命令
    public static final int CMD_NEXT = 4; // 下一首命令
    public static final int CMD_SINGLE = 5; // 单曲播放模式
    public static final int CMD_LOOP = 6; // 无限循环模式
    public static final int CMD_ORDER_BY_ORDER = 7; // 进入一遍播放完后就暂停模式
    public static final int CMD_RANDOM = 8; // 随机播放模式
    public static final int CMD_DEL = 9;// 从手机中删除正在播放的歌曲
    public static final int CMD_PLAY_ALBUM_Default = 10; // 默认专辑的命令
    public static final int CMD_PLAY_ALBUM_SPEC = 11; // 改变专辑的命令
    public static final int CMD_PLAY_ALBUM_RENCENT = 12;// 播放最近播放的歌曲
    public static final int CMD_SHOW_LRC = 13;// 显示歌词
    public static final int CMD_NOT_SHOW_LRC = 14;// 不显示歌词
    public static final int CMD_SHOW_DESKTOP_LRC = 15;//显示桌面歌词
    public static final int CMD_NOT_SHOW_DESKTOP_LRC = 16;//不显示桌面歌词
    public static final int FLAG_PART = 0;// 播放部分歌曲的标记

    // 用于判断是否切换了专辑
    public static final int FLAG_ALL = 1;// 播放所有歌曲的标记
    public static final int FLAG_RECENT = 2;// 播放部分歌曲的标记
    // 设置不同类型的手机铃声
    public static final int RINGTONE = 0; // 铃声
    public static final int ALARM = 1; // 闹钟
    public static final int NOTIFICATION = 2; // 通知音
    public static final int ALL = 3; // 所有声音
    //调试指令Debug(调试时打印LOG改为true)
    public static boolean D = true;
}
