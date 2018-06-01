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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    private DatabaseReference mDatabase,mDatabase1,mDatabase2;
    String image1,price1,catagory1,title1,email;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef,mStorageRef1,mStorageRef2;
    private DatabaseReference mdatabaseRef,mdatabaseRef1,mdatabaseRef2;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    TextView phoneTv,emailTv,countTv,nameTv;
    DatabaseReference mdatabaseref;

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

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();


        navigationView2.setNavigationItemSelectedListener(this);
        mAuth=FirebaseAuth.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference("favourites");
        mdatabaseRef=FirebaseDatabase.getInstance().getReference("favourites");
        mStorageRef1= FirebaseStorage.getInstance().getReference("recent_cart");
        mdatabaseRef1=FirebaseDatabase.getInstance().getReference("recent_cart");
        mStorageRef2= FirebaseStorage.getInstance().getReference("all_order");
        mdatabaseRef2=FirebaseDatabase.getInstance().getReference("all_order");

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
        //email=intent.getExtras().getString("email");
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
            userdata();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void userdata() {
        mdatabaseref= FirebaseDatabase.getInstance().getReference("Users");
        mdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    UserInfoClass pro=postSnapshot.getValue(UserInfoClass.class);
                    if (firebaseUser!=null){
                        if(firebaseUser.getEmail().equals(pro.getEmail())){
                            nameTv=findViewById(R.id.person_nameTv);
                            nameTv.setText(pro.getName());
                            phoneTv=findViewById(R.id.phoneTv);
                            phoneTv.setText(pro.getPhone());
                            emailTv=findViewById(R.id.EmailTv);
                            emailTv.setText(pro.getEmail());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(product_details.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.home){
            Intent mainint=new Intent(product_details.this,MainActivity.class);
            mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainint);
        }
        else if(id == R.id.signout){
            if(firebaseUser!=null){
                firebaseAuth.signOut();
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                finish();
                Intent mainint=new Intent(product_details.this,MainActivity.class);
                mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainint);

            }else {
                Intent intent = new Intent(product_details.this, login.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.admin){
            if(firebaseUser!=null){
                Intent mainint=new Intent(product_details.this,Admin_Deshboard.class);
                mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainint);
            }else {
                Intent intent = new Intent(product_details.this, Admin_Login.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.myorder){
            if(firebaseUser!=null){
                Intent intent=new Intent(product_details.this,AllOrders.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(product_details.this, Admin_Login.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.favourites){
            if(firebaseUser!=null){
                Intent intent=new Intent(product_details.this,FavouriteClass.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Please Sign in !!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(product_details.this, login.class);
                startActivity(intent);
            }
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void favouriteBtnClicked(View view) {
        if(firebaseUser!=null){
            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
            String uid=current_user.getUid();

            //mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("favoutites").push();
            mDatabase= FirebaseDatabase.getInstance().getReference().child("favourites").push();
            // complex data storing
            HashMap<String, String> usermap=new HashMap<>();
            usermap.put("productTitle",title1);
            usermap.put("productCatagory",catagory1);
            usermap.put("productPrice",price1);
            usermap.put("thumbnil",image1);
            usermap.put("ftoken",uid);
            String up=mdatabaseRef.push().getKey();
            email=firebaseUser.getEmail();
            FavouritePInfo favouritePInfo=new FavouritePInfo(title1,price1,catagory1,image1,email);
            favouritePInfo.setMkey(up);
            mdatabaseRef.child(up).setValue(favouritePInfo);

            Toast.makeText(product_details.this, "Added Favourite", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(product_details.this,MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(product_details.this, login.class);
            startActivity(intent);
        }

    }

    public void cartBtnClicked(View view) {
        if(firebaseUser!=null){
            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
            String uid=current_user.getUid();

            //mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("favoutites").push();
            mDatabase1= FirebaseDatabase.getInstance().getReference().child("recent_cart").push();
            // complex data storing
            HashMap<String, String> usermap=new HashMap<>();
            usermap.put("productTitle",title1);
            usermap.put("productCatagory",catagory1);
            usermap.put("productPrice",price1);
            usermap.put("thumbnil",image1);
            usermap.put("ftoken",uid);
            String up=mdatabaseRef1.push().getKey();
            email=firebaseUser.getEmail();
            cartmodel car=new cartmodel(title1,price1,catagory1,image1,email);
            car.setMkey(up);
            mdatabaseRef1.child(up).setValue(car);

            Toast.makeText(product_details.this, "Added to cart", Toast.LENGTH_SHORT).show();

            addto();
            Intent mainint=new Intent(product_details.this,MainActivity.class);
            mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainint);

        }else {
            Intent intent = new Intent(product_details.this, login.class);
            startActivity(intent);
        }


    }

    private void addto() {
        FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
        String uid=current_user.getUid();

        //mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("favoutites").push();
        mDatabase2= FirebaseDatabase.getInstance().getReference().child("all_order").push();
        // complex data storing
        HashMap<String, String> usermap=new HashMap<>();
        usermap.put("productTitle",title1);
        usermap.put("productCatagory",catagory1);
        usermap.put("productPrice",price1);
        usermap.put("thumbnil",image1);
        usermap.put("ftoken",uid);
        String up=mdatabaseRef2.push().getKey();
        email=firebaseUser.getEmail();
        cartmodel car=new cartmodel(title1,price1,catagory1,image1,email);
        mdatabaseRef2.child(up).setValue(car);

    }
}
