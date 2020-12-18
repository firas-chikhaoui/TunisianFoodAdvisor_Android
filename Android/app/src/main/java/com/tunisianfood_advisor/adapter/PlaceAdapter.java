package com.tunisianfood_advisor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.activity.FavoritePlacesActivity;
import com.tunisianfood_advisor.activity.HomeActivity;
import com.tunisianfood_advisor.activity.PlacesListActivity;
import com.tunisianfood_advisor.model.Place;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ItemHolder>{

    private List<Place> mPlaces = new ArrayList<>();
    private Context context;
    private final OnPlaceClickListener mListener;
    private String categoryName;
    private String categoryName1;
    private String User1;
    private String UserFavoris;
    private String SearchRus;
    private String his;



    public PlaceAdapter(Context context, String categoryName1,String Username,String UserFav,String Search){
        this.context = context;
        categoryName = categoryName1;
        UserFavoris = UserFav;
        User1 = Username;
        SearchRus = Search;

        try {
            this.mListener = ((OnPlaceClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnPlaceClickListener.");
        }




        OkHttpClient client = new OkHttpClient();


        String url;
        // Log.d("search","res "+SearchRus);

        Log.d("search2","his2 "+his);

        if(SearchRus == null){
            if(User1 == null){
                if(categoryName == null){
                    String url_ = context.getString(R.string.s_url);
                    url = url_+"getAllRest/"+UserFavoris;
                }else{
                    String url_ = context.getString(R.string.s_url);
                    url = url_+"getAllRestwithCat/"+categoryName+"/"+UserFavoris;
                }
            }else {
                String url_ = context.getString(R.string.s_url);
                url = url_+"getRestwithUser/"+User1;
            }
        }else{
            String url_ = context.getString(R.string.s_url);
            url = url_+"getSearchAllRest/"+UserFavoris;
        }


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
                    Type restType = new TypeToken<ArrayList<Place>>(){}.getType();
                    List<Place> restList = gson.fromJson(response.body().string(), restType);
                    Log.d("TAG","rep1 : "+categoryName);


                    int placeId[] = new int[restList.size()];
                    for (int i = 0; i < restList.size(); i++){
                        placeId[i] = restList.get(i).getPlaceId();
                    }

                    String placeNames[] = new String[restList.size()];
                    for (int i = 0; i < restList.size(); i++){
                        placeNames[i] = restList.get(i).getPlaceName();
                    }

                    String placeLocation[] = new String[restList.size()];
                    for (int i = 0; i < restList.size(); i++){
                        placeLocation[i] = restList.get(i).getLocation();
                    }

                    String placeRating[] = new String[restList.size()];
                    for (int i = 0; i < restList.size(); i++){
                        placeRating[i] = restList.get(i).getRating();
                    }

                    String placeDelivery[] = new String[restList.size()];
                    for (int i = 0; i < restList.size(); i++){
                        placeDelivery[i] = restList.get(i).getDelivery();
                    }

                    int placeFavorite[] = new int[restList.size()];
                    for (int i = 0; i < restList.size(); i++){
                        placeFavorite[i] = restList.get(i).isFavorite();
                    }

                    for (int i = 0; i < restList.size(); i++){
                        Place place = new Place(placeId[i],placeNames[i], placeLocation[i], placeRating[i], placeDelivery[i],placeFavorite[i]);
                        mPlaces.add(place);
                    }


                }
            }
        });

    }

    public void setFavorite(int placeId) {
        if(mPlaces.size() > 0) {
            for (int i = 0; i < mPlaces.size(); i++) {
                if(mPlaces.get(i).getPlaceId() == placeId) {
                    if (mPlaces.get(i).isFavorite() == 0) {
                        String url_ = context.getString(R.string.s_url);
                        String url_R = url_+"setFavoriteUserRestau/";
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user", UserFavoris)
                                .add("restau", String.valueOf(mPlaces.get(i).getPlaceId()))
                                .build();
                        Request requestR = new Request.Builder()
                                .url(url_R)
                                .post(requestBody)
                                .build();


                        okHttpClient.newCall(requestR).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String s = response.body().string();
                                JSONArray jsonArray = null;
                                try {
                                    String state = "";
                                    jsonArray = new JSONArray(s);
                                    for (int i = 0 ; i < jsonArray.length() ; i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        state = jsonObject.getString("STATE");
                                    }
                                    if (state.equals("success")){

                                        if(User1 == null){
                                            if(categoryName == null){
                                                ((HomeActivity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context, "Ajoute a votre favorie", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }else{
                                                ((PlacesListActivity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context, "Ajoute a votre favorie", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }else {
                                            ((FavoritePlacesActivity)context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context, "Ajoute a votre favorie", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }



                                    }else {

                                        if(User1 == null){
                                            if(categoryName == null){
                                                ((HomeActivity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context, "échec", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }else{
                                                ((PlacesListActivity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context, "échec", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }else {
                                            ((FavoritePlacesActivity)context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context, "échec", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        mPlaces.get(i).setFavorite(1);
                        break;
                    } else {
                        String url_ = context.getString(R.string.s_url);
                        String url_R = url_+"deleteFavoriteUserRestau/";
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user", UserFavoris)
                                .add("restau", String.valueOf(mPlaces.get(i).getPlaceId()))
                                .build();
                        Request requestR = new Request.Builder()
                                .url(url_R)
                                .post(requestBody)
                                .build();


                        okHttpClient.newCall(requestR).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String s = response.body().string();
                                JSONArray jsonArray = null;
                                try {
                                    String state = "";
                                    jsonArray = new JSONArray(s);
                                    for (int i = 0 ; i < jsonArray.length() ; i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        state = jsonObject.getString("STATE");
                                    }
                                    if (state.equals("success")){

                                        if(User1 == null){
                                            if(categoryName == null){
                                                ((HomeActivity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context, "Supprime du votre favorie", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }else{
                                                ((PlacesListActivity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context, "Supprime du votre favorie", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }else {
                                            ((FavoritePlacesActivity)context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context, "Supprime du votre favorie", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }



                                    }else {

                                        if(User1 == null){
                                            if(categoryName == null){
                                                ((HomeActivity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context, "échec", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }else{
                                                ((PlacesListActivity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context, "échec", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }else {
                                            ((FavoritePlacesActivity)context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context, "échec", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        mPlaces.get(i).setFavorite(0);
                        break;

                    }
                }
            }
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_card, viewGroup, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        final Place place =  mPlaces.get(position);

        holder.mItem = place;

        holder.placeName.setText(place.getPlaceName());
        holder.placeLocation.setText(place.getLocation());
        holder.placeRating.setText(place.getRating());
        holder.placeDelivery.setText(place.getDelivery());

        if (holder.mItem.isFavorite() == 1) {
            holder.icFavorite.setImageResource(R.drawable.star);
        } else {
            holder.icFavorite.setImageResource(R.drawable.star2);
        }

        holder.lnlFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener  != null)
                    mListener.onPlaceFavoriteClick(place);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onPlaceClickListener(place);
            }
        });
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView placeName, placeLocation, placeRating, placeDelivery;
        public RelativeLayout lnlFavorite;
        public ImageView icFavorite;
        public final View mView;
        public Place mItem;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            placeName = itemView.findViewById(R.id.place_name);
            placeLocation = itemView.findViewById(R.id.place_location);
            placeRating = itemView.findViewById(R.id.place_rating);
            placeDelivery = itemView.findViewById(R.id.place_delivery);
            lnlFavorite = itemView.findViewById(R.id.lnl_favorite);
            icFavorite = itemView.findViewById(R.id.ic_favorite);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Clicked Item", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnPlaceClickListener {
        void onPlaceClickListener(Place place);
        void onPlaceFavoriteClick(Place place);
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }
}
