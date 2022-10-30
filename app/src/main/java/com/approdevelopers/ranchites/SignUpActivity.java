package com.approdevelopers.ranchites;

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
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.Models.User;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.SIgnUpViewModel;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, NetworkConnection.ReceiverListener {


    //Firebase Auth variables
    private FirebaseAuth auth;

    private User userData;

    private TextInputLayout signUpTextInputName,signUpTextInputEmail, signUpTextInputPassword,
            signUpTextInputPhone,signUpTextInputOtp,signUpTextInputPassConfirm;

    private RelativeLayout signUpFormParent,otpValidationSignUpPageParent,signUpPhoneNumberParent,accountCreatedSignUpParent;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout signUpParentView;

    private CountryCodePicker ccpSignUpPhoneNumber;
    ProgressDialog progressDialog;

    private SIgnUpViewModel viewModel;
    private TextView txtSignUpAcceptTerms;
    private CheckBox checkBoxSignUpAcceptTerms;
    private TextInputLayout txtInputSignUpTerms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        networkConnection  = new NetworkConnection();
        getWindow().setStatusBarColor(getColor(R.color.sign_up_screen_bg));

        //hooks
        //Ui element variables
        Button btnSignUp = findViewById(R.id.btn_sign_up);
        signUpTextInputEmail = findViewById(R.id.signUpTextInputEmail);
        signUpTextInputName = findViewById(R.id.signUpTextInputName);
        signUpTextInputPassword = findViewById(R.id.signUpTextInputPassword);
        signUpTextInputPhone = findViewById(R.id.signUpTextInputPhoneNumber);
        signUpPhoneNumberParent = findViewById(R.id.phone_number_sign_up_scr_parent);
        accountCreatedSignUpParent = findViewById(R.id.account_created_sign_up_parent);
        Button btnVerifyOtp = findViewById(R.id.btn_verify_otp);
        signUpTextInputOtp = findViewById(R.id.signUpTextInputOtp);
        signUpTextInputPassConfirm = findViewById(R.id.signUpTextInputPasswordConfirm);
        Button btnSendOtp = findViewById(R.id.btn_sign_up_send_otp);
        ccpSignUpPhoneNumber = findViewById(R.id.country_code_sign_up_screen);
        checkBoxSignUpAcceptTerms = findViewById(R.id.checkbox_sign_up_agree_terms);
        txtInputSignUpTerms = findViewById(R.id.text_input_sign_up_terms);

        signUpParentView = findViewById(R.id.sign_up_parent_view);

        signUpFormParent = findViewById(R.id.sign_up_form_parent);
        otpValidationSignUpPageParent = findViewById(R.id.otp_valiation_sign_up_page_parent);
        txtSignUpAcceptTerms = findViewById(R.id.txt_sign_up_accept_terms);


        ImageView imgBtnCloseSignUp = findViewById(R.id.img_btn_sign_up_form_close);
        ImageView imgBtnEnterOtpBack = findViewById(R.id.img_btn_sign_up_otp_back);
        ImageView imgBtnEnterPhoneBack = findViewById(R.id.img_btn_sign_up_phone_back);

        Button btnSignUpScreenLogin = findViewById(R.id.btn_sign_up_screen_login);

        viewModel = new ViewModelProvider(this).get(SIgnUpViewModel.class);
        

        //init firebase auth
        auth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
        imgBtnCloseSignUp.setOnClickListener(this);
        imgBtnEnterPhoneBack.setOnClickListener(this);
        imgBtnEnterOtpBack.setOnClickListener(this);
        btnSendOtp.setOnClickListener(this);
        btnSignUpScreenLogin.setOnClickListener(this);

        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        //adding text watcher on edittexts
        Objects.requireNonNull(signUpTextInputName.getEditText()).addTextChangedListener(nameTextWatcher);
        Objects.requireNonNull(signUpTextInputEmail.getEditText()).addTextChangedListener(emailTextWatcher);
        Objects.requireNonNull(signUpTextInputPassword.getEditText()).addTextChangedListener(passTextWatcher);
        Objects.requireNonNull(signUpTextInputPassConfirm.getEditText()).addTextChangedListener(passConfirmTextWatcher);
        Objects.requireNonNull(signUpTextInputPhone.getEditText()).addTextChangedListener(phoneTextWatcher);
        Objects.requireNonNull(signUpTextInputOtp.getEditText()).addTextChangedListener(otpTextWatcher);

        txtSignUpAcceptTerms.setClickable(true);
        txtSignUpAcceptTerms.setMovementMethod(LinkMovementMethod.getInstance());

        checkBoxSignUpAcceptTerms.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                txtInputSignUpTerms.setError(null);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        
        if (isNetworkAvailable()){
            if (viewId==R.id.btn_sign_up){
                String userEmail = Objects.requireNonNull(signUpTextInputEmail.getEditText()).getText().toString().trim();
                String userPassword = Objects.requireNonNull(signUpTextInputPassword.getEditText()).getText().toString().trim();
                //
                String userName = Objects.requireNonNull(signUpTextInputName.getEditText()).getText().toString().trim();
                String userPassConfirm = Objects.requireNonNull(signUpTextInputPassConfirm.getEditText()).getText().toString().trim();

                if (validateName(userName) && validateEmail(userEmail) && validatePassword(userPassword) && validatePassConfirm(userPassword, userPassConfirm) && validateCheckbox()){
                    userData = null;
                    userData = new User("", userName,null, userEmail, userPassConfirm,"",false,null,null,2);
                    showPhoneNumberScreen();
                }
            }

            if (viewId== R.id.btn_sign_up_send_otp){
                String countryCode = ccpSignUpPhoneNumber.getSelectedCountryCodeWithPlus();
                String phoneNumEntered = Objects.requireNonNull(signUpTextInputPhone.getEditText()).getText().toString().trim();

                if (validateCountryCode(countryCode) && validatePhoneNumber(phoneNumEntered)){
                    String completePhone = countryCode+ phoneNumEntered;
                    userData.setUserPhone(completePhone);
                    createFirebaseUserWithEmailAndPassWord(userData);
                    showCreatingAccountDialog();
                }
            }

            if (viewId==R.id.btn_verify_otp){
                String otpEntered = Objects.requireNonNull(signUpTextInputOtp.getEditText()).getText().toString().trim();
                if (validateOTP(otpEntered)){
                    String otpVerificationId = viewModel.getOtpVerificationId().getValue();
                    if (otpVerificationId==null || otpVerificationId.equals("")){
                        Toast.makeText(SignUpActivity.this, "Technical error.\nTry again", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpVerificationId,otpEntered);
                    linkCurrentUserWithPhone(credential);
                    showLinkingUserData();
                }
            }
        }else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

      

        if (viewId==R.id.img_btn_sign_up_form_close){
            finish();
        }
        if (viewId==R.id.img_btn_sign_up_phone_back){
            Objects.requireNonNull(signUpTextInputPhone.getEditText()).setText(null);
            showSignUpForm();
        }if (viewId==R.id.img_btn_sign_up_otp_back){
            Objects.requireNonNull(signUpTextInputOtp.getEditText()).setText(null);
            showPhoneNumberScreen();
        }

        if (viewId==R.id.btn_sign_up_screen_login){
            finish();
        }
    }

    private boolean validateOTP(String otpEntered) {
        if (otpEntered.equals("")){
            signUpTextInputOtp.setError("Otp cannot be empty");
        }else if (otpEntered.length()!=6){
            signUpTextInputOtp.setError("Otp must be 6 digits");
        }
        return true;
    }

    private boolean validateCheckbox(){
        if (checkBoxSignUpAcceptTerms.isChecked()){
            return true;
        }
        else {
            txtInputSignUpTerms.setError("Kindly accept to continue");
            return false;
        }
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

    private boolean validatePhoneNumber(String phoneNo){
        if (phoneNo.equals("")){
            signUpTextInputPhone.setError("Phone number cannot be empty");
            return false;
        }
        else if (phoneNo.length()!=10){
            signUpTextInputPhone.setError("Invalid Phone Number");
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
        if (email.equals("")){
            signUpTextInputEmail.setError("Email cannot be empty");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpTextInputEmail.setError("Invalid Email");
            return false;
        }
        return true;
    }

    private boolean validateName(String userName) {
        if (userName.equals("")){
            signUpTextInputName.setError("Name cannot be empty");
            return false;
        }
        return true;
    }



    private boolean validatePassConfirm(String pass,String passConfirm){

        if (passConfirm.isEmpty()){
            signUpTextInputPassConfirm.setError("Password cannot be empty");
            return false;
        }else if (!passConfirm.equals(pass)){
            signUpTextInputPassConfirm.setError("Password did not match");
            return false;
        }
        return true;
    }


    private boolean validatePassword(String pass) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(pass);

        if (pass.isEmpty()) {
            signUpTextInputPassword.setError("Password cannot be empty");
            return false;
        } else if (!matcher.matches()) {

            signUpTextInputPassword.setError("Password must contain one uppercase,lowercase,a number,a special symbol and no white spaces");
            return false;
        } else if (pass.length()<6){
            signUpTextInputPassword.setError("Password must be atleast 6 characters");
            return false;
        }else {
            signUpTextInputPassword.setError(null);
            signUpTextInputPassword.setErrorEnabled(false);
            return true;
        }
    }


    private void createFirebaseUserWithEmailAndPassWord(User userData) {
        String email = userData.getUserEmail();
        String password = userData.getUserPassword();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = auth.getCurrentUser();

                        if (user != null) {
                            userData.setuId(user.getUid());

                        }
                        Toast.makeText(SignUpActivity.this, "Sending OTP", Toast.LENGTH_SHORT).show();
                        sendOtpTophoneNumber(userData);

                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void sendOtpTophoneNumber(User userData) {
        showSendingOtpDialog();
        String completePhoneNumber = userData.getUserPhone();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(completePhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        showOTPValidationScreen();

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


                if (otpValidationSignUpPageParent.getVisibility()==View.VISIBLE){
                    linkCurrentUserWithPhone(credential);
                    showLinkingUserData();

                }


        }


        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(SignUpActivity.this, "Verification Failed\nInvalid Credentials...", Toast.LENGTH_SHORT).show();
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(SignUpActivity.this, "Too many OTP requests\nTry again later.", Toast.LENGTH_SHORT).show();
                FirebaseUser user = auth.getCurrentUser();
                if (user!=null){
                    user.delete();
                }
                finish();
            }

            // Show a message and update the UI


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
            //otpVerificationId = verificationId;
        }
    };

    private void linkCurrentUserWithPhone(PhoneAuthCredential credential) {
        Objects.requireNonNull(auth.getCurrentUser()).linkWithCredential(credential).addOnCompleteListener(task -> {
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if (task.isSuccessful()){
                showUpdatingDataDialog();
                Toast.makeText(SignUpActivity.this, "Phone Number Linked Successfully", Toast.LENGTH_SHORT).show();
                Log.i("user", "linkCurrentUserWithPhone: "+ userData);
                Task<Void> taskUpdate =  FirestoreRepository.getInstance().uploadUser(userData);
                taskUpdate.addOnCompleteListener(task1 -> {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    if (userData.getUserPhone()!=null){
                        FirestoreRepository.getInstance().addPhoneNumberList(userData.getUserPhone());
                    }
                    FirebaseUser user = auth.getCurrentUser();
                    if (user!=null){
                        auth.signOut();
                    }
                    showAccountCreatedScreen();

                    new Handler().postDelayed(this::finish,1000);
                });

            }
            else {
                Toast.makeText(SignUpActivity.this, "Unable to link Phone number" + "\n" + Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();

                FirebaseUser user = auth.getCurrentUser();
                if (user!=null){
                    user.delete();
                }
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(SignUpActivity.this,networkConnection,signUpParentView);
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
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
    }

    private void showSignUpForm(){
        signUpFormParent.setVisibility(View.VISIBLE);
        otpValidationSignUpPageParent.setVisibility(View.GONE);
        signUpPhoneNumberParent.setVisibility(View.GONE);
        accountCreatedSignUpParent.setVisibility(View.GONE);

    }
    private void showOTPValidationScreen(){
        otpValidationSignUpPageParent.setVisibility(View.VISIBLE);
        signUpFormParent.setVisibility(View.GONE);
        signUpPhoneNumberParent.setVisibility(View.GONE);
        accountCreatedSignUpParent.setVisibility(View.GONE);

    }
private void showPhoneNumberScreen(){
        otpValidationSignUpPageParent.setVisibility(View.GONE);
        signUpFormParent.setVisibility(View.GONE);
        signUpPhoneNumberParent.setVisibility(View.VISIBLE);
    accountCreatedSignUpParent.setVisibility(View.GONE);

}

    private void showAccountCreatedScreen(){
        accountCreatedSignUpParent.setVisibility(View.VISIBLE);
        otpValidationSignUpPageParent.setVisibility(View.GONE);
        signUpFormParent.setVisibility(View.GONE);
        signUpPhoneNumberParent.setVisibility(View.GONE);

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof TextInputEditText) {
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

    private void showCreatingAccountDialog(){
        progressDialog.setTitle("Registering User");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }private void showSendingOtpDialog(){
        progressDialog.setTitle("Sending OTP");
        progressDialog.setMessage("Do not minimize this window...");
        progressDialog.show();
    } private void showLinkingUserData(){
        progressDialog.setTitle("Verifying OTP");
        progressDialog.setMessage("Verifying...");
        progressDialog.show();
    }private void showUpdatingDataDialog(){
        progressDialog.setTitle("Updating Data");
        progressDialog.setMessage("Updating...");
        progressDialog.show();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            signUpTextInputName.setError(null);
            signUpTextInputName.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            signUpTextInputEmail.setError(null);
            signUpTextInputEmail.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    TextWatcher passTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            signUpTextInputPassword.setError(null);
            signUpTextInputPassword.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }; TextWatcher passConfirmTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            signUpTextInputPassConfirm.setError(null);
            signUpTextInputPassConfirm.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }; TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            signUpTextInputPhone.setError(null);
            signUpTextInputPhone.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };TextWatcher otpTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            signUpTextInputOtp.setError(null);
            signUpTextInputOtp.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        if (auth!=null){
            if (auth.getCurrentUser()!=null){
                String phone = auth.getCurrentUser().getPhoneNumber();
                if (phone==null || phone.equals("")){
                    auth.getCurrentUser().delete();
                }else {
                    auth.signOut();
                }
            }
        }

    }
}