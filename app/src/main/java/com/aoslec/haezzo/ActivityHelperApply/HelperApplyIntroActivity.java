package com.aoslec.haezzo.ActivityHelperApply;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.aoslec.haezzo.R;

public class HelperApplyIntroActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_apply_intro);

        button = findViewById(R.id.btn_helper_apply_intro);

        button.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HelperApplyIntroActivity.this, HelperApplyAccountActivity.class);
            startActivity(intent);
            finish();

        }
    };

}