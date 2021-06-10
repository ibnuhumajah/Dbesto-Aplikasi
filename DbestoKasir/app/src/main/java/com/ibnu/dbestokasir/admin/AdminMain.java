package com.ibnu.dbestokasir.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
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
import com.ibnu.dbestokasir.admin.absen.DataAbsen;
import com.ibnu.dbestokasir.admin.user.DataUser;

public class AdminMain extends AppCompatActivity {

    private Button logout;
    private FirebaseAuth mAuth;
    private SectionsPagerAdapter mSections;
    private ViewPager mviewPager;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);
//        logout = findViewById(R.id.btnlogout);
        mAuth = FirebaseAuth.getInstance();

//        setContentView(R.layout.admin_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("Menejemen Karyawan");
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
                    DataAbsen tab1 = new DataAbsen();
                    return tab1;
                case 1:
                    DataUser tab2 = new DataUser();
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
                    return "DATA ABSEN";
                case 1:
                    return "DATA KARYAWAN";
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