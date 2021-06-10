package com.ibnu.dbestokasir.Penilaian.Terbaik;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibnu.dbestokasir.Penilaian.PenilaianFragment;
import com.ibnu.dbestokasir.Presensi.PresensiFragment;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.Penilaian.Terbaik.RecyclerViewDataTerbaik;
import com.ibnu.dbestokasir.admin.RegisterActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DataTerbaik extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private RecyclerViewDataTerbaik adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Model> models;
    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    private Dialog myDialog, myDialogAdd;
    private Fragment penilaian;
    PenilaianFragment penilaianFragment;
    private EditText tv_nama, tv_nbi;
    private TextView terbaik;
    private Button btn_simpan;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String url = "https://dbesto-default-rtdb.firebaseio.com/";

    public DataTerbaik(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_data_terbaik, container, false);
        models =new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recyclerview_data_terbaik);

        setHasOptionsMenu(true);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_700);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);

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

    private void initData(){
        mSwipeRefreshLayout.setRefreshing(true);
        databaseReference.child("Terbaik").addValueEventListener(new ValueEventListener() {
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
        adapter = new RecyclerViewDataTerbaik(models,getActivity());
        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
                    String nama = data.getNama().toUpperCase();
                    String nik = data.getBulan().toUpperCase();
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