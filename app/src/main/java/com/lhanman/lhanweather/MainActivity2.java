package com.lhanman.lhanweather;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lhanman.lhanweather.View.NoScrollViewPager;
import com.lhanman.lhanweather.adapter.Myadapter;
import com.lhanman.lhanweather.base.BaseActivity;
import com.lhanman.lhanweather.fragment.MeFragment;
import com.lhanman.lhanweather.fragment.MusicFragment;
import com.lhanman.lhanweather.fragment.ShipuFragment;
import com.lhanman.lhanweather.fragment.WeatherFragment1;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity2 extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.main_pager)
    NoScrollViewPager mainpager;
    Myadapter myadapter;
    private long clickTime;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void init() {
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new WeatherFragment1());
        fragments.add(new ShipuFragment());
        fragments.add(new MusicFragment());
        fragments.add(new MeFragment());
        myadapter=new Myadapter(getSupportFragmentManager(),fragments);
        mainpager.setOffscreenPageLimit(1);
        mainpager.setAdapter(myadapter);
        radioGroup.setOnCheckedChangeListener(this);

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.tab_dynamic:
                mainpager.setCurrentItem(0,false);
                break;
            case R.id.tab_rank:
                mainpager.setCurrentItem(1,false);
                break;
            case R.id.tab_message:
                mainpager.setCurrentItem(2,false);
                break;
            case R.id.tab_me:
                mainpager.setCurrentItem(3,false);
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(this, "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            try {
                //正常退出
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}