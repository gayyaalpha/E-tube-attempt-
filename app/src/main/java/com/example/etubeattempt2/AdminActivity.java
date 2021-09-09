package com.example.etubeattempt2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.Month;

import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.StartMeetingOptions;
import us.zoom.sdk.ZoomApiError;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKAuthenticationListener;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;


public class AdminActivity extends AppCompatActivity {

    private Button logoutAdmin , loginStart , scheduleClass;
    private FirebaseAuth auth;
    private EditText usernameText ,meetingNUmText ,  passwordText;
    private ZoomSDKAuthenticationListener authListener;
    private TextView monthView ;
    private String monthStr;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        logoutAdmin = findViewById(R.id.logoutAdmin);
        monthView = findViewById(R.id.monthView);
        auth = FirebaseAuth.getInstance();

        loginStart = findViewById(R.id.btnStartMeeting);
        scheduleClass = findViewById(R.id.btnScheduleClass);


        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        monthStr  = currentMonth.toString();

        monthView.setText(monthStr);



        logoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(AdminActivity.this, "logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                finish();
            }
        });



        initializeSdk(this);


         loginStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                authListener = new ZoomSDKAuthenticationListener() {
                    /**
                     * This callback is invoked when a result from the SDK's request to the auth server is
                     * received.
                     */
                    @Override
                    public void onZoomSDKLoginResult(long result) {
                        if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
                            // Once we verify that the request was successful, we may start the meeting
                            startMeeting(AdminActivity.this);
                        }
                    }

                    @Override
                    public void onZoomSDKLogoutResult(long l) {
                    }

                    @Override
                    public void onZoomIdentityExpired() {
                    }

                    @Override
                    public void onZoomAuthIdentityExpired() {
                    }
                };

                String username = "dewanarayana48@gmail.com";
                String password = "Gaya12345";


                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("2021").document(monthStr);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()){
                                Toast.makeText(AdminActivity.this, "successful", Toast.LENGTH_SHORT).show();
                                Log.d("Document" , snapshot.getData().toString());

                                String MNum =snapshot.getData().get("meetingID").toString();
                                String MPassword = snapshot.getData().get("meetingPassword").toString();
                                String userName = "Admin";

                                if(MNum.trim().length() > 0 && MPassword.trim().length() >0 && userName.trim().length()>0){
                                    Toast.makeText(AdminActivity.this, "load zoom", Toast.LENGTH_SHORT).show();

                                    login(username, password);
                                    joinMeeting(AdminActivity.this , MNum , MPassword , userName);

                                }
                                else
                                    Toast.makeText(AdminActivity.this, "error", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Log.d("Document" , "no Data");

                        }
                    }
                });



//                String MNum = meetingNUmText.getText().toString();
//                String MPassword = passwordText.getText().toString();
//                String MuserName = usernameText.getText().toString();
//
//                login(username, password);
////                startMeeting(AdminActivity.this);
//                joinMeeting(AdminActivity.this, MNum, MPassword, MuserName);


            }
        });

        scheduleClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, "ok man", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminActivity.this , ScheduleActivity.class));


            }
        });
    }



    private void login(String username, String password) {
        int result = ZoomSDK.getInstance().loginWithZoom(username, password);
        if (result == ZoomApiError.ZOOM_API_ERROR_SUCCESS) {

            // 2. After request is executed, listen for the authentication result prior to starting a meeting
            ZoomSDK.getInstance().addAuthenticationListener(authListener);
        }
    }
    private void startMeeting(Context context) {
        ZoomSDK sdk = ZoomSDK.getInstance();
        if (sdk.isLoggedIn()) {
            MeetingService meetingService = sdk.getMeetingService();
            StartMeetingOptions options = new StartMeetingOptions();
            meetingService.startInstantMeeting(context, options);
        }
    }

    private void joinMeeting(Context context, String mNum, String mPassword, String MuserName) {
        MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
        JoinMeetingOptions options = new JoinMeetingOptions();
        JoinMeetingParams params =new JoinMeetingParams();
        params.displayName = MuserName;
        params.meetingNo = mNum;
        params.password = mPassword;
        meetingService.joinMeetingWithParams(context ,params , options);


    }


    private void initializeSdk(Context context) {
        ZoomSDK sdk = ZoomSDK.getInstance();
        ZoomSDKInitParams params = new ZoomSDKInitParams();
        params.appKey = "WsDuI4VxvJRkjp47gqCV8fadFEB4KlS63UqI";
        params.appSecret = "T4nCpfpkwHUsMyblHtTmZTeIacUZAvzdFqeD";
        params.domain = "zoom.us";
        params.enableLog = true;

        ZoomSDKInitializeListener listener = new ZoomSDKInitializeListener() {
            @Override
            public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) { }

            @Override
            public void onZoomAuthIdentityExpired() { }
        };
        sdk.initialize(context, listener, params);

    }
}