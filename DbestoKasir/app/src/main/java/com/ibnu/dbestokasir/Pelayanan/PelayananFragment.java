package com.ibnu.dbestokasir.Pelayanan;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibnu.dbestokasir.Presensi.QrPresensi;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.SessionManager.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;
import static android.text.TextUtils.split;


public class PelayananFragment extends Fragment {
    private CardView scan;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, database;
    public String valueQR[];
    String url = "https://dbesto-default-rtdb.firebaseio.com/";

    String uniqueKey;
    SessionManager sessionManager;
    private ArrayList<Model> models = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pelayanan, container, false);
        scan = (CardView) rootView.findViewById(R.id.scanqr2);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),PelayananMain.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
