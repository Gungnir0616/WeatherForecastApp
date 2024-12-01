package com.lhanman.lhanweather.fragment;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import android.content.SharedPreferences;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lhanman.lhanweather.CircleImageView;
import com.lhanman.lhanweather.R;
import com.lhanman.lhanweather.UI.AboutActivity;
import com.lhanman.lhanweather.UI.ChangeUserInfoActivity;
import com.lhanman.lhanweather.UI.UserInfoActivity;
import com.lhanman.lhanweather.base.LazyFragment;
import com.lhanman.lhanweather.db.SqliteDBUtils;
import com.lhanman.lhanweather.db.User;

import butterknife.BindView;
import butterknife.OnClick;

public class MeFragment extends LazyFragment {
    @BindView(R.id.image_head)
    CircleImageView imageHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_userinfo)
    RelativeLayout rlUserinfo;
    @BindView(R.id.rl_change_pwd)
    RelativeLayout rlChangePwd;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.rl_restart)
    RelativeLayout rlRestart;
    @BindView(R.id.rl_zhuxiao)
    RelativeLayout rlZhuxiao;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void loadData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user",0);
        String intname = preferences.getString("name", "");
        User user= SqliteDBUtils.getInstance(getActivity()).loadUserByName(intname);
        tvName.setText(user.getUsername());
        if (!TextUtils.isEmpty(user.getHead_url())){
            Glide.with(getActivity()).load(user.getHead_url()).into(imageHead);
        }else {
            Glide.with(getActivity()).load(R.mipmap.head).into(imageHead);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getActivity().getSharedPreferences("user",0);
        String intname = preferences.getString("name", "");
        User user= SqliteDBUtils.getInstance(getActivity()).loadUserByName(intname);
        if (tvName != null && user != null){

            tvName.setText(user.getUsername());
            if (!TextUtils.isEmpty(user.getHead_url())){
                Glide.with(getActivity()).load(user.getHead_url()).into(imageHead);
            }else {
                Glide.with(getActivity()).load(R.mipmap.head).into(imageHead);
            }
        }
    }

    @OnClick({R.id.rl_userinfo, R.id.rl_change_pwd, R.id.rl_about, R.id.rl_restart, R.id.rl_zhuxiao, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_userinfo:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.rl_change_pwd:
                startActivity(new Intent(getActivity(), ChangeUserInfoActivity.class));//修改密码
                break;
            case R.id.rl_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.rl_restart:
                reStartApp();
                break;
            case R.id.rl_zhuxiao:
                SharedPreferences preferences = getActivity().getSharedPreferences("user",0);
                String intname = preferences.getString("name", "");
                User user= SqliteDBUtils.getInstance(getActivity()).loadUserByName(intname);
                SqliteDBUtils.getInstance(getActivity()).delete(getActivity(),user.getId()+"");
                reStartApp();
                break;
            case R.id.tv_login:
                dialog();
                break;
        }
    }

    public void reStartApp() {
        Intent intent = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//与正常页面跳转一样可传递序列化数据,在Launch页面内获得
        intent.putExtra("REBOOT", "reboot");
        startActivity(intent);
        getActivity().finish();
    }

    protected void dialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    //正常退出
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}