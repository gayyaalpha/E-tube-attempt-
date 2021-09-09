package com.example.etubeattempt2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.Month;

public class PaymentActivity extends AppCompatActivity {

    private Button payOnline , payCash ;
    private TextView  classYear ,classMonth ;
    private  String monthStr;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        payOnline = findViewById(R.id.payOnline);
        payCash =findViewById(R.id.payCash);
        classMonth = findViewById(R.id.classMonth);
        classYear = findViewById(R.id.classYear);

        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        monthStr  = currentMonth.toString();


        classMonth.setText(monthStr);

        payOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this , PayhereActivity.class);
                intent.putExtra("month" , monthStr);
                intent.putExtra("year" , classYear.getText().toString());
                startActivity(intent);



            }
        });

        payCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this , PaycashActivity.class));
            }
        });

    }
}