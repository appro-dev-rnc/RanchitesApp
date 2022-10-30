package com.approdevelopers.ranchites.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.ForgotPassViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity implements NetworkConnection.ReceiverListener, View.OnClickListener {

    private CoordinatorLayout forgotPassActParent;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    ForgotPassViewModel viewModel;

    //UI elements variables
    Button btnSendOtp, btnVerifyOtp, btnUpdatePass;
    TextInputLayout textLayoutForgotPassPhoneNum, textLayoutForgotPassOtp, textLayoutForgotNewPass, textLayoutForgotPassConfirm;
    RelativeLayout forgotPassPageOneParent, forgotPassPageTwoParent, forgotPassPageThreeParent;
    ProgressDialog progressDialog;
    ImageView imgBtnCloseForgotPassPage;
    CountryCodePicker forgotPassCountryCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        networkConnection = new NetworkConnection();
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorGreenLight, getTheme()));

        //hooks
        forgotPassActParent = findViewById(R.id.forgot_pass_act_parent);
        btnSendOtp = findViewById(R.id.btn_forgot_pass_send_otp);
        textLayoutForgotPassPhoneNum = findViewById(R.id.textLayoutForgotPassPhoneNumber);
        btnVerifyOtp = findViewById(R.id.btn_forgot_pass_verify_otp);
        textLayoutForgotPassOtp = findViewById(R.id.textLayoutForgotPassOtp);
        textLayoutForgotNewPass = findViewById(R.id.textLayoutForgotPassNewPass);
        textLayoutForgotPassConfirm = findViewById(R.id.textLayoutForgotPassNewPassConfirm);
        btnUpdatePass = findViewById(R.id.btn_forgot_pass_update_pass);
        forgotPassPageOneParent = findViewById(R.id.forgot_page_first_parent);
        forgotPassPageTwoParent = findViewById(R.id.forgot_page_second_parent);
        forgotPassPageThreeParent = findViewById(R.id.forgot_page_third_parent);
        imgBtnCloseForgotPassPage = findViewById(R.id.img_btn_close_forgot_pass_page);
        forgotPassCountryCode = findViewById(R.id.forgot_pass_country_code);


        viewModel = new ViewModelProvider(this).get(ForgotPassViewModel.class);


        //onclick listener
        btnSendOtp.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
        btnUpdatePass.setOnClickListener(this);
        imgBtnCloseForgotPassPage.setOnClickListener(this);

        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);


        //adding on text change listener
        Objects.requireNonNull(textLayoutForgotPassPhoneNum.getEditText()).addTextChangedListener(phoneTextWatcher);
        Objects.requireNonNull(textLayoutForgotPassOtp.getEditText()).addTextChangedListener(otpTextWatcher);
        Objects.requireNonNull(textLayoutForgotNewPass.getEditText()).addTextChangedListener(passTextWatcher);
        Objects.requireNonNull(textLayoutForgotPassConfirm.getEditText()).addTextChangedListener(passConfirmTextWatcher);


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextInputEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    protected void onResume() {
        super.onResume();

        connectionUtils = new ConnectionUtils(ForgetPasswordActivity.this, networkConnection, forgotPassActParent);
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
    protected void onStop() {
        super.onStop();

    }


    @Override
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
    }

    @Override
    public void onClick(View view) {
        if (isNetworkAvailable()){
            if (view.getId() == R.id.btn_forgot_pass_send_otp) {
                String enteredPhoneNumber = Objects.requireNonNull(textLayoutForgotPassPhoneNum.getEditText()).getText().toString().trim();
                String countryCode = forgotPassCountryCode.getSelectedCountryCodeWithPlus();
                if ( validateCountryCode(countryCode) && validatePhoneNumber(enteredPhoneNumber)) {
                    showSendingOtpProgress();
                    String phoneNumberComplete = countryCode+ enteredPhoneNumber;
                    viewModel.getPhoneNumberExists(phoneNumberComplete).observe(this, aBoolean -> {
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        if (aBoolean) {
                            sendOtpToPhoneNumber(phoneNumberComplete);
                            showSecondPage();
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, "No account linked with this phone number", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }

            if (view.getId() == R.id.btn_forgot_pass_verify_otp) {

                String otpId = Objects.requireNonNull(textLayoutForgotPassOtp.getEditText()).getText().toString().trim();

                if (validateOtpEntered(otpId)) {
                    showValidatingOtpProgress();
                    String otpVerificationId = viewModel.getOtpVerificationId().getValue();
                    if (otpVerificationId==null || otpVerificationId.equals("")){
                        Toast.makeText(ForgetPasswordActivity.this, "Technical error.\nTry again", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpVerificationId, otpId);
                    signInWithPhoneAuthCredential(credential);

                }

            }

            if (view.getId() == R.id.btn_forgot_pass_update_pass) {
                String pass = Objects.requireNonNull(textLayoutForgotNewPass.getEditText()).getText().toString().trim();
                String passConfirm = Objects.requireNonNull(textLayoutForgotPassConfirm.getEditText()).getText().toString().trim();
                if (validatePassword(pass) && validatePassConfirm(pass,passConfirm)) {
                    showUpdatingPassProgress();
                    updatePassword(passConfirm);
                }
            }

        }else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        if (view.getId()==R.id.img_btn_close_forgot_pass_page){
            if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                FirebaseAuth.getInstance().signOut();
            }
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            FirebaseAuth.getInstance().signOut();
        }
        super.onBackPressed();
    }

    private void showValidatingOtpProgress() {
        progressDialog.setTitle("Validating OTP");
        progressDialog.setMessage("Validating...");
        progressDialog.show();
    }

    private void showUpdatingPassProgress() {
        progressDialog.setTitle("Updating password");
        progressDialog.setMessage("Updating...");
        progressDialog.show();
    }

    private void showSendingOtpProgress() {
        progressDialog.setTitle("Sending OTP");
        progressDialog.setMessage("Verifying user and sending OTP...");
        progressDialog.show();
    }


    private boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.equals("")) {
            textLayoutForgotPassPhoneNum.setErrorEnabled(true);
            textLayoutForgotPassPhoneNum.setError("Phone Number cannot be empty");
            return false;
        } else if (phoneNumber.length() != 10) {
            textLayoutForgotPassPhoneNum.setError("Phone number must be 10 digits");
            textLayoutForgotPassPhoneNum.setErrorEnabled(true);
            return false;
        }
        return true;
    }


    private boolean validateOtpEntered(String otp) {
        if (otp.equals("")) {
            textLayoutForgotPassOtp.setError("OTP cannot be empty");
            textLayoutForgotPassOtp.setErrorEnabled(true);
            return false;
        }else if (otp.length()!=6){
            textLayoutForgotPassOtp.setError("OTP must be 6 digits");
            textLayoutForgotPassOtp.setErrorEnabled(true);
        }
        return true;
    }


    private boolean validateCountryCode(String countryCode) {
        if (countryCode.equals("")){
            Toast.makeText(this, "Country code empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!countryCode.equals("+91")){
            Toast.makeText(this, "This country code is not allowed", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth auth = viewModel.getAuth();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }


        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = task.getResult().getUser();
                        viewModel.setVerifiedUser(user);
                        showThirdPage();
                        // Update UI
                    } else {

                        // Sign in failed, display a message and update the UI
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(ForgetPasswordActivity.this, "Incorrect Otp", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void sendOtpToPhoneNumber(String phoneNumber) {
        showSendingOtpDialog();
        FirebaseAuth auth = viewModel.getAuth();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.


            showValidatingOtpProgress();
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(ForgetPasswordActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(ForgetPasswordActivity.this, "Too many OTP requests\n Try again", Toast.LENGTH_SHORT).show();
            }

            // Show a message and update the UI
            Toast.makeText(ForgetPasswordActivity.this, "Otp verification failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.


            // Save verification ID and resending token so we can use them later

            if (progressDialog!=null){
                progressDialog.dismiss();
            }
            viewModel.getOtpVerificationId().postValue(verificationId);
        }
    };


    private void showSendingOtpDialog(){
        progressDialog.setTitle("Sending OTP");
        progressDialog.setMessage("Do not minimize this window...");
        progressDialog.show();
    }


    public void updatePassword(String passwordConfirm) {
        FirebaseUser user = viewModel.getVerifiedUser();
        if (user != null) {
            user.updatePassword(passwordConfirm).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Task<Void> taskUpdate = viewModel.updateUserPassword(user.getUid(), passwordConfirm);
                    taskUpdate.addOnCompleteListener(task1 -> {
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(ForgetPasswordActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                            FirebaseAuth.getInstance().signOut();
                        }
                        finish();
                    });
                } else {

                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                        FirebaseAuth.getInstance().signOut();
                    }
                    Toast.makeText(ForgetPasswordActivity.this, "Failed to update password\nTry again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




    private void showSecondPage() {
        forgotPassPageOneParent.setVisibility(View.GONE);
        forgotPassPageTwoParent.setVisibility(View.VISIBLE);
        forgotPassPageThreeParent.setVisibility(View.GONE);
    }

    private void showThirdPage() {
        forgotPassPageOneParent.setVisibility(View.GONE);
        forgotPassPageTwoParent.setVisibility(View.GONE);
        forgotPassPageThreeParent.setVisibility(View.VISIBLE);
    }

    private boolean validatePassword(String pass) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(pass);

        if (pass.isEmpty()) {
            textLayoutForgotNewPass.setError("Password cannot be empty");
            return false;
        } else if (!matcher.matches()) {

            textLayoutForgotNewPass.setError("Password must contain one uppercase,lowercase letter,a number,a special symbol and no white spaces");

            return false;
        } else if (pass.length()<6){
            textLayoutForgotNewPass.setError("Password must be at least 6 characters");
            return false;
        }
        else {
            textLayoutForgotNewPass.setError(null);
            textLayoutForgotNewPass.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassConfirm(String pass,String passConfirm){

        if (passConfirm.isEmpty()){
            textLayoutForgotPassConfirm.setError("Password cannot be empty");
            return false;
        }else if (!passConfirm.equals(pass)){
            textLayoutForgotPassConfirm.setError("Password did not match");
            return false;
        }
        return true;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    TextWatcher phoneTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            textLayoutForgotPassPhoneNum.setErrorEnabled(false);
            textLayoutForgotPassPhoneNum.setError(null);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };TextWatcher passTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            textLayoutForgotNewPass.setErrorEnabled(false);
            textLayoutForgotNewPass.setError(null);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };TextWatcher otpTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            textLayoutForgotPassOtp.setErrorEnabled(false);
            textLayoutForgotPassOtp.setError(null);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };TextWatcher passConfirmTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            textLayoutForgotPassConfirm.setErrorEnabled(false);
            textLayoutForgotPassConfirm.setError(null);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


}