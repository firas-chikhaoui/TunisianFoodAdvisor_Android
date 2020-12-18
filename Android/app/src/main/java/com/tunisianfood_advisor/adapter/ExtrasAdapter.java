package com.tunisianfood_advisor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.model.Extra;
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

public class ExtrasAdapter extends RecyclerView.Adapter<ExtrasAdapter.ItemHolder>{

    private static String TAG = "ExtrasAdapter";

    private List<Extra> mExtras = new ArrayList<>();
    private Context context;
    private final OnExtraClickListener mListener;

    private String[] extrasNames = {"Thon, Saumon, Wasabi, Anguille, Des légumes, Nouilles"};

    public ExtrasAdapter(Context context){
        this.context = context;

        try {
            this.mListener = ((OnExtraClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnPlaceClickListener.");
        }


        OkHttpClient client = new OkHttpClient();


        String url_ = context.getString(R.string.s_url);
        String url = url_+"getAllExtra/";

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
                    Type extraType = new TypeToken<ArrayList<Extra>>(){}.getType();
                    List<Extra> extraList = gson.fromJson(response.body().string(), extraType);


                    String extrasNames[] = new String[extraList.size()];
                    for (int i = 0; i < extraList.size(); i++){
                        extrasNames[i] = extraList.get(i).getExtraName();
                    }

                    String extrasValues[] = new String[extraList.size()];
                    for (int i = 0; i < extraList.size(); i++){
                        extrasValues[i] = extraList.get(i).getExtraValue();
                    }



                    for (int i = 0; i < extraList.size(); i++){
                        Extra extra = new Extra((i + 1), extrasNames[i], extrasValues[i] );
                        mExtras.add(extra);
                    }
                }
            }
        });

    }

    public void addExtra(int extraId) {
        if(mExtras.size() > 0) {
            for (int i = 0; i < mExtras.size(); i++) {
                if(mExtras.get(i).getExtraId() == extraId) {
                    if (!mExtras.get(i).isAdded()) {
                        mExtras.get(i).addExtra(true);
                        break;
                    } else {
                        mExtras.get(i).addExtra(false);
                        break;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.extra_item, viewGroup, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final Extra extra =  mExtras.get(position);

        holder.mItem = extra;

        holder.mExtraName.setText(extra.getExtraName());
        holder.mExtraValue.setText(extra.getExtraValue());

        if (holder.mItem.isAdded()) {
            holder.icAdd.setImageResource(R.drawable.ic_check);
        } else {
            holder.icAdd.setImageResource(R.drawable.ic_plus);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onExtraClickListener(extra);
            }
        });
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mExtraName, mExtraValue;
        public ImageView icAdd;
        public final View mView;
        public Extra mItem;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mExtraName = itemView.findViewById(R.id.extra_name);
            mExtraValue = itemView.findViewById(R.id.extra_value);
            icAdd = itemView.findViewById(R.id.ic_add);
        }

        @Override
        public void onClick(View v) {}
    }

    @Override
    public int getItemCount() {
        return mExtras.size();
    }

    public interface OnExtraClickListener {
        void onExtraClickListener(Extra extra);
    }
}
