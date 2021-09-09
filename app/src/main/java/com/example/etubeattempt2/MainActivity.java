package com.example.etubeattempt2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.ZoomApiError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKAuthenticationListener;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton  , join , shop ;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private EditText nameText , MNumText , MPasswordText;
    private ZoomSDKAuthenticationListener authListener;
    private String monthStr;
    private FirebaseUser currentFirebaseUser;
    private boolean  freeCardStatus , paymentStatus;
    private FirebaseFirestore db;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();


        join = findViewById(R.id.joinClass);
        shop = findViewById(R.id.paymentActivity);

        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        monthStr  = currentMonth.toString();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        Bundle extras = getIntent().getExtras();
        freeCardStatus = extras.getBoolean("isFree" , false);
        paymentStatus = extras.getBoolean("paymentStatus" , false);

        if (paymentStatus){

            Map<String, Object> map = new HashMap<>();
            map.put(monthStr, true);
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(currentFirebaseUser.getUid());
            documentReference.set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "upload successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (freeCardStatus){
            join.setVisibility(View.VISIBLE);
        }
        else {
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(currentFirebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            Toast.makeText(MainActivity.this, "successful", Toast.LENGTH_SHORT).show();

                            if(snapshot.get(monthStr) != null){
                                Toast.makeText(MainActivity.this, "works", Toast.LENGTH_SHORT).show();
                                boolean paymentStatus = snapshot.getBoolean(monthStr);
                                if (paymentStatus) {
                                    join.setVisibility(View.VISIBLE);
                                } else
                                    join.setVisibility(View.GONE);
                            }
                            else{
                                Toast.makeText(MainActivity.this, "works properly", Toast.LENGTH_SHORT).show();
                                join.setVisibility(View.GONE);
                            }
                        } else
                            join.setVisibility(View.GONE);

                    }
                }
            });
        }

        initializeSdk(this);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(MainActivity.this, "logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("2021").document(monthStr);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()){
                            Toast.makeText(MainActivity.this, "successful", Toast.LENGTH_SHORT).show();
                            Log.d("Document" , snapshot.getData().toString());

                            String MNum =snapshot.getData().get("meetingID").toString();
                            String MPassword = snapshot.getData().get("meetingPassword").toString();
                            String userName = "user";

                            if(MNum.trim().length() > 0 && MPassword.trim().length() >0 && userName.trim().length()>0){
                                Toast.makeText(MainActivity.this, "load zoom", Toast.LENGTH_SHORT).show();
//                                String username = "laldewanarayana@gmail.com";
//                                String password = "Physics12345";
//                                login(username, password);
                                joinMeetingUser(MainActivity.this , MNum , MPassword , userName);

                            }
                            else
                                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Log.d("Document" , "no Data");

                    }
                }
            });
            }
        });

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , PaymentActivity.class));
            }
        });
    }
    private void joinMeetingUser(Context context, String mNum, String mPassword, String userName) {
        MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
        JoinMeetingOptions options = new JoinMeetingOptions();
        JoinMeetingParams params =new JoinMeetingParams();
        params.displayName = userName;
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