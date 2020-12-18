package com.tunisianfood_advisor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class EditerProfilActivity extends AppCompatActivity {


    private EditText nicknameET;
    private EditText phoneNumET;
    private EditText passwordET;
    private EditText confirmET;
    private TextView mTextView;
    private Button mButton;

    private EditText titreuser;
    private EditText teluser;
    public String UserR;
    // Session Manager Class
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editer_profil);


        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        HashMap<String, String> userR = session.getUserDetails();
        UserR = userR.get(SessionManager.KEY_NAME);


        OkHttpClient client = new OkHttpClient();

        String  url = "http://192.168.1.6:4000/getAllInfoUser/"+UserR;

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
                    Type userType = new TypeToken<ArrayList<User>>(){}.getType();
                    List<User> userList = gson.fromJson(response.body().string(), userType);

                    titreuser = (EditText) findViewById(R.id.edit_nickname);
                    titreuser.setText(userList.get(0).getNickname());

                    teluser = (EditText) findViewById(R.id.edit_phone_num);
                    teluser.setText(userList.get(0).getPhoneNum());

                }
            }
        });

        initElement();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submit
                finishSubmit();
            }
        });

    }
    private void initElement(){
        nicknameET = (EditText) findViewById(R.id.edit_nickname);
        phoneNumET = (EditText) findViewById(R.id.edit_phone_num);
        passwordET = (EditText) findViewById(R.id.frag_edit_password_et);
        confirmET = (EditText) findViewById(R.id.frag_edit_password_check_et);
        mTextView = (TextView) findViewById(R.id.frag_edit_tip_tv);
        mButton = (Button) findViewById(R.id.frag_edit_submit);
    }

    private void finishSubmit(){
        String nickname = nicknameET.getText().toString();
        String phoneNum = phoneNumET.getText().toString();
        String password = passwordET.getText().toString();
        String confirm = confirmET.getText().toString();
        if (nickname.equals("")){
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(getString(R.string.s_activity_main_register_input_nickname));
            return;
        }else if (phoneNum.equals("")){
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(getString(R.string.s_activity_main_register_input_phone_num));
            return;
        }else if (password.equals("")){
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(getString(R.string.s_activity_main_register_input_password));
            return;
        }else if (confirm.equals("")){
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(getString(R.string.s_activity_main_register_input_confirm_password));
            return;
        }else if (!confirm.equals(password)){
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(getString(R.string.s_activity_main_register_input_password_wrong));
            return;
        }else {

            String url_P = "http://192.168.1.6:4000/putEditProfil/";
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("user", UserR)
                    .add("name", nickname)
                    .add("password",password)
                    .add("telephone",phoneNum)
                    .build();
            Request requestP = new Request.Builder()
                    .url(url_P)
                    .post(requestBody)
                    .build();


            okHttpClient.newCall(requestP).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
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

                            Thread thread = new Thread(){
                                public void run(){
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),"Modification avec succès",Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(EditerProfilActivity.this, ProfileActivity.class);
                                            startActivity(i);
                                        }
                                    });
                                }
                            };
                            thread.start();

                        }else {
                            Thread thread = new Thread(){
                                public void run(){
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),"échec",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            };
                            thread.start();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onResponseE: " + s);
                }
            });


        }
    }

}
