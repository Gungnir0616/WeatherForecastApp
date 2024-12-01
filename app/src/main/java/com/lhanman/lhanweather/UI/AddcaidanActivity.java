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

public class AddcaidanActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_tupian;
    private EditText et_author;
    private Spinner sp_leixing;
    private EditText etbiaoti;
    private EditText et_xiangqing;
    String path;
    private TextView commit1;
    String leixing11="旅行";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addcaidan;
    }

    @Override
    protected void init() {
        etbiaoti=findViewById(R.id.et_biaoti);
        et_author=findViewById(R.id.et_author);
        et_xiangqing=findViewById(R.id.et_jieshao);
        iv_tupian=findViewById(R.id.iv_tupian);
        sp_leixing=findViewById(R.id.sp_type);
        commit1=findViewById(R.id.commit1);
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
                        .create(AddcaidanActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture();
                break;
            case R.id.commit1:
                String biaoti=etbiaoti.getText().toString();
                String zuozhe=et_author.getText().toString();
                String xiangqing=et_xiangqing.getText().toString();
                Redian medic=new Redian();
                medic.setImag(path);
                medic.setBiaoti(biaoti);
                medic.setAuthor(zuozhe);
                medic.setXiangqing(xiangqing);
                medic.setLeixing(leixing11);
                medic.setTimest(getCurrentTime());
               int i= RedianDbutils.getInstance(getApplicationContext()).insert(medic);
                if(i==0){
            showToast("添加成功");
//                    Intent intent = new Intent();
//                    intent.putExtra("medic", medic);
//                    setResult(RESULT_OK,intent);
//                    finish();

                finish();
                }else {
                    showToast("添加失败");
                }
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