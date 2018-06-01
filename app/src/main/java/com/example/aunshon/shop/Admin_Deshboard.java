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

public class Admin_Deshboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdminRecyclerviewAdapter.OnItemClickListener{

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    SearchView searchView;
    ImageButton imageButton;
    NavigationView navigationView;

    List<porduct> tempProduct;
    RecyclerView recyclerview;
    AdminRecyclerviewAdapter myadapter,newAdapter;
    DatabaseReference mdatabaseref;
    ValueEventListener mDbListener;
    CardView cardView;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    TextView phoneTv,emailTv,countTv,nameTv;

    ProgressBar mporgressbar;
    private FirebaseStorage mstorage;

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
        mporgressbar=findViewById(R.id.progerssBaradmin);
        cardView=findViewById(R.id.cardView_id);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Admin Deshboard");

        tempProduct=new ArrayList<>();
        recyclerview=findViewById(R.id.recyclerviewadmin);
        recyclerview.setHasFixedSize(true);

        myadapter=new AdminRecyclerviewAdapter(Admin_Deshboard.this,tempProduct);
        recyclerview.setLayoutManager(new GridLayoutManager(Admin_Deshboard.this,3));
        recyclerview.setAdapter(myadapter);
        myadapter.setOnItemClickListener(Admin_Deshboard.this);
        mstorage= FirebaseStorage.getInstance();

        mdatabaseref= FirebaseDatabase.getInstance().getReference("uploads");
        mDbListener=mdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tempProduct.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    porduct pro=postSnapshot.getValue(porduct.class);
                    pro.setMkey(postSnapshot.getKey());
                    tempProduct.add(pro);
                }
                myadapter.notifyDataSetChanged();
                mporgressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Admin_Deshboard.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mporgressbar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.home){
            Intent mainint=new Intent(Admin_Deshboard.this,MainActivity.class);
            mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainint);
        }
        else if(id == R.id.signout){
            if(firebaseUser!=null){
                firebaseAuth.signOut();
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                finish();
                Intent mainint=new Intent(Admin_Deshboard.this,MainActivity.class);
                mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainint);

            }else {
                Intent intent = new Intent(Admin_Deshboard.this, login.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.admin){
            if(firebaseUser!=null){
                Intent mainint=new Intent(Admin_Deshboard.this,Admin_Deshboard.class);
                mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainint);
            }else {
                Intent intent = new Intent(Admin_Deshboard.this, Admin_Login.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.myorder){
            if(firebaseUser!=null){
                Intent intent=new Intent(Admin_Deshboard.this,AllOrders.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(Admin_Deshboard.this, Admin_Login.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.favourites){
            if(firebaseUser!=null){
                Intent intent=new Intent(Admin_Deshboard.this,FavouriteClass.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Please Sign in !!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Admin_Deshboard.this, login.class);
                startActivity(intent);
            }
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
                final  List<porduct> filtermodelist=filtered(tempProduct,newText);
                newAdapter=new AdminRecyclerviewAdapter(Admin_Deshboard.this,filtermodelist);
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
                Toast.makeText(Admin_Deshboard.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mdatabaseref.removeEventListener(mDbListener);
    }

    @Override
    public void onDeleteClick(int position) {
        porduct selectedItem = tempProduct.get(position);
        final String selectedKey = selectedItem.getMkey();

        StorageReference imageRef = mstorage.getReferenceFromUrl(selectedItem.getThumbnil());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mdatabaseref.child(selectedKey).removeValue();
                Toast.makeText(Admin_Deshboard.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
