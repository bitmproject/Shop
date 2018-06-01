package com.example.aunshon.shop;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteRecyclerAdapter extends RecyclerView.Adapter<FavouriteRecyclerAdapter.MyviewHolder> {


    private Context mcontext;
    private List<FavouritePInfo> mdata;
    private  OnItemClickListener mListener;

    public FavouriteRecyclerAdapter(Context mcontext, List<FavouritePInfo> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mcontext).inflate(R.layout.cardview_item,parent,false);
        return new MyviewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        holder.tv_product_title.setText(mdata.get(position).getProductTitle());
        Picasso.with(mcontext)
                .load(mdata.get(position).getThumbnil())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(holder.product_tumbnil);
        holder.tv_price.setText(mdata.get(position).getProductPrice().toString());
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener
            ,MenuItem.OnMenuItemClickListener{

        TextView tv_product_title,tv_price;
        ImageView product_tumbnil;
        CardView cardView;

        public MyviewHolder(View itemView) {
            super(itemView);
            tv_product_title=itemView.findViewById(R.id.priduct_name);
            product_tumbnil=itemView.findViewById(R.id.image_card);
            tv_price=itemView.findViewById(R.id.price);
            cardView=itemView.findViewById(R.id.cardView_id);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    mListener.onDeleteClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem delete=menu.add(menu.NONE,1,1,"Delete");
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    mListener.onDeleteClick(position);
                }
            }
            return false;
        }
    }
    /*public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Delete");
        builder.setMessage("Delete This Prodect ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder;
    }*/

    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
}
