package com.example.etubeattempt2;

import android.app.usage.NetworkStats;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class fragment1 extends Fragment {


    private FirebaseFirestore db;
    private FirebaseUser currentFirebaseUser;
    private  TextView textView;
    private DocumentReference documentReference;

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment1_layout, container, false);

        db = FirebaseFirestore.getInstance();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        textView = v.findViewById(R.id.textView);





        documentReference = db.collection("orders").document(currentFirebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {

                        //if the fileld doesnt exist app is going to crash look into that shit
                        Toast.makeText(getContext(), snapshot.getData().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "snapshot =  null", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());

                }
            }
        });


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (snapshot != null && snapshot.exists()) {

                    //if the fileld doesnt exist app is going to crash look into that shit
                    Toast.makeText(getContext(), snapshot.getData().toString(), Toast.LENGTH_SHORT).show();
                    textView.setText(snapshot.getData().toString());
                } else {
                    Toast.makeText(getContext(), "snapshot =  null", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "No such document");
                }


            }

        });


                    return v;
                }
    }




