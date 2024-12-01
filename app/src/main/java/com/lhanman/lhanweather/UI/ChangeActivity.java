package com.lhanman.lhanweather.UI;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import com.lhanman.lhanweather.R;
import com.lhanman.lhanweather.base.BaseActivity;
import com.lhanman.lhanweather.db.Redian;
import com.lhanman.lhanweather.db.RedianDbutils;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

public class ChangeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_tupian;
    private EditText et_author;
    private EditText et_biaoti;
    private EditText et_xiangqing;
    private Spinner sp_leixing;
    Redian medic;
    String path;
   private TextView commit1;
String leixing11="旅行";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_change;
    }

    @Override
    protected void init() {
        et_biaoti=findViewById(R.id.et_biaoti);
        et_author=findViewById(R.id.et_author);
        iv_tupian=findViewById(R.id.iv_tupian);
        et_xiangqing=findViewById(R.id.et_jieshao);
        sp_leixing=findViewById(R.id.sp_type);
        commit1=findViewById(R.id.commit1);


        medic= (Redian) getIntent().getSerializableExtra("bean");
        et_biaoti.setText(medic.getBiaoti());
        et_author.setText(medic.getAuthor());
        et_xiangqing.setText(medic.getXiangqing());
        path=medic.getImag();
        Glide.with(this).load(path).into(iv_tupian);
        sp_leixing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] language=getResources().getStringArray(R.array.leixing);
                leixing11=language[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iv_tupian.setOnClickListener(this);
        commit1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_tupian:
                PictureSelector
                        .create(ChangeActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture();
                break;
            case R.id.commit1:
                String biaoti=et_biaoti.getText().toString();
                String zuozhe=et_author.getText().toString();
                String xiangqing=et_xiangqing.getText().toString();
                medic.setBiaoti(biaoti);
                medic.setAuthor(zuozhe);
                medic.setXiangqing(xiangqing);
                medic.setLeixing(leixing11);
                medic.setTimest(getCurrentTime());
                medic.setImag(path);
                RedianDbutils.getInstance(getApplicationContext()).change(getApplicationContext(),medic);
                showToast("修改成功");
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== PictureSelector.SELECT_REQUEST_CODE){
            if(data!=null){
                PictureBean pictureBean=data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                path=pictureBean.getPath();
                Glide.with(this).load(pictureBean.getPath()).into(iv_tupian);
            }
        }
    }
}