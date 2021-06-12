package com.ibnu.dbestokasir.Pelayanan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibnu.dbestokasir.MainActivity;
import com.ibnu.dbestokasir.Pelayanan.Meja.Pemesanan;
import com.ibnu.dbestokasir.Pelayanan.Meja.Pemesanan2;
import com.ibnu.dbestokasir.Pelayanan.Meja.Pemesanan3;
import com.ibnu.dbestokasir.Pelayanan.Meja.Pemesanan4;
import com.ibnu.dbestokasir.Pelayanan.Meja.Pemesanan5;
import com.ibnu.dbestokasir.Pelayanan.Meja.Pemesanan6;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranModel;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.Stringaddress;
import com.ibnu.dbestokasir.Presensi.PresensiBerhasil;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.SessionManager.SessionManager;
import com.ibnu.dbestokasir.admin.RegisterActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.text.TextUtils.split;

public class PelayananMain extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DatabaseReference databaseReference;
    private CardView meja1, meja2, meja3, meja4, meja5, meja6;
    private Button btn_scan, btn_out;
    private RelativeLayout rl_scan, kasir, notif, notif2, notif3, notif4,  notif5, notif6;
    private LinearLayout main_meja;
    private TextView txnama, txnik, txwaktu, txnotif, txnotif2, txnotif3, txnotif4, txnotif5, txnotif6;
    Stringaddress stringaddress;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference database;
    public String valueQR[];
    String url = "https://dbesto-default-rtdb.firebaseio.com/";

    String uniqueKey;
    SessionManager sessionManager;
    private ArrayList<Model> models = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pelayanan_main);
        databaseReference = FirebaseDatabase.getInstance(url).getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("Menu Pelayanan");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        meja1 = findViewById(R.id.cv_meja1); meja2 = findViewById(R.id.cv_meja2);
        meja3 = findViewById(R.id.cv_meja3); meja4 = findViewById(R.id.cv_meja4);
        meja5 = findViewById(R.id.cv_meja5); meja6 = findViewById(R.id.cv_meja6);
        notif = findViewById(R.id.coverBadge); notif2 = findViewById(R.id.coverBadge2);
        notif3 = findViewById(R.id.coverBadge3); notif4 = findViewById(R.id.coverBadge4);
        notif5 = findViewById(R.id.coverBadge5); notif6 = findViewById(R.id.coverBadge6);
        txnotif = findViewById(R.id.badge); txnotif2 = findViewById(R.id.badge2);
        txnotif3 = findViewById(R.id.badge3); txnotif4 = findViewById(R.id.badge4);
        txnotif5 = findViewById(R.id.badge5); txnotif6 = findViewById(R.id.badge6);

        btn_scan = (Button) findViewById(R.id.scankasir);
        btn_out = (Button) findViewById(R.id.outpelayan);
        rl_scan = (RelativeLayout) findViewById(R.id.scanbann);
        kasir = (RelativeLayout) findViewById(R.id.namakasir);
        main_meja = (LinearLayout) findViewById(R.id.datameja);
        txnama = (TextView) findViewById(R.id.txtkasir); txnik = (TextView) findViewById(R.id.nikkasir);
        txwaktu = (TextView) findViewById(R.id.waktukasir);

        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_700);
        sessionManager = new SessionManager(this);

        check_pemesanan();
        check_pemesanan2();
        check_pemesanan3();
        check_pemesanan4();
        check_pemesanan5();
        check_pemesanan6();

        meja1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PelayananMain.this, Pemesanan.class);
                startActivity(i);
            }
        });
        meja2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PelayananMain.this, Pemesanan2.class);
                startActivity(i);
            }
        });
        meja3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PelayananMain.this, Pemesanan3.class);
                startActivity(i);
            }
        });
        meja4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PelayananMain.this, Pemesanan4.class);
                startActivity(i);
            }
        });
        meja5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PelayananMain.this, Pemesanan5.class);
                startActivity(i);
            }
        });
        meja6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PelayananMain.this, Pemesanan6.class);
                startActivity(i);
            }
        });

        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (txnama.getText().equals("Silahkan masuk pelayanan dahulu")){
                        btn_out.setVisibility(View.GONE);
                    }else {
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
                        final String date = df.format(Calendar.getInstance().getTime());

                        databaseReference.child(uniqueKey).setValue(new com.ibnu.dbestokasir.Pelayanan.Meja.KasirModel(txnama.getText().toString(), txnik.getText().toString(), txnama.getText().toString() + "#" + txnik.getText().toString(), txwaktu.getText().toString(), date, uniqueKey))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(PelayananMain.this, MainActivity.class));
                                        Toast.makeText(PelayananMain.this, "Berhasil Keluar Pelayanan", Toast.LENGTH_SHORT).show();
                                        onRefresh();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(PelayananMain.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        sessionManager.logout();
                    }
                }catch (Exception e){
                    Toast.makeText(PelayananMain.this, "Silahkan refresh dengan menswipe layar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PelayananMain.this, QrPelayanan.class);
                startActivityForResult(intent,1);
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance("https://dbesto-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Kasir");
        database = FirebaseDatabase.getInstance("https://dbesto-default-rtdb.firebaseio.com/").getReference();

        checkLogin();
    }

    void check_pemesanan() {
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
                            notif.setVisibility(View.VISIBLE);
                            txnotif.setText("");
                        } else {
                            notif.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    void check_pemesanan2() {
        List<PembayaranModel> pembayaranModels = new ArrayList<>();
        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
                .getReference("pembayaran").child("2")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
                                pembayaranModels.add(pembayaranModel);
                            }
                            notif2.setVisibility(View.VISIBLE);
                            txnotif2.setText("");
                        } else {
                            notif2.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    void check_pemesanan3() {
        List<PembayaranModel> pembayaranModels = new ArrayList<>();
        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
                .getReference("pembayaran").child("3")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
                                pembayaranModels.add(pembayaranModel);
                            }
                            notif3.setVisibility(View.VISIBLE);
                            txnotif3.setText("");
                        } else {
                            notif3.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    void check_pemesanan4() {
        List<PembayaranModel> pembayaranModels = new ArrayList<>();
        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
                .getReference("pembayaran").child("4")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
                                pembayaranModels.add(pembayaranModel);
                            }
                            notif4.setVisibility(View.VISIBLE);
                            txnotif4.setText("");
                        } else {
                            notif4.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    void check_pemesanan5() {
        List<PembayaranModel> pembayaranModels = new ArrayList<>();
        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
                .getReference("pembayaran").child("5")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
                                pembayaranModels.add(pembayaranModel);
                            }
                            notif5.setVisibility(View.VISIBLE);
                            txnotif5.setText("");
                        } else {
                            notif5.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    void check_pemesanan6() {
        List<PembayaranModel> pembayaranModels = new ArrayList<>();
        FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
                .getReference("pembayaran").child("6")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot pembayaranSnapshot : snapshot.getChildren()) {
                                PembayaranModel pembayaranModel = pembayaranSnapshot.getValue(PembayaranModel.class);
                                pembayaranModel.setKey(pembayaranSnapshot.getKey());
                                pembayaranModels.add(pembayaranModel);
                            }
                            notif6.setVisibility(View.VISIBLE);
                            txnotif6.setText("");
                        } else {
                            notif6.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void checkLogin() {
        mSwipeRefreshLayout.setRefreshing(true);

        if (sessionManager.isLogin()){



            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    uniqueKey = sessionManager.getSessionData().get("ID");
                    showData(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else {
            rl_scan.setVisibility(View.VISIBLE);
            main_meja.setVisibility(View.GONE);
            kasir.setVisibility(View.GONE);

        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

            //jika uniquekey sama dengan getkey maka tampilkan
            if (childDataSnapshot.getKey().equals(uniqueKey)) {
                txnama.setText("Kasir: " + childDataSnapshot.child("nama").getValue().toString());
                txnik.setText(childDataSnapshot.child("nik").getValue().toString());
                txwaktu.setText(childDataSnapshot.child("masuk").getValue().toString());
            }
//            Log.v("key",""+ childDataSnapshot.getKey()); //displays the key for the node
//            Log.v("nama",""+ childDataSnapshot.child("nama").getValue());   //gives the value for given keyname
            //Log.v("child",uniqueKey);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            main_meja.setVisibility(View.VISIBLE);
            rl_scan.setVisibility(View.GONE);
            kasir.setVisibility(View.VISIBLE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                try{
                    final String[] strEditText = split(data.getStringExtra("hasil"),"#");

                    database.child("User").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("snap",dataSnapshot.toString());
                            /**
                             * Saat ada data baru, masukkan datanya ke ArrayList
                             */
                            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                /**
                                 * Mapping data pada DataSnapshot ke dalam object Barang
                                 * Dan juga menyimpan primary key pada object Barang
                                 * untuk keperluan Edit dan Delete data
                                 */
                                Model model = noteDataSnapshot.getValue(Model.class);
                                model.setKey(noteDataSnapshot.getKey());

                                /**
                                 * Menambahkan object Barang yang sudah dimapping
                                 * ke dalam ArrayList
                                 */
                                models.add(model);
                            }
                            for (int i = 0; i<models.size();i++){
                                //Toast.makeText(getActivity(), models.get(i).getNama()+" "+models.get(i).getNbi(), Toast.LENGTH_SHORT).show();
                                if (strEditText[0].equals(models.get(i).getNama()) && strEditText[1].equals(models.get(i).getNik())){
                                    Toast.makeText(PelayananMain.this, "Berhasil Login Pelayanan", Toast.LENGTH_SHORT).show();

                                    txnama.setText("Kasir: " + strEditText[0]);
                                    txnik.setText(strEditText[1]);

                                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
                                    String date = df.format(Calendar.getInstance().getTime());

                                    txwaktu.setText(date);



                                    inputData(strEditText[0],strEditText[1],data.getStringExtra("hasil"),date,"");

                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // This method is called once with the initial value and again
                                            // whenever data at this location is updated.
                                            showData(dataSnapshot);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    break;
                                }else {
                                    Toast.makeText(PelayananMain.this, "Data tidak ada dalam database", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                        }

                    });

                }catch (Exception e){
                    Intent intent = new Intent(PelayananMain.this,QrPelayanan.class);
                    startActivityForResult(intent,1);
                }
            }
            if(resultCode == RESULT_FIRST_USER) {
                Toast.makeText(PelayananMain.this, "QR Kode tidak sesuai format", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PelayananMain.this,QrPelayanan.class);
                startActivityForResult(intent,1);
            }
        }
    }

    private void inputData(String nama, String nik, String qr, String masuk, String keluar) {
        com.ibnu.dbestokasir.Pelayanan.Meja.KasirModel model = new com.ibnu.dbestokasir.Pelayanan.Meja.KasirModel();
        model.setNama(nama);
        model.setNik(nik);
        model.setQr(qr);
        model.setMasuk(masuk);
        model.setKeluar(keluar);

        databaseReference.push().setValue(model, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   DatabaseReference databaseReference) {
                sessionManager.createSession(databaseReference.getKey());
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_history:
                //Ubah Register
                startActivity(new Intent(PelayananMain.this,
                        RegisterActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    @Override
    public void onRefresh() {checkLogin();
    check_pemesanan();
        check_pemesanan2();
        check_pemesanan3();
        check_pemesanan4();
        check_pemesanan5();
        check_pemesanan6();
    }
}
