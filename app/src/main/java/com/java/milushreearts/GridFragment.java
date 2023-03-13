package com.java.milushreearts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class GridFragment extends Fragment {

    private RecyclerView recViewGrid;
    private ItemAdapterGrid adapterGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        recViewGrid = view.findViewById(R.id.recView_Grid);
        recViewGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));

        FirebaseRecyclerOptions<ItemHolderGrid> options = new FirebaseRecyclerOptions.Builder<ItemHolderGrid>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("dataStorage"), ItemHolderGrid.class)
                .build();

        adapterGrid = new ItemAdapterGrid(options);
        recViewGrid.setAdapter(adapterGrid);




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterGrid.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterGrid.stopListening();
    }
}