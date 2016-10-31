package com.song.study.activity;


import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.song.study.R;
import com.song.study.base.BaseActivity;


public class AboutAuthor extends BaseActivity {

    TextView textView1;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_author);
        textView1 = (TextView) findViewById(R.id.textview1);
        textView2 = (TextView) findViewById(R.id.textview2);
        String html = "<font color='red' ><big>音乐播放器-V2.0.</big></font><br>";
        html += "1.该音乐播放器实现了播放器的基本功能.<br>2.该音乐播放器实现了甩歌功能,将手机摇一摇即可换歌.<br>3.该音乐播放器实现了歌词同步和桌面面歌词滚动效果" +
                ".<br>4.该音乐播放器实现了来电话自动暂停播放,通话结束自动播放功能.<br>5.该音乐播放器实现了歌词字体颜色大小控制的功能.<br>6.界面清新时尚," +
                "其它一些功能....--------没有最好,只有更好！--------<br><br>";
        CharSequence charSequence = Html.fromHtml(html);
        textView1.setText(charSequence);
        textView1.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "作者:Kevin Song\n\n";
        text += "Email:ssh617918014@163.com\n";
        textView2.setText(text);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
    }


}
