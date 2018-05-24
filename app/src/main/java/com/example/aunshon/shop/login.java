package com.example.aunshon.shop;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    RelativeLayout r1,r2;
    private Button registration,forgotpass,userlogin;
    private EditText username,userpassword;

    private FirebaseAuth mAuth;
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.usernameedt);
        userpassword=findViewById(R.id.userpass);

        registration=findViewById(R.id.userreg);
        forgotpass=findViewById(R.id.userforgotpass);
        userlogin=findViewById(R.id.usersignin);

        mAuth=FirebaseAuth.getInstance();

        r1=findViewById(R.id.rela_1);
        r2=findViewById(R.id.rela_2);

        handler.postDelayed(runnable,400);
    }



    public void signin(View view) {
        String usernemail=username.getText().toString().trim();
        String password=userpassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(usernemail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            Toast.makeText(login.this, "sign in succesfull", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            String mess = task.getException().getMessage();
                            Toast.makeText(login.this, mess, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public void registrationcompletion(View view) {
        Intent intent=new Intent(login.this,signUp.class);
        startActivity(intent);

    }
}
