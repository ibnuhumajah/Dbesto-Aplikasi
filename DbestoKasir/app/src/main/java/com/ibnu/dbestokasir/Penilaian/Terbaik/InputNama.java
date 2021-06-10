package com.ibnu.dbestokasir.Penilaian.Terbaik;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibnu.dbestokasir.Penilaian.PenilaianFragment;
import com.ibnu.dbestokasir.R;

import java.util.ArrayList;

public class InputNama extends Fragment {
    private EditText et_nama, et_bln;
    private RecyclerViewDataTerbaik adapter;
    private Button btn_simpan;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<Model> models;
    private DatabaseReference databaseReference;
    String url = "https://dbesto-default-rtdb.firebaseio.com/";


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.input_nama, container, false);
        models =new ArrayList<>();
        et_nama = rootView.findViewById(R.id.txnama);
        et_bln = rootView.findViewById(R.id.txbulan);
        btn_simpan = rootView.findViewById(R.id.btnsubmit);
        databaseReference = FirebaseDatabase.getInstance(url).getReference();

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString("key",et_nama.getText().toString());
//                PenilaianFragment fragment = new PenilaianFragment();
//                fragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.input_nama, fragment).commit();
                if (et_nama.getText().toString().isEmpty())
                {
                    et_nama.setError("Nama harus diisi");
                }
                if (et_bln.getText().toString().isEmpty()){
                    et_bln.setError("Bulan harus diisi");
                }
                if(!et_bln.getText().toString().isEmpty() && !et_nama.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), et_nama.getText().toString()+et_bln.getText().toString(), Toast.LENGTH_SHORT).show();
                    inputData(new Model(et_nama.getText().toString(), et_bln.getText().toString()));
                    Terbaik.setInput(et_nama.getText().toString());
                }
            }
        });

        if(adapter != null){
            models.clear();
            adapter.notifyDataSetChanged();
            initData();
        }else {
            initData();
        }

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("reload"));
        return rootView;
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    private void initData(){
        databaseReference.child("Terbaik").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    Model model = noteDataSnapshot.getValue(Model.class);
                    model.setKey(noteDataSnapshot.getKey());

                    models.add(model);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                /**
                 * Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint error nya
                 * ke LogCat
                 */
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }

//    private void setRvAdapter(ArrayList<Model> models) {
//        adapter = new RecyclerViewDataTerbaik(models,getActivity());
//        layoutManager = new GridLayoutManager(getActivity(),1);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//    }

    private void inputData(Model model){
        databaseReference.child("Terbaik").push().setValue(model).addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
