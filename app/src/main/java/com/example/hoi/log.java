package com.example.hoi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class log extends AppCompatActivity {

    private ListView listoo;
    private FirebaseAuth mauth;
    private DatabaseReference userref;
    ArrayList<String> alist = new ArrayList<>();

    ArrayAdapter<String> madapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mauth=FirebaseAuth.getInstance();
        String currentid = mauth.getCurrentUser().getUid();



        userref = FirebaseDatabase.getInstance().getReference("userlog").child(currentid);
        madapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alist);

        listoo = (ListView)findViewById(R.id.listo);
        listoo.setAdapter(madapter);
        listoo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String selectedItem = (String) parent.getItemAtPosition(i);
                String[] str = selectedItem.split(" :          ");
                String[] str1 =str[1].split("\n\n");
                Intent intent = new Intent(log.this,MainActivity.class);
                intent.putExtra("key",str[0]);
                intent.putExtra("key1",str1[0]);
                startActivity(intent);
            }
        });

userref.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {

                String phone = dataSnapshot1.child("phone").getValue(String.class);
                String time = dataSnapshot1.child("time").getValue(String.class);
                String date = dataSnapshot1.child("date").getValue(String.class);
                String name=dataSnapshot1.child("name").getValue(String.class);

                alist.add(name+"                :          "+phone + "\n\n" + date + "      " + time);

                madapter.notifyDataSetChanged();


        }
            sort sorting = new sort(alist);
        ArrayList<String> sortedArrayList = sorting.sortDecending();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        madapter.notifyDataSetChanged();

    }
});


    }
}