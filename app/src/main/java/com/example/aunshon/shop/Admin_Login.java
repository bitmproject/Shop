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

import java.util.regex.Pattern;

public class Admin_Login extends AppCompatActivity {
    private static final Pattern p=
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{1,}" +               //at least 1 characters
                    "$");

    RelativeLayout r1,r2;
    EditText email,password;
    Button loginadmin;
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
        setContentView(R.layout.activity_admin__login);

        r1=findViewById(R.id.rela_1);
        r2=findViewById(R.id.rela_2);
        email=findViewById(R.id.adminemailEt);
        password=findViewById(R.id.adminpasswordEt);
        loginadmin=findViewById(R.id.loignAdminbtn);
        mAuth=FirebaseAuth.getInstance();

        handler.postDelayed(runnable,600);
    }

    public void AdminloginBtn(View view) {
        String e = email.getText().toString().trim();
        String p= password.getText().toString().trim();

        if (email.getText().toString().trim().isEmpty() ||password.getText().toString().trim().isEmpty()
                || email.getText().toString().trim().equals(" ") || password.getText().toString().trim().equals(" ")) {
            email.setHint("Email ?");
            email.setError("Fill it up");
            password.setHint("password ?");
            password.setError("Fill it up");
        } else{

            mAuth.signInWithEmailAndPassword(e, p)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information


                                Toast.makeText(Admin_Login.this, "sign in by Admin", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Admin_Login.this,Admin_Deshboard.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                String mess = task.getException().getMessage();
                                Toast.makeText(Admin_Login.this, mess, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }
}
