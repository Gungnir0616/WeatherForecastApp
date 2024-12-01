package com.lhanman.lhanweather.UI;

import android.view.View;
import android.widget.RelativeLayout;


import com.lhanman.lhanweather.R;
import com.lhanman.lhanweather.base.BaseActivity;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {
@BindView(R.id.rl_back)
RelativeLayout back;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void init() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}