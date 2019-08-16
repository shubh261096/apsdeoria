package com.pb.apszone.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.ProfileResponseModel;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.ProfileAdapter;
import com.pb.apszone.viewModel.ProfileFragmentViewModel;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;
import static com.pb.apszone.utils.AppConstants.PROFILE_ADDRESS;
import static com.pb.apszone.utils.AppConstants.PROFILE_DOB;
import static com.pb.apszone.utils.AppConstants.PROFILE_EMAIL;
import static com.pb.apszone.utils.AppConstants.PROFILE_FATHER_NAME;
import static com.pb.apszone.utils.AppConstants.PROFILE_GUARDIAN_NAME;
import static com.pb.apszone.utils.AppConstants.PROFILE_HUSBAND_NAME;
import static com.pb.apszone.utils.AppConstants.PROFILE_ID;
import static com.pb.apszone.utils.AppConstants.PROFILE_MOTHER_NAME;
import static com.pb.apszone.utils.AppConstants.PROFILE_PHONE;
import static com.pb.apszone.utils.AppConstants.PROFILE_QUALIFICATION;
import static com.pb.apszone.utils.AppConstants.USER_GENDER_MALE;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_STUDENT;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_TEACHER;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.userDp)
    ImageView userDp;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.rvProfileUI)
    RecyclerView rvProfileUI;
    Unbinder unbinder;
    @BindView(R.id.userClass)
    TextView userClass;
    @BindView(R.id.toolbar_profile)
    Toolbar toolbarProfile;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    ProfileFragmentViewModel profileFragmentViewModel;
    String user_type, user_id;
    KeyStorePref keyStorePref;
    HashMap<String, String> profileValueHashmap = new HashMap<>();
    ProfileAdapter profileAdapter;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        rvProfileUI.setLayoutManager(new LinearLayoutManager(getActivity()));
        profileAdapter = new ProfileAdapter(profileValueHashmap);
        rvProfileUI.setAdapter(profileAdapter);
        toolbarProfile.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        profileFragmentViewModel = ViewModelProviders.of(this).get(ProfileFragmentViewModel.class);
        observeProfile();
    }

    private void observeProfile() {
        profileFragmentViewModel.getProfile().observe(this, responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);

                if (profileAdapter != null) {
                    profileAdapter.clearData();
                }

                if (responseEvent.isSuccess()) {
                    ProfileResponseModel profileResponseModel = responseEvent.getProfileResponseModel();
                    /* Showing profile picture by gender */
                    if (!TextUtils.isEmpty(profileResponseModel.getProfile().getGender())) {
                        int drawable = TextUtils.equals(profileResponseModel.getProfile().getGender(), USER_GENDER_MALE) ? R.drawable.profile_boy : R.drawable.profile_girl;
                        userDp.setImageResource(drawable);
                    }
                    /* Showing name */
                    if (!TextUtils.isEmpty(profileResponseModel.getProfile().getFullname())) {
                        userName.setText(profileResponseModel.getProfile().getFullname());
                    }
                    /* Showing Id*/
                    if (!TextUtils.isEmpty(profileResponseModel.getProfile().getId())) {
                        profileValueHashmap.put(PROFILE_ID, profileResponseModel.getProfile().getId());
                    }

                    /* Checking is uer type is parent */
                    if (TextUtils.equals(user_type, USER_TYPE_STUDENT)) {
                        /* If user type is parent then user class is visible */
                        if (!TextUtils.isEmpty(profileResponseModel.getProfile().getClassId().getName())) {
                            userClass.setVisibility(View.VISIBLE);
                            userClass.setText(profileResponseModel.getProfile().getClassId().getName());
                        }

                        /* Checking if student email is empty then show father's email here which is at position 0 */
                        if (!TextUtils.isEmpty(profileResponseModel.getProfile().getEmail())) {
                            profileValueHashmap.put(PROFILE_EMAIL, profileResponseModel.getProfile().getEmail());
                        } else if (profileResponseModel.getProfile().getParent() != null && !TextUtils.isEmpty(profileResponseModel.getProfile().getParent().get(0).getEmail())) {
                            profileValueHashmap.put(PROFILE_EMAIL, profileResponseModel.getProfile().getParent().get(0).getEmail());
                        }

                        /* Checking if student phone is empty then show father's phone here which is at position 0 */
                        if (!TextUtils.isEmpty(profileResponseModel.getProfile().getPhone())) {
                            profileValueHashmap.put(PROFILE_PHONE, profileResponseModel.getProfile().getPhone());
                        } else if (profileResponseModel.getProfile().getParent() != null && !TextUtils.isEmpty(profileResponseModel.getProfile().getParent().get(0).getPhone())) {
                            profileValueHashmap.put(PROFILE_PHONE, profileResponseModel.getProfile().getParent().get(0).getPhone());
                        }

                        /* Showing father's address which is at position 0 of parent */
                        if (profileResponseModel.getProfile().getParent() != null && !TextUtils.isEmpty(profileResponseModel.getProfile().getParent().get(0).getAddress())) {
                            profileValueHashmap.put(PROFILE_ADDRESS, profileResponseModel.getProfile().getParent().get(0).getAddress());
                        }

                        /* Showing Parent FullName */
                        if (profileResponseModel.getProfile().getParent() != null) {
                            for (int i = 0; i < profileResponseModel.getProfile().getParent().size(); i++) {
                                if (TextUtils.equals(profileResponseModel.getProfile().getParent().get(i).getType(), getString(R.string.type_father))) {
                                    profileValueHashmap.put(PROFILE_FATHER_NAME, profileResponseModel.getProfile().getParent().get(i).getFullname());
                                }
                                if (TextUtils.equals(profileResponseModel.getProfile().getParent().get(i).getType(), getString(R.string.type_mother))) {
                                    profileValueHashmap.put(PROFILE_MOTHER_NAME, profileResponseModel.getProfile().getParent().get(i).getFullname());
                                }
                                if (TextUtils.equals(profileResponseModel.getProfile().getParent().get(i).getType(), getString(R.string.type_guardian))) {
                                    profileValueHashmap.put(PROFILE_GUARDIAN_NAME, profileResponseModel.getProfile().getParent().get(i).getFullname());
                                }
                            }
                        }

                    } else if (TextUtils.equals(user_type, USER_TYPE_TEACHER)) { /* Checking if user type is teacher */
                        /* Showing teacher's email */
                        if (!TextUtils.isEmpty(profileResponseModel.getProfile().getEmail())) {
                            profileValueHashmap.put(PROFILE_EMAIL, profileResponseModel.getProfile().getEmail());
                        }
                        /* Showing teacher's phone */
                        if (!TextUtils.isEmpty(profileResponseModel.getProfile().getPhone())) {
                            profileValueHashmap.put(PROFILE_PHONE, profileResponseModel.getProfile().getPhone());
                        }
                        /* Showing teacher's address */
                        if (!TextUtils.isEmpty(profileResponseModel.getProfile().getAddress())) {
                            profileValueHashmap.put(PROFILE_ADDRESS, profileResponseModel.getProfile().getAddress());
                        }
                        /* Showing teacher's qualification */
                        if (!TextUtils.isEmpty(profileResponseModel.getProfile().getQualification())) {
                            profileValueHashmap.put(PROFILE_QUALIFICATION, profileResponseModel.getProfile().getQualification());
                        }
                        /* Showing teacher's father name */
                        if (!TextUtils.isEmpty(profileResponseModel.getProfile().getFatherName())) {
                            profileValueHashmap.put(PROFILE_FATHER_NAME, profileResponseModel.getProfile().getFatherName());
                        }
                        /* Showing teacher's mother name */
                        if (!TextUtils.isEmpty(profileResponseModel.getProfile().getMotherName())) {
                            profileValueHashmap.put(PROFILE_MOTHER_NAME, profileResponseModel.getProfile().getMotherName());
                        }
                        /* Showing teacher's husband name */
                        if (!TextUtils.isEmpty(profileResponseModel.getProfile().getHusbandName())) {
                            profileValueHashmap.put(PROFILE_HUSBAND_NAME, profileResponseModel.getProfile().getHusbandName());
                        }
                    }
                    /* Showing Date of birth */
                    if (!TextUtils.isEmpty(profileResponseModel.getProfile().getDob())) {
                        profileValueHashmap.put(PROFILE_DOB, profileResponseModel.getProfile().getDob());
                    }
                    profileAdapter.notifyDataSetChanged();
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    @Override
    public void getNetworkData(boolean status) {
        if (status) {
            if (profileAdapter != null) {
                profileAdapter.clearData();
            }
            subscribe();
        }
    }

    private void subscribe() {
        profileFragmentViewModel.sendRequest(user_id, user_type);
        progressBar.setVisibility(View.VISIBLE);
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

}
