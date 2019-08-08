package com.pb.apszone.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.CommonResponseModel;
import com.pb.apszone.viewModel.ResetPasswordFragmentViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static com.pb.apszone.utils.CommonUtils.hideProgress;
import static com.pb.apszone.utils.CommonUtils.hideSoftKeyboard;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;
import static com.pb.apszone.utils.CommonUtils.showProgress;

public class ResetPasswordFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.toolbar_reset_password)
    Toolbar toolbarResetPassword;
    @BindView(R.id.til_school_id)
    TextInputLayout tilSchoolId;
    @BindView(R.id.til_dob)
    TextInputLayout tilDob;
    @BindView(R.id.btn_validate)
    Button btnValidate;
    @BindView(R.id.ll_validate)
    LinearLayout llValidate;
    @BindView(R.id.til_new_password)
    TextInputLayout tilNewPassword;
    @BindView(R.id.til_confirm_password)
    TextInputLayout tilConfirmPassword;
    @BindView(R.id.btn_reset)
    Button btnReset;
    @BindView(R.id.ll_reset)
    LinearLayout llReset;
    ResetPasswordFragmentViewModel resetPasswordFragmentViewModel;
    @BindView(R.id.edt_schoolId)
    TextInputEditText edtSchoolId;
    @BindView(R.id.edt_dob)
    TextInputEditText edtDob;
    @BindView(R.id.edt_new_password)
    TextInputEditText edtNewPassword;
    @BindView(R.id.edt_confirm_password)
    TextInputEditText edtConfirmPassword;
    String schoolID;


    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    public static ResetPasswordFragment newInstance() {
        return new ResetPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarResetPassword.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }

    @Override
    public void getNetworkData(boolean status) {

    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        resetPasswordFragmentViewModel = ViewModelProviders.of(this).get(ResetPasswordFragmentViewModel.class);
        observeValidateResetPassword();
        observeResetPassword();
    }

    private void observeValidateResetPassword() {
        resetPasswordFragmentViewModel.validateResetPasswordResponse().observe(this, responseEvent -> {
            hideProgress();
            if (responseEvent != null) {

                if (responseEvent.isSuccess()) {
                    CommonResponseModel commonResponseModel = responseEvent.getCommonResponseModel();
                    if (!commonResponseModel.isError()) {
                        llValidate.setVisibility(View.GONE);
                        llReset.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), commonResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), commonResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    private void observeResetPassword() {
        resetPasswordFragmentViewModel.resetPassword().observe(this, responseEvent -> {
            hideProgress();
            if (responseEvent != null) {
                if (responseEvent.isSuccess()) {
                    CommonResponseModel commonResponseModel = responseEvent.getCommonResponseModel();
                    if (!commonResponseModel.isError()) {
                        Toast.makeText(getActivity(), commonResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(getActivity()).onBackPressed();
                    } else {
                        Toast.makeText(getActivity(), commonResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
                }
            }
        });
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

    @OnClick({R.id.btn_validate, R.id.btn_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_validate:
                schoolID = Objects.requireNonNull(tilSchoolId.getEditText()).getText().toString().trim();
                String dob = Objects.requireNonNull(tilDob.getEditText()).getText().toString().trim();
                validateBtnValidateInput(schoolID, dob);
                break;
            case R.id.btn_reset:
                String new_pass = Objects.requireNonNull(tilNewPassword.getEditText()).getText().toString().trim();
                String confirm_pass = Objects.requireNonNull(tilConfirmPassword.getEditText()).getText().toString().trim();
                validateBtnResetInput(new_pass, confirm_pass);
                break;
        }
    }

    private void validateBtnValidateInput(String id, String dob) {
        if (TextUtils.isEmpty(id)) {
            tilSchoolId.setError("School Id is required");
            return;
        }
        if (TextUtils.isEmpty(dob)) {
            tilDob.setError("DOB is required");
            return;
        }

        if (!TextUtils.isEmpty(dob) && !TextUtils.isEmpty(id)) {
            hideSoftKeyboard(getActivity());
            showProgress(getActivity(), "Please wait...");
            resetPasswordFragmentViewModel.validateResetPasswordRequest(id, dob);
        }
    }

    @OnTextChanged(value = R.id.edt_schoolId, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onSchoolIdTextChanged(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            tilSchoolId.setError(null);
        } else {
            tilSchoolId.setError("School Id is required");
        }
    }

    @OnTextChanged(value = R.id.edt_dob, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onDobTextChanged(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            tilDob.setError(null);
        } else {
            tilDob.setError("School Id is required");
        }
    }

    private void validateBtnResetInput(String new_pass, String confirm_pass) {
        if (TextUtils.isEmpty(new_pass)) {
            tilNewPassword.setError("New password is required");
            return;
        }
        if (TextUtils.isEmpty(confirm_pass)) {
            tilConfirmPassword.setError("Confirm password is required");
            return;
        }

        if (!TextUtils.equals(new_pass, confirm_pass)) {
            tilConfirmPassword.setError("Password do not match");
            return;
        }

        if (!TextUtils.isEmpty(new_pass) && !TextUtils.isEmpty(confirm_pass) && TextUtils.equals(new_pass, confirm_pass)) {
            hideSoftKeyboard(getActivity());
            showProgress(getActivity(), "Please wait...");
            resetPasswordFragmentViewModel.resetPasswordRequest(schoolID, confirm_pass);
        }
    }

    @OnTextChanged(value = R.id.edt_new_password, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onNewPasswordTextChanged(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            tilNewPassword.setError(null);
        } else {
            tilNewPassword.setError("New password is required");
        }
    }

    @OnTextChanged(value = R.id.edt_confirm_password, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onConfirmPasswordTextChanged(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            tilConfirmPassword.setError(null);
        } else {
            tilConfirmPassword.setError("Confirm password is required");
        }
    }

    @OnEditorAction(R.id.edt_dob)
    boolean onDobEditorAction(KeyEvent key, int actionId) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            btnValidate.performClick();
            handled = true;
        }
        return handled;
    }

    @OnEditorAction(R.id.edt_confirm_password)
    boolean onConfirmPasswordEditorAction(KeyEvent key, int actionId) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            btnReset.performClick();
            handled = true;
        }
        return handled;
    }

}
