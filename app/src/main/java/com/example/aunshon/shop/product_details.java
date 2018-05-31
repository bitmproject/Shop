package com.example.aunshon.shop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class product_details extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView2;
    ImageView imageView;
    TextView Price,Catagory,Title;
    Button addToCart ,favouriteBtn;
    private DatabaseReference mDatabase;
    String image1,price1,catagory1,title1,ftoken;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        toolbar=findViewById(R.id.mytoolbar_product);
        drawerLayout=findViewById(R.id.product_);
        navigationView2=findViewById(R.id.navigationView2);
        imageView=findViewById(R.id.image);
        Price=findViewById(R.id.priceEt);
        Title=findViewById(R.id.titleEt);
        Catagory=findViewById(R.id.catagoryEt);
        addToCart=findViewById(R.id.cartbtn);
        favouriteBtn=findViewById(R.id.favouritebtn);
        navigationView2.setNavigationItemSelectedListener(this);
        mAuth=FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setTitle("Product Details");

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        title1=intent.getExtras().getString("title");
        catagory1=intent.getExtras().getString("catagory");
        price1=intent.getExtras().getString("price");
        image1=intent.getExtras().getString("image");

        Title.setText(title1);
        Catagory.setText(catagory1);
        Price.setText(price1.toString());
        Picasso.with(product_details.this)
                .load(image1)
                .fit()
                .into(imageView);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.home){
            Intent intent=new Intent(product_details.this,MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.signout){
            Intent intent=new Intent(product_details.this,login.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void favouriteBtnClicked(View view) {
        FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
        String uid=current_user.getUid();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("favoutites").push();
;
        // complex data storing
        HashMap<String, String> usermap=new HashMap<>();
        usermap.put("title",title1);
        usermap.put("price",price1);
        usermap.put("catagory",catagory1);
        usermap.put("imageurl",image1);
        usermap.put("ftoken",uid);

        FavouritePInfo favouritePInfo=new FavouritePInfo(title1,price1,catagory1,image1);

        mDatabase.setValue(favouritePInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    /*FirebaseUser user = mAuth.getCurrentUser();
                    Intent mainint=new Intent(signUp.this,MainActivity.class);
                    mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainint);*/

                }
            }
        });

        Toast.makeText(product_details.this, "Favourite added", Toast.LENGTH_SHORT).show();
    }

    public void cartBtnClicked(View view) {
    }
}
