package com.tunisianfood_advisor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.model.Item;
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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder>{

    private List<Item> items = new ArrayList<>();
    private Context context;
    private final onClickListener mListener;
    private String place_id;

    public ItemAdapter(Context context,String placeId){
        this.context = context;
        place_id = placeId;

        try {
            this.mListener = ((onClickListener) context);
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
                    Type itemType = new TypeToken<ArrayList<Item>>(){}.getType();
                    List<Item> itemList = gson.fromJson(response.body().string(), itemType);


                    String itemNames[] = new String[itemList.size()];
                    for (int i = 0; i < itemList.size(); i++){
                        itemNames[i] = itemList.get(i).getItemName();
                    }

                    String itemPrices[] = new String[itemList.size()];
                    for (int i = 0; i < itemList.size(); i++){
                        itemPrices[i] = itemList.get(i).getItemPrice();
                    }

                    String itemDesc[] = new String[itemList.size()];
                    for (int i = 0; i < itemList.size(); i++){
                        itemDesc[i] = itemList.get(i).getItemDesc();
                    }

                    for (int i = 0; i < itemList.size(); i++){
                        Item item = new Item(itemNames[i],itemDesc[i],itemPrices[i]);
                        items.add(item);
                    }

                }
            }
        });
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, viewGroup, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final Item item =  items.get(position);

        holder.itemName.setText(item.getItemName());
        holder.itemPrice.setText(item.getItemPrice());
        holder.itemDesc.setText(item.getItemDesc());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onClickListener(item);
            }
        });
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView itemName,itemDesc,itemPrice;
        public View mView;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemDesc = itemView.findViewById(R.id.item_description);
        }

        @Override
        public void onClick(View v) {}
    }

    public interface onClickListener {
        void onClickListener(Item item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
