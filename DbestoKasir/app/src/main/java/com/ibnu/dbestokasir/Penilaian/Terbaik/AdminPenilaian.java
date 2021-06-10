package com.ibnu.dbestokasir.Penilaian.Terbaik;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.admin.AdminMain;
import com.ibnu.dbestokasir.admin.absen.DataAbsen;
import com.ibnu.dbestokasir.admin.user.DataUser;


public class AdminPenilaian extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SectionsPagerAdapter mSections;
    private ViewPager mviewPager;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_penilaian);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("Data Karyawan Terbaik");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSections = new SectionsPagerAdapter(getSupportFragmentManager());

        mviewPager = findViewById(R.id.container);
        mviewPager.setAdapter(mSections);

        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#FF0000"));

        mviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mviewPager));
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    DataTerbaik tab1 = new DataTerbaik();
                    return tab1;
                case 1:
                    InputNama tab2 = new InputNama();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "Data Terbaik";
                case 1:
                    return "Input Nama";
            }
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
