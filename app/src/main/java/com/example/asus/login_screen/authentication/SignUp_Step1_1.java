package com.example.asus.login_screen.authentication;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.asus.login_screen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SignUp_Step1_1 extends android.support.v4.app.Fragment {

    private FirebaseAuth mAuth;
    EditText email;
    EditText password;
    ProgressDialog progressDialog;
    TextInputLayout passwordWrapper;

    public SignUp_Step1_1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up__step1_1, container, false);
        email = (EditText) v.findViewById(R.id.email);
        final TextInputLayout emailWrapper = (TextInputLayout) v.findViewById(R.id.emailWrapper);
        password = (EditText) v.findViewById(R.id.password);
        passwordWrapper = (TextInputLayout) v.findViewById(R.id.passwordWrapper);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !password.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !email.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });
        Button SignUp_btn = (Button) v.findViewById(R.id.btn_SignUp);

        mAuth = FirebaseAuth.getInstance();
        SignUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String str_Email = email.getText().toString();
                String pass = password.getText().toString();
                String passPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
                String emailPattern = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
                if (!str_Email.matches(emailPattern)) {
                    emailWrapper.setErrorTextAppearance(R.style.error_appearance);
                    if (str_Email.isEmpty()) {
                        emailWrapper.setError("Vui lòng nhập Email");
                    } else emailWrapper.setError("Email vừa nhập không hợp lệ");
                } else {
                    emailWrapper.setError(null);
                }
                if (!pass.matches(passPattern)) {
                    passwordWrapper.setErrorTextAppearance(R.style.error_appearance);
                    if (str_Email.isEmpty()) {
                        passwordWrapper.setError("Vui lòng nhập password");
                    } else
                        passwordWrapper.setError("Mật khẩu hợp lệ cần có ít nhất 8 kí tự bao gồm cả số, chữ thường, chữ hoa.");

                } else {
                    passwordWrapper.setError(null);
                }
                if (str_Email.matches(emailPattern) && pass.matches(passPattern)) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Creating account...");
                    progressDialog.show();
                    //passwordWrapper.setError(null);

                    mAuth.createUserWithEmailAndPassword(str_Email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Bundle bundle = new Bundle();
                                bundle.putString("username", str_Email); // Put anything what you want
                                SignUp_Step2 fr = new SignUp_Step2();
                                fr.setArguments(bundle);
                                FragmentChangeListener fc = (FragmentChangeListener) getActivity();
                                fc.replaceFragment(fr);

                            } else {
                                new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme)
                                        .setTitle("Thông báo")
                                        .setMessage("Tài khoản email này đã được sử dụng.")
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                                //Toast.makeText(getActivity(), erorrMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }

        });
        // Inflate the layout for this fragment
        return v;
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
