package com.song.study.conts;

/**
 * 用到的常量
 *
 * @author 顾修忠
 */
public class Constant {

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
