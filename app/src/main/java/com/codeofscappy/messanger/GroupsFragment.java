package com.codeofscappy.messanger;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GroupsFragment extends Fragment {


    private View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    //Firebase
    private DatabaseReference GroupRef;





    public GroupsFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_groups, container, false);


        // Initzialisiert den Aktuellen Gruppen Namen aus dem Database-Datenstaz der Referenz "Groups"
        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

         // Initialize
        initializeFields();

        // Abrufen und Anzeigen von Gruppen im TabView-Fragment
        RetrieveAndDisplayGroups();


        return groupFragmentView;

    }




    // Methode of Field of Init
    private void initializeFields()
    {
        list_view = (ListView) groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_groups);
        list_view.setAdapter(arrayAdapter);
    }


    // Abrufen und Anzeigen der Erstellten Gruppe im Tabview-Fragment von [Groups]
    private void RetrieveAndDisplayGroups()
    {
        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}