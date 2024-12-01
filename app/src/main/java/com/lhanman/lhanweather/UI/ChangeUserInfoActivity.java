package com.lhanman.lhanweather.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.lhanman.lhanweather.CircleImageView;
import com.lhanman.lhanweather.R;
import com.lhanman.lhanweather.db.SqliteDBUtils;
import com.lhanman.lhanweather.db.User;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 修改用户信息
 */
public class ChangeUserInfoActivity extends AppCompatActivity {


    String path = "";
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
    EditText tvName;
    @BindView(R.id.tv_password)
    EditText tvPassword;
    @BindView(R.id.tv_change)
    TextView tvChange;
    User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_userinfo);

        ButterKnife.bind(this);
        tvTitle.setText("修改用户信息");
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user",0);
        String intname = preferences.getString("name", "");
        user= SqliteDBUtils.getInstance(getApplicationContext()).loadUserByName(intname);
        if (user != null) {
            tvName.setText(user.getUsername());
            tvPassword.setText(user.getUserpwd());
            if (!TextUtils.isEmpty(user.getHead_url())) {
                path = user.getHead_url();
                Glide.with(this).load(user.getHead_url()).into(imageHead);
            }
        }

    }


    @OnClick({R.id.rl_back, R.id.rl_head, R.id.tv_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_head:
                PictureSelector
                        .create(ChangeUserInfoActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture();
                break;
            case R.id.tv_change:
                String name = tvName.getText().toString();
                String password = tvPassword.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "请检查输入信息是否完整", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    user.setHead_url(path);
                    user.setUsername(name);
                    user.setUserpwd(password);
                    SqliteDBUtils.getInstance(getApplicationContext()).change(getApplicationContext(), user);
                    Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                Glide.with(this).load(pictureBean.getPath()).into(imageHead);
                path = pictureBean.getPath();
            }
        }
    }
}
