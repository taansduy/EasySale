package com.example.asus.login_screen.Activity_of_MoreComponent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.asus.login_screen.Activity_of_MoreComponent.AddProduct.AddProduct;
import com.example.asus.login_screen.Local_Cache_Store;
import com.example.asus.login_screen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PassWord extends AppCompatActivity {

    LinearLayout ln_Ok;
    EditText edt_Newpass, edt_RenewPass,edt_Oldpass;
    TextInputLayout til_Newpass,til_Renewpass,til_Oldpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        ln_Ok=findViewById(R.id.Ok);
        edt_Newpass=findViewById(R.id.newpass);
        edt_RenewPass=findViewById(R.id.renewpass);
        edt_Oldpass=findViewById(R.id.oldpass);
        til_Newpass=findViewById(R.id.newpasstWrapper);
        til_Oldpass=findViewById(R.id.oldpasstWrapper);
        til_Renewpass=findViewById(R.id.renewpasstWrapper);
        ln_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String pass = edt_Oldpass.getText().toString();
              String passPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
                if(edt_Oldpass.getText().toString().equals("")||!pass.matches(passPattern)){
                    til_Oldpass.setErrorTextAppearance(R.style.error_appearance);
                    til_Oldpass.setError("Mật khẩu không hợp lệ");
                }
                else {
                    til_Oldpass.setError(null);
                }
                pass = edt_Newpass.getText().toString();
                if(edt_Newpass.getText().toString().equals("")||!pass.matches(passPattern)){
                    til_Newpass.setErrorTextAppearance(R.style.error_appearance);
                    til_Newpass.setError("Mật khẩu không hợp lệ");
                }else {
                    til_Newpass.setError(null);
                }
                pass = edt_RenewPass.getText().toString();
                if(edt_RenewPass.getText().toString().equals("")||!pass.matches(passPattern)){
                    til_Renewpass.setErrorTextAppearance(R.style.error_appearance);
                    til_Renewpass.setError("Mật khẩu không hợp lệ");
                }else {
                    til_Renewpass.setError(null);
                }
                if(!edt_Newpass.getText().toString().equals(edt_RenewPass.getText().toString())){
                    til_Renewpass.setErrorTextAppearance(R.style.error_appearance);
                    til_Renewpass.setError("Mật khẩu chưa trùng khớp");
                }
                if(!edt_RenewPass.getText().toString().equals("")
                        &&!edt_Newpass.getText().toString().equals("")
                        &&!edt_Oldpass.getText().toString().equals("")
                        &&edt_Newpass.getText().toString().equals(edt_RenewPass.getText().toString())){
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(Local_Cache_Store.getOwnerDetail().getEmail(), edt_Oldpass.getText().toString());
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(edt_RenewPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(PassWord.this,AlertDialog.THEME_HOLO_LIGHT);
                                                    builder1.setMessage("Bạn đã thay đỗi mật khẩu thành công!");
                                                    builder1.setCancelable(true);
                                                    builder1.setPositiveButton(
                                                            "Ok",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    onBackPressed();
                                                                }
                                                            });
                                                    AlertDialog alert11 = builder1.create();
                                                    alert11.show();
                                                } else {
                                                    Log.d("DDD", "Error password not updated");
                                                }
                                            }
                                        });
                                     } else {

                                    }
                                }
                            });

                }

            }

        });
    }
}
