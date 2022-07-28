package com.example.cartsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cartsystem.DataBase.SQLiteAdaptor;
import com.example.cartsystem.DataBase.SQLitePOJO;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    List<SQLitePOJO> arrayListProduct=new ArrayList<>();
    RecyclerView rv_product;
    SQLiteAdaptor sqLiteAdaptor;
    TextView tv_total_amount;
    int total_all=0;
    int quantity=0;
    int total_quantity=0;
    TextView shipping;
    TextView grand_total;
    JSONObject product_object=new JSONObject();
    Button btn_checkot;
    private  float total_shipping=0;
    int total_amount=0;
    CartAdapter cartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().hide();
        tv_total_amount = findViewById(R.id.sub_total);
        shipping = findViewById(R.id.shipping);
        grand_total = findViewById(R.id.grand_total);
        btn_checkot= findViewById(R.id.btn_checkout);


        rv_product = findViewById(R.id.rvProducts);
        rv_product.setHasFixedSize(true);
        rv_product.setNestedScrollingEnabled(false);
        rv_product.setLayoutManager(new GridLayoutManager(this, 1));
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sqLiteAdaptor = new SQLiteAdaptor(this);
        UpdateAfterRemove();
    }
    private void UpdateAfterRemove()
    {
        total_all=0;

        total_shipping=0;
        quantity=0;
        total_quantity=0;
        total_amount=0;
        arrayListProduct.clear();


        arrayListProduct = sqLiteAdaptor.getProduct();

        if (arrayListProduct.size() > 0) {

            try {
//                JSONObject json = new JSONObject(arrayListProduct.toString());
                JSONArray data_array = new JSONArray(arrayListProduct.toString());
                for (int i = 0; i < arrayListProduct.size(); i++) {
                    JSONObject user_object = data_array.getJSONObject(i);


                    try {

                        total_all+=Float.parseFloat((user_object.getString("price")))*Float.parseFloat(user_object.getString("count"));
                        tv_total_amount.setText("\u20B9"+total_all);

                        shipping.setText("\u20B9"+total_shipping);
                        total_amount= (int) (total_all+total_shipping);
                        total_quantity+= Integer.parseInt(user_object.getString("count"));
                        product_object.put("product_id",user_object.getString("id"));
                        product_object.put("quantity",user_object.getString("count"));
                        grand_total.setText("\u20B9"+total_amount);

                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
                cartAdapter =new CartAdapter(getApplicationContext(), (ArrayList<SQLitePOJO>) arrayListProduct);
                rv_product.setItemAnimator(new DefaultItemAnimator());
                rv_product.setAdapter(cartAdapter);

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

        }
    }
    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

        private List<SQLitePOJO> products;
        private SQLiteAdaptor sqlite;
        private SQLiteAdaptor sqlites;
        private Context context;
        public CartAdapter(Context context, List<SQLitePOJO> product) {
            this.context = context;
            this.products = product;
            sqlites=new SQLiteAdaptor(context);

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView product_name, product_price,product_quantity;
            ImageView product_image,increment,decrement,iv_delete;
            public int count = 0;
            RelativeLayout root;


            ViewHolder(View itemView) {

                super(itemView);
                product_image=itemView.findViewById(R.id.product_image);
                product_name=itemView.findViewById(R.id.product_name);
                product_price=itemView.findViewById(R.id.product_price);
                increment=itemView.findViewById(R.id.increment);
                decrement=itemView.findViewById(R.id.decrement);
                product_quantity=itemView.findViewById(R.id.count);
                iv_delete=itemView.findViewById(R.id.iv_delete);
                root=itemView.findViewById(R.id.root);

            }

        }
        @Override
        public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_cart_item_holder, parent, false);
            CartAdapter.ViewHolder viewholder= new CartAdapter.ViewHolder(view);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(final CartAdapter.ViewHolder holder, final int position) {
            holder.itemView.setTag(products.get(position));
            final SQLitePOJO productlist = products.get(position);
            sqlite = new SQLiteAdaptor(context);
            Picasso.with(context).load(String.valueOf(productlist.getimagesurl())).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).into(holder.product_image);
            Float price= Float.parseFloat(productlist.getprices());
            quantity= Integer.parseInt(productlist.getWeight());
            holder.count= Integer.parseInt(productlist.getWeight());
            holder.product_price.setText("Amount: "+" \u20B9 "+price);
            holder.product_name.setText(productlist.getproductname());
            holder.product_quantity.setText(productlist.getWeight());


            SQLiteAdaptor sqLiteAdaptor1=new SQLiteAdaptor(getApplicationContext());
            holder.increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.count++;
                    if (holder.count >= 1) {
                        holder.product_quantity.setText((String.valueOf(holder.count)));


                        sqLiteAdaptor1.updatecart(holder.count, String.valueOf(productlist.getIds()));
                        total_all+=price;
                        tv_total_amount.setText("\u20B9"+total_all);

                        shipping.setText("\u20B9"+total_shipping);
                        total_amount= (int) (total_all+total_shipping);
                        grand_total.setText("\u20B9"+total_amount);
                    }
                }
            });

            holder.decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.count--;
                    if (holder.count <= 0) {
                        holder.product_quantity.setText("1");
                    } else {
                        sqLiteAdaptor1.updatecart(holder.count,String.valueOf(productlist.getIds()));
                        holder.product_quantity.setText((String.valueOf(holder.count)));
                        total_all-=price;
                        tv_total_amount.setText("\u20B9"+total_all);


                        shipping.setText("\u20B9"+total_shipping);
                        total_amount= (int) (total_all+total_shipping);
                        grand_total.setText("\u20B9"+total_amount);

                    }
                }
            });

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        sqLiteAdaptor.RemoveCart(String.valueOf(productlist.getIds()));
                        delete(position);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
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
            UpdateAfterRemove();
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount() - position);
            notifyDataSetChanged();
            Log.d("HelloLength", String.valueOf(arrayListProduct.size()));
            if (arrayListProduct.size()==0)
            {
                tv_total_amount.setText("\u20B9"+"0");

                shipping.setText("\u20B9"+"0");
                grand_total.setText("\u20B9"+"0");
                btn_checkot.setClickable(false);
                btn_checkot.setEnabled(false);
            }
        }

    }


}