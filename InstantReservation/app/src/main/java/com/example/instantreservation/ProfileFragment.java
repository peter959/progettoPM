package com.example.instantreservation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ProfileFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SharedPreferences userInfo;


    private String mParam1;
    private String mParam2;



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

        userInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String name = userInfo.getString("userName" , "null");
        String email = userInfo.getString("userEmail" , "null");

        TextView tv_userName = (TextView) returnView.findViewById(R.id.userName);
        TextView tv_userEmail = (TextView) returnView.findViewById(R.id.userEmail);
        tv_userName.setText(name);
        tv_userEmail.setText(email);
        // Inflate the layout for this fragment
        return returnView;
    }
}
