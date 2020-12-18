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

public class PlaceFeatureAdapter extends RecyclerView.Adapter<PlaceFeatureAdapter.ItemHolder>{

    private List<Product> products = new ArrayList<>();
    private Context context;
    private final OnPlaceClickListener mListener;
    private String place_id;

    public PlaceFeatureAdapter(Context context,String placeId){
        this.context = context;
        place_id = placeId;

        try {
            this.mListener = ((OnPlaceClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnPlaceClickListener.");
        }



        OkHttpClient client = new OkHttpClient();

        String url_ = context.getString(R.string.s_url);
        String url = url_+"getAllRepaswithId/"+place_id;

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


                    String trendingNames[] = new String[prodList.size()];
                    for (int i = 0; i < prodList.size(); i++){
                        trendingNames[i] = prodList.get(i).getmProductName();
                    }

                    String trendingDesc[] = new String[prodList.size()];
                    for (int i = 0; i < prodList.size(); i++){
                        trendingDesc[i] = prodList.get(i).getmProductDescription();
                    }

                    String trendingPrices[] = new String[prodList.size()];
                    for (int i = 0; i < prodList.size(); i++){
                        trendingPrices[i] = prodList.get(i).getmProductValue();
                    }


                    for (int i = 0; i < prodList.size(); i++){
                        Product prod = new Product(trendingNames[i], trendingDesc[i] ,"Restaurant " + (i + 1), trendingPrices[i]);
                        products.add(prod);
                    }

                }
            }
        });


    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_product_card, viewGroup, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        final Product prod =  products.get(position);

        holder.mProductName.setText(prod.getmProductName());
        holder.mProductprice.setText(prod.getmProductValue());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onTrendingClickListener(prod);
            }
        });


    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mProductName, mProductprice;
        public ImageView mNoFavorite, mFavorite;
        public View mView;
        public Product mItem;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mProductName = itemView.findViewById(R.id.product_name);
            mProductprice = itemView.findViewById(R.id.product_price);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Article cliqué", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnPlaceClickListener {
        void OnPlaceNoFavoriteClick(Product products);
        void OnPlaceFavoriteClick(Product products);
        void onTrendingClickListener(Product product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
