package com.example.aunshon.shop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class signUp extends AppCompatActivity {
    private EditText name,email,ppassword,phone;
    private Button signupbtn;
    private FirebaseAuth mAuth;
    String a="",b="",c="",d="";
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name=findViewById(R.id.usernamesign);
        email=findViewById(R.id.useremail);
        ppassword=findViewById(R.id.userpass);
        phone=findViewById(R.id.userphone);

        mAuth=FirebaseAuth.getInstance();

        name.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (name.getText().length()<2){
                    name.setHint("Enter Your Name");
                    a="1";
                }

            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (email.getText().length()<2){
                    email.setHint("Enter valid mail");
                    b="1";
                }

            }
        });
        ppassword.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (ppassword.getText().length()<2){
                    ppassword.setHint("Enter password");
                    c="1";
                }

            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (phone.getText().length()<11&&phone.getText().length()>12){
                    phone.setHint("Enter valid phone number");

                    d="1";
                }

            }
        });

    }

    public void signup(View view) {

        final String usernemail=email.getText().toString().trim();
        String password=ppassword.getText().toString().trim();
        final String userna=name.getText().toString().trim();
        final String userphone=phone.getText().toString().trim();


  //if (a.equals("")&&b.equals("")&&b.equals("")&&d.equals("")) {

      mAuth.createUserWithEmailAndPassword(usernemail,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()){
                  FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                  String uid=current_user.getUid();

                  mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                  String device_token= FirebaseInstanceId.getInstance().getToken();
                  // complex data storing
                  HashMap<String, String > usermap=new HashMap<>();
                  usermap.put("name",userna);
                  usermap.put("phone",userphone);
                  usermap.put("email",usernemail);
                  usermap.put("device_token",device_token);


                  mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful()){

                              FirebaseUser user = mAuth.getCurrentUser();
                              Intent mainint=new Intent(signUp.this,MainActivity.class);
                              mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              startActivity(mainint);

                          }
                      }
                  });

                  Toast.makeText(signUp.this, "Registration succesfull", Toast.LENGTH_SHORT).show();
                  Intent intent=new Intent(signUp.this,MainActivity.class);
                  startActivity(intent);
              }else{
                  String mess=task.getException().getMessage();
                  Toast.makeText(signUp.this, mess, Toast.LENGTH_SHORT).show();
              }
          }
      });
  //}else{
      //Toast.makeText(this, "please validate all from", Toast.LENGTH_SHORT).show();
  //}
    }
}
