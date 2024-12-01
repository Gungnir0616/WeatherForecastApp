package com.lhanman.lhanweather.UI;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lhanman.lhanweather.CircleImageView;
import com.lhanman.lhanweather.R;
import com.lhanman.lhanweather.base.BaseActivity;
import com.lhanman.lhanweather.db.SqliteDBUtils;
import com.lhanman.lhanweather.db.User;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author admin
 * @description:
 * @date :2022/2/26 12:23
 */
public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    @BindView(R.id.image_head)
    CircleImageView imageHead;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_password)
    TextView tvPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void init() {
        tvTitle.setText("用户信息");
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user",0);
        String intname = preferences.getString("name", "");
       User user= SqliteDBUtils.getInstance(getApplicationContext()).loadUserByName(intname);
        if (!TextUtils.isEmpty(user.getHead_url())){
            Glide.with(this).load(user.getHead_url()).into(imageHead);
        }else {
            Glide.with(this).load(R.mipmap.head).into(imageHead);
        }
        tvName.setText(user.getUsername());
        tvPassword.setText(user.getUserpwd());
    }


    @OnClick({R.id.rl_back, R.id.rl_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_head:
                break;
        }
    }
}
