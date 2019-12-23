package com.pb.apszone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.pb.apszone.R;
import com.pb.apszone.view.receiver.NetworkChangeReceiver;

import java.util.Objects;

public abstract class BaseFragment extends Fragment {

    private LinearLayout includeNetworkLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        includeNetworkLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.includeNetworkLayout);
        observeInternetChange();

    }

    private void observeInternetChange() {
        NetworkChangeReceiver.getStatus().observe(this, status -> {
            if (status != null) {
                if (includeNetworkLayout == null)
                    return;
                getNetworkData(status);
                if (status) {
                    includeNetworkLayout.setVisibility(View.GONE);
                } else {
                    includeNetworkLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public abstract void getNetworkData(boolean status);


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
