package com.tunisianfood_advisor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.model.Category;
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

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ItemHolder>{

    private List<Category> categories = new ArrayList<>();
    private Context context;
    private final OnCategoryClickListener mListener;

    public CategoriesAdapter(Context context){
        this.context = context;

        try {
            this.mListener = ((OnCategoryClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnPlaceClickListener.");
        }




        OkHttpClient client = new OkHttpClient();

        String url_ = context.getString(R.string.s_url);
        String url = url_+"getAllCat/";

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
                    Type catType = new TypeToken<ArrayList<Category>>(){}.getType();
                    List<Category> catList = gson.fromJson(response.body().string(), catType);
                    Log.d("TAG","rep : "+Integer.toString(R.drawable.hamburger_photo)+" "+Integer.toString(R.drawable.pizza_category)+" "+Integer.toString(R.drawable.category_sushi)+" "+Integer.toString(R.drawable.barbecue_category)+" "+Integer.toString(R.drawable.brazilian_category)+" "+Integer.toString(R.drawable.fitness_category)+" "+Integer.toString(R.drawable.sweet_category)+" "+Integer.toString(R.drawable.ice_cream_category)+" "+Integer.toString(R.drawable.chinese_category)+" "+Integer.toString(R.drawable.arab_category));



                    // String[] categoryNames = {"Des collations", "Pizza", "Japonais", "Barbecue", "Brésilien", "En bonne santé", "Bonbons", "Glace", "Chinois", "Arabe"};
                    String categoryNames[] = new String[catList.size()];
                    for (int i = 0; i < catList.size(); i++){
                        categoryNames[i] = catList.get(i).getCategoryName();
                    }

                    int images_array[] = new int[catList.size()];
                    for (int i = 0; i < catList.size(); i++){
                        images_array[i] = catList.get(i).getCategoryDrawable();
                    }



/*
                        int images_array[] = {
                                R.drawable.hamburger_photo,
                                R.drawable.pizza_category,
                                R.drawable.category_sushi,
                                R.drawable.barbecue_category,
                                R.drawable.brazilian_category,
                                R.drawable.fitness_category,
                                R.drawable.sweet_category,
                                R.drawable.ice_cream_category,
                                R.drawable.chinese_category,
                                R.drawable.arab_category,

                        };*/


                    for (int i = 0; i < 10; i++){
                        Category category = new Category(categoryNames[i], images_array[i]);
                        categories.add(category);
                    }

                }
            }
        });
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, viewGroup, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final Category category =  categories.get(position);

        holder.mCategoryName.setText(category.getCategoryName());

        holder.mCategoryImage.setImageResource(category.getCategoryDrawable());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onCategoryClickListener(category);
            }
        });
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mCategoryName;
        public ImageView mCategoryImage;
        public View mView;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mCategoryName = itemView.findViewById(R.id.category_name);
            mCategoryImage = itemView.findViewById(R.id.category_photo);
        }

        @Override
        public void onClick(View v) {}
    }

    public interface OnCategoryClickListener {
        void onCategoryClickListener(Category category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
