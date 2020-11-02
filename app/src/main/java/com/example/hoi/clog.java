package com.example.hoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class clog extends AppCompatActivity {
    private EditText username, mail, password, phone;
    private Button regbtn,cregbtn;

    private FirebaseAuth mauth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clog);

        mail = findViewById(R.id.edit_text_email);
        password = findViewById(R.id.edit_text_password);

        regbtn = findViewById(R.id.button_register);
        cregbtn=findViewById(R.id.btnregister);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mail.getText().toString();
                String pass = password.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(clog.this, "enter email", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(clog.this, "enter password", Toast.LENGTH_SHORT).show();
                }


                else {
                    mauth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(clog.this, "login sucuessfull ", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(clog.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(clog.this, "error" + message, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }


            }


        });
        cregbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regintent = new Intent(clog.this, creg.class);
                startActivity(regintent);
            }
        });


    }

}






