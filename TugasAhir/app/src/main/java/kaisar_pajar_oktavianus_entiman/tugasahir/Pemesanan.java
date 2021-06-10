package kaisar_pajar_oktavianus_entiman.tugasahir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kaisar_pajar_oktavianus_entiman.tugasahir.adapter.MenuAdapter;
import kaisar_pajar_oktavianus_entiman.tugasahir.adapter.NotaAdapter;
import kaisar_pajar_oktavianus_entiman.tugasahir.adapter.PembayaranAdapter;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.CartLoadListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.MenuLoadListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.PembayaranLoadListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.CartModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.MenuModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.NomorMeja;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.NotaModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.PembayaranModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.utils.SpaceItemDecoration;

public class Pemesanan extends AppCompatActivity {

    RecyclerView recyclerView;

    Stringaddress stringaddress;

    PembayaranLoadListener pembayaranLoadListener;
    CartLoadListener cartLoadListener;

    String nomormeja = NomorMeja.getNomormeja();

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<NotaModel> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);
        final BottomSheetDialog bottomSheetDialogNota = new BottomSheetDialog(this);

        bottomSheetDialogNota.setContentView(R.layout.nota);

        bottomSheetDialogNota.setCanceledOnTouchOutside(false);

        RecyclerView nota = bottomSheetDialogNota.findViewById(R.id.recyclerNota);
        TextView totalBayarnota = bottomSheetDialogNota.findViewById(R.id.totalBayarNota);

//        recyclerView = findViewById(R.id.recyclerNota);

        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mManager;
        mItems = new ArrayList<>();
//        List<CartModel> mItems = new ArrayList<>();

        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        nota.setLayoutManager(mManager);
        mAdapter = new NotaAdapter(this, mItems);

        List<NotaModel> notaModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance(stringaddress.firebaseDbesto)
                .getReference("cart").child(nomormeja)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                NotaModel notaModel = dataSnapshot.getValue(NotaModel.class);
                                notaModel.setKey(dataSnapshot.getKey());
                                notaModels.add(notaModel);
                                Log.e("response", ""+mItems);
//                                recyclerView.setVisibility(View.VISIBLE);
                            }
//                            mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//                            recyclerView.setLayoutManager(mManager);
                            NotaAdapter notaAdapter = new NotaAdapter(getApplicationContext(), notaModels);
                            nota.setAdapter(notaAdapter);
//                            succes(notaModels);
//                            cartLoadListener.onCartLoadSuccess(cartModels);
                        }
                        if (!snapshot.exists()){
//                            recyclerView.setVisibility(View.INVISIBLE);
//                            recyclerView.removeAllViewsInLayout();
//                             txtTotal.setText("Total");
                        }
                        else {
//                    recyclerView.removeAllViewsInLayout();
//                    recyclerView.setVisibility(View.INVISIBLE);
//                    txtTotal.setText("Total");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });


//        recyclerView.setAdapter(mAdapter);

        bottomSheetDialogNota.show();
    }

    public void ambil(){

    }

    public void succes(List<NotaModel> notaModelList){
        NotaAdapter notaAdapter = new NotaAdapter(this, notaModelList);
        recyclerView.setAdapter(notaAdapter);
    }

}