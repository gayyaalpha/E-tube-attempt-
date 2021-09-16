package com.example.etubeattempt2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class PayhereActivity extends AppCompatActivity {

//    private TextView textView;
    private Button payHere;
    private EditText firstNameText , lastNameText , emailText , phoneNumberText , addressText ,cityText ,countryText ;
    FirebaseAuth auth;
    private FirebaseUser currentFirebaseUser;
    private String classYear , classMonth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef;
    private  TextView textView;



    private final static int PAYHERE_REQUEST = 11010;
    private final static  String merchantID = "1218101";
    private final static  String merchantSecret = "4a9v8PgEjzh4Utli0Yq5sw8LONfKIalkJ8X12ocsggyu";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payhere);

//        textView = findViewById(R.id.textView);
        payHere = findViewById(R.id.payHere);

        firstNameText = findViewById(R.id.firstName);
        lastNameText = findViewById(R.id.lastName);
        emailText = findViewById(R.id.email);
        phoneNumberText = findViewById(R.id.phoneNumber);
        addressText = findViewById(R.id.address);
        cityText = findViewById(R.id.city);
        countryText = findViewById(R.id.country);
        textView = findViewById(R.id.textView);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;


        Bundle extras = getIntent().getExtras();
         classYear = extras.getString("year" , "");
         classMonth = extras.getString("month" , "");

        payHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = firstNameText.getText().toString();
                String lastName = lastNameText.getText().toString();
                String email = emailText.getText().toString();
                String phoneNumber = phoneNumberText.getText().toString();
                String address = addressText.getText().toString();
                String city = cityText.getText().toString();
                String country = countryText.getText().toString();



                InitRequest req = new InitRequest();
                req.setMerchantId(merchantID);       // Your Merchant PayHere ID
                req.setMerchantSecret(merchantSecret); // Your Merchant secret (Add your app at Settings > Domains & Credentials, to get this))
                req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
                req.setAmount(1200.00);             // Final Amount to be charged
                req.setOrderId("230000123");        // Unique Reference ID
                req.setItemsDescription(classYear + " " + classMonth);  // Item description title
                req.getCustomer().setFirstName(firstName);
                req.getCustomer().setLastName(lastName);
                req.getCustomer().setEmail(email);
                req.getCustomer().setPhone(phoneNumber);
                req.getCustomer().getAddress().setAddress(address);
                req.getCustomer().getAddress().setCity(city);
                req.getCustomer().getAddress().setCountry(country);

                // Optional Param
                req.setStartupFee(0);               // +/- Adjustment to the fist charge


                Intent intent = new Intent(PayhereActivity.this, PHMainActivity.class);
                intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
                PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
                startActivityForResult(intent, PAYHERE_REQUEST); //unique request ID like private final static int PAYHERE_REQUEST = 11010;
                Toast.makeText(PayhereActivity.this, "comming through", Toast.LENGTH_SHORT).show();
                Intent intentBack = new Intent(PayhereActivity.this,MainActivity.class);




                onActivityResult(11010 , 2 , intentBack  );


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT )) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (resultCode == Activity.RESULT_OK) {
                String msg;
                if (response != null)
                    if (response.isSuccess())
                        msg = "Activity result:" + response.getData().toString();
                    else
                        msg = "Result:" + response.toString();
                else
                    msg = "Result: no response";
                Toast.makeText(PayhereActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.d("TAG", msg);
                textView.setText(msg);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(PayhereActivity.this, "result canceled", Toast.LENGTH_SHORT).show();
                if (response != null)
                    textView.setText(response.toString());
                else
                    textView.setText("User canceled the request");
            }
        }
    }






//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PAYHERE_REQUEST && data!=null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
//            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
//            if (resultCode == Activity.RESULT_OK) {
//                String msg;
//                if (response != null) {
//                    if (response.isSuccess()) {
//                        msg = "Activity result:" + response.getData().toString() + resultCode;
////                        SharedPreferences.Editor editor = sp.edit();
////                        editor.putBoolean("paymentStatus" , true);
////                        editor.commit();
//                        Toast.makeText(PayhereActivity.this, "payment successful", Toast.LENGTH_SHORT).show();
////                        Intent intent = new Intent(PayhereActivity.this ,MainActivity.class);
////                        intent.putExtra("paymentStatus" , true);
////                        startActivity(intent);
//                    } else {
//                        msg = "Result:" + response.toString();
////                        Toast.makeText(PayhereActivity.this, "payment unsuccessful", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else {
//                    msg = "Result: no response";
//                    Log.d("TAG", msg);
//                    textView.setText(msg);
//                    Toast.makeText(PayhereActivity.this, "payment unsuccessful" + msg, Toast.LENGTH_SHORT).show();
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                if (response != null){
//                    textView.setText(response.toString());
//                    Toast.makeText(PayhereActivity.this, "payment unsuccessful else if ", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    textView.setText("User canceled the request");
//                    Toast.makeText(PayhereActivity.this, "payment unsuccessful else", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }



//        DocumentReference documentReference = firestore.collection("Users").document(currentFirebaseUser.getUid());
//        Map<String,Object> userInfo= new HashMap<>();
//        userInfo.put(classMonth,true);
//        documentReference.set(userInfo);
//    }
}