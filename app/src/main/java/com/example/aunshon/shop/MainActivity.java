package com.example.aunshon.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.renderscript.ScriptGroup;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    List<porduct> tempProduct;
    RecyclerView recyclerView;
    RecyclerViewAdapter myadapter,newAdapter;
    DatabaseReference mdatabaseref;
    private FirebaseStorage mstorage;

    SearchView searchView;
    ImageButton imageButton;
    NavigationView navigationView;
    ProgressBar mprogressBar;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckInternetConnection checkInternetConnection =new CheckInternetConnection();

        if(!checkInternetConnection.isConnected(MainActivity.this)){
            checkInternetConnection.buildDialog(MainActivity.this).show();
        }

        toolbar=findViewById(R.id.mytoolbar);


        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setTitle("Home");
        drawerLayout=findViewById(R.id.main);
        imageButton=findViewById(R.id.cartimage);
        navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        mprogressBar=findViewById(R.id.progerssBarMain);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tempProduct=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        myadapter=new RecyclerViewAdapter(MainActivity.this,tempProduct);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
        recyclerView.setAdapter(myadapter);
        mstorage=FirebaseStorage.getInstance();

        mdatabaseref= FirebaseDatabase.getInstance().getReference("uploads");
        mdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tempProduct.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    porduct pro=postSnapshot.getValue(porduct.class);
                    pro.setMkey(dataSnapshot.getKey());
                    tempProduct.add(pro);
                }
                myadapter.notifyDataSetChanged();
                mprogressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mprogressBar.setVisibility(View.INVISIBLE);
            }
        });

       /* myadapter=new RecyclerViewAdapter(this,tempProduct);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(myadapter);*/

    }

    @SuppressLint("WrongConstant")
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
                newAdapter=new RecyclerViewAdapter(MainActivity.this,filtermodelist);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
                recyclerView.setAdapter(newAdapter);
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
    //for changing the text color of searchview
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.home){
            Intent intent=new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.signout){
            if(firebaseUser!=null){
                firebaseAuth.signOut();
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                finish();
                Intent mainint=new Intent(MainActivity.this,MainActivity.class);
                mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainint);

            }else {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.admin){
            if(firebaseUser!=null){
                Intent intent=new Intent(MainActivity.this,Admin_Deshboard.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(MainActivity.this, Admin_Login.class);
                startActivity(intent);
            }
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
