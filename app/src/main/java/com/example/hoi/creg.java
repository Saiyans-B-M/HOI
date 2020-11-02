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

import com.example.hoi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class creg extends AppCompatActivity {
    private EditText username, mail, password, repass, phone;
    private Button regbtn;

    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    FirebaseUser mUser;
    private DatabaseReference userref;
    String currentid;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creg);
        username = findViewById(R.id.edit_text_name);
        mail = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repass = findViewById(R.id.repassword);
        phone = findViewById(R.id.edit_text_phone);
        regbtn = findViewById(R.id.button_register);
        mUser = mauth.getCurrentUser();


        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mail.getText().toString();
             final   String pass = password.getText().toString();
                final String reenter = repass.getText().toString();
                final String user = username.getText().toString();
                final String number = phone.getText().toString();

                if (TextUtils.isEmpty(user)) {
                    Toast.makeText(creg.this, "enter username", Toast.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(creg.this, "enter email", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(creg.this, "enter password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(reenter)) {
                    Toast.makeText(creg.this, " re enter  password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(number)) {

                    Toast.makeText(creg.this, "enter phone", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(reenter)) {
                    Toast.makeText(creg.this, "password mismatch ", Toast.LENGTH_SHORT).show();
                } else {
                    mauth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()){
                                               Toast.makeText(creg.this, "Verification code sent to" + mUser.getEmail(), Toast.LENGTH_SHORT).show();

                                           }else
                                               Toast.makeText(creg.this, "Verification code not sent" , Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        Toast.makeText(creg.this, "register successfull ", Toast.LENGTH_SHORT).show();
                                        currentid = mauth.getCurrentUser().getUid();
                                        userref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentid);
                                        HashMap userm = new HashMap();
                                        userm.put("username", user);
                                        userm.put("email", email);
                                        userm.put("pass", reenter);
                                        userm.put("phone", number);

                                        userref.updateChildren(userm).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(creg.this, " successfull ", Toast.LENGTH_SHORT).show();
                                                    sendusertolog();
                                                } else {
                                                    String message = task.getException().getMessage();
                                                    Toast.makeText(creg.this, "error" + message, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(creg.this, "error" + message, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                    }
                }


        });


    }

    private void sendEmailVerificationCode() {
        mUser.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(creg.this, "Verification code failed to sent ", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void sendusertolog() {
        Intent logint=new Intent(creg.this,clog.class);
        startActivity(logint);
        finish();

    }

}






