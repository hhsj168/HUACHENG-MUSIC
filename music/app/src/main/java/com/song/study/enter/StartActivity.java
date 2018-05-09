package com.song.study.enter;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.song.study.R;
import com.song.study.conts.SharedKeywords;
import com.song.study.main.MainActivity;
import com.song.study.adpter.MyViewPagerAdapter;
import com.song.study.socket.InetAddressTest;
import com.song.study.view.autoplayviewpager.AutoPlayViewPager;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    private static final String strs[] = {"换歌只需摇摇手机", "功能简介", "点击进入..."};
    private AutoPlayViewPager viewPager;
    private TextView textView;

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSON_REQUESTCODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        textView = (TextView) findViewById(R.id.tv_title);
        textView.setText(strs[0]);
        viewPager = (AutoPlayViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyViewPagerAdapter(this));
        viewPager.setFlipInterval(5000);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(needPermissions);
        } else {
            jump();
        }
    }

    private void jump() {
        // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
        SharedPreferences spf = getSharedPreferences(SharedKeywords.SHARED_NAME_SETTING, 0);
        if (spf.getBoolean(SharedKeywords.SETTING_IS_FIRST_RUN, true)) {
            spf.edit().putBoolean(SharedKeywords.SETTING_IS_FIRST_RUN, false).commit();
            viewPager.startAutoFlip();
        } else {
            openActivity(null);
            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (viewPager != null) {
            viewPager.stopAutoFlip();
        }
    }

    /**
     * @param permissions
     * @since 2.5.0
     * requestPermissions方法是请求某一权限，
     */

    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        } else {
            jump();
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     * checkSelfPermission方法是在用来判断是否app已经获取到某一个权限
     * shouldShowRequestPermissionRationale方法用来判断是否
     * 显示申请权限对话框，如果同意了或者不在询问则返回false
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                needRequestPermissonList.add(perm);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                    needRequestPermissonList.add(perm);
                }
            }
        }
        return needRequestPermissonList;
    }


    /**
     * 检测是否所有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限结果的回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            } else {
                jump();
            }
        }
    }


    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);
        builder.show();
    }


    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    public void openActivity(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
    }

    private class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            textView.setText(strs[arg0]);
        }
    }
}
