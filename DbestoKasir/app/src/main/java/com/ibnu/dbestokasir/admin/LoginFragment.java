package com.ibnu.dbestokasir.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.admin.RegisterActivity;
import com.ibnu.dbestokasir.admin.AdminMain;


public class LoginFragment extends Fragment {

    private EditText username, password;
    private TextView textlogup;
    private Button login;
    private FirebaseAuth mAuth;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View i = inflater.inflate(R.layout.fragment_login,container,false);

        username = i.findViewById(R.id.txtusername);
        password = i.findViewById(R.id.txtpass);
        login = i.findViewById(R.id.btnlogin);
        textlogup = i.findViewById(R.id.txtlogup);

        mAuth = FirebaseAuth.getInstance();

        textlogup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser();
            }
        });

        return i;
    }

    private void loginuser() {
        String user = username.getText().toString();
        String pass = password.getText().toString();

        if (!user.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(user).matches()){
            if (!pass.isEmpty()){
                mAuth.signInWithEmailAndPassword(user,pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                 Toast.makeText(getActivity(),"Login Berhasil!",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), AdminMain.class));
                                 username.getText().clear();
                                 password.getText().clear();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getActivity(), "Login Gagal!", Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                password.setError("Password Kosong");
            }
        }else if (user.isEmpty()){
            username.setError("Username Kosong");
        }else {
            username.setError("Isi Username dengan Benar");
        }
    }
}
