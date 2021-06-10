package kaisar_pajar_oktavianus_entiman.tugasahir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import kaisar_pajar_oktavianus_entiman.tugasahir.adapter.ChartAdapter;
import kaisar_pajar_oktavianus_entiman.tugasahir.adapter.NotaAdapter;
import kaisar_pajar_oktavianus_entiman.tugasahir.eventbus.UpdateCartEvent;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.CartLoadListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.CartModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.MenuModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.NomorMeja;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.NotaModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.PembayaranModel;

public class CartActivity extends AppCompatActivity implements CartLoadListener {

    @BindView(R.id.recycler_car)
    RecyclerView recyclerView;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    FloatingActionButton btnBayarlangsung, btnDanaPayment;

    Stringaddress stringaddress;
    String nomormeja = NomorMeja.getNomormeja();
    Uri dana_ = Uri.parse("https://link.dana.id/qr/l3g1ke8");
    String Dana = "https://link.dana.id/qr/64i1lbz";

    CartLoadListener cartLoadListener;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(UpdateCartEvent event) {
        loadCartDariFirebase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btnBayarlangsung = findViewById(R.id.btnBayarlangsung);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_payment_24);
        Drawable drawablWhite = drawable.getConstantState().newDrawable();
        drawablWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        btnBayarlangsung.setImageDrawable(drawablWhite);


        btnBayarlangsung.setOnClickListener(view -> {
            final BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(this);
            bottomSheetDialog2.setContentView(R.layout.bottom_sheet_dialog);

            bottomSheetDialog2.setCanceledOnTouchOutside(false);

            LinearLayout bottom_sheet_process = bottomSheetDialog2.findViewById(R.id.bottom_sheet_process);
            CardView btn_BayaraLangsung = bottomSheetDialog2.findViewById(R.id.btn_Bayarlangsung);
            CardView btn_Dana = bottomSheetDialog2.findViewById(R.id.btn_Online);
            CardView btn_cancel = bottomSheetDialog2.findViewById(R.id.btn_cancel2);
            bottom_sheet_process.setVisibility(View.VISIBLE);

            btn_BayaraLangsung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
////                        fungsi yes pada bottom sheet
                    bayarLangsung();

                    bottomSheetDialog2.hide();

                    final BottomSheetDialog bottomSheetDialogNota = new BottomSheetDialog(CartActivity.this);
                    bottomSheetDialogNota.setContentView(R.layout.nota);
                    bottomSheetDialogNota.setCanceledOnTouchOutside(false);

                    RecyclerView nota = bottomSheetDialogNota.findViewById(R.id.recyclerNota);
                    TextView totalBayarnota = bottomSheetDialogNota.findViewById(R.id.totalBayarNota);

                    RecyclerView.LayoutManager mManager;
                    mManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
                    nota.setLayoutManager(mManager);

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
                                        }

                                        double sum = 0;
                                        for (NotaModel notaModel : notaModels) {
                                            sum += notaModel.getTotalPrice();
                                        }
                                        totalBayarnota.setText(new StringBuilder("Total bayar = Rp").append(sum) + "00");

//                            mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//                            recyclerView.setLayoutManager(mManager);
                                        NotaAdapter notaAdapter = new NotaAdapter(getApplicationContext(), notaModels);
                                        nota.setAdapter(notaAdapter);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                    cartLoadListener.onCartLoadFailed(error.getMessage());
                                }
                            });
                    bottomSheetDialogNota.show();
                }
            });
            btn_Dana.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        fungsi yes pada bottom sheet
//                    Intent i = new Intent(Intent.ACTION_VIEW, dana_);
                    Toast.makeText(CartActivity.this, "DANA", Toast.LENGTH_SHORT).show();
//                    startActivity(i);
                    bottomSheetDialog2.hide();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        fungsi cancel bottom sheet
                    bottomSheetDialog2.hide();
//                    tx_numberOfRuns.setText("1");
                }
            });
            bottomSheetDialog2.show();
    });

    init();

    loadCartDariFirebase();

}

    void bayarLangsung() {
        // tarik table cart
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.
                getInstance(stringaddress.firebaseDbesto).
                getReference("cart").child(nomormeja).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                        cartModel.setKey(dataSnapshot.getKey());
                        cartModels.add(cartModel);

                        //masukan data ke table pembayaran
                        DatabaseReference useCart = FirebaseDatabase.
                                getInstance(stringaddress.firebaseDbesto).
                                getReference("pembayaran").child(nomormeja);

                        useCart.child(cartModel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                {
                                    PembayaranModel pembayaranModel = new PembayaranModel();
                                    pembayaranModel.setNamaMakanan(cartModel.getNamaMakanan());
                                    pembayaranModel.setHarga(cartModel.getHarga());
                                    pembayaranModel.setKey(cartModel.getKey());
                                    pembayaranModel.setGambar(cartModel.getGambar());
                                    pembayaranModel.setTotalPrice((cartModel.getTotalPrice() * 1000));
                                    pembayaranModel.setQuantity(cartModel.getQuantity());

                                    useCart.child(cartModel.getKey())
                                            .setValue(pembayaranModel);

                                    //updatestok
                                    List<MenuModel> menuModels = new ArrayList<>();
                                    FirebaseDatabase.getInstance(stringaddress.firebaseDbesto)
                                            .getReference("menuMakanan")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        for (DataSnapshot menuSnapshot : snapshot.getChildren()) {
                                                            MenuModel menuModel = menuSnapshot.getValue(MenuModel.class);
                                                            menuModel.setKey(menuSnapshot.getKey());
                                                            menuModels.add(menuModel);

                                                            DatabaseReference stok = FirebaseDatabase.
                                                                    getInstance(stringaddress.firebaseDbesto).
                                                                    getReference("menuMakanan");

                                                            stok.child(pembayaranModel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                    if (snapshot.exists()) //jika sudah ada item di cart
                                                                    {
                                                                        MenuModel menuModel1 = snapshot.getValue(MenuModel.class);
                                                                        Map<String, Object> updateData = new HashMap<>();
                                                                        updateData.put("stok", menuModel1.getStok() - pembayaranModel.getQuantity());

                                                                        stok.child(pembayaranModel.getKey()).updateChildren(updateData);
                                                                    }
                                                                    EventBus.getDefault().postSticky(new UpdateCartEvent());
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                                    cartLoadListener.onCartLoadFailed(error.getMessage());
                                                                }
                                                            });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                }
                                            });
                                }
                                EventBus.getDefault().postSticky(new UpdateCartEvent());
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                cartLoadListener.onCartLoadFailed(error.getMessage());
                            }
                        });

                    }
//                        cartLoadListener.onCartLoadSuccess(cartModels);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                cartLoadListener.onCartLoadFailed(error.getMessage());
            }
        });
    }

    private void loadCartDariFirebase() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance(stringaddress.firebaseDbesto)
                .getReference("cart").child(nomormeja)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                        cartModel.setKey(dataSnapshot.getKey());
                        cartModels.add(cartModel);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    cartLoadListener.onCartLoadSuccess(cartModels);
                }
                if (!snapshot.exists()){
                    recyclerView.setVisibility(View.INVISIBLE);
                    recyclerView.removeAllViewsInLayout();
                    txtTotal.setText("Total");
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
    }

    private void init() {
        ButterKnife.bind(this);

        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        double sum = 0;
        for (CartModel cartModel : cartModelList) {
            sum += cartModel.getTotalPrice();
        }
        txtTotal.setText(new StringBuilder("Rp").append(sum) + "00");
        ChartAdapter adapter = new ChartAdapter(this, cartModelList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }
}