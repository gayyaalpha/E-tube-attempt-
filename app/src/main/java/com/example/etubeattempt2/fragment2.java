package com.example.etubeattempt2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.Month;

public class fragment2 extends Fragment {
    Button payOnline23 , payOnline22 , payonline21 , payonline21R ;
    Button payCash23 , payCash22 ,payCash21 ,payCash21R ;
    String monthStr;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment2_layout,container,false);

        payOnline23 = v.findViewById(R.id.payOnline23);
        payOnline22 = v.findViewById(R.id.payOnline22);
        payonline21 = v.findViewById(R.id.payOnline21);
        payonline21R = v.findViewById(R.id.payOnline21R);
        payCash23 = v.findViewById(R.id.payCash23);
        payCash22 = v.findViewById(R.id.payCash22);
        payCash21 = v.findViewById(R.id.payCash21);
        payCash21R = v.findViewById(R.id.payCash21R);

        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        monthStr  = currentMonth.toString();


        payOnline23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , PayhereActivity.class);
                intent.putExtra("month" , monthStr);
                intent.putExtra("year" ,"2023");
                startActivity(intent);

            }
        });

        payOnline22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , PayhereActivity.class);
                intent.putExtra("month" , monthStr);
                intent.putExtra("year" ,"2022");
                startActivity(intent);

            }
        });

        payonline21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , PayhereActivity.class);
                intent.putExtra("month" , monthStr);
                intent.putExtra("year" ,"2021");
                startActivity(intent);

            }
        });

        payonline21R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , PayhereActivity.class);
                intent.putExtra("month" , monthStr);
                intent.putExtra("year" ,"2021-Revision");
                startActivity(intent);

            }
        });










        return v;
    }
}
