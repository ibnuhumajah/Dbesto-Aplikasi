package com.ibnu.dbestokasir.admin;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ibnu.dbestokasir.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password;
    private TextView textlogin;
    private Button logup;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_regis);


//        LoginFragment fragment = new LoginFragment();
//        FragmentManager manager = getSupportFragmentManager();

//        manager.beginTransaction().add(R.id.admin_regis, fragment).commit();

        username = findViewById(R.id.txtusername);
        password = findViewById(R.id.txtpass);
        logup = findViewById(R.id.btnlogup);

        mAuth = FirebaseAuth.getInstance();

        logup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { createuser(); }
        });

        createuser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("Tambah Data Admin");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void createuser() {
        String user = username.getText().toString();
        String pass = password.getText().toString();

        if (!user.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
            if (!pass.isEmpty()) {
                mAuth.createUserWithEmailAndPassword(user, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterActivity.this, "Registasi Berhasil!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(RegisterActivity.this, "Registasi Gagal!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
         else if (pass.isEmpty()){
             password.setError("Password Kosong");
         } else {
             password.setError("Isi dengan 6 karakter lebih");
            }
        }
        else if (user.isEmpty()){
            username.setError("Username Kosong");
        }
        else {
            username.setError("Isi Username dengan Benar");
        }
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
