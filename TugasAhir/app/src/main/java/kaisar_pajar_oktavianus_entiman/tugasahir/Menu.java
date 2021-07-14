package kaisar_pajar_oktavianus_entiman.tugasahir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.intellij.lang.annotations.JdkConstants;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kaisar_pajar_oktavianus_entiman.tugasahir.adapter.MenuAdapter;
import kaisar_pajar_oktavianus_entiman.tugasahir.eventbus.UpdateCartEvent;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.CartLoadListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.MenuLoadListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.CartModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.MenuModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.NomorMeja;
import kaisar_pajar_oktavianus_entiman.tugasahir.utils.SpaceItemDecoration;

public class Menu extends AppCompatActivity implements MenuLoadListener, CartLoadListener {

    @BindView(R.id.recyclerviewFried)
    RecyclerView recyclerviewFried;
    @BindView(R.id.recyclerviewPaket)
    RecyclerView recyclerviewPaket;
    @BindView(R.id.recyclerviewBurger)
    RecyclerView recyclerviewBurger;
    @BindView(R.id.recyclerviewCemilan)
    RecyclerView recyclerviewCemilan;
    @BindView(R.id.recyclerviewMinuman)
    RecyclerView recyclerviewMinuman;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.btnBack)
    ImageView btnBack;

    RelativeLayout coverBadge;

    TextView textView, badge;
    Button btnFriedChicken, btnPaket, btnBurger, btnCemilan, btnMinuman;

    MenuLoadListener menuLoadListener;
    CartLoadListener cartLoadListener;

    Stringaddress stringaddress;
    String nomormeja = NomorMeja.getNomormeja();

    int b = 0;
    int cartSum = 0;

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
        counCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        coverBadge = findViewById(R.id.coverBadge);
        badge = findViewById(R.id.badge);
        badge.setVisibility(View.GONE);
        coverBadge.setVisibility(View.GONE);

        init();
        counCartItem();

        checkInternet();

        btnFriedChicken = findViewById(R.id.btnFriedChicken);
        btnPaket = findViewById(R.id.btnPaket);
        btnBurger = findViewById(R.id.btnBurger);
        btnCemilan = findViewById(R.id.btnCemilan);
        btnMinuman = findViewById(R.id.btnMinuman);
        textView = findViewById(R.id.txtNomormeja);
        textView.setText(NomorMeja.getNamacabangsel() + "\nMeja " + NomorMeja.getNomormeja());

        btnFriedChicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerviewFried.setVisibility(View.VISIBLE);
                recyclerviewPaket.setVisibility(View.GONE);
                recyclerviewBurger.setVisibility(View.GONE);
                recyclerviewCemilan.setVisibility(View.GONE);
                recyclerviewMinuman.setVisibility(View.GONE);
            }
        });
        btnPaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerviewPaket.setVisibility(View.VISIBLE);
                recyclerviewFried.setVisibility(View.GONE);
                recyclerviewBurger.setVisibility(View.GONE);
                recyclerviewCemilan.setVisibility(View.GONE);
                recyclerviewMinuman.setVisibility(View.GONE);
            }
        });
        btnBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerviewBurger.setVisibility(View.VISIBLE);
                recyclerviewFried.setVisibility(View.GONE);
                recyclerviewPaket.setVisibility(View.GONE);
                recyclerviewCemilan.setVisibility(View.GONE);
                recyclerviewMinuman.setVisibility(View.GONE);
            }
        });
        btnCemilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerviewCemilan.setVisibility(View.VISIBLE);
                recyclerviewFried.setVisibility(View.GONE);
                recyclerviewBurger.setVisibility(View.GONE);
                recyclerviewPaket.setVisibility(View.GONE);
                recyclerviewMinuman.setVisibility(View.GONE);
            }
        });
        btnMinuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerviewMinuman.setVisibility(View.VISIBLE);
                recyclerviewFried.setVisibility(View.GONE);
                recyclerviewBurger.setVisibility(View.GONE);
                recyclerviewCemilan.setVisibility(View.GONE);
                recyclerviewPaket.setVisibility(View.GONE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kembali();
            }
        });
    }

    private void LoadMenuDariFirebase() {

        List<MenuModel> menuModels = new ArrayList<>();
        FirebaseDatabase.getInstance(NomorMeja.getNamacabang())
                .getReference("menu").child("friedChicken")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot menuSnapshot : snapshot.getChildren()) {
                                MenuModel menuModel = menuSnapshot.getValue(MenuModel.class);
                                menuModel.setKey(menuSnapshot.getKey());
                                menuModels.add(menuModel);
                            }
                            menuLoadListener.onMenuLoadSuccess(menuModels);
                        } else {
                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        menuLoadListener.onMenuLoadFailed(error.getMessage());
                    }
                });

        List<MenuModel> menuModels2 = new ArrayList<>();
        FirebaseDatabase.getInstance(NomorMeja.getNamacabang())
                .getReference("menu").child("Burger")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot menuSnapshot : snapshot.getChildren()) {
                                MenuModel menuModel = menuSnapshot.getValue(MenuModel.class);
                                menuModel.setKey(menuSnapshot.getKey());
                                menuModels2.add(menuModel);
                            }
                            menuLoadListener.onMenuLoadSuccessBurger(menuModels2);
                        } else {
                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        menuLoadListener.onMenuLoadFailed(error.getMessage());
                    }
                });

        List<MenuModel> menuModels3 = new ArrayList<>();
        FirebaseDatabase.getInstance(NomorMeja.getNamacabang())
                .getReference("menu").child("Cemilan")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot menuSnapshot : snapshot.getChildren()) {
                                MenuModel menuModel = menuSnapshot.getValue(MenuModel.class);
                                menuModel.setKey(menuSnapshot.getKey());
                                menuModels3.add(menuModel);
                            }
                            menuLoadListener.onMenuLoadSuccessCemilan(menuModels3);
                        } else {
                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        menuLoadListener.onMenuLoadFailed(error.getMessage());
                    }
                });

        List<MenuModel> menuModels4 = new ArrayList<>();
        FirebaseDatabase.getInstance(NomorMeja.getNamacabang())
                .getReference("menu").child("Minuman")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot menuSnapshot : snapshot.getChildren()) {
                                MenuModel menuModel = menuSnapshot.getValue(MenuModel.class);
                                menuModel.setKey(menuSnapshot.getKey());
                                menuModels4.add(menuModel);
                            }
                            menuLoadListener.onMenuLoadSuccessMinuman(menuModels4);
                        } else {
                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        menuLoadListener.onMenuLoadFailed(error.getMessage());
                    }
                });

        List<MenuModel> menuModels5 = new ArrayList<>();
        FirebaseDatabase.getInstance(NomorMeja.getNamacabang())
                .getReference("menu").child("Paket")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot menuSnapshot : snapshot.getChildren()) {
                                MenuModel menuModel = menuSnapshot.getValue(MenuModel.class);
                                menuModel.setKey(menuSnapshot.getKey());
                                menuModels5.add(menuModel);
                            }
                            menuLoadListener.onMenuLoadSuccessPaket(menuModels5);
                        } else {
                            menuLoadListener.onMenuLoadFailed("Kesalahan jaringan");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        menuLoadListener.onMenuLoadFailed(error.getMessage());
                    }
                });
    }

    void checkInternet() {
        if (isNetworkConnected())
            LoadMenuDariFirebase();
        if (!isNetworkConnected())
            Toast.makeText(this, "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void init() {
        ButterKnife.bind(this);

        menuLoadListener = this;
        cartLoadListener = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerviewFried.setLayoutManager(gridLayoutManager);
        recyclerviewFried.addItemDecoration(new SpaceItemDecoration());

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 2);
        recyclerviewPaket.setLayoutManager(gridLayoutManager2);
        recyclerviewPaket.addItemDecoration(new SpaceItemDecoration());

        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(this, 2);
        recyclerviewBurger.setLayoutManager(gridLayoutManager3);
        recyclerviewBurger.addItemDecoration(new SpaceItemDecoration());

        GridLayoutManager gridLayoutManager4 = new GridLayoutManager(this, 2);
        recyclerviewCemilan.setLayoutManager(gridLayoutManager4);
        recyclerviewCemilan.addItemDecoration(new SpaceItemDecoration());

        GridLayoutManager gridLayoutManager5 = new GridLayoutManager(this, 2);
        recyclerviewMinuman.setLayoutManager(gridLayoutManager5);
        recyclerviewMinuman.addItemDecoration(new SpaceItemDecoration());


        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

    }

    @Override
    public void onMenuLoadSuccessBurger(List<MenuModel> menuModelList) {
        MenuAdapter menuAdapter = new MenuAdapter(this, menuModelList, cartLoadListener);
        recyclerviewBurger.setAdapter(menuAdapter);
    }

    @Override
    public void onMenuLoadSuccessPaket(List<MenuModel> menuModelList) {
        MenuAdapter menuAdapter = new MenuAdapter(this, menuModelList, cartLoadListener);
        recyclerviewPaket.setAdapter(menuAdapter);
    }

    @Override
    public void onMenuLoadSuccessCemilan(List<MenuModel> menuModelList) {
        MenuAdapter menuAdapter = new MenuAdapter(this, menuModelList, cartLoadListener);
        recyclerviewCemilan.setAdapter(menuAdapter);
    }

    @Override
    public void onMenuLoadSuccessMinuman(List<MenuModel> menuModelList) {
        MenuAdapter menuAdapter = new MenuAdapter(this, menuModelList, cartLoadListener);
        recyclerviewMinuman.setAdapter(menuAdapter);

    }

    @Override
    public void onMenuLoadSuccess(List<MenuModel> menuModelList) {
        MenuAdapter menuAdapter = new MenuAdapter(this, menuModelList, cartLoadListener);
        recyclerviewFried.setAdapter(menuAdapter);
    }

    @Override
    public void onMenuLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
//        int a = 0;
//
//            for (CartModel cartModel : cartModelList) {
//                a += cartModel.getQuantity();
//            }
//        if (a > 0){
//            coverBadge.setVisibility(View.VISIBLE);
//            badge.setVisibility(View.VISIBLE);
//            badge.setText(String.valueOf(a));
//        }
//        if (a <= 0){
//            coverBadge.setVisibility(View.GONE);
//            badge.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
        counCartItem();
    }

    private void counCartItem() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.
                getInstance(NomorMeja.getNamacabang()).
                getReference("cart").child(nomormeja)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                            CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                            cartModel.setKey(cartSnapshot.getKey());
                            cartModels.add(cartModel);
                        }
//                        cartLoadListener.onCartLoadSuccess(cartModels);
                        int a = 0;

                        for (CartModel cartModel : cartModels) {
                            a += cartModel.getQuantity();
                        }
                        if (a > 0) {
                            coverBadge.setVisibility(View.VISIBLE);
                            badge.setVisibility(View.VISIBLE);
                            badge.setText(String.valueOf(a));
                        }
                        if (a <= 0) {
                            coverBadge.setVisibility(View.GONE);
                            badge.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        kembali();
    }

    void kembali() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
        //settittle
        builder.setTitle("Allert");
        //setmessage
        builder.setMessage("Ingin keluar dari halaman menu?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Menu.this, HalamanScan.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}