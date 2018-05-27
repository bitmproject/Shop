package com.example.aunshon.shop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyviewHolder> {

    private Context mcontext;
    private List<porduct> mdata;

    public RecyclerViewAdapter(Context mcontext, List<porduct> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(mcontext).inflate(R.layout.cardview_item,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, final int position) {

        holder.tv_product_title.setText(mdata.get(position).getProductTitle());
        Picasso.with(mcontext)
                .load(mdata.get(position).getThumbnil())
                .fit()
                .into(holder.product_tumbnil);
        //holder.product_tumbnil.setImageResource(mdata.get(position).getThumbnil());
        holder.tv_price.setText(mdata.get(position).getProductPrice().toString());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,product_details.class);
                intent.putExtra("title",mdata.get(position).getProductTitle());
                intent.putExtra("price",mdata.get(position).getProductPrice());
                intent.putExtra("catagory",mdata.get(position).getProductCatagory());
                intent.putExtra("image",mdata.get(position).getThumbnil());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{

        TextView tv_product_title,tv_price;
        ImageView product_tumbnil;
        CardView cardView;

        public MyviewHolder(View itemView) {
            super(itemView);

            tv_product_title=itemView.findViewById(R.id.priduct_name);
            product_tumbnil=itemView.findViewById(R.id.image_card);
            tv_price=itemView.findViewById(R.id.price);
            cardView=itemView.findViewById(R.id.cardView_id);
        }
    }
}
