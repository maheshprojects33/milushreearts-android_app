package com.java.milushreearts;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.icu.text.IDNA;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Switch;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.zip.Inflater;


public class HomeFragment extends Fragment {

    private RecyclerView recView;
    private ItemAdapter adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recView = view.findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<ItemHolder> options =
                new FirebaseRecyclerOptions.Builder<ItemHolder>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("dataStorage"), ItemHolder.class)
                .build();

        adapter = new ItemAdapter(options);
        recView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_icon, menu);
        MenuItem searchItem = menu.findItem(R.id.searchTop);

        SearchView searchView = (SearchView) searchItem.getActionView();



        super.onCreateOptionsMenu(menu,inflater);
    }





    public void searchProcessCriteria(String s){
        FirebaseRecyclerOptions<ItemHolder> options =
                new FirebaseRecyclerOptions.Builder<ItemHolder>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("dataStorage")
                                .child("name")
                                .startAt(s)
                                .endAt(s + "\uf8ff"), ItemHolder.class)
                        .build();

        adapter = new ItemAdapter(options);
        adapter.startListening();
        recView.setAdapter(adapter);

    }
}