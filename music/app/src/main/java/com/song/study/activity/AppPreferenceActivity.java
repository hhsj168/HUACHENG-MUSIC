package com.song.study.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.song.study.R;
import com.song.study.musicutil.ColorFont;
import com.song.study.conts.Constant;
import com.song.study.view.LrcView;
import com.song.study.view.MyFloatView;
import com.song.study.service.MusicService;

public class AppPreferenceActivity extends PreferenceActivity implements OnPreferenceChangeListener {
    private static final String TAG = "AppPreferenceActivity";

    private Preference setFontSizePreference;
    private Preference setFontColorPreference;
    private Preference about_preference;
    private Preference set_stream_volume;
    private Preference setFontAlphaPreference;
    private CheckBoxPreference setDeskTopLrcPreference;

    private AudioManager audioManager;// 音量管理者
    private int maxVolume;// 最大音量
    private int currentVolume;// 当前音量
    private SharedPreferences sharedPreferences;
    // size1 高亮色的字体大小下标，size2非 高亮色的字体大小下标
    private int size1, size2;
    // color_index1 高亮色的颜色下标，color_index2非 高亮色的颜色下标
    private int color_index1, color_index2;
    // alpha1 高亮色的透明度，alpha2非 高亮色的透明度
    private int alpha1, alpha2;
    // 选择是设置高亮字体的颜色还是设置非高亮字体的颜色
    private int index_ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 改变PreferenceActivity保存数据的使用的XML文件的名字
        getPreferenceManager().setSharedPreferencesName("setting");
        // 装载配置文件
        addPreferencesFromResource(R.xml.preference_setting);

        // 获取关于作者项对应的Preference对象
        about_preference = findPreference("ABOUT_AUTHOR");
        setFontColorPreference = findPreference("SET_FONT_COLOR");
        setFontSizePreference = findPreference("SET_FONT_SIZE");
        set_stream_volume = findPreference("SetStreamVolume");
        setFontAlphaPreference = findPreference("SET_FONT_ALPHA");
        setDeskTopLrcPreference = (CheckBoxPreference) findPreference("SetDeskTopLrc");

        // 对关于各列表项添加事件
        setFontColorPreference.setOnPreferenceChangeListener(this);
        setFontSizePreference.setOnPreferenceChangeListener(this);
        about_preference.setOnPreferenceChangeListener(this);
        set_stream_volume.setOnPreferenceChangeListener(this);
        setFontAlphaPreference.setOnPreferenceChangeListener(this);
        setDeskTopLrcPreference.setOnPreferenceChangeListener(this);

        // 获取SharedPreferences对象
        // Activity.MODE_PRIVATE(对其它用户不可访问)指定文件的建立模式
        sharedPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);

        // 设置setFontColorPreference和setFontColorPreference,setFontAlphaPreference是否可用
        if (sharedPreferences.getBoolean("LRC_IS_SHOW", true)) {
            setFontColorPreference.setEnabled(true);
            setFontSizePreference.setEnabled(true);
            setFontAlphaPreference.setEnabled(true);
        } else {
            setFontColorPreference.setEnabled(false);
            setFontSizePreference.setEnabled(false);
            setFontAlphaPreference.setEnabled(false);
        }

        // 根据XML文件设置summary属性
        size1 = sharedPreferences.getInt("LRCFontSizeCurrent", 25);
        size2 = sharedPreferences.getInt("LRCFontSizeNotCurrent", 20);
        setFontSizePreference.setSummary("高亮色:" + size1 + "/非高亮色:" + size2);
        final String colors[] = getResources().getStringArray(R.array.set_fontcolor_dialog_msg);
        color_index1 = sharedPreferences.getInt("LRCFontColorCurrent", 0);
        color_index2 = sharedPreferences.getInt("LRCFontColorNotCurrent", 6);
        setFontColorPreference.setSummary("高亮色:" + colors[color_index1] + "/非高亮色:" + colors[color_index2]);
        alpha1 = sharedPreferences.getInt("LRCFontAlphaCurrent", 255);
        alpha2 = sharedPreferences.getInt("LRCFontAlphaNotCurrent", 100);
        setFontAlphaPreference.setSummary("高亮色:" + alpha1 + "/非高亮色:" + alpha2);

        // 获取音量管理器
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获得最大音量
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 获得当前音量
        set_stream_volume.setSummary("当前系统音量:" + currentVolume);
    }

    @Override
    public boolean onPreferenceChange(Preference arg0, Object arg1) {
        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        // 进入关于界面
        if (preference.getKey().equals("ABOUT_AUTHOR")) {
            startActivity(new Intent(this, AboutAuthor.class));
            overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
            // 设置歌词字体的颜色
        } else if (preference.getKey().equals("SET_FONT_COLOR")) {
            showChoiceDialog();
            // 设置歌词字体的大小
        } else if (preference.getKey().equals("SET_FONT_SIZE")) {
            showSetSizeDialog2();
        } else if (preference.getKey().equals("LRC_IS_SHOW")) {
            setFontColorPreference.setEnabled(!setFontColorPreference.isEnabled());
            setFontSizePreference.setEnabled(!setFontSizePreference.isEnabled());
            setFontAlphaPreference.setEnabled(!setFontAlphaPreference.isEnabled());
        } else if (preference.getKey().equals("SetStreamVolume")) {
            setStreamVolumnDialog();
        } else if (preference.getKey().equals("SET_FONT_ALPHA")) {
            setFontAlphaDialog();
        } else if (preference.getKey().equals("SetDeskTopLrc")) {
            setDeskTopLrcPreference.setChecked(!setDeskTopLrcPreference
                    .isChecked());
            Intent intent = new Intent(MusicService.ACTION);
            Bundle bundle = new Bundle();
            int data[] = new int[2];
            // 判断是否显示桌面歌词
            boolean b = setDeskTopLrcPreference.isChecked();
            if (b) {
                data[0] = Constant.CMD_SHOW_DESKTOP_LRC;
            } else {
                data[0] = Constant.CMD_NOT_SHOW_DESKTOP_LRC;
            }
            bundle.clear();
            data[1] = 0;
            bundle.putIntArray("CMD", data);
            // 将命令及数据打包到Intent里
            intent.putExtras(bundle);
            // 发送广播
            this.sendBroadcast(intent);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    // * 设置音量
    private void setStreamVolumnDialog() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.streamvolumn, null);
        final SeekBar seekBarVolume = (SeekBar) view.findViewById(R.id.seekBarStreamVolumn_id);
        seekBarVolume.setMax(maxVolume);
        seekBarVolume.setProgress(currentVolume);
        seekBarVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                currentVolume = progress;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, AudioManager.FLAG_ALLOW_RINGER_MODES);
            }
        });
        new AlertDialog.Builder(AppPreferenceActivity.this)
                .setIcon(R.drawable.info)
                .setTitle(R.string.setstreamvolumn_dialog_title)
                .setView(view)
                .setPositiveButton(R.string.dailog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        set_stream_volume.setSummary("当前系统音量:" + currentVolume);
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showChoiceDialog() {
        CharSequence s[] = new CharSequence[]{"设置高亮字体的颜色", "设置非高亮字体的颜色"};
        new AlertDialog.Builder(AppPreferenceActivity.this)
                .setIcon(R.drawable.info)
                .setTitle(R.string.setfontcolor_dialog_title)
                .setSingleChoiceItems(s, 0, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // showSetColorDialog(which);
                        index_ = which;
                        // dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.dailog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        showSetColorDialog(index_);
                        dialog.dismiss();
                    }
                }).show();
    }

    // 显示设置歌词字体颜色的对话框
    private void showSetColorDialog(final int index) {
        final String colors[] = getResources().getStringArray(
                R.array.set_fontcolor_dialog_msg);

        int i = 0;
        if (index == 0) {
            i = color_index1;
        } else {
            i = color_index2;
        }
        new AlertDialog.Builder(AppPreferenceActivity.this)
                .setIcon(R.drawable.info)
                .setTitle(R.string.setfontcolor_dialog_title)
                .setSingleChoiceItems(colors, i, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // save
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (index == 0) {
                            LrcView.colorFont.setColor_current(ColorFont.COLORS[which]);
                            MyFloatView.colorFont.setColor_current(ColorFont.COLORS[which]);
                            setFontColorPreference.setSummary("高亮色："
                                    + colors[which] + "/" + "非高亮色："
                                    + colors[color_index2]);
                            editor.putInt("LRCFontColorCurrent", which);
                        } else {
                            LrcView.colorFont.setColor_not_current(ColorFont.COLORS[which]);
                            MyFloatView.colorFont.setColor_not_current(ColorFont.COLORS[which]);
                            setFontColorPreference.setSummary("高亮色："
                                    + colors[color_index1] + "/" + "非高亮色："
                                    + colors[which]);
                            editor.putInt("LRCFontColorNotCurrent", which);
                        }
                        editor.commit();
                    }
                })
                .setPositiveButton(R.string.dailog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dailog_cancle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // 显示设置歌词字体大小的对话框
    private void showSetSizeDialog2() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_view, null);
        final TextView text1 = (TextView) view.findViewById(R.id.text1);
        final TextView text2 = (TextView) view.findViewById(R.id.text2);

        final SeekBar seekBar_current = (SeekBar) view.findViewById(R.id.seekBar_current_id);
        final SeekBar seekBar_notcurrent = (SeekBar) view.findViewById(R.id.seekBar_not_current_id);
        seekBar_current.setMax(60);
        seekBar_notcurrent.setMax(60);

        seekBar_current.setProgress(size1);
        seekBar_notcurrent.setProgress(size2);
        text1.setText("高亮歌词的大小:" + seekBar_current.getProgress());
        text2.setText("非高亮歌词的大小:" + seekBar_notcurrent.getProgress());

        seekBar_current.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                text1.setText("高亮歌词的大小:" + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text1.setText("高亮歌词的大小:" + progress);
                MyFloatView.colorFont.setFont_current_size(seekBar_current.getProgress());
            }
        });
        seekBar_notcurrent.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                text2.setText("非高亮歌词的大小:" + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text2.setText("非高亮歌词的大小:" + progress);
            }
        });
        new AlertDialog.Builder(AppPreferenceActivity.this)
                .setIcon(R.drawable.info)
                .setTitle(R.string.setfontsize_dialog_title)
                .setView(view)
                .setPositiveButton(R.string.dailog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setFontSizePreference.setSummary("高亮色："
                                + seekBar_current.getProgress()
                                + "/非高亮色："
                                + seekBar_notcurrent.getProgress());
                        LrcView.colorFont.setFont_current_size(seekBar_current.getProgress());
                        LrcView.colorFont.setFont_not_current_size(seekBar_notcurrent.getProgress());
                        MyFloatView.colorFont.setFont_current_size(seekBar_current.getProgress());
                        MyFloatView.colorFont.setFont_not_current_size(seekBar_notcurrent.getProgress());
                        // save setting.xml
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("LRCFontSizeCurrent", seekBar_current.getProgress());
                        editor.putInt("LRCFontSizeNotCurrent", seekBar_notcurrent.getProgress());
                        // 提交任务保存
                        editor.commit();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dailog_cancle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // 显示设置歌词字体透明度的对话框
    private void setFontAlphaDialog() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_view, null);
        final TextView text1 = (TextView) view.findViewById(R.id.text1);
        final TextView text2 = (TextView) view.findViewById(R.id.text2);

        final SeekBar seekBar_current = (SeekBar) view.findViewById(R.id.seekBar_current_id);
        final SeekBar seekBar_notcurrent = (SeekBar) view.findViewById(R.id.seekBar_not_current_id);
        // 设置进度条的最大值
        seekBar_current.setMax(255);
        seekBar_notcurrent.setMax(255);

        // 设置进度条的当前值
        seekBar_current.setProgress(alpha1);
        seekBar_notcurrent.setProgress(alpha2);
        text1.setText("高亮歌词的透明度:" + seekBar_current.getProgress());
        text2.setText("非高亮歌词的透明度:" + seekBar_notcurrent.getProgress());

        seekBar_current.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                text1.setText("高亮歌词的透明度:" + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text1.setText("高亮歌词的透明度:" + progress);
                MyFloatView.colorFont.setFont_current_alpha(seekBar_current.getProgress());
            }
        });
        seekBar_notcurrent.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                text2.setText("非高亮歌词的透明度:" + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text2.setText("非高亮歌词的透明度:" + progress);
            }
        });
        new AlertDialog.Builder(AppPreferenceActivity.this)
                .setIcon(R.drawable.info)
                .setTitle(R.string.setfontsize_dialog_title)
                .setView(view)
                .setPositiveButton(R.string.dailog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setFontAlphaPreference.setSummary("高亮色："
                                + seekBar_current.getProgress()
                                + "/非高亮色："
                                + seekBar_notcurrent.getProgress());
                        LrcView.colorFont.setFont_current_alpha(seekBar_current.getProgress());
                        LrcView.colorFont.setFont_not_current_alpha(seekBar_notcurrent.getProgress());
                        MyFloatView.colorFont.setFont_current_alpha(seekBar_current.getProgress());
                        MyFloatView.colorFont.setFont_not_current_alpha(seekBar_notcurrent.getProgress());
                        // save in setting.xml
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("LRCFontAlphaCurrent", seekBar_current.getProgress());
                        editor.putInt("LRCFontAlphaNotCurrent", seekBar_notcurrent.getProgress());
                        // 提交任务保存
                        editor.commit();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dailog_cancle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
