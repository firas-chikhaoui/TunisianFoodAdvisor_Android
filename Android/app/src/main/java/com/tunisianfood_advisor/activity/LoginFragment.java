package com.tunisianfood_advisor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.model.User;
import com.tunisianfood_advisor.model.UserLab;

public class LoginFragment extends Fragment {

    private static final Object MODE_PRIVATE = "MODE_PRIVATE";
    private EditText mAccountET;
    private EditText mPasswordET;
    private Button mButton;

    // Session Manager Class
    SessionManager session;


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initElement(view);

        // Session Manager
        session = new SessionManager(getActivity());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = mAccountET.getText().toString();
                String password = mPasswordET.getText().toString();

                User user = UserLab.get(getActivity(),account,password).getUser();
                if (user == null){
                    Toast.makeText(getActivity(),getString(R.string.s_activity_main_login_failure),Toast.LENGTH_SHORT).show();
                }else {

                     // Creating user login session
                    // For testing i am stroing name, email as follow
                    // Use user real data
                    session.createLoginSession(account, password);

                    Toast.makeText(getActivity(),getString(R.string.s_activity_main_login_success),Toast.LENGTH_SHORT).show();
                    //INTENT OBJ
                    Intent i = new Intent(getActivity().getBaseContext(),HomeActivity.class);
                    //PACK DATA
                    i.putExtra("accountname", account);
                    //START ACTIVITY
                    getActivity().startActivity(i);
                }
            }
        });
    }

    private void initElement(View view){
        mAccountET = (EditText) view.findViewById(R.id.frag_login_account_et);
        mPasswordET = (EditText) view.findViewById(R.id.frag_login_password_et);
        mButton = (Button) view.findViewById(R.id.frag_login_btn);
    }


}
