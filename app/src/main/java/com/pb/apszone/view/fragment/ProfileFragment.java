package com.pb.apszone.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.viewModel.ProfileFragmentViewModel;

import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;

public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    ProfileFragmentViewModel profileFragmentViewModel;
    String user_type, user_id;
    KeyStorePref keyStorePref;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyStorePref = KeyStorePref.getInstance(getContext());
        if (getArguments() != null) {
            user_id = getArguments().getString(KEY_USER_ID);
            user_type = getArguments().getString(KEY_USER_TYPE);
        } else {
            user_id = keyStorePref.getString(KEY_USER_ID);
            user_type = keyStorePref.getString(KEY_USER_TYPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        profileFragmentViewModel = ViewModelProviders.of(this).get(ProfileFragmentViewModel.class);
        profileFragmentViewModel.sendRequest(user_id, user_type);
        subscribe();
    }

    private void subscribe() {
        profileFragmentViewModel.getProfile().observe(this, profileResponseModel -> {
            if (profileResponseModel != null) {
                Toast.makeText(getActivity(), profileResponseModel.getProfile().getFullname(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
