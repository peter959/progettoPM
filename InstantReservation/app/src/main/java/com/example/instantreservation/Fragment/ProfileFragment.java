package com.example.instantreservation.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.instantreservation.R;
import com.example.instantreservation.Activity.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SharedPreferences userInfo;


    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;



    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        userInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String name = userInfo.getString("userName" , "name");
        String email = userInfo.getString("userEmail" , "email");


        TextView tv_userName = (TextView) returnView.findViewById(R.id.userName);
        TextView tv_userEmail = (TextView) returnView.findViewById(R.id.userEmail);

        tv_userName.setText(name);
        tv_userEmail.setText(email);
        // Inflate the layout for this fragment

        final TextView logout = (TextView) returnView.findViewById(R.id.txt_log_out);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                //SHARED PREFERENCES
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userUID", null);
                editor.putString("userName", null);
                editor.putString("userEmail", null);
                editor.putString("userPhone", null);
                editor.commit();
                //SHARED PREFERENCES
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return returnView;
    }
}
