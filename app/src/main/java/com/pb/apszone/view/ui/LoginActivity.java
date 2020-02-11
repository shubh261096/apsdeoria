package com.pb.apszone.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.pb.apszone.R;
import com.pb.apszone.service.model.LoginResponseModel;
import com.pb.apszone.utils.SnackbarMessage;
import com.pb.apszone.utils.SnackbarUtils;
import com.pb.apszone.view.fragment.ResetPasswordFragment;
import com.pb.apszone.viewModel.LoginViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

import static com.pb.apszone.utils.AppConstants.PRIVACY_POLICY_URL;
import static com.pb.apszone.utils.AppConstants.WEBSITE_URL;
import static com.pb.apszone.utils.CommonUtils.hideProgress;
import static com.pb.apszone.utils.CommonUtils.hideSoftKeyboard;
import static com.pb.apszone.utils.CommonUtils.openWebIntent;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;
import static com.pb.apszone.utils.CommonUtils.showProgress;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    @BindView(R.id.edt_id)
    EditText edtID;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.tvTerms)
    TextView tvTerms;
    @BindView(R.id.tvPolicy)
    TextView tvPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        ButterKnife.bind(this);
        setupSnackbar();
        subscribe();
    }

    private void setupSnackbar() {
        loginViewModel.getSnackbarMessage().observe(this, (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId -> SnackbarUtils.showSnackbar(findViewById(R.id.myCoordinatorLayout), getString(snackbarMessageResourceId)));
    }

    private void subscribe() {
        loginViewModel.getUser().observe(this, responseEvent -> {
            hideProgress();
            if (responseEvent != null) {
                if (responseEvent.isSuccess()) {
                    LoginResponseModel loginResponseModel = responseEvent.getLoginResponseModel();
                    Toast.makeText(LoginActivity.this, loginResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    if (!loginResponseModel.isError()) {
                        loginViewModel.putSharedPrefData(loginResponseModel);
                        startDashboardActivity();
                    } else {
                        forgotPassword.setVisibility(View.VISIBLE);
                    }
                } else {
                    showInformativeDialog(this, responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    private void startDashboardActivity() {
        startActivity(new Intent(this, DashboardActivity.class));
    }


    @OnClick(R.id.btn_login)
    public void onLoginButtonClicked() {
        String id = edtID.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        validateLogin(id, password);
    }

    private void validateLogin(String id, String password) {
        if (loginViewModel.validateLogin(id, password)) {
            hideSoftKeyboard(this);
            showProgress(this, getString(R.string.msg_please_wait));
            loginViewModel.sendRequest(id, password);
        } else {
            hideProgress();
        }
    }

    @OnTextChanged(value = R.id.edt_password, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onPasswordTextChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            edtPassword.setError(getString(R.string.error_password_required));
        }
    }

    @OnTextChanged(value = R.id.edt_id, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onIdTextChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            edtID.setError(getString(R.string.error_id_required));
        }
    }

    @SuppressWarnings("unused")
    @OnEditorAction(R.id.edt_password)
    boolean onPasswordEditorAction(KeyEvent key, int actionId) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            validateLogin(edtID.getText().toString().trim(), edtPassword.getText().toString().trim());
            handled = true;
        }
        return handled;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            /* clean resource */
            edtID.setText(null);
            edtPassword.setText(null);
            edtID.setError(null);
            edtPassword.setError(null);
            forgotPassword.setVisibility(View.GONE);
        } else {
            finishAffinity();
        }
    }

    @OnClick(R.id.forgot_password)
    public void onForgotPasswordClicked() {
        hideSoftKeyboard(this);
        Fragment resetPassFragment = ResetPasswordFragment.newInstance();
        replaceFragment(resetPassFragment);
    }

    private void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.login_frame_layout, destFragment).addToBackStack(destFragment.getClass().getSimpleName());
        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }

    @OnClick({R.id.tvPolicy, R.id.tvTerms})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvPolicy:
                openWebIntent(this, PRIVACY_POLICY_URL);
                break;
            case R.id.tvTerms:
                openWebIntent(this, WEBSITE_URL);
                break;
        }
    }
}