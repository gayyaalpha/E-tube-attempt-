package com.example.etubeattempt2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    private EditText userNameText , emailText ,phoneNumText , passwordText;
    private Button registerButton;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userNameText  = findViewById(R.id.userName);
        passwordText =findViewById(R.id.password);
        emailText = findViewById(R.id.email);
        registerButton = findViewById(R.id.registerButton);
        phoneNumText = findViewById(R.id.phoneNum);



        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String userName = userNameText.getText().toString();
                String phoneNum = phoneNumText.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "fill the required spaces", Toast.LENGTH_SHORT).show();
                }
                else if (password.length()<6){
                    Toast.makeText(RegisterActivity.this, "Weak password", Toast.LENGTH_SHORT).show();
                }
                else
                    registerUser(email , password , phoneNum ,userName);

            }
        });

    }

    private void registerUser(String email, String password , String phoneNum , String userName ) {
        auth.createUserWithEmailAndPassword(email ,password).addOnCompleteListener(RegisterActivity.this ,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                    storeUserInfo(email  ,phoneNum , userName );
                    startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
                    finish();
                }
                else
                    Toast.makeText(RegisterActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void storeUserInfo(String email, String phoneNum, String userName) {
        FirebaseUser user = auth.getCurrentUser();
        DocumentReference documentReference = firestore.collection("Users").document(user.getUid());
        Map<String,Object> userInfo= new HashMap<>();
        userInfo.put("name",userName);
        userInfo.put("email" ,email);
        userInfo.put("PhoneNumber",phoneNum);


        userInfo.put("isUser" , "1");
        userInfo.put("isFree" , false);
        documentReference.set(userInfo);




    }
}