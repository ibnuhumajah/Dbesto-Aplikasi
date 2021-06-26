package com.ibnu.dbestokasir.Pelayanan.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.CartLoadListener;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.CartModel;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranAdapter;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranLoadListener;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranModel;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.Stringaddress;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.admin.absen.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {

    Stringaddress stringaddress;
    RecyclerView recyclerView;
    CartLoadListener cartLoadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerviewHistory);

        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        loadHistory();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("History Pemesanan");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void loadHistory() {
        ArrayList<HistoryModel> models = new ArrayList<>();

        DatabaseReference database = FirebaseDatabase.getInstance(stringaddress.firebaseDbesto).getReference();
        database.child("history").addValueEventListener(new ValueEventListener() {
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
                    for (DataSnapshot isidataSnapshot : noteDataSnapshot.getChildren()) {
                        HistoryModel model = isidataSnapshot.getValue(HistoryModel.class);
                        model.setKey(model.getKey());
                        models.add(model);
                    }
                    HistoryAdapter notaAdapter = new HistoryAdapter(HistoryActivity.this, models, cartLoadListener);
                    recyclerView.setAdapter(notaAdapter);
                    /**
                     * Menambahkan object Barang yang sudah dimapping
                     * ke dalam ArrayList
                     */
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_save:
//                createPDF(nama);
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