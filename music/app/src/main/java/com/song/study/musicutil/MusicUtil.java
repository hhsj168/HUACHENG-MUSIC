package com.song.study.musicutil;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import com.song.study.R;
import com.song.study.musicobject.Music;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取手机中的歌曲的类,格式化歌曲时间
 */
public class MusicUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("mm:ss");

    /**
     * 返回所有在外部存储卡上的音乐文件的信息 获取手机歌曲，利用系统提供的接口，这样我们就不必自己写方法判断ＳＤ卡是否存在，以及遍历歌曲等；
     *
     * @param context
     */
    public static List<Music> getAllMusics(Context context) {
        ContentResolver mContentResolver = context.getContentResolver();
        // 返回所有歌曲的集合Set
        Cursor cursor = mContentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        return getMusicInfos(cursor);
    }

    /**
     * 格式化时间 SimpleDateFormat类很实用的类
     *
     * @param time
     * @return s
     */
    public static String formatTime(long time) {
        return format.format(time);
    }

    /**
     * 获取一张专辑中的歌曲
     *
     * @param context
     * @param albumname
     */
    public static List<Music> getMusicInOneAlum(Context context, String albumname) {
        ContentResolver mContentResolver = context.getContentResolver();
        // 返回所有的专辑
        Cursor cursor = mContentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.Media.ALBUM + "=?", new String[]{albumname},
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        return getMusicInfos(cursor);
    }

    /**
     * 遍历游标集合，初始化歌曲信息
     *
     * @param cursor
     */
    public static List<Music> getMusicInfos(Cursor cursor) {
        List<Music> musicList = new ArrayList<Music>();
        if (cursor.moveToFirst()) {
            do {
                Music music = new Music();
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                if (singer.equalsIgnoreCase("<unknown>") || singer.equalsIgnoreCase("unknown")) {
                    singer = "未知艺术家";
                }
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String year = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR));
                String type = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
                long album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                music.setId(id);
                music.setAlbum_id(album_id);
                music.setUri(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), album_id));
                music.setTitle(title);
                // String showName=name.substring(0, name.lastIndexOf('.'));
                music.setTitle(title);
                music.setAlbum(album);
                music.setName(name);
                music.setSize(size);
                music.setUrl(url);
                music.setTime(time);
                music.setSinger(singer);
                music.setYear(year);

                if ("audio/mpeg".equals(type.trim())) {
                    music.setType("mp3");
                } else if ("audio/x-ms-wma".equals(type.trim())) {
                    music.setType("wma");
                } else if ("audio/x-wav".equals(type.trim())) {
                    music.setType("wav");
                } else if ("audio/ffmpeg".equals(type.trim())) {
                    music.setType("m4a");
                }
                if (time > 60 * 1000 && "mp3".equals(music.getType())) {
                    musicList.add(music);
                }
            } while (cursor.moveToNext());
        }
        Log.e("musiclist.size==", musicList.size() + "");
        cursor.close();
        return musicList;
    }

    /**
     * 获取所有的歌手
     *
     * @param context
     * @return List<String>
     */
    public static List<String> getAllSingers(Context context) {
        int i = 0;
        List<String> list = new ArrayList<String>();
        ContentResolver mContentResolver = context.getContentResolver();
        Cursor cursor = mContentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            do {
                String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                if (singer.equalsIgnoreCase("<unknown>") || singer.equalsIgnoreCase("unknown")) {
                    singer = "未知艺术家";
                    i++;
                    if (i == 1) {
                        list.add(singer);
                    }
                    continue;
                } else {
                    list.add(singer);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 获取所有的歌手
     *
     * @param context
     * @return List<String>
     */
    public static List<String> getAllSingers_Org(Context context) {

        List<String> list = new ArrayList<String>();
        ContentResolver mContentResolver = context.getContentResolver();
        Cursor cursor = mContentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            do {
                String singer = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                list.add(singer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static List<String> getAlbumNameBaseSingers(Context context, String singer) {
        int i = 0;
        List<String> list = new ArrayList<String>();
        ContentResolver mContentResolver = context.getContentResolver();

        Cursor cursor = mContentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.Media.ARTIST + "=?", new String[]{singer},
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            do {
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                if (album.equalsIgnoreCase("music")) {
                    album = "Music";
                    i++;
                    if (i == 1) {
                        list.add(album);
                    }
                    continue;
                } else {
                    list.add(album);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static int getAlbumNumber(Context context, String album) {
        int number = 0;
        ContentResolver mContentResolver = context.getContentResolver();
        Cursor cursor = mContentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.Media.ALBUM + "=?", new String[]{album},
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor.moveToFirst()) {
            do {
                number++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return number;
    }

    public static Bitmap getArtwork(Context context, long song_id, long album_id, boolean allowdefault) {
        if (album_id < 0) {
            if (song_id >= 0) {
                Bitmap bm = getArtworkFromFile(context, song_id, -1);
                if (bm != null) {
                    return bm;
                }
            }
            if (allowdefault) {
                return getDefaultArtwork(context);
            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), album_id);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, S_BITMAP_OPTIONS);
            } catch (FileNotFoundException ex) {
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if (bm == null && allowdefault) {
                            return getDefaultArtwork(context);
                        }
                    }
                } else if (allowdefault) {
                    bm = getDefaultArtwork(context);
                }
                return bm;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                }
            }
        }

        return null;
    }


    public static Bitmap getAlbumPhoto(Context context, Uri uri) {
        Bitmap bm = null;
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bm;
    }

    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
        Bitmap bm = null;
        byte[] art = null;
        String path = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(S_ARTWORK_URI, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException ex) {

        }
        if (bm != null) {
            mCachedBit = bm;
        }
        return bm;
    }

    private static Bitmap getDefaultArtwork(Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher, opts);
    }

    private static final Uri S_ARTWORK_URI = Uri.parse("content://media/external/audio/albumart");
    private static final BitmapFactory.Options S_BITMAP_OPTIONS = new BitmapFactory.Options();
    private static Bitmap mCachedBit = null;
}
