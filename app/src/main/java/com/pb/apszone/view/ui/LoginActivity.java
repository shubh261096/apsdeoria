package com.pb.apszone.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.utils.SnackbarUtils;
import com.pb.apszone.viewModel.LoginViewModel;
import com.pb.apszone.utils.SnackbarMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.pb.apszone.utils.CommonUtils.hideProgress;
import static com.pb.apszone.utils.CommonUtils.showProgress;


public class LoginActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        ButterKnife.bind(this);
        setupSnackbar();
    }

    private void setupSnackbar() {
        loginViewModel.getSnackbarMessage().observe(this, (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId -> {
            SnackbarUtils.showSnackbar(findViewById(R.id.myCoordinatorLayout), getString(snackbarMessageResourceId));
        });
    }

    public void subscribe() {
        loginViewModel.getUser().observe(this, loginResponseModel -> {
            hideProgress();
            if (loginResponseModel != null) {
                Toast.makeText(LoginActivity.this, loginResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.btn_login)
    public void onLoginButtonClicked() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        validateLogin(email, password);
    }

    private void validateLogin(String email, String password) {
        if (loginViewModel.validateLogin(email, password)) {
            showProgress(this, "Please wait...");
            loginViewModel.sendRequest(email, password);
            subscribe();
        } else {
            hideProgress();
        }
    }


    @OnTextChanged(value = R.id.edt_email, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onEmailTextChanged(CharSequence text) {
        if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            edtEmail.setError(getString(R.string.error_valid_email));
        }
    }

    @OnTextChanged(value = R.id.edt_password, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onPasswordTextChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            edtPassword.setError(getString(R.string.error_password_required));
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}