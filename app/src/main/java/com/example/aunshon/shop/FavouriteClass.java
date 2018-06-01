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

public class FavouriteClass extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,FavouriteRecyclerAdapter.OnItemClickListener{

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    SearchView searchView;
    ImageButton imageButton;
    NavigationView navigationView;

    List<FavouritePInfo> tempProduct;
    RecyclerView recyclerview;
    FavouriteRecyclerAdapter myadapter,newAdapter;
    DatabaseReference mdatabaseref;
    ValueEventListener mDbListener;
    CardView cardView;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    ProgressBar mporgressbar;
    private FirebaseStorage mstorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_class);

        toolbar=findViewById(R.id.mytoolbarfavourite);


        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        drawerLayout=findViewById(R.id.favouriteDeshboard);
        imageButton=findViewById(R.id.cartimage);
        navigationView=findViewById(R.id.navigationViewfavourite);
        navigationView.setNavigationItemSelectedListener(this);
        mporgressbar=findViewById(R.id.progerssBarfavourite);
        cardView=findViewById(R.id.cardView_id);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        actionBarDrawerToggle=new ActionBarDrawerToggle(FavouriteClass.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Favourites");

        tempProduct=new ArrayList<>();
        recyclerview=findViewById(R.id.recyclerviewfavourite);
        recyclerview.setHasFixedSize(true);

        myadapter=new FavouriteRecyclerAdapter(FavouriteClass.this,tempProduct);
        recyclerview.setLayoutManager(new GridLayoutManager(FavouriteClass.this,3));
        recyclerview.setAdapter(myadapter);
        myadapter.setOnItemClickListener(FavouriteClass.this);
        mstorage= FirebaseStorage.getInstance();

        mdatabaseref= FirebaseDatabase.getInstance().getReference("favourites");
        mDbListener=mdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tempProduct.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    FavouritePInfo pro=postSnapshot.getValue(FavouritePInfo.class);
                    if(firebaseUser.getEmail().equals(pro.getEmail())){
                        pro.setMkey(dataSnapshot.getKey());
                        tempProduct.add(pro);
                    }
                }
                myadapter.notifyDataSetChanged();
                mporgressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FavouriteClass.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mporgressbar.setVisibility(View.INVISIBLE);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.home){
            Intent intent=new Intent(FavouriteClass.this,MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.signout){
            Intent intent=new Intent(FavouriteClass.this,login.class);
            startActivity(intent);
        }
        else if(id == R.id.admin){
            Intent intent=new Intent(FavouriteClass.this,Admin_Login.class);
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
                final  List<FavouritePInfo> filtermodelist=filtered(tempProduct,newText);
                newAdapter=new FavouriteRecyclerAdapter(FavouriteClass.this,filtermodelist);
                recyclerview.setLayoutManager(new GridLayoutManager(FavouriteClass.this,3));
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
    private List<FavouritePInfo>filtered(List<FavouritePInfo>p1,String query){
        query= query.toLowerCase();
        List<FavouritePInfo> filteredArrayList=new ArrayList<>();
        for(FavouritePInfo model:p1){
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
        FavouritePInfo selectedItem = tempProduct.get(position);
        final String selectedKey = selectedItem.getMkey();

        StorageReference imageRef = mstorage.getReferenceFromUrl(selectedItem.getThumbnil());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mdatabaseref.child(selectedKey).removeValue();
                Toast.makeText(FavouriteClass.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FavouriteClass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
