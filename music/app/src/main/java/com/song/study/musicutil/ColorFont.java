package com.song.study.musicutil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * 歌词字体颜色辅助类
 **/
@SuppressLint("NewApi")
public class ColorFont {

    public static final int[] COLORS = {
            Color.RED,
            Color.BLACK,
            Color.CYAN,
            Color.BLUE,
            Color.DKGRAY,
            Color.GRAY,
            Color.GREEN,
            Color.LTGRAY,
            Color.MAGENTA,
            Color.WHITE,
            Color.YELLOW
    };

    private int color_current;// 歌词字体高亮色的颜色
    private int color_not_current;// 歌词字体非高亮色的颜色
    private int font_current_size;// 歌词字体高亮色的大小
    private int font_not_current_size;// 歌词字体非高亮色的大小
    private int font_current_alpha;// 歌词字体高亮色的透明度
    private int font_not_current_alpha;// 歌词字体高亮色的透明度

    /**
     * 构造函数无参
     */
    public ColorFont(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        this.color_current = COLORS[sharedPreferences.getInt("LRCFontColorCurrent", 0)];
        this.color_not_current = COLORS[sharedPreferences.getInt("LRCFontColorNotCurrent", 6)];
        this.font_current_size = sharedPreferences.getInt("LRCFontSizeCurrent", 25);
        this.font_not_current_size = sharedPreferences.getInt("LRCFontSizeNotCurrent", 20);
        this.font_current_alpha = sharedPreferences.getInt("LRCFontAlphaCurrent", 255);// 默认值255,完全不透明。注：alpha(0,255)
        this.font_not_current_alpha = sharedPreferences.getInt("LRCFontAlphaNotCurrent", 100);
    }

    public ColorFont() {
        this.color_current = Color.RED;
        this.color_not_current = Color.GREEN;
        this.font_current_size = 25;
        this.font_not_current_size = 20;
        this.font_current_alpha = 255;
        this.font_not_current_alpha = 100;
    }

    /**
     * 构造函数有参
     *
     * @param color_current         歌词字体高亮色的颜色
     * @param color_not_current     歌词字体非高亮色的颜色
     * @param font_current_size     歌词字体高亮色的大小
     * @param font_not_current_size 歌词字体非高亮色的大小
     */
    public ColorFont(int color_current, int color_not_current, int font_current_size, int font_not_current_size) {
        this.color_current = color_current;
        this.color_not_current = color_not_current;
        this.font_current_size = font_current_size;
        this.font_not_current_size = font_not_current_size;
    }

    public int getColor_current() {
        return color_current;
    }

    public void setColor_current(int color_current) {
        this.color_current = color_current;
    }

    public int getColor_not_current() {
        return color_not_current;
    }

    public void setColor_not_current(int color_not_current) {
        this.color_not_current = color_not_current;
    }

    public int getFont_current_size() {
        return font_current_size;
    }

    public void setFont_current_size(int font_current_size) {
        this.font_current_size = font_current_size;
    }

    public int getFont_not_current_size() {
        return font_not_current_size;
    }

    public void setFont_not_current_size(int font_not_current_size) {
        this.font_not_current_size = font_not_current_size;
    }

    public int getFont_current_alpha() {
        return font_current_alpha;
    }

    public void setFont_current_alpha(int font_current_alpha) {
        this.font_current_alpha = font_current_alpha;
    }

    public int getFont_not_current_alpha() {
        return font_not_current_alpha;
    }

    public void setFont_not_current_alpha(int font_not_current_alpha) {
        this.font_not_current_alpha = font_not_current_alpha;
    }

}
