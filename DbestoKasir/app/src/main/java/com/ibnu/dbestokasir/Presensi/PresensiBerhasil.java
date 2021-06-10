package com.ibnu.dbestokasir.Presensi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.ibnu.dbestokasir.MainActivity;
import com.ibnu.dbestokasir.Pelayanan.PelayananFragment;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.admin.LoginFragment;

public class PresensiBerhasil extends AppCompatActivity {
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presensi_berhasil);

        back = (Button) findViewById(R.id.btn_kembali);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PresensiBerhasil.this, MainActivity.class);
                startActivity(intent);
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.absen_berhasil, new PresensiMain()).commit();
                finish();
            }
        });
    }
}
