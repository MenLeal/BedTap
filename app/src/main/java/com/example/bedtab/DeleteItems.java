package com.example.bedtab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.bedtab.Adapters.OfferAdapter;
import com.example.bedtab.models.Offer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeleteItems extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private ArrayList<Offer> mList;
    private OfferAdapter mAdapter;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_items);
        recyclerView=findViewById(R.id.rvOffer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DeleteItems.this));
        database=FirebaseDatabase.getInstance();
        mList=new ArrayList<>();
        mAdapter=new OfferAdapter(DeleteItems.this,mList);
        recyclerView.setAdapter(mAdapter);
        ref=database.getReference("Productos");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Offer e=snapshot1.getValue(Offer.class);
                    mList.add(e);
                }
                mAdapter.setOnItemClickListener(new OfferAdapter.OnItemClickListener() {
                    @Override
                    public void deleteItem(int position) {
                        String sid=mList.get(position).getId();

                    }
                });
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
}