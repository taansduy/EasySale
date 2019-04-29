package com.example.asus.login_screen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.florent37.materialtextfield.MaterialTextField;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.google.android.gms.auth.api.signin.GoogleSignIn.getClient;

public class SignIn_Screen extends AppCompatActivity {
    EditText email,password;
    TextInputLayout emailWrapper,passwordWrapper;
    TextView tv_SignUp;
    ImageView fb_login,gg_login;
    Button SignIn_btn;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            handleFacebookAccessToken(loginResult.getAccessToken());
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());

                            // Application code
                            try {
                                String Name = object.getString("name");

                                String FEmail = object.getString("email");



                            } catch (JSONException e) {
                                e.printStackTrace();
                            } // 01/31/1980 format
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

           new AlertDialog.Builder(SignIn_Screen.this,R.style.MyAlertDialogTheme)
                    .setTitle("Thông báo")
                    .setMessage("Đã xảy ra lỗi trong quá trình xử lý.\nXin vui lòng thử lại sau.\n"+e.toString())
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_in__screen);

        setupToolBar();
        email=(EditText)findViewById(R.id.email);
        emailWrapper=(TextInputLayout) findViewById(R.id.emailWrapper);
        password=(EditText)findViewById(R.id.password);
        passwordWrapper=(TextInputLayout) findViewById(R.id.passwordWrapper);
        SignIn_btn=(Button) findViewById(R.id.btn_SignIn);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !password.hasFocus()){
                    hideKeyboard(v);
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !email.hasFocus()){
                    hideKeyboard(v);
                }
            }
        });
        tv_SignUp= (TextView) findViewById(R.id.tv_SignUp);
        tv_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.alpha_anim));
                startActivity(new Intent(getBaseContext(),SignUp_Screen.class));
                overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        fb_login=(ImageView) findViewById(R.id.fb_login);
        gg_login=(ImageView) findViewById(R.id.gg_login);
        AppEventsLogger.activateApp(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("799889260725-95bb3irdm58t2kucd6ik8qu205a8at5g.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mCallbackManager=CallbackManager.Factory.create();

        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient= getClient(this,gso);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();


        mGoogleApiClient.connect();
        gg_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

                /**
                 * Clearing default account every time so that the account picker dialog can be enforced
                 */
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                }

                startActivityForResult(signInIntent, 1);
            }
        });
        //Set on click for FB
        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SignIn_Screen.this, Arrays.asList(
                        "public_profile", "email"));
                LoginManager.getInstance().registerCallback(mCallbackManager,mCallback);
            }
        });

        final Button SignIn_btn=(Button) findViewById(R.id.btn_SignIn);
        SignIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str_Email = email.getText().toString();
                String pass = password.getText().toString();
                String passPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
                String emailPattern = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
                if (!str_Email.matches(emailPattern)) {
                    emailWrapper.setErrorTextAppearance(R.style.error_appearance);
                    if(str_Email.isEmpty())
                    {
                        emailWrapper.setError("Vui lòng nhập Email");
                    }
                    else emailWrapper.setError("Email vừa nhập không hợp lệ");
                } else {
                    emailWrapper.setError(null);
                }
                if (!pass.matches(passPattern)) {
                    passwordWrapper.setErrorTextAppearance(R.style.error_appearance);
                    if(str_Email.isEmpty())
                    {
                        passwordWrapper.setError("Vui lòng nhập password");
                    }
                    else passwordWrapper.setError("Mật khẩu hợp lệ cần có ít nhất 8 kí tự bao gồm cả số, chữ thường, chữ hoa.");

                } else {
                    passwordWrapper.setError(null);
                }
                if (str_Email.matches(emailPattern)&& pass.matches(passPattern))
                {
                    final ProgressDialog progressDialog = new ProgressDialog(SignIn_Screen.this);
                    progressDialog.setMessage("Waiting...");
                    progressDialog.show();
                    //passwordWrapper.setError(null);

                    mAuth.signInWithEmailAndPassword(str_Email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                startActivity(new Intent(SignIn_Screen.this,Main_Screen.class));
                            } else {
                                new AlertDialog.Builder(SignIn_Screen.this,R.style.MyAlertDialogTheme)
                                        .setTitle("Thông báo")
                                        .setMessage("Tài khoản email này đã được sử dụng.")
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                                //Toast.makeText(getActivity(), erorrMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

    private void setupToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar2);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                new AlertDialog.Builder(this,R.style.MyAlertDialogTheme)
                        .setTitle("Thông báo")
                        .setMessage("Đã xảy ra lỗi trong quá trình xử lý.\n Xin vui lòng thử lại sau.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                // ...
            }
        }
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting...");
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                Query ref=mDatabase.child("stores").orderByChild("ownerDetail/email").equalTo(acct.getEmail());
                                synchronized (this)
                                {
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            progressDialog.dismiss();
                                            if (dataSnapshot.exists()) {
                                                startActivity(new Intent(SignIn_Screen.this,Main_Screen.class));
                                            }
                                            else{
                                                Intent mIntent = new Intent(SignIn_Screen.this, SignUp_Screen.class);
                                                Bundle mBundle = new Bundle();
                                                mBundle.putString("Screen","Step2");
                                                mBundle.putString("email",acct.getEmail().toString());
                                                mIntent.putExtras(mBundle);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                    startActivity(mIntent,mBundle);
                                                }
                                                else
                                                {
                                                    startActivity(mIntent);
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                            } else {


                            }

                            // ...
                        }
                    });

    }
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting...");
        progressDialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            Query ref=mDatabase.child("stores").orderByChild("ownerDetail/email").equalTo(task.getResult().getUser().getEmail());
                            synchronized (this)
                            {
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        progressDialog.dismiss();
                                        if (dataSnapshot.exists()) {
                                            startActivity(new Intent(SignIn_Screen.this,Main_Screen.class));
                                        }
                                        else{

                                            Intent mIntent = new Intent(SignIn_Screen.this, SignUp_Screen.class);
                                            Bundle mBundle = new Bundle();
                                            mBundle.putString("Screen","Step2");
                                            mBundle.putString("email",task.getResult().getUser().getEmail());
                                            mIntent.putExtras(mBundle);
                                            startActivity(mIntent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        } else {
                            if(task.getException().toString().contains("already exists")) {
                                new AlertDialog.Builder(SignIn_Screen.this, R.style.MyAlertDialogTheme)
                                        .setTitle("Thông báo")
                                        .setMessage("Email này đã được sử dụng với nhà cung cấp khác (Google,...). Vui lòng chọn đăng nhập bằng hình thức khác.")
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                            }
                            else
                            {
                                new AlertDialog.Builder(SignIn_Screen.this, R.style.MyAlertDialogTheme)
                                        .setTitle("Thông báo")
                                        .setMessage("Đã xảy ra lỗi trong qua trình đăng nhập bằng tài khoản này.")
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                            }
                        }
                        //Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }

                    // ...
                });
    }
}
