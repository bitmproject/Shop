package com.example.aunshon.shop;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Deshboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    SearchView searchView;
    ImageButton imageButton;
    NavigationView navigationView;

    List<porduct> tempProduct;
    RecyclerView recyclerview;
    RecyclerViewAdapter myadapter,newAdapter;
    DatabaseReference mdatabaseref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__deshboard);
        toolbar=findViewById(R.id.mytoolbaradmin);


        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("Home");
        drawerLayout=findViewById(R.id.admindesh);
        imageButton=findViewById(R.id.cartimage);
        navigationView=findViewById(R.id.navigationViewadmin);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Admin Deshboard");

        tempProduct=new ArrayList<>();
        recyclerview=findViewById(R.id.recyclerviewadmin);
        recyclerview.setHasFixedSize(true);

        mdatabaseref= FirebaseDatabase.getInstance().getReference("uploads");
        mdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    porduct pro=postSnapshot.getValue(porduct.class);
                    tempProduct.add(pro);
                }
                myadapter=new RecyclerViewAdapter(Admin_Deshboard.this,tempProduct);
                recyclerview.setLayoutManager(new GridLayoutManager(Admin_Deshboard.this,3));
                recyclerview.setAdapter(myadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Admin_Deshboard.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.home){
            Intent intent=new Intent(Admin_Deshboard.this,MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.signout){
            Intent intent=new Intent(Admin_Deshboard.this,login.class);
            startActivity(intent);
        }
        else if(id == R.id.admin){
            Intent intent=new Intent(Admin_Deshboard.this,Admin_Login.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scarch,menu);
        getMenuInflater().inflate(R.menu.menu,menu);

        final MenuItem menuItem=menu.findItem(R.id.scarch);
        searchView= (SearchView) menuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.white));
        searchView.setMaxWidth(700);
        searchView.setQueryHint("Search Here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                menuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final  List<porduct> filtermodelist=filtered(tempProduct,newText);
                newAdapter=new RecyclerViewAdapter(Admin_Deshboard.this,filtermodelist);
                recyclerview.setLayoutManager(new GridLayoutManager(Admin_Deshboard.this,3));
                recyclerview.setAdapter(newAdapter);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }
    private List<porduct>filtered(List<porduct>p1,String query){
        query= query.toLowerCase();
        List<porduct>filteredArrayList=new ArrayList<>();
        for(porduct model:p1){
            final String text=model.getProductTitle().toLowerCase();
            if(text.startsWith(query)){
                filteredArrayList.add(model);
            }
        }
        return filteredArrayList;
    }

    public void fabClicked(View view) {
        Intent intent=new Intent(Admin_Deshboard.this,addProductActivity.class);
        startActivity(intent);
    }
}
