package com.example.bedtab.ui.places;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bedtab.Adapters.PlacesAdapter;
import com.example.bedtab.Place;
import com.example.bedtab.R;

import java.util.ArrayList;

public class PlacesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Place> placeList;
    private PlacesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_places, container, false);
        placeList = new ArrayList<>();
        placeList.add(new Place(R.mipmap.grupob, "Refaccionaria", "De 07:00 AM a 2:00 PM","Refaccionaria Bedher, Calle 49, Centro, Tizimín, Yuc."," 9868632211"));
        placeList.add(new Place(R.mipmap.autospa, "AutoSpa", "De ChecarHora","Bedher auto spa, Calle 48, 8 Calles, Tizimín, Yuc."," 9868636912"));
        placeList.add(new Place(R.mipmap.serviciog, "Servicio de Grúa 24hrs", "Llantera: De 07:00 AM a 2:00 PM","Mobil Llantera Y Mecánica Express","9861191626"));
        placeList.add(new Place(R.mipmap.carwash, "Carwash", "De ChecarHora","LLantera Car Wash, Calle 48, 8 Calles, Tizimín, Yuc.","9868636556"));
        recyclerView = root.findViewById(R.id.rvPlaces);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new PlacesAdapter(getContext(),placeList);
        adapter.setOnItemClickListener(new PlacesAdapter.OnItemClickListener() {
            @Override
            public void OpenPhone(int position) {
                Place place = placeList.get(position);
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel: "+place.getNumplace()));
                startActivity(i);
            }

            @Override
            public void OpenLocation(int position) {
                Place place=placeList.get(position);
                Uri uri = Uri.parse("geo:0, 0?q="+place.getGeo());
                Intent i= new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }
}
