package com.ibnu.dbestokasir.Penilaian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ibnu.dbestokasir.Pelayanan.PelayananFragment;
import com.ibnu.dbestokasir.Penilaian.Terbaik.DataTerbaik;
import com.ibnu.dbestokasir.Penilaian.Terbaik.Model;
import com.ibnu.dbestokasir.Penilaian.Terbaik.RecyclerViewDataTerbaik;
import com.ibnu.dbestokasir.Penilaian.Terbaik.Terbaik;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.SessionManager.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.text.TextUtils.split;


public class PenilaianFragment extends Fragment{
    private Button login;
    private TextView name;
    private ArrayList<Model> models = new ArrayList<>();
    SessionManager sessionManager;

    public PenilaianFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_penilaian, container, false);

        sessionManager = new SessionManager(getActivity());
        name = (TextView) rootView.findViewById(R.id.employee);
        login = (Button) rootView.findViewById(R.id.loginpenilai);

        if(Terbaik.getInput() == null){
            name.setText("Karywan Terbaik");
        } if (Terbaik.getInput() != null){
            name.setText(Terbaik.getInput());
        }
//        if (name.getText() != null){
//            name.setText("kosong");
//        }else {
//            Bundle bundle = this.getArguments();
//            String data = bundle.getString("key");
//            name.setText(data);
//        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginPenilaian loginPenilaian = new LoginPenilaian();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment, loginPenilaian);
                transaction.commit();
            }
        });


        return rootView;
    }

}