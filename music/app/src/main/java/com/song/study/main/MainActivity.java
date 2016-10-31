package com.song.study.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.song.study.R;
import com.song.study.base.BaseActivity;
import com.song.study.main.fragment.AlbumsListFragment;
import com.song.study.main.fragment.ArtistsListFrament;
import com.song.study.main.fragment.MusicListFrament;
import com.song.study.main.fragment.RecentPlaylListFragment;
import com.song.study.musicutil.AnimCommon;
import com.song.study.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    // *********************************
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static int maxTabIndex = 3;
    private TabHost mTabHost;
    private int currentView = 0;

    private List<Fragment> fragmentList = new ArrayList<>();
    // @see覆盖onKeyDown方法 捕获KEYCODE_BACK(返回键)设置点击2次退出的效果
    private boolean is_kill_process = false;
    private long firstTime = 0;
    TabLayout tabLayout;

    String[] tabTitles = {"所有曲目", "艺术家列表", "最近播放", "发现"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.GINGERBREAD) {
            showNotSupportDialog(getApplicationInfo().targetSdkVersion);
        }
        setContentView(R.layout.activity_main);//

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        initTabs();
    }

    private void initTabs() {
        MusicListFrament frament1 = new MusicListFrament();
        ArtistsListFrament frament2 = new ArtistsListFrament();
        AlbumsListFragment fragment3 = new AlbumsListFragment();
        RecentPlaylListFragment fragment4 = new RecentPlaylListFragment();
        fragmentList.add(frament1);
        fragmentList.add(frament2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);

        ViewPager viewPager = (ViewPager) findViewById(R.id.mviewpage);

        FragmentPagerAdapter adapter = new MyFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);

        tabLayout.setupWithViewPager(viewPager);
    }


    class MyFragmentPageAdapter extends FragmentPagerAdapter {
        FragmentManager mFragmentManager;

        FragmentTransaction mCurTransaction;

        Fragment mCurrentPrimaryItem;

        public MyFragmentPageAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public void startUpdate(ViewGroup container) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mCurTransaction == null) {
                // 创建新事务
                mCurTransaction = mFragmentManager.beginTransaction();
            }

            // 获取单项的Id
            final long itemId = getItemId(position);

            // 根据View的Id和单项Id生成名称
            String name = container.getId() + "" + itemId;
            // 根据生成的名称获取FragmentManager中的Fragment
            Fragment fragment = mFragmentManager.findFragmentByTag(name);
            if (fragment != null) {
                // 如果Fragment已被添加到FragmentManager中,则只需要附着到Activity
                mCurTransaction.attach(fragment);
            } else {
                // 如果Fragment未被添加到FragmentManager中,则先获取,再添加到Activity中
                fragment = getItem(position);
                mCurTransaction.add(container.getId(), fragment, container.getId() + "" + itemId);
            }
            // 非当前主要项,需要隐藏相关的菜单及信息
            if (fragment != mCurrentPrimaryItem) {
                fragment.setMenuVisibility(false);
                fragment.setUserVisibleHint(false);
            }

            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (mCurTransaction == null) {
                // 创建新事务
                mCurTransaction = mFragmentManager.beginTransaction();
            }
            // 将Fragment移出UI,但并未从FragmentManager中移除
            mCurTransaction.detach((Fragment) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            Fragment fragment = (Fragment) object;
            if (fragment != mCurrentPrimaryItem) {
                // 主要项切换,相关菜单及信息进行切换
                if (mCurrentPrimaryItem != null) {
                    mCurrentPrimaryItem.setMenuVisibility(false);
                    mCurrentPrimaryItem.setUserVisibleHint(false);
                }
                if (fragment != null) {
                    fragment.setMenuVisibility(true);
                    fragment.setUserVisibleHint(true);
                }
                mCurrentPrimaryItem = fragment;
            }
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            if (mCurTransaction != null) {
                // 提交事务
                mCurTransaction.commitAllowingStateLoss();
                mCurTransaction = null;
                // 立即运行等待中事务
                mFragmentManager.executePendingTransactions();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return ((Fragment) object).getView() == view;
        }

    }

    private void showNotSupportDialog(int verson) {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_title))
                .setIcon(R.drawable.info)
                .setTitle("targetSdkVersion is:" + verson + "not support!")
                .setPositiveButton(
                        getResources().getString(R.string.dailog_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    finish();
                                } catch (Exception e) {
                                    finish();
                                }
                                dialog.dismiss();
                            }
                        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onPause() {
        if (AnimCommon.in != 0 && AnimCommon.out != 0) {
            super.overridePendingTransition(AnimCommon.in, AnimCommon.out);
            AnimCommon.clear();
        }
        super.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            // 当连续按返回键两次的时间不超过800毫秒就退出
            if (secondTime - firstTime > 800) {
                Toast.makeText(this, "亲...再按一次返回键退出程序",
                        Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                if (!is_kill_process) {
                    // 退出程序,后台播放音乐
                    finish();
                } else {
                    // 停止后台服务，不支持后台播放音乐
                    stopService(new Intent(this, MusicService.class));
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
