package com.lhanman.lhanweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lhanman.lhanweather.base.BaseActivity;
import com.lhanman.lhanweather.db.SqliteDBUtils;

import butterknife.BindView;

public class LoginActivity  extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_login)
    TextView tvLogin;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        tvRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register:
                Intent intent=new Intent(LoginActivity.this,RegiseterActivity.class);
                startActivity(intent);
            case R.id.tv_login:
                String intname=etPhone.getText().toString().trim();
                String intpwd=etPwd.getText().toString().trim();

                    if(TextUtils.isEmpty(intname)||TextUtils.isEmpty(intpwd)){
                        showToast("请检查输入内容");
                        return;
                    }   else {
                        int i = SqliteDBUtils.getInstance(LoginActivity.this).Quer(intpwd, intname);
                        if (i==1) {
                            SharedPreferences preferences = getSharedPreferences("user",MODE_PRIVATE);
                            SharedPreferences.Editor edit = preferences.edit();
                            edit.putString("name",intname);
                            edit.commit();
                            Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent1);
                            showToast("登录成功");
                            finish();
                        }else if(i==-1){
                            showToast("密码错误");
                            return;
                        }else {
                            showToast("无此用户");
                            return;
                        }
                    }
                }
        }

}
