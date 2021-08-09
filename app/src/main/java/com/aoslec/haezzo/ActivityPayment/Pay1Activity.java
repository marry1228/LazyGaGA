package com.aoslec.haezzo.ActivityPayment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aoslec.haezzo.R;

public class Pay1Activity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay1);
        //        getHashKey();
        editTextName = findViewById(R.id.editName);
        editTextPrice = findViewById(R.id.editPrice);

        // 버튼 클릭 이벤트
        Button button = findViewById(R.id.buttonPay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String price = editTextPrice.getText().toString();

                Pay2Activity pay_2_activityActivity = new Pay2Activity(name, price);

                Intent intent = new Intent(getApplicationContext(), pay_2_activityActivity.getClass());
                startActivity(intent);
            }
        });


    }
}