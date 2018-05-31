package com.example.aunshon.shop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signUp extends AppCompatActivity {
    private EditText name,email,ppassword,phone;
    private Button signupbtn;
    private FirebaseAuth mAuth;
    String a="",b="",c="",d="";
    private DatabaseReference mDatabase;

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

                    a="1";
                }

            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (email.getText().length()<2){

                    b="1";
                }

            }
        });
        ppassword.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (ppassword.getText().length()<2){

                    c="1";
                }

            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (phone.getText().length()<11&&phone.getText().length()>12){
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


  if (email.getText().toString().trim().isEmpty() || ppassword.getText().toString().trim().isEmpty()||ppassword.getText().toString().equals(" ")
         || name.getText().equals(" ") || name.getText().toString().trim().isEmpty() || phone.getText().toString().trim().isEmpty()) {
      if(ppassword.getText().toString().equals(" ")){
          ppassword.setError("space can't be a password");
      }
      Matcher m=p.matcher(name.getText().toString());
      if (name.getText().toString().equals(" ")) {
          name.setError("space can't be a name");
      }

      name.setHint("Username ?");
      name.setError("Fill it up");
      ppassword.setHint("Password ?");
      ppassword.setError("Fill it up");
      email.setHint("Email ?");
      email.setError("Fill it up");
      phone.setHint("Phone ?");
      phone.setError("Fill it up");

  }else{
      if(!p.matcher(name.getText().toString().trim()).matches()){

          mAuth.createUserWithEmailAndPassword(usernemail,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()){
                      FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                      String uid=current_user.getUid();

                      mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                      String device_token= FirebaseInstanceId.getInstance().getToken();
                      // complex data storing
                      HashMap<String, String> usermap=new HashMap<>();
                      usermap.put("name",userna);
                      usermap.put("email",usernemail);
                      usermap.put("phone",userphone);
                      usermap.put("device_token",device_token);

                      UserInfoClass userInfoClass=new UserInfoClass(userna,usernemail,userphone,device_token);


                      mDatabase.setValue(userInfoClass).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                  }else{
                      String mess=task.getException().getMessage();
                      Toast.makeText(signUp.this, mess, Toast.LENGTH_SHORT).show();
                  }
              }
          });
      }

      else {
          name.setError("Without space or special symbol");
      }

  }
    }
}
