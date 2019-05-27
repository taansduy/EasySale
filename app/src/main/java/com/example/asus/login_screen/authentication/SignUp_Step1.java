package com.example.asus.login_screen.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.login_screen.R;
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


public class SignUp_Step1 extends android.support.v4.app.Fragment {
    private static final String TAG ="fb_login";
    private static final int GG_SIGN_IN = 1 ;
    Button btn_GG;
    Button btn_FB;
    TextView btn_Email;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    boolean check=true;
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
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

            new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme)
                    .setTitle("Thông báo")
                    .setMessage("Đã xảy ra lỗi trong quá trình xử lý.\n Xin vui lòng thử lại sau.")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    };

    public SignUp_Step1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sign_up__step1, container, false);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        btn_FB=(Button) view.findViewById(R.id.btn_SignUpFb);
        btn_GG=(Button) view.findViewById(R.id.btn_SignUpGG);
        btn_Email=(TextView) view.findViewById(R.id.SignUp_Email_btn);


        AppEventsLogger.activateApp(getActivity().getApplicationContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("799889260725-95bb3irdm58t2kucd6ik8qu205a8at5g.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mCallbackManager=CallbackManager.Factory.create();

        mAuth = FirebaseAuth.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mGoogleSignInClient= getClient(getContext(),gso);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        mGoogleApiClient.connect();
        //Set on click for GG
        btn_GG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

                /**
                 * Clearing default account every time so that the account picker dialog can be enforced
                 */
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                }

                startActivityForResult(signInIntent, GG_SIGN_IN);
            }
        });
        //Set on click for FB
        btn_FB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList(
                        "public_profile", "email"));
                LoginManager.getInstance().registerCallback(mCallbackManager,mCallback);
            }
        });

        //Set on click for Email Sign Up method
        btn_Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.Fragment fr=new SignUp_Step1_1();
                FragmentChangeListener fc=(FragmentChangeListener)getActivity();
                fc.replaceFragment(fr);
            }
        });
        return view;
    }

    //solve after login success


    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GG_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogTheme)
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
        synchronized (this) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Checking account...");
            progressDialog.show();
            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Query ref = mDatabase.child("stores").orderByChild("ownerDetail/email").equalTo(acct.getEmail());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();
                        if (dataSnapshot.exists()) {
                            check = false;
                        } else {
                            check = true;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if (check == true) {
                check = false;
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", acct.getEmail()); // Put anything what you want

                                    SignUp_Step2 fr = new SignUp_Step2();
                                    fr.setArguments(bundle);
                                    FragmentChangeListener fc = (FragmentChangeListener) getActivity();
                                    fc.replaceFragment(fr);

                                } else {
                                    String errorMsg = task.getException().toString();
                                    if (errorMsg.toLowerCase().contains("already exists")) {
                                        new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme)
                                                .setTitle("Thông báo")
                                                .setMessage("Email này đã được sử dụng.")
                                                .setPositiveButton(android.R.string.ok, null)
                                                .show();
                                    } else {
                                        new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme)
                                                .setTitle("Thông báo")
                                                .setMessage("Đã xảy ra lỗi trong quá trình đăng ký hãy đảm bảo tài khoản Gmail chưa được sử dụng trên hệ thống.")
                                                .setPositiveButton(android.R.string.ok, null)
                                                .show();
                                    }

                                }

                                // ...
                            }
                        });
            } else {
                new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme)
                        .setTitle("Thông báo")
                        .setMessage("Email này đã được sử dụng.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            synchronized (this) {
                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Checking account...");
                                progressDialog.show();
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                Query ref = mDatabase.child("stores").orderByChild("ownerDetail/email").equalTo(task.getResult().getUser().getEmail());
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        progressDialog.dismiss();
                                        if (dataSnapshot.exists()) {
                                            String errorMsg=task.getException().toString();
                                            if(errorMsg.toLowerCase().contains("already exists"))
                                            {
                                                new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogTheme)
                                                        .setTitle("Thông báo")
                                                        .setMessage("Email liên kết với tài khoản Facebook này đã được sử dụng.")
                                                        .setPositiveButton(android.R.string.ok, null)
                                                        .show();
                                            }
                                            else{
                                                new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogTheme)
                                                        .setTitle("Thông báo")
                                                        .setMessage("Đã xảy ra lỗi trong quá trình đăng ký hãy đảm bảo tài khoản liên kết với Facebook chưa được sử dụng trên hệ thống.")
                                                        .setPositiveButton(android.R.string.ok, null)
                                                        .show();
                                            }
                                        } else {
                                            SignUp_Step2 fr = new SignUp_Step2();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("username",task.getResult().getUser().getEmail()); // Put anything what you want
                                            fr.setArguments(bundle);
                                            FragmentChangeListener fc=(FragmentChangeListener)getActivity();
                                            fc.replaceFragment(fr);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogTheme)
                                    .setTitle("Thông báo")
                                    .setMessage("Đã xảy ra lỗi trong quá trình liên kết với tài khoản Facebook.")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();

                        }

                        // ...
                    }
                });
    }


    public void bindViews(){


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
