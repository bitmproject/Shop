package com.example.aunshon.shop;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AllOrders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,CartAdapter.OnItemClickListener {

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    SearchView searchView;
    ImageButton imageButton;
    NavigationView navigationView;

    List<cartmodel> tempProduct;
    RecyclerView recyclerview;
    CartAdapter myadapter, newAdapter;
    DatabaseReference mdatabaseref;
    ValueEventListener mDbListener;
    CardView cardView;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    ProgressBar mporgressbar;
    private FirebaseStorage mstorage,mstorage1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

        toolbar = findViewById(R.id.mytoolbarallorder);


        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        drawerLayout = findViewById(R.id.allorderDeshboard);
        imageButton = findViewById(R.id.cartimage);
        navigationView = findViewById(R.id.navigationViewallorder);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) AllOrders.this);
        mporgressbar = findViewById(R.id.progerssBarallorder);
        cardView = findViewById(R.id.cardView_id);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        actionBarDrawerToggle = new ActionBarDrawerToggle(AllOrders.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Orders");

        tempProduct = new ArrayList<>();
        recyclerview = findViewById(R.id.recyclerviewallorder);
        recyclerview.setHasFixedSize(true);

        myadapter = new CartAdapter(AllOrders.this, tempProduct);
        recyclerview.setLayoutManager(new GridLayoutManager(AllOrders.this, 3));
        recyclerview.setAdapter(myadapter);
        myadapter.setOnItemClickListener((CartAdapter.OnItemClickListener) AllOrders.this);
        mstorage = FirebaseStorage.getInstance();

        mdatabaseref = FirebaseDatabase.getInstance().getReference("all_order");
        mDbListener = mdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tempProduct.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    cartmodel pro= postSnapshot.getValue(cartmodel.class);
                    if (firebaseUser.getEmail().equals(pro.getEmail())) {
                        pro.setMkey(dataSnapshot.getKey());
                        tempProduct.add(pro);
                    }
                }
                myadapter.notifyDataSetChanged();
                mporgressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AllOrders.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mporgressbar.setVisibility(View.INVISIBLE);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.home){
            Intent intent=new Intent(AllOrders.this,MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.signout){
            Intent intent=new Intent(AllOrders.this,login.class);
            startActivity(intent);
        }
        else if(id == R.id.admin){
            Intent intent=new Intent(AllOrders.this,Admin_Login.class);
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
                final  List<cartmodel> filtermodelist=filtered(tempProduct,newText);
                newAdapter=new CartAdapter(AllOrders.this,filtermodelist);
                recyclerview.setLayoutManager(new GridLayoutManager(AllOrders.this,3));
                recyclerview.setAdapter(newAdapter);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
    private List<cartmodel>filtered(List<cartmodel>p1,String query){
        query= query.toLowerCase();
        List<cartmodel> filteredArrayList=new ArrayList<>();
        for(cartmodel model:p1){
            final String text=model.getProductTitle().toLowerCase();
            if(text.startsWith(query)){
                filteredArrayList.add(model);
            }
        }
        return filteredArrayList;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mdatabaseref.removeEventListener(mDbListener);
    }

    @Override
    public void onDeleteClick(int position) {

    }
}

