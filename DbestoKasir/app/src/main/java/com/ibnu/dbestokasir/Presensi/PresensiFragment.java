package com.ibnu.dbestokasir.Presensi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.admin.RegisterActivity;

public class PresensiFragment extends Fragment {

    private CardView scan;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, database;

    public PresensiFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_presensi, container,false);

        scan = (CardView) rootView.findViewById(R.id.scanpresensi);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PresensiMain presensiMain = new PresensiMain();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment, presensiMain);
                transaction.commit();
            }
        });

        return rootView;
    }

}
