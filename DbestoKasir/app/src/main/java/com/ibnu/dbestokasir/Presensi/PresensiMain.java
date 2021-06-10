package com.ibnu.dbestokasir.Presensi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.SessionManager.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;
import static android.text.TextUtils.split;

public class PresensiMain extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView txnama, txnik, txwaktu, scan;
    private Button out;
    public String valueQR[];

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, database;

    String uniqueKey;
    SessionManager sessionManager;

    private SwipeRefreshLayout mSwipeRefresh;
    private ArrayList<Model> models = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.presensi_main, container, false);
        txnama = (TextView) rootView.findViewById(R.id.username);
        txnik = (TextView) rootView.findViewById(R.id.nik);
        txwaktu = (TextView) rootView.findViewById(R.id.waktu);
        scan = (TextView) rootView.findViewById(R.id.btn_scan);
        out = (Button) rootView.findViewById(R.id.logout_cust);

        mSwipeRefresh = rootView.findViewById(R.id.swipe_container);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        setHasOptionsMenu(true);

        sessionManager = new SessionManager(getActivity());
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (txnama.getText().equals("Silahkan absen dahulu")){
                        out.setVisibility(View.GONE);
                    }else {
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
                        final String date = df.format(Calendar.getInstance().getTime());

                        databaseReference.child(uniqueKey).setValue(new com.ibnu.dbestokasir.admin.absen.Model(txnama.getText().toString(), txnik.getText().toString(), txnama.getText().toString() + "#" + txnik.getText().toString(), txwaktu.getText().toString(), date, uniqueKey))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(getActivity(), PresensiBerhasil.class));
                                       Toast.makeText(getActivity(), "Berhasil Melakukan Absensi ", Toast.LENGTH_SHORT).show();
                                        onRefresh();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        sessionManager.logout();
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Silahkan refresh dengan menswipe layar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),QrPresensi.class);
                startActivityForResult(intent,1);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance("https://dbesto-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Absen");
        database = FirebaseDatabase.getInstance("https://dbesto-default-rtdb.firebaseio.com/").getReference();

        checkLogin();

        return rootView;
    }

    private void checkLogin() {
        mSwipeRefresh.setRefreshing(true);

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
            txnik.setText("NIK");
            txnama.setText("Nama Karyawan");
            txwaktu.setText("Waktu");
            out.setVisibility(View.GONE);

        }
        mSwipeRefresh.setRefreshing(false);
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

            //jika uniquekey sama dengan getkey maka tampilkan
            if (childDataSnapshot.getKey().equals(uniqueKey)) {
                txnama.setText(childDataSnapshot.child("nama").getValue().toString());
                txnik.setText(childDataSnapshot.child("nik").getValue().toString());
                txwaktu.setText(childDataSnapshot.child("masuk").getValue().toString());
            }
//            Log.v("key",""+ childDataSnapshot.getKey()); //displays the key for the node
//            Log.v("nama",""+ childDataSnapshot.child("nama").getValue());   //gives the value for given keyname
            //Log.v("child",uniqueKey);

            out.setVisibility(View.VISIBLE);
            scan.setVisibility(View.GONE);
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
                                    Toast.makeText(getActivity(), "Mendapatkan Data Karyawan", Toast.LENGTH_SHORT).show();

                                    txnama.setText(strEditText[0]);
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
                                    Toast.makeText(getActivity(), "Data tidak ada dalam database", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                        }

                    });

                }catch (Exception e){
                    Intent intent = new Intent(getActivity(),QrPresensi.class);
                    startActivityForResult(intent,1);
                }
            }
            if(resultCode == RESULT_FIRST_USER) {
                Toast.makeText(getActivity(), "QR Kode tidak sesuai format", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),QrPresensi.class);
                startActivityForResult(intent,1);
            }
        }
    }

    private void inputData(String nama, String nik, String qr, String masuk, String keluar) {
        com.ibnu.dbestokasir.admin.absen.Model model = new com.ibnu.dbestokasir.admin.absen.Model();
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
    public void onRefresh() { checkLogin();

    }
}
