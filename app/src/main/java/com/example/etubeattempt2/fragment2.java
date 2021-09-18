package com.example.etubeattempt2;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDate;
import java.time.Month;

public class fragment2 extends Fragment {
    Button payOnline23, payOnline22, payonline21, payonline21R;
    Button payCash23, payCash22, payCash21, payCash21R;
    String monthStr;
    FirebaseFirestore firebaseFirestore;
    RecyclerView firestoreList;
    private FirestoreRecyclerAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment2_layout, container, false);

        payOnline23 = v.findViewById(R.id.payOnline23);
//        payOnline22 = v.findViewById(R.id.payOnline22);
//        payonline21 = v.findViewById(R.id.payOnline21);
//        payonline21R = v.findViewById(R.id.payOnline21R);
//        payCash23 = v.findViewById(R.id.payCash23);
//        payCash22 = v.findViewById(R.id.payCash22);
//        payCash21 = v.findViewById(R.id.payCash21);
//        payCash21R = v.findViewById(R.id.payCash21R);

        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        monthStr = currentMonth.toString();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firestoreList = v.findViewById(R.id.firestoreList);

        //query
        Query query = firebaseFirestore.collection("products");

        //recycler option
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query, ProductsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ProductsModel, ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull ProductsModel model) {
                holder.listCapital.setText(model.getTitle());
                holder.listIsland.setText(model.getDate());
                String documentId = getSnapshots().getSnapshot(position).getId();
                Toast.makeText(getActivity(), documentId, Toast.LENGTH_SHORT).show();

            }
            //view holder
        };
        firestoreList.setHasFixedSize(true);
        firestoreList.setLayoutManager(new LinearLayoutManager(getActivity()));
        firestoreList.setAdapter(adapter);

        return v;
    }

    private class ProductsViewHolder extends RecyclerView.ViewHolder {

        private TextView listCapital;
        private TextView listIsland;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            listCapital = itemView.findViewById(R.id.listCapital);
            listIsland = itemView.findViewById(R.id.listIsland);

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
        if(adapter!=null) {
            adapter.startListening();
        }
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











