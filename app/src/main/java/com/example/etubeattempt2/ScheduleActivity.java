package com.example.etubeattempt2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity {

    private Button btnSchedule;
    private EditText classYearText , monthText , meetingIDText , meetingPasswordText ,dayOfTheWeekText , scheduledTimeText;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        classYearText = findViewById(R.id.classYear);
        monthText = findViewById(R.id.month);
        meetingIDText = findViewById(R.id.meetingID);
        meetingPasswordText = findViewById(R.id.meetingPassword);
        dayOfTheWeekText = findViewById(R.id.dayOfTheWeek);
        scheduledTimeText = findViewById(R.id.scheduledTime);
        btnSchedule = findViewById(R.id.btnScheduleClass);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();



        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classYear = classYearText.getText().toString();
                String month = monthText.getText().toString().toUpperCase();
                String meetingID = meetingIDText.getText().toString();
                String meetingPassword = meetingPasswordText.getText().toString();
                String dayOfTheWeek = dayOfTheWeekText.getText().toString();
                String scheduledTime = scheduledTimeText.getText().toString();
                scheduleMeeting(classYear , month , meetingID , meetingPassword , dayOfTheWeek , scheduledTime);
            }
        });




    }

    private void scheduleMeeting(String classYear, String month, String meetingID , String meetingPassword , String dayOfTheWeek , String scheduledTime) {

        DocumentReference documentReference = firestore.collection(classYear).document(month);
        Map<String,Object> classDetails= new HashMap<>();
        classDetails.put("meetingID",meetingID);
        classDetails.put("meetingPassword" ,meetingPassword);
        classDetails.put("date",dayOfTheWeek);
        classDetails.put("time",scheduledTime);

        documentReference.set(classDetails);
        Intent intent = new Intent(ScheduleActivity.this , AdminActivity.class);
        startActivity(intent);





    }
}