package com.approdevelopers.ranchites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.ranchites.Activities.ForgetPasswordActivity;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, NetworkConnection.ReceiverListener {

    FirebaseAuth auth ;
    TextView txtForgotPassword;
    Button btnLogin,btnLoginScrSignUp;
    TextInputLayout loginTextInputEmail,loginTextInputPassword;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout loginParentView;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(getColor(R.color.login_screen_green));

        networkConnection = new NetworkConnection();

        auth= FirebaseAuth.getInstance();
        loginTextInputEmail= findViewById(R.id.loginTextInputEmail);
        loginTextInputPassword = findViewById(R.id.loginTextInputPassword);

        btnLogin= findViewById(R.id.btn_login);
        txtForgotPassword = findViewById(R.id.txt_forgot_password);
        btnLoginScrSignUp = findViewById(R.id.btn_login_screen_sign_up);
        loginParentView = findViewById(R.id.login_parent_view);


        //onclick listeners
        btnLogin.setOnClickListener(this);
        btnLoginScrSignUp.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);

        //adding on text change listeners
        Objects.requireNonNull(loginTextInputEmail.getEditText()).addTextChangedListener(emailTextWatcher);
        Objects.requireNonNull(loginTextInputPassword.getEditText()).addTextChangedListener(passwordTextWatcher);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId==R.id.btn_login){
            if (isNetworkAvailable()){
                String email = Objects.requireNonNull(loginTextInputEmail.getEditText()).getText().toString().trim();
                String password = Objects.requireNonNull(loginTextInputPassword.getEditText()).getText().toString().trim();
                if (validateEmail(email) && validatePassword(password)){
                    showLoginProgress();
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        if (task.isSuccessful()){
                            Intent intentMain = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intentMain);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Failed :\n" +  Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }


        }

        if (viewId==R.id.txt_forgot_password){
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        }
        if (viewId==R.id.btn_login_screen_sign_up){
            Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(intent);
        }
    }

    private boolean validatePassword(String password) {
        if (password.equals("")){
            loginTextInputPassword.setError("Password cannot be empty");
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
        if (email.equals("")){
            loginTextInputEmail.setError("Email cannot be empty");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginTextInputEmail.setError("Invalid Email");
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(LoginActivity.this,networkConnection,loginParentView);
        NetworkConnection.Listener = this;
        connectionUtils.checkConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectionUtils.destroySnackBar();

        this.unregisterReceiver(networkConnection);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof TextInputEditText ) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
    }


    TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            loginTextInputEmail.setError(null);
            loginTextInputEmail.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };  TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            loginTextInputPassword.setError(null);
            loginTextInputPassword.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void showLoginProgress(){
        progressDialog.setTitle("Login ");
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}