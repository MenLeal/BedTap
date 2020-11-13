package com.example.bedtab.ui.places;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_places, container, false);
        placeList = new ArrayList<>();
        placeList.add(new Place(R.mipmap.autospa, "Refaccionaria", "De 07:00 AM a 2:00 PM"));

        recyclerView = root.findViewById(R.id.rvPlaces);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new PlacesAdapter(getContext(),placeList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }
}
