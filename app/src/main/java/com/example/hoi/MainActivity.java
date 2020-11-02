package com.example.hoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mauth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FrameLayout frameLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private DatabaseReference userref;
    private EditText editText;
    private Button button;
    private TextView text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();

        editText = (EditText) findViewById(R.id.phno);
        button = (Button) findViewById(R.id.call);
        text = (TextView)findViewById(R.id.name);
        Intent intent=getIntent();
        String name=intent.getStringExtra("key");
        String number=intent.getStringExtra("key1");
        text.setText(name);
        editText.setText(number);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String phno = editText.getText().toString().trim();
                Intent itentCall = new Intent(Intent.ACTION_CALL);
                if (phno.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "please enter your number", Toast.LENGTH_SHORT).show();
                } else {
                    itentCall.setData(Uri.parse("tel:" + phno));
                }
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Please grant permission", Toast.LENGTH_SHORT).show();
                    requestionPermission();
                } else {
                    startActivity(itentCall);
                }

                savenumber();
            }
        });


        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.Drawer_open, R.string.Drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });

    }

    private void savenumber() {

        Calendar cald = Calendar.getInstance();
        SimpleDateFormat currenetdate = new SimpleDateFormat("dd-MMMM-yyyy");
        String savedate = currenetdate.format(cald.getTime());
        String mtext=text.getText().toString();
        String phno = editText.getText().toString();
        SimpleDateFormat currenettime = new SimpleDateFormat("HH:mm");
        String savetime = currenettime.format(cald.getTime());
        String currentid = mauth.getCurrentUser().getUid();
        userref = FirebaseDatabase.getInstance().getReference().child("userlog").child(currentid).child(savedate+":"+savetime );
        HashMap userm = new HashMap();
        userm.put("name",mtext);
        userm.put("phone",phno);
        userm.put("date",savedate);
        userm.put("time",savetime);
        userref.updateChildren(userm).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {

                    Toast.makeText(MainActivity.this, " CALLING... ", Toast.LENGTH_LONG).show();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(MainActivity.this, "error" + message, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void requestionPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.home:
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contacts:
                Intent intent = new Intent(MainActivity.this, contacts.class);
                startActivity(intent);
                Toast.makeText(this, "contacts", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logs:
                Toast.makeText(this, "call logs", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(MainActivity.this, log.class);
                startActivity(in);
                break;
            case R.id.feedback:
                Toast.makeText(this, "feedback", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact_us:
                Toast.makeText(this, "contact us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();

                mauth.signOut();
                sendusertologinactivity();
                break;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser currentuser = mauth.getCurrentUser();
        if (currentuser == null) {
            sendusertologinactivity();
        }


    }

    private void sendusertologinactivity() {
        Intent loginintent = new Intent(MainActivity.this, clog.class);
        loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginintent);
        finish();
    }


}