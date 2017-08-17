package com.song.study.musicobject;

import android.net.Uri;

public class Music {


    public static final String MUSIC_TYPE_MP3 = "mp3";
    public static final String MUSIC_TYPE_WAV = "wav";
    public static final String MUSIC_TYPE_WMA = "wma";

    /**
     * 歌曲文件的名称 /带后缀(eg：.mp3)
     */

    private String type;

    private String name;
    /**
     * 歌曲的名称
     */
    private String title;
    /**
     * 歌曲的歌手名
     */
    private String singer;
    /**
     * 歌曲的专辑名
     */
    private String album;
    /**
     * 歌曲文件的全路径
     */
    private String url;
    /**
     * 歌曲文件的大小
     */
    private long size;
    /**
     * 歌曲的总播放时长
     */
    private long time;
    /**
     * 歌曲文件的发行日期
     */
    private String year;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * ------------------- 一系列的对外接口 -------------------
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private long id;

    private long album_id;

    private Uri uri;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
