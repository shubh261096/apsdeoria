package com.pb.apszone.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.pb.apszone.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.utils.CommonUtils.openWebIntent;

public class ContactFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.toolbar_contact)
    Toolbar toolbarContact;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_whatsapp)
    LinearLayout llWhatsapp;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarContact.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    }

    @Override
    public void getNetworkData(boolean status) {
    }

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
        unbinder.unbind();
    }

    @OnClick({R.id.ll_email, R.id.ll_phone, R.id.ll_whatsapp, R.id.ll_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9454549454"));
                startActivity(intent);
                break;
            case R.id.ll_email:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "contact@apsdeoria.com", null));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.msg_send_email)));
                break;
            case R.id.ll_whatsapp:
                String number = "919454549454";
                number = number.replace(" ", "").replace("+", "");
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setType("text/plain");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");
                try {
                    startActivity(sendIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), getString(R.string.whatsapp_not_installed), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_location:
                openWebIntent(Objects.requireNonNull(getContext()), "https://maps.app.goo.gl/FQNSjweof7YQ6u3h8");
                break;
        }
    }
}
