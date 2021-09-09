package com.example.etubeattempt2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText emailText , passwordText;
    private Button loginButton;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();


                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "email or password can't be empty", Toast.LENGTH_SHORT).show();
                }
                else
                    loginUser(email,password);



            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                checkUserAccessLevel(authResult.getUser().getUid());


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference documentReference = firestore.collection("Users").document(uid);
        //extracy data from the document
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " +documentSnapshot.getData());

                if (documentSnapshot.getString("isUser") != null){
                    if(documentSnapshot.getBoolean("isFree")){
                        Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                        intent.putExtra("isFree" , true);
                        startActivity(intent);
                        finish();
                    }
                    else if (!(documentSnapshot.getBoolean("isFree"))){
                        Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                        intent.putExtra("isFree" , false);
                        startActivity(intent);
                        finish();

                    }


                }
                if (documentSnapshot.getString("isAdmin") != null){
                    startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                    finish();

                }

            }
        });
    }


}