package com.ibnu.dbestokasir.Penilaian;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.admin.AdminMain;

public class LoginPenilaian extends Fragment {
    private Button login;
    private EditText username, password;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.login_penilaian, container, false);

        login = (Button) rootview.findViewById(R.id.btnlogin);
        username = (EditText) rootview.findViewById(R.id.txtusername);
        password = (EditText) rootview.findViewById(R.id.txtpass);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginuser();
            }
        });

        return rootview;
    }


    private void Loginuser(){
        String user = username.getText().toString();
        String pass = password.getText().toString();

        if (!user.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(user).matches()){
            if (!pass.isEmpty()){
                mAuth.signInWithEmailAndPassword(user,pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(getActivity(),"Login Berhasil!",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), PenilaianMain.class));
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
