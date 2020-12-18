package com.tunisianfood_advisor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ItemHolder>{

    private List<Product> products = new ArrayList<>();
    private Context context;
    private final OnProductClickListener mListener;
    private String User;

    public ProductAdapter(Context context, String Username){
        this.context = context;
        User = Username;

        try {
            this.mListener = ((OnProductClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnPlaceClickListener.");
        }

        OkHttpClient client = new OkHttpClient();

        String url_ = context.getString(R.string.s_url);
        String url = url_+"getRepaswithUser/"+User;

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Parse and get server response text data.
                    Gson gson = new Gson();
                    Type prodType = new TypeToken<ArrayList<Product>>(){}.getType();
                    List<Product> prodList = gson.fromJson(response.body().string(), prodType);


                    String favoriteFoodNames[] = new String[prodList.size()];
                    for (int i = 0; i < prodList.size(); i++){
                        favoriteFoodNames[i] = prodList.get(i).getmProductName();
                    }

                    String favoriteFoodPlaces[] = new String[prodList.size()];
                    for (int i = 0; i < prodList.size(); i++){
                        favoriteFoodPlaces[i] = prodList.get(i).getmProductRestaurant();
                    }

                    String favoriteFoodPrices[] = new String[prodList.size()];
                    for (int i = 0; i < prodList.size(); i++){
                        favoriteFoodPrices[i] = prodList.get(i).getmProductValue();
                    }

                    String favoriteFoodDesc[] = new String[prodList.size()];
                    for (int i = 0; i < prodList.size(); i++){
                        favoriteFoodDesc[i] = prodList.get(i).getmProductDescription();
                    }


                    for (int i = 0; i < prodList.size(); i++){
                        Product prod = new Product(favoriteFoodNames[i], favoriteFoodDesc[i], favoriteFoodPlaces[i], favoriteFoodPrices[i]);
                        products.add(prod);
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_card, viewGroup, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        final Product prod =  products.get(position);

        holder.mProductName.setText(prod.getmProductName());
        holder.mProductDescription.setText(prod.getmProductRestaurant());
        holder.mProductValue.setText(prod.getmProductValue());

        holder.mNoFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener  != null){
                    mListener.OnProductFavoriteClick(prod);
                    holder.mNoFavorite.setVisibility(View.GONE);
                    holder.mFavorite.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener  != null){
                    mListener.OnProductNoFavoriteClick(prod);
                    holder.mNoFavorite.setVisibility(View.VISIBLE);
                    holder.mFavorite.setVisibility(View.GONE);
                }
            }
        });
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mProductName, mProductDescription, mProductValue, mProductRestaurant;
        public ImageView mNoFavorite, mFavorite;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mProductName = itemView.findViewById(R.id.product_name);
            mProductDescription = itemView.findViewById(R.id.product_description);
            mProductValue = itemView.findViewById(R.id.product_price);
            mFavorite = itemView.findViewById(R.id.favorite);
            mNoFavorite = itemView.findViewById(R.id.no_favorite);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Article cliqué", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnProductClickListener {
        void OnProductNoFavoriteClick(Product products);
        void OnProductFavoriteClick(Product products);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
