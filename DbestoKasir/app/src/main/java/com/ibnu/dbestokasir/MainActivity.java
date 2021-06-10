package com.ibnu.dbestokasir;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ibnu.dbestokasir.Pelayanan.PelayananFragment;
import com.ibnu.dbestokasir.Penilaian.PenilaianFragment;
import com.ibnu.dbestokasir.Penilaian.Terbaik.DataTerbaik;
import com.ibnu.dbestokasir.Penilaian.Terbaik.InputNama;
import com.ibnu.dbestokasir.Presensi.PresensiFragment;
import com.ibnu.dbestokasir.admin.LoginFragment;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    boolean doubleBackToExitPressedOnce = false;
    private static final int REQUEST_CAMERARESULT=201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView btnNav = findViewById(R.id.main_nav);
        btnNav.setOnNavigationItemSelectedListener(navListener);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, new PelayananFragment()).commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(this.checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                ///method to get Images
            }else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    Toast.makeText(this,"Silahkan setujui permintaan akses agar aplikasi berjalan normal",Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_CAMERARESULT);
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.item1:
                            selectedFragment = new PelayananFragment();
                            break;

                        case R.id.item2:
                            selectedFragment = new PresensiFragment();
                            break;

                        case R.id.item3:
                            selectedFragment = new PenilaianFragment();
                            break;

                        case R.id.item4:
                            selectedFragment = new LoginFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment
                            ,selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            MainActivity.this.finish();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(this, "Tekan tombol kembali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}