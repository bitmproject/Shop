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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class product_details extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView2;
    ImageView imageView;
    TextView Price,Catagory,Title;
    Button addToCart;

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
        navigationView2.setNavigationItemSelectedListener(this);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setTitle("Product Details");

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        String title1=intent.getExtras().getString("title");
        String catagory1=intent.getExtras().getString("catagory");
        String price1=intent.getExtras().getString("price");
        String image1=intent.getExtras().getString("image");

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
}
