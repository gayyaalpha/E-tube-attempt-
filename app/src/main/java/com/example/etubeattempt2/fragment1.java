package com.example.etubeattempt2;

import android.app.usage.NetworkStats;
import android.content.Context;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class fragment1 extends Fragment {


    private FirebaseFirestore db, db2;
    private FirebaseUser currentFirebaseUser;
    private TextView textView;
    private DocumentReference documentReference;
    private String monthStr;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView firestoreList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment1_layout, container, false);

        db = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        firestoreList = v.findViewById(R.id.firestoreList1);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        textView = v.findViewById(R.id.textView);

        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        monthStr = currentMonth.toString();


        Query query = db.collection("orders").document("uid").collection(currentFirebaseUser.getUid()).whereEqualTo(monthStr, "true");


        //recycler option(using the setters products model class is taking the necessary values from the firestore database)
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query, ProductsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ProductsModel, fragment1.ProductsViewHolder>(options) {
            @NonNull
            @Override
            //products view holder and single list item xml file got connected
            public fragment1.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new fragment1.ProductsViewHolder(view);
            }

            //
            @Override
            protected void onBindViewHolder(@NonNull fragment1.ProductsViewHolder holder, int position, @NonNull ProductsModel model) {
//                Task<QuerySnapshot> user =  query.get(position);
                holder.classTitle.setText(model.getTitle());
                holder.classMonth.setText(model.getMonth());
                holder.classPrice.setText(model.getPrice());
                holder.className = model.getName();
//                holder.position = position;
//                holder.user = user;
//                String documentId = getSnapshots().getSnapshot(position).getId();
//                Toast.makeText(getActivity(), documentId, Toast.LENGTH_SHORT).show();
                //pay online button function
//                holder.payOnline.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(getContext(), "get title" + model.getTitle(), Toast.LENGTH_SHORT).show();
//                        storeOrderInfo(monthStr, holder.className);

//                    }
//                });
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(getContext(), "get title" + model.getTitle() , Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
            //view holder
        };
        firestoreList.setHasFixedSize(true);
        firestoreList.setLayoutManager(new LinearLayoutManager(getActivity()));
        firestoreList.setAdapter(adapter);


        return v;
    }


    private class ProductsViewHolder extends RecyclerView.ViewHolder {

        private TextView classTitle;
        private TextView classMonth;
        private TextView classPrice;
        private Button payOnline;
        private String className;


        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            classTitle = itemView.findViewById(R.id.titleClass);
            classMonth = itemView.findViewById(R.id.monthClass);
            classPrice = itemView.findViewById(R.id.priceClass);
            payOnline = itemView.findViewById(R.id.payOnline);


        }
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }
}


//        documentReference = db.collection("orders").document(currentFirebaseUser.getUid());
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot snapshot = task.getResult();
//                    if (snapshot != null && snapshot.exists()) {
//
//                        //if the fileld doesnt exist app is going to crash look into that shit
//                        Toast.makeText(getContext(), snapshot.getData().toString(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getContext(), "snapshot =  null", Toast.LENGTH_SHORT).show();
////                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d("TAG", "get failed with ", task.getException());
//
//                }
//            }
//        });


//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
//                if (snapshot != null && snapshot.exists()) {
//
//                    //if the fileld doesnt exist app is going to crash look into that shit
//                    Toast.makeText(getContext(), snapshot.getData().toString(), Toast.LENGTH_SHORT).show();
//
//
//                } else {
//                    Toast.makeText(getContext(), "snapshot =  null", Toast.LENGTH_SHORT).show();
////                        Log.d(TAG, "No such document");
//                }
//
//
//            }
//
//        });

//        Query query = db.collection("products").whereEqualTo("OCTOBER" , "TRUE");
//
//        db2.collection("products").whereEqualTo("OCTOBER" , "true").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(getContext(), "fuck you", Toast.LENGTH_SHORT).show();
//                    QuerySnapshot snapshot =task.getResult();
//                    if (snapshot!=null ){
//                        Toast.makeText(getContext(), "snapshot ok", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });

//        db.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                List<String> ids = new ArrayList<>();
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        String id = document.getId();
//
//
////                                .collection("orders").document(currentFirebaseUser.getUid());
//                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()){
//                                    Toast.makeText(getContext(), "fuck you", Toast.LENGTH_SHORT).show();
//                                    DocumentSnapshot snapshot = task.getResult();
//                                    if (snapshot!=null && snapshot.exists()){
//                                        Toast.makeText(getContext(), "snapshot ok", Toast.LENGTH_SHORT).show();
//                                        String thisMonth = snapshot.getString("OCTOBER");
//                                        Toast.makeText(getContext(), thisMonth, Toast.LENGTH_SHORT).show();
//                                        if (thisMonth=="true"){
//                                            db.collection("cities").whereEqualTo("capital", true).get();
//// future.get() blocks on response
//                                            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//                                            for (DocumentSnapshot document : documents) {
//                                                System.out.println(document.getId() + " => " + document.toObject(City.class));
//                                        }
//                                    }
//                                }
//
//                            }
//                        });
//                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    DocumentSnapshot snapshot = task.getResult();
//                                    if (snapshot != null && snapshot.exists()) {
//                                        String thisMonth = snapshot.getString("october");
//
//                                        //if the fileld doesnt exist app is going to crash look into that shit
//                                        Toast.makeText(getContext(),thisMonth, Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(getContext(), "snapshot =  null", Toast.LENGTH_SHORT).show();
////                        Log.d(TAG, "No such document");
//                                    }
//                                } else {
//                                    Log.d("TAG", "get failed with ", task.getException());
//
//                                }
//                            }
//                        });

//                        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
//                        textView.setText(ids.toString());
//                    }
//                }
//                ListView listView = (ListView) findViewById(R.id.list_view);
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice, ids);
//                listView.setAdapter(arrayAdapter);
//            }
//        });

