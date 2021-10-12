package com.example.etubeattempt2;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import androidx.fragment.app.Fragment;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class fragment2 extends Fragment {

    private Button payCash;
    private String monthStr;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView firestoreList;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth auth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment2_layout, container, false);


        payCash = v.findViewById(R.id.payCash);


        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        monthStr = currentMonth.toString();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firestoreList = v.findViewById(R.id.firestoreList);
        auth = FirebaseAuth.getInstance();
        //query(get data from the collection)
        Query query = firebaseFirestore.collection("products");
//                .whereEqualTo("title" ,"A/L - 2021 ");

        //recycler option(using the setters products model class is taking the necessary values from the firestore database)
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query, ProductsModel.class)
                .build();
        // query and the adapter was connected using the firestore adapter

        adapter = new FirestoreRecyclerAdapter<ProductsModel, ProductsViewHolder>(options) {
            @NonNull
            @Override
            //products view holder and single list item xml file got connected
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new ProductsViewHolder(view);
            }
//
            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull ProductsModel model) {
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
                holder.payOnline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "get title" + model.getTitle() , Toast.LENGTH_SHORT).show();
                        storeOrderInfo(monthStr , holder.className);

                    }
                });
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
            classTitle  = itemView.findViewById(R.id.titleClass);
            classMonth = itemView.findViewById(R.id.monthClass);
            classPrice = itemView.findViewById(R.id.priceClass);
            payOnline= itemView.findViewById(R.id.payOnline);




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

    private void storeOrderInfo(String monthStr , String classInfo) {
        FirebaseUser user = auth.getCurrentUser();
        DocumentReference documentReference = firebaseFirestore.collection("orders").document("uid").collection(user.getUid()).document(classInfo);
        Map<String,Object> userInfo= new HashMap<>();
        userInfo.put(monthStr,"true");

        //merging is done (if there is no document will be created upon request)

        documentReference.set(userInfo, SetOptions.merge());

    }


}






















//        payOnline23.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity() , PayhereActivity.class);
//                intent.putExtra("month" , monthStr);
//                intent.putExtra("year" ,"2023");
//                startActivity(intent);
//
//            }
//        });
//
//        payOnline22.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity() , PayhereActivity.class);
//                intent.putExtra("month" , monthStr);
//                intent.putExtra("year" ,"2022");
//                startActivity(intent);
//
//            }
//        });
//
//        payonline21.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity() , PayhereActivity.class);
//                intent.putExtra("month" , monthStr);
//                intent.putExtra("year" ,"2021");
//                startActivity(intent);
//
//            }
//        });
//
//        payonline21R.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity() , PayhereActivity.class);
//                intent.putExtra("month" , monthStr);
//                intent.putExtra("year" ,"2021-Revision");
//                startActivity(intent);
//
//            }
//        });











