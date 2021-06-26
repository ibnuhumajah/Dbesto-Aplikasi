package com.ibnu.dbestokasir.Penilaian;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ibnu.dbestokasir.Pelayanan.PelayananMain;
import com.ibnu.dbestokasir.Penilaian.Terbaik.AdminPenilaian;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.admin.LoginFragment;
import com.ibnu.dbestokasir.admin.RegisterActivity;

public class PenilaianMain extends AppCompatActivity{
    WebView webView;
    WebSettings webSettings;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String url="http://200.200.200.178/dbesto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.penilaian_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("Menu Karyawan Terbaik");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_700);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                webView.loadUrl(url);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 3*1000);
//            }
//        });


        webView = (WebView)findViewById(R.id.webview1);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.input_nama, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.input_nama:
                startActivity(new Intent(PenilaianMain.this, AdminPenilaian.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
