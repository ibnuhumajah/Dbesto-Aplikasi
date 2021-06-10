package com.ibnu.dbestokasir.Pelayanan.Meja;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibnu.dbestokasir.Pelayanan.PelayananMain;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.CartLoadListener;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.CartModel;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranAdapter;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranLoadListener;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranModel;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.Stringaddress;
import com.ibnu.dbestokasir.R;

import java.util.ArrayList;
import java.util.List;
import java.lang.CharSequence;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Pemesanan extends AppCompatActivity implements PembayaranLoadListener, CartLoadListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private Button proses;
    private LinearLayout empty;
    private TextView total;

    Stringaddress stringaddress;

    PembayaranLoadListener pembayaranLoadListener;
    CartLoadListener cartLoadListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        ButterKnife.bind(this);

        pembayaranLoadListener = this;
        cartLoadListener = this;
        proses = findViewById(R.id.proses);
        empty = findViewById(R.id.datakosong);
        total = findViewById(R.id.totalprice);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        loadPemesanan();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("Daftar Pesanan Meja 1");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadPemesanan();
    }

    private void loadPemesanan() {
        DatabaseReference db = FirebaseDatabase.
                getInstance(stringaddress.firebaseDbesto).
                getReference("pembayaran");

        List<PembayaranModel> pembayaranModels = new ArrayList<>();
        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
                .getReference("pembayaran").child("1")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
                                pembayaranModels.add(pembayaranModel);
                            }
                            int a = 0;
                            for (PembayaranModel pembayaranModel: pembayaranModels){
                                a +=pembayaranModel.getTotalPrice();
                            }
                            total.setText("Total Harga: Rp" + (a));
                            pembayaranLoadListener.onPembayaranLoadSuccess(pembayaranModels);
                            empty.setVisibility(View.GONE);
                            proses.setVisibility(View.VISIBLE);
                        } else {
                            empty.setVisibility(View.VISIBLE);
                            proses.setVisibility(View.GONE);
//                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
//                        menuLoadListener.onMenuLoadFailed(error.getMessage());
                    }
                });
//        List<PembayaranModel> pembayaranModels2 = new ArrayList<>();
//        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
//                .getReference("pembayaran").child("2")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
//                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
//                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
//                                pembayaranModels.add(pembayaranModel);
//                            }
//                            pembayaranLoadListener.onPembayaranLoadSuccess(pembayaranModels);
//                        } else {
////                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
////                        menuLoadListener.onMenuLoadFailed(error.getMessage());
//                    }
//                });
//        List<PembayaranModel> pembayaranModels3 = new ArrayList<>();
//        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
//                .getReference("pembayaran").child("3")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
//                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
//                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
//                                pembayaranModels.add(pembayaranModel);
//                            }
//                            pembayaranLoadListener.onPembayaranLoadSuccess(pembayaranModels);
//                        } else {
////                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
////                        menuLoadListener.onMenuLoadFailed(error.getMessage());
//                    }
//                });
//        List<PembayaranModel> pembayaranModels4 = new ArrayList<>();
//        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
//                .getReference("pembayaran").child("4")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
//                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
//                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
//                                pembayaranModels.add(pembayaranModel);
//                            }
//                            pembayaranLoadListener.onPembayaranLoadSuccess(pembayaranModels);
//                        } else {
////                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
////                        menuLoadListener.onMenuLoadFailed(error.getMessage());
//                    }
//                });
//        List<PembayaranModel> pembayaranModels5 = new ArrayList<>();
//        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
//                .getReference("pembayaran").child("5")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
//                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
//                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
//                                pembayaranModels.add(pembayaranModel);
//                            }
//                            pembayaranLoadListener.onPembayaranLoadSuccess(pembayaranModels);
//                        } else {
////                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
////                        menuLoadListener.onMenuLoadFailed(error.getMessage());
//                    }
//                });
//        List<PembayaranModel> pembayaranModels6 = new ArrayList<>();
//        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
//                .getReference("pembayaran").child("6")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
//                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
//                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
//                                pembayaranModels.add(pembayaranModel);
//                            }
//                            pembayaranLoadListener.onPembayaranLoadSuccess(pembayaranModels);
//                        } else {
////                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
////                        menuLoadListener.onMenuLoadFailed(error.getMessage());
//                    }
//                });
//        List<PembayaranModel> pembayaranModels7 = new ArrayList<>();
//        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
//                .getReference("pembayaran").child("7")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
//                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
//                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
//                                pembayaranModels.add(pembayaranModel);
//                            }
//                            pembayaranLoadListener.onPembayaranLoadSuccess(pembayaranModels);
//                        } else {
////                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
////                        menuLoadListener.onMenuLoadFailed(error.getMessage());
//                    }
//                });
    }


    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {

    }

    @Override
    public void onCartLoadFailed(String message) {

    }

    @Override
    public void onPembayaranLoadSuccess(List<PembayaranModel> pembayaranModelList) {
        PembayaranAdapter pembayaranAdapter = new PembayaranAdapter(this, pembayaranModelList, cartLoadListener);
        recyclerView.setAdapter(pembayaranAdapter);
    }

    @Override
    public void onPembayaranFailed(String message) {

    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}