package com.lhanman.lhanweather.UI;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lhanman.lhanweather.R;
import com.lhanman.lhanweather.base.BaseActivity;
import com.lhanman.lhanweather.db.Redian;


public class CaidandetailActivity extends BaseActivity {
    private ImageView iv_tupian;
    private EditText et_author;
    private EditText sp_leixing;
    private EditText etbiaoti;
    private EditText et_xiangqing;
    String path;
    Redian medic;
    private TextView commit1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_caidandetail;
    }

    @Override
    protected void init() {
        etbiaoti=findViewById(R.id.et_biaoti);
        et_author=findViewById(R.id.et_author);
        iv_tupian=findViewById(R.id.iv_tupian);
        et_xiangqing=findViewById(R.id.et_jieshao);
        sp_leixing=findViewById(R.id.et_leixing);
        iv_tupian=findViewById(R.id.iv_tupian);

        medic= (Redian) getIntent().getSerializableExtra("bean");
        etbiaoti.setText(medic.getBiaoti());
        et_author.setText(medic.getAuthor());
        sp_leixing.setText(medic.getLeixing());
        et_xiangqing.setText(medic.getXiangqing());
        path=medic.getImag();
        Glide.with(this).load(path).into(iv_tupian);
    }
}