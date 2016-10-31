package com.song.study.musicutil;

import android.util.Log;

import com.song.study.conts.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// 处理歌词文件的类
public class LrcProcess {


    static String TAG = "HHSJ的APP";
    private List<LrcContent> LrcList;

    private LrcContent mLrcContent;

    public LrcProcess() {

        mLrcContent = new LrcContent();
        LrcList = new ArrayList<LrcContent>();
    }

    /**
     * 读取歌词文件的内容
     */
    public String readLRC(String song_path) {

        // 清空原来的哦
        if (!LrcList.isEmpty()) {
            LrcList.clear();
        }
        StringBuilder stringBuilder = new StringBuilder();
        song_path = song_path.toLowerCase();

        File f = new File(song_path.replace(".mp3", ".lrc"));
        if (Constant.D)
            Log.e("-----------", "" + song_path.replace(".mp3", ".lrc"));
        try {
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis, "GBK");
            BufferedReader br = new BufferedReader(isr);
            String s = "";
            // 一次读一句歌词哦
            while ((s = br.readLine()) != null) {
                // 替换字符
                // s=[01:30.57]感恩的心 感谢有你
                s = s.replace("[", "");
                s = s.replace("]", "@");
                // a=01:20.57@感恩的心 感谢有你

                // 分离"@"字符
                String splitLrc_data[] = s.split("@");
                // data[0]=01:20.57
                // data[1]=感恩的心 感谢有你
                if (splitLrc_data.length > 1) {
                    mLrcContent.setLrc(splitLrc_data[1]);

                    // 处理歌词取得歌曲时间
                    int LrcTime = TimeStr(splitLrc_data[0]);

                    mLrcContent.setLrc_time(LrcTime);

                    // 添加进列表数组
                    LrcList.add(mLrcContent);
                    // 创建对象
                    mLrcContent = new LrcContent();
                }
            }
            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            stringBuilder.append("亲...木有歌词文件，赶紧去下载！...");
        } catch (IOException e) {
            e.printStackTrace();
            stringBuilder.append("亲...木有读取到歌词啊！...");
        }

        return stringBuilder.toString();
    }

    /**
     * 解析歌曲时间处理类
     *
     * @param timeStr like 01:20.57
     * @return int 毫秒数
     */
    public int TimeStr(String timeStr) {
        Log.i(TAG, timeStr);
        timeStr = timeStr.replace(":", ".");// 01.20.57
        timeStr = timeStr.replace(".", "@");
        timeStr = timeStr.replace("@", "-");
        String timeData[] = timeStr.split("-");
        // 01@13@01
        // 分离出分、秒并转换为整型
        int minute = Integer.parseInt(timeData[0]);
        int second = Integer.parseInt(timeData[1]);
        int millisecond = Integer.parseInt(timeData[2]);
        // 计算上一行与下一行的时间转换为毫秒数
        int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
        return currentTime;
    }

    public List<LrcContent> getLrcContent() {
        return LrcList;
    }

    // 获得每句歌词和对应的时间(毫秒)并返回的类

    public class LrcContent {
        private String Lrc;
        private int Lrc_time;

        public String getLrc() {
            return Lrc;
        }

        public void setLrc(String lrc) {
            Lrc = lrc;
        }

        public int getLrc_time() {
            return Lrc_time;
        }

        public void setLrc_time(int lrc_time) {
            Lrc_time = lrc_time;
        }
    }
}
