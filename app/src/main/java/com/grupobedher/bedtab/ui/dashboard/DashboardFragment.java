package com.grupobedher.bedtab.ui.dashboard;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grupobedher.bedtab.ImagenCrop;
import com.grupobedher.bedtab.LoginActivity;
import com.grupobedher.bedtab.models.Offer;
import com.grupobedher.bedtab.Adapters.OfferAdapter;
import com.grupobedher.bedtab.R;
import com.grupobedher.bedtab.models.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private FirebaseDatabase database2;
    private DatabaseReference ref;
    private DatabaseReference refdelete;
    private FirebaseStorage mStoragedelete;
    private ArrayList<Offer> mList;
    private OfferAdapter mAdapter;
    private String admin;
    private FirebaseAuth fAuth;
    private TextView nombredialog;
    private TextView numdialog;
    private TextView emailDialog;
    private Button btnCerrar;
    private String id;
    private Usuario u;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        FloatingActionButton fab =root.findViewById(R.id.fab);
        FloatingActionButton perfil =root.findViewById(R.id.perfilbtn);
        fAuth= FirebaseAuth.getInstance();
        admin=fAuth.getCurrentUser().getEmail();
        id=fAuth.getCurrentUser().getUid();
        recyclerView=root.findViewById(R.id.rvOfertas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mList=new ArrayList<>();
        mAdapter=new OfferAdapter(getContext(),mList);
        recyclerView.setAdapter(mAdapter);
        database=FirebaseDatabase.getInstance();
        database2=FirebaseDatabase.getInstance();
        ref=database.getReference("Productos");
        refdelete=database2.getReference();
        mStoragedelete= FirebaseStorage.getInstance();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Offer e=snapshot1.getValue(Offer.class);
                    String sid=snapshot1.getKey();
                    e.setId(sid);
                    mList.add(e);
                }
                mAdapter.setOnItemClickListener(new OfferAdapter.OnItemClickListener() {
                    @Override
                    public void deleteItem(final int position) {
                        String sid=mList.get(position).getId();
                        StorageReference delete=mStoragedelete.getReferenceFromUrl(mList.get(position).getImageURL());
                        delete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                refdelete.child("Productos").child(mList.get(position).getId()).removeValue();
                                Toast.makeText(getContext(),"Eliminado",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"ERROR",Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                });
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        if(!admin.equals("bedherapp@gmail.com")){
            fab.setVisibility(View.INVISIBLE);
        }
        else{
            perfil.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ImagenCrop.class));
            }
        });
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity());
                View view1=getLayoutInflater().inflate(R.layout.profile_dialog,null);
                mBuilder.setView(view1);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
                nombredialog=view1.findViewById(R.id.Nombredialog);
                numdialog=view1.findViewById(R.id.telefonodialog);
                emailDialog=view1.findViewById(R.id.emaildialog);
                btnCerrar=view1.findViewById(R.id.cerrars);
                ref=database.getReference().child("Users").child("private").child(id);

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        u=snapshot.getValue(Usuario.class);
                        nombredialog.setText(u.getNombre());
                        numdialog.setText(u.getTelefono());
                        emailDialog.setText(u.getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(),"Cerrado Sesión",Toast.LENGTH_LONG).show();
                    }
                });

                btnCerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        getActivity().finish();
                        Intent i = new Intent(getActivity(),LoginActivity.class);
                        startActivity(i);
                    }
                });

            }
        });
        return root;
    }


}