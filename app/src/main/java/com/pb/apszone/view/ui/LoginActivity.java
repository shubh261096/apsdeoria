package com.pb.apszone.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;
import static com.pb.apszone.utils.CommonUtils.showProgress;


public class LoginActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;
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
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        ButterKnife.bind(this);
        setupSnackbar();
        subscribe();
    }

    private void setupSnackbar() {
        loginViewModel.getSnackbarMessage().observe(this, (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId -> {
            SnackbarUtils.showSnackbar(findViewById(R.id.myCoordinatorLayout), getString(snackbarMessageResourceId));
        });
    }

    public void subscribe() {
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
            showProgress(this, "Please wait...");
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
        Fragment resetPassFragment = ResetPasswordFragment.newInstance();
        replaceFragment(resetPassFragment);
    }

    public void replaceFragment(Fragment destFragment) {
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
                Intent privacyIntent = new Intent(Intent.ACTION_VIEW);
                privacyIntent.setData(Uri.parse(PRIVACY_POLICY_URL));
                startActivity(privacyIntent);
                break;
            case R.id.tvTerms:
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(WEBSITE_URL));
                startActivity(webIntent);
                break;
        }
    }
}