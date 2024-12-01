package com.lhanman.lhanweather;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lhanman.lhanweather.base.BaseActivity;
import com.lhanman.lhanweather.db.SqliteDBUtils;
import com.lhanman.lhanweather.db.User;

import butterknife.BindView;
import butterknife.OnClick;


public class RegiseterActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.ll_pwd)
    LinearLayout llPwd;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {

    }

    @OnClick({R.id.rl_back, R.id.tv_next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_next_step:
                if (TextUtils.isEmpty(etPwd.getText().toString()) || TextUtils.isEmpty(etName.getText().toString())) {
                    showToast("检查输入信息");
                    return;
                } else if (etPwd.getText().toString().length() < 6) {
                    showToast("密码长度不能小于6位数");
                    return;
                } else {
                    User user = new User();
                    user.setUsername(etName.getText().toString());
                    user.setUserpwd(etPwd.getText().toString());
                    user.setHead_url("");
                    int i = SqliteDBUtils.getInstance(getApplicationContext()).saveUser(user);
                    if (i == 1) {
                        showToast("注册成功");
                        finish();
                    } else {
                        showToast("注册失败");
                    }

                }
                break;
        }
    }

}
