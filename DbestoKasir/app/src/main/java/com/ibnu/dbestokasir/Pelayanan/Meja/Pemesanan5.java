package com.ibnu.dbestokasir.Pelayanan.Meja;
import androidx.annotation.NonNull;
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
import com.google.firebase.database.annotations.NotNull;
import com.ibnu.dbestokasir.Pelayanan.History.HistoryModel;
import com.ibnu.dbestokasir.Pelayanan.PelayananMain;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.CartLoadListener;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.CartModel;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranAdapter;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranLoadListener;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranModel;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.Stringaddress;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.eventbus.UpdatePemesanan;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Pemesanan5 extends AppCompatActivity implements PembayaranLoadListener, CartLoadListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private Button proses;
//    private LinearLayout empty;
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
//        empty = (LinearLayout)findViewById(R.id.datakosong);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        loadPemesanan();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("Daftar Pesanan Meja 5");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proses();
            }
        });
    }

    private void Proses() {
        // tarik table cart
        List<PembayaranModel> pembayaranModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance(stringaddress.firebaseDbesto).
                getReference("pembayaran")
                .child("5")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                PembayaranModel pembayaranModel = dataSnapshot.getValue(PembayaranModel.class);
                                pembayaranModel.setKey(dataSnapshot.getKey());
                                pembayaranModels.add(pembayaranModel);

                                FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
                                        .getReference("pembayaran").child("5").child(pembayaranModel.getKey())
                                        .removeValue();

                                FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
                                        .getReference("cart").child("5").child(pembayaranModel.getKey())
                                        .removeValue();

                                //masukan data ke table pembayaran
                                Date waktu = Calendar.getInstance().getTime();

                                DatabaseReference dbPemesanan = FirebaseDatabase.
                                        getInstance(stringaddress.firebaseDbesto).
                                        getReference("history").child(""+waktu);

                                dbPemesanan.child(pembayaranModel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NotNull DataSnapshot snapshot) {
                                        {
                                            HistoryModel historyModel = new HistoryModel();
                                            historyModel.setKey(""+waktu);
                                            historyModel.setNama(pembayaranModel.getNama());
                                            historyModel.setHarga(pembayaranModel.getHarga());
                                            historyModel.setGambar(pembayaranModel.getGambar());
                                            historyModel.setTotalPrice((pembayaranModel.getTotalPrice()));
                                            historyModel.setQuantity(pembayaranModel.getQuantity());

                                            dbPemesanan.child(pembayaranModel.getKey())
                                                    .setValue(historyModel);
                                            EventBus.getDefault().postSticky(new UpdatePemesanan());

                                            Intent proses = new Intent(Pemesanan5.this, PelayananMain.class);
                                            startActivity(proses);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled( @NotNull DatabaseError error) {
                                        cartLoadListener.onCartLoadFailed(error.getMessage());
                                    }
                                });

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadPemesanan();
    }

    private void loadPemesanan() {
        // tarik table cart
        List<PembayaranModel> pembayaranModels = new ArrayList<>();
        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
                .getReference("pembayaran").child("5")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
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
                            onPembayaranLoadSuccess(pembayaranModels);
                        } else {
                            empty.setVisibility(View.VISIBLE);
                            proses.setVisibility(View.GONE);
//                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
                        }
                    }

                    @Override
                    public void onCancelled( DatabaseError error) {
                    }
                });
    }


    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {

    }

    @Override
    public void onCartLoadFailed(String message) {

    }

    @Override
    public void onPembayaranLoad(boolean notificationBadge) {

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