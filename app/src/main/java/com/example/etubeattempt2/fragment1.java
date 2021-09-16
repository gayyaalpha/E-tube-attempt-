package com.example.etubeattempt2;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class fragment1 extends Fragment {

    RecyclerView recyclerView;
    private FirebaseFirestore db;
//    DatabaseReference databaseReference;
    MyAdapter myAdapter;
    ArrayList<User> list;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment1_layout,container,false);

        db = FirebaseFirestore.getInstance();
        recyclerView = v.findViewById(R.id.userList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(getActivity(),list);
        recyclerView.setAdapter(myAdapter);









//        DocumentReference docRef = db.collection("2020").document("AUGUST");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    Log.d("TAG", "No such document");
//                    DocumentSnapshot document = task.getResult();
//                    if (document != null && document.exists()) {
//                        Toast.makeText(getActivity(), "working thorugh NICE", Toast.LENGTH_SHORT).show();
//
////                        User user = (User) document.getData();
////                        String teams = document.getString("teams"); //Print the name
////                        matchName.setText(teams);
//                    } else {
//                        Log.d("TAG", "No such document");
//                        Toast.makeText(getActivity(), "No document", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Log.d("TAG", "get failed with ", task.getException());
//                    Toast.makeText(getActivity(), "working thorugh 2", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        DocumentReference docRef = db.collection("cities").document("SL");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    Log.d("TAG", "No such document");
//                    DocumentSnapshot document = task.getResult();
//                    if (document != null && document.exists()) {
////                        String teams = document.getString("teams"); //Print the name
//////                        matchName.setText(teams);
//                    } else {
//                        Log.d("TAG", "No such document");
//                    }
//                } else {
//                    Log.d("TAG", "get failed with ", task.getException());
//                    Toast.makeText(MainActivity2.this, "working thorugh 2", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });









        return v;
    }
}
