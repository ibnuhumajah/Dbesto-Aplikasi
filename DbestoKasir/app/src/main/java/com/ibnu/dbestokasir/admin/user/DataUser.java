package com.ibnu.dbestokasir.admin.user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.admin.RegisterActivity;

import java.util.ArrayList;
import java.util.List;


public class DataUser extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    String url = "https://dbesto-default-rtdb.firebaseio.com/";
    private RecyclerView recyclerView;
    private RecyclerViewDataUser adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Model> models;
    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    private Dialog myDialog, myDialogAdd;
    private EditText tv_nama, tv_nbi;
    private Button btn_simpan;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_data_user, container,false);
        models =new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recyclerview_data_user);

        setHasOptionsMenu(true);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_700);


        databaseReference = FirebaseDatabase.getInstance(url).getReference();

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
            onRefresh();
        }
    };

    private void initData() {
        mSwipeRefreshLayout.setRefreshing(true);
        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    Model model = noteDataSnapshot.getValue(Model.class);
                    model.setKey(noteDataSnapshot.getKey());

                    models.add(model);
                }


                setRvAdapter(models);
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /**
                 * Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint error nya
                 * ke LogCat
                 */
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setRvAdapter(ArrayList<Model> models) {
        adapter = new RecyclerViewDataUser(models,getActivity());
        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void myTambahButton(){
        myDialogAdd = new Dialog(getActivity());
        myDialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialogAdd.setContentView(R.layout.popup_tambah_user);

        tv_nama = myDialogAdd.findViewById(R.id.nama);
        tv_nbi = myDialogAdd.findViewById(R.id.nik_user);
        btn_simpan = myDialogAdd.findViewById(R.id.btnSimpan);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_nama.getText().toString().equals("")){
                    tv_nama.setError("Nama harus diisi");
                }else if (tv_nbi.getText().toString().equals("")){
                    tv_nbi.setError("NIK harus diisi");
                }else {
                    inputData(new Model(tv_nama.getText().toString(), tv_nbi.getText().toString()),myDialogAdd);
                }
            }
        });

        myDialogAdd.setCancelable(true);
        myDialogAdd.show();
        Window window = myDialogAdd.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
    }

    private void inputData(Model model, final Dialog myDialogAdd){
        databaseReference.child("User").push().setValue(model).addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myDialogAdd.dismiss();
                Toast.makeText(getActivity(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_admin,menu);
        inflater.inflate(R.menu.tambah_menu, menu);
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView  = new SearchView(getActivity());

        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.search_hint) + "</font>"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                nextText = nextText.toLowerCase();
                List<Model> dataFilter = new ArrayList<>();
                for(Model data : models){
                    String nama = data.getNama().toLowerCase();
                    String nik = data.getNik().toLowerCase();
                    if(nama.contains(nextText) || nik.contains(nextText)){
                        dataFilter.add(data);
                    }
                }
                if(adapter != null){
                    adapter.setFilter(dataFilter);
                }else {
                    return false;
                }
                return true;

            }
        });
        searchItem.setActionView(searchView);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {

            case R.id.action_tambah:
                myTambahButton();
                return true;
            case R.id.register:
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        if(adapter != null){
            models.clear();
            adapter.notifyDataSetChanged();
            initData();
        }else {
            initData();
        }
    }
}