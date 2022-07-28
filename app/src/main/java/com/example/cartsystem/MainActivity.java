package com.example.cartsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cartsystem.DataBase.SQLiteAdaptor;
import com.example.cartsystem.DataBase.SQLitePOJO;
import com.example.cartsystem.Modal.Modal;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
 private ArrayList<Modal.Product> arrayListProduct=new ArrayList<>();
 ProductAdapter productAdapter;
 RecyclerView rv_products;
 RelativeLayout ll_cart;
 SQLiteAdaptor sqLiteAdaptor;
    List<SQLitePOJO> arrayListCartcounter=new ArrayList<>();
    TextView tv_cart_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        sqLiteAdaptor=new SQLiteAdaptor(getApplicationContext());
        rv_products=findViewById(R.id.rv_products);
        ll_cart=findViewById(R.id.ll_cart);
        tv_cart_item=findViewById(R.id.tv_cart_item);
        rv_products.setHasFixedSize(true);
        rv_products.setNestedScrollingEnabled(false) ;
        rv_products.setLayoutManager(new GridLayoutManager(this,2));
        tv_cart_item.setText(String.valueOf(CartItem()));

        BindData();
        ll_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CartActivity.class));
            }
        });

    }
    private void BindData()
    {
        arrayListProduct.clear();
        try {
            JSONArray jsonArray = new JSONArray(LoadData());
            Log.d("Ressssssss", String.valueOf(jsonArray));
            for (int i=0;i<jsonArray.length();i++)
            {
                Modal.Product product=new Modal.Product();
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                product.setTitle(jsonObject.getString("title"));
                product.setId(jsonObject.getInt("id"));
                product.setCategory(jsonObject.getString("category"));
                product.setPrice(jsonObject.getDouble("price"));
                product.setImage(jsonObject.getString("image"));
                JSONObject rating=jsonObject.getJSONObject("rating");
                product.setRating(rating.getDouble("rate"));
                arrayListProduct.add(product);

            }
            productAdapter=new ProductAdapter(MainActivity.this);
            rv_products.setAdapter(productAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String LoadData() {
        String json = null;
        try {
            InputStream is = getAssets().open("products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

        Context context;

        public ProductAdapter(Context context) {
            this.context=context;
        }

        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_title,tv_mrp,tv_price;
            ImageView iv_image;
            LinearLayout ll_view;
            TextView tv_category,tv_off;
            RatingBar ratingBar;
            TextView tv_add_cart;

            ViewHolder(View itemView) {

                super(itemView);
                tv_title=itemView.findViewById(R.id.tv_title);
                tv_mrp=itemView.findViewById(R.id.tv_mrp);
                tv_price=itemView.findViewById(R.id.tv_price);
                iv_image=itemView.findViewById(R.id.iv_image);
                ll_view=itemView.findViewById(R.id.ll_view);
                tv_off=itemView.findViewById(R.id.tv_off);
                tv_category=itemView.findViewById(R.id.tv_category);
                ratingBar=itemView.findViewById(R.id.rating);
                tv_add_cart=itemView.findViewById(R.id.tv_add_cart);


            }

        }
        // inflates the row layout from xml when needed
        @Override
        public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_product_holder, parent, false);
            ProductAdapter.ViewHolder viewholder= new ProductAdapter.ViewHolder(view);

            return viewholder;
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(final ProductAdapter.ViewHolder holder, final int position) {


            holder.tv_title.setText(arrayListProduct.get(position).getTitle());
            holder.tv_category.setText(arrayListProduct.get(position).getCategory());
            holder.tv_price.setText("\u20B9 "+String.valueOf(arrayListProduct.get(position).getPrice()));
            holder.tv_mrp.setText("\u20B9 "+"10");
            holder.tv_off.setText("4% off");
            holder.ratingBar.setRating((float) arrayListProduct.get(position).getRating());

            holder.tv_mrp.setPaintFlags(holder.tv_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            Picasso.with(context).load(arrayListProduct.get(position).getImage()).placeholder(R.drawable.ic_baseline_image_24).into(holder.iv_image);


            if (sqLiteAdaptor.checkExistsss(String.valueOf(arrayListProduct.get(position).getId())))
            {
                Drawable d2 = getResources().getDrawable(R.drawable.alreadcart);
                holder.tv_add_cart.setBackground(d2);
                holder.tv_add_cart.setText("Go to Cart");

            }
            else {
                Drawable d2 = getResources().getDrawable(R.drawable.tv_back_add_cart);
                holder.tv_add_cart.setBackground(d2);
                holder.tv_add_cart.setText("Add to Cart");

            }
            holder.tv_add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.tv_add_cart.getText().toString().equalsIgnoreCase("Add to Cart"))
                    {
                        sqLiteAdaptor.insert(1,String.valueOf(arrayListProduct.get(position).getId()),String.valueOf(arrayListProduct.get(position).getPrice()),arrayListProduct.get(position).getTitle(),arrayListProduct.get(position).getImage());
                        Drawable d2 = getResources().getDrawable(R.drawable.alreadcart);
                        holder.tv_add_cart.setBackground(d2);
                        holder.tv_add_cart.setText("Go to Cart");
                        tv_cart_item.setText(String.valueOf(CartItem()));


                    }
                    else if (holder.tv_add_cart.getText().toString().equalsIgnoreCase("Go to Cart"))

                    {
                        startActivity(new Intent(MainActivity.this,CartActivity.class));
                    }

                }
            });
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return arrayListProduct.size();
        }

        public void delete(int position) { //removes the row
            arrayListProduct.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount() - position);
            notifyDataSetChanged();
        }

    }

    private int CartItem()
    {
        arrayListCartcounter.clear();
        arrayListCartcounter = sqLiteAdaptor.getProduct();
        return arrayListCartcounter.size();

    }

    @Override
    protected void onResume() {
        tv_cart_item.setText(String.valueOf(CartItem()));
        BindData();
        super.onResume();
    }
}