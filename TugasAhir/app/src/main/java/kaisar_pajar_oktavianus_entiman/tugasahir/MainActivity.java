package kaisar_pajar_oktavianus_entiman.tugasahir;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import kaisar_pajar_oktavianus_entiman.tugasahir.databinding.ActivityMainBinding;
import kaisar_pajar_oktavianus_entiman.tugasahir.ui.dashboard.DashboardFragment;
import kaisar_pajar_oktavianus_entiman.tugasahir.ui.home.HomeFragment;
import kaisar_pajar_oktavianus_entiman.tugasahir.ui.notifications.NotificationsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    HomeFragment homeFragment = new HomeFragment();
    DashboardFragment dashboardFragment = new DashboardFragment();
    NotificationsFragment notificationsFragment = new NotificationsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportActionBar().setTitle("Halaman Utama");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment);
        fragmentTransaction.commit();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId()==R.id.navigation_home){
                    fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment);
                    getSupportActionBar().setTitle("Halaman Utama");
                }else if (item.getItemId()==R.id.navigation_dashboard){
                    getSupportActionBar().setTitle("Berita");
                    fragmentTransaction.replace(R.id.nav_host_fragment, dashboardFragment);
                }else{
                    fragmentTransaction.replace(R.id.nav_host_fragment, notificationsFragment);
                    getSupportActionBar().setTitle("Profile");
                }
                fragmentTransaction.commit();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(this, HalamanScan.class);
        startActivity(back);
        super.onBackPressed();
    }
}