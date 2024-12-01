package com.lhanman.lhanweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 文件名：WellCOmeActivity
 * 作  者：
 * 日  期：12/13/21 10:06 AM
 * 描述：TOOD
 */
public class WellCOmeActivity extends AppCompatActivity {

    private TextView tv_start;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.well_come);
        tv_start = findViewById(R.id.tv_start);
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WellCOmeActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}
