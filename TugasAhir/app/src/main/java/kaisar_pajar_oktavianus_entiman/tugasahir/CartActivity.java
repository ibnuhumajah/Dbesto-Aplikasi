package kaisar_pajar_oktavianus_entiman.tugasahir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import kaisar_pajar_oktavianus_entiman.tugasahir.adapter.ChartAdapter;
import kaisar_pajar_oktavianus_entiman.tugasahir.adapter.NotaAdapter;
import kaisar_pajar_oktavianus_entiman.tugasahir.eventbus.UpdateCartEvent;
import kaisar_pajar_oktavianus_entiman.tugasahir.eventbus.UpdateCartEventonCart;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.CartLoadListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.CartModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.KategoriModel;
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
    FloatingActionButton btnBayar, btnDanaPayment;

    double a = 0;
    int number = 55;

    Stringaddress stringaddress;
    String nomormeja = NomorMeja.getNomormeja();
    Uri dana_ = Uri.parse("https://link.dana.id/qr/l3g1ke8");
    String Dana = "https://link.dana.id/qr/64i1lbz";

    CartLoadListener cartLoadListener;
    boolean sudahjalan = false;
    int stokkurangbeli = 0;

    WebView webView;
    String urllangsung = "http://dbesto.epizy.com/index.php";
    String urldana = "http://dbesto.epizy.com/dana.php";

    private static final String TAG = "PushNotificationa";
    private static final String CHANNEL_ID = "102";

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(UpdateCartEventonCart.class))
            EventBus.getDefault().removeStickyEvent(UpdateCartEventonCart.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(UpdateCartEventonCart event) {
        loadCartDariFirebase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btnBayar = findViewById(R.id.btnBayarlangsung);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_payment_24);
        Drawable drawablWhite = drawable.getConstantState().newDrawable();
        drawablWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        btnBayar.setImageDrawable(drawablWhite);
        webView = findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        btnBayar.setOnClickListener(view -> {
//            validasiBayar();
            fungsiButton();

        });

        init();

        loadCartDariFirebase();

    }

    void printPDF() {
        List<NotaModel> notaModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance(NomorMeja.getNamacabang())
                .getReference("cart").child(NomorMeja.getNomormeja())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @com.google.firebase.database.annotations.NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                NotaModel notaModel = dataSnapshot.getValue(NotaModel.class);
                                notaModel.setKey(dataSnapshot.getKey());
                                notaModels.add(notaModel);
                            }
//                            for (NotaModel notaModel : notaModels){
                            PdfDocument pdfDocument = new PdfDocument();
                            Paint paint = new Paint();
                            Paint linepaint = new Paint();
                            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(250, 350, 1).create();
                            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                            Canvas canvas = page.getCanvas();

                            paint.setTextSize(15.5f);
                            paint.setColor(Color.rgb(0, 50, 250));

                            canvas.drawText("D,Besto " + NomorMeja.getNamacabangsel(), 20, 20, paint);
                            paint.setTextSize(8.5f);
                            linepaint.setStyle(Paint.Style.STROKE);
                            linepaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
                            linepaint.setStrokeWidth(2);
                            canvas.drawLine(20, 40, 230, 40, linepaint);

                            canvas.drawText("Pembelian: ", 20, 55, paint);

//                                a += notaModel.getTotalPrice();
                            for (int i = 0; i < notaModels.size(); i++) {
                                Log.e("total harga: ", String.valueOf(notaModels.get(i).getTotalPrice()));
                                a += notaModels.get(i).getTotalPrice();
                                number += 20;
                                canvas.drawText(notaModels.get(i).getNama(), 20, number, paint);
                                canvas.drawText("Rp" + notaModels.get(i).getHarga(), 120, number, paint);
                                canvas.drawText("x" + String.valueOf(notaModels.get(i).getQuantity()), 200, number, paint);
                            }
                            canvas.drawLine(20, number + 20, 230, number + 20, linepaint);
                            canvas.drawText("Total", 120, number + 40, paint);
                            paint.setTextAlign(Paint.Align.RIGHT);
                            canvas.drawText("Rp" + a + "00", 230, number + 40, paint);

                            pdfDocument.finishPage(page);
                            File file = new File(CartActivity.this.getExternalFilesDir("/"), "d'besto.pdf");
                            try {
                                pdfDocument.writeTo(new FileOutputStream(file));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            pdfDocument.close();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @com.google.firebase.database.annotations.NotNull DatabaseError error) {
                    }
                });
    }

    void validasiBayar() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance(NomorMeja.getNamacabang())
                .getReference("cart").child(nomormeja)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                                cartModel.setKey(dataSnapshot.getKey());
                                cartModels.add(cartModel);


                            }
                        }
                        if (!snapshot.exists()) {
                            cartLoadListener.onCartLoadFailed("Pesanan belum ada ...");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    void bayarLangsung() {
        //tarik cart
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance(NomorMeja.getNamacabang())
                .getReference("cart").child(nomormeja)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                                cartModel.setKey(dataSnapshot.getKey());
                                cartModels.add(cartModel);


                                //update stok
                                List<MenuModel> menuModels = new ArrayList<>();
                                FirebaseDatabase
                                        .getInstance(NomorMeja.getNamacabang())
                                        .getReference("menu")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        KategoriModel kategoriModel = new KategoriModel();
                                                        kategoriModel.setKategori(dataSnapshot.getKey());
                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                            MenuModel menuModel = dataSnapshot1.getValue(MenuModel.class);
                                                            menuModel.setKey(dataSnapshot1.getKey());
                                                            menuModels.add(menuModel);

                                                            DatabaseReference stok = FirebaseDatabase.
                                                                    getInstance(NomorMeja.getNamacabang()).
                                                                    getReference("menu");

                                                            stok.child(kategoriModel.getKategori()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                    for (DataSnapshot dataSnapshot2 : snapshot.getChildren()) {
                                                                        if (dataSnapshot2.getKey().equals(cartModel.getKey())) {
                                                                            Map<String, Object> updateData = new HashMap<>();
                                                                            updateData.put("stok", cartModel.getStok() - cartModel.getQuantity());
                                                                            stok.child(kategoriModel.getKategori()).child(cartModel.getKey()).updateChildren(updateData);

                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                                    cartLoadListener.onCartLoadFailed(error.getMessage());
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                cartLoadListener.onCartLoadFailed(error.getMessage());
                                            }
                                        });


                                //buat tabel pembayaran
                                DatabaseReference pembayaran = FirebaseDatabase.
                                        getInstance(NomorMeja.getNamacabang()).
                                        getReference("pembayaran").child(nomormeja);

                                pembayaran.child(cartModel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        {
                                            PembayaranModel pembayaranModel = new PembayaranModel();
                                            pembayaranModel.setKey(cartModel.getKey());
                                            pembayaranModel.setGambar(cartModel.getGambar());
                                            pembayaranModel.setNama(cartModel.getNama());
                                            pembayaranModel.setHarga(cartModel.getHarga());
                                            pembayaranModel.setQuantity(cartModel.getQuantity());
                                            pembayaranModel.setTotalPrice((cartModel.getTotalPrice() * 1000));

                                            pembayaran.child(cartModel.getKey())
                                                    .setValue(pembayaranModel)
                                                    .addOnSuccessListener(aVoid -> cartLoadListener.onCartLoadFailed("Pesanan anda diterima oleh kasir"))
                                                    .addOnFailureListener(e -> cartLoadListener.onCartLoadFailed(e.getMessage()));


                                            //hapus cart
                                            FirebaseDatabase.
                                                    getInstance(NomorMeja.getNamacabang())
                                                    .getReference("cart").child(nomormeja)
                                                    .removeValue().addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new UpdateCartEventonCart()));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        cartLoadListener.onCartLoadFailed(error.getMessage());
                                    }
                                });
                            }
                        }
//                        if (!snapshot.exists()) {
//                            Toast.makeText(CartActivity.this, "data cart kosong", Toast.LENGTH_SHORT).show();
//                        }
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
                .getInstance(NomorMeja.getNamacabang())
                .getReference("cart").child(nomormeja)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                                cartModel.setKey(dataSnapshot.getKey());
                                cartModels.add(cartModel);
                            }
                            recyclerView.setVisibility(View.VISIBLE);
                            cartLoadListener.onCartLoadSuccess(cartModels);
                        }
                        if (!snapshot.exists()) {
                            recyclerView.setVisibility(View.INVISIBLE);
                            txtTotal.setText("Total");
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

    @Override
    protected void onResume() {
        super.onResume();
        loadCartDariFirebase();
    }

    void fungsiButton() {

        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance(NomorMeja.getNamacabang())
                .getReference("cart").child(nomormeja)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                                cartModel.setKey(dataSnapshot.getKey());
                                cartModels.add(cartModel);


                            }
                            final BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(CartActivity.this);
                            bottomSheetDialog2.setContentView(R.layout.bottom_sheet_dialog);

                            bottomSheetDialog2.setCanceledOnTouchOutside(false);
                            bottomSheetDialog2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    sudahjalan = false;
                                }
                            });

                            LinearLayout bottom_sheet_process = bottomSheetDialog2.findViewById(R.id.bottom_sheet_process);
                            LinearLayout bottom_sheet_validasi = bottomSheetDialog2.findViewById(R.id.bottom_sheet_validasi);
                            CardView btn_BayaraLangsung = bottomSheetDialog2.findViewById(R.id.btn_Bayarlangsung);
                            CardView btn_Dana = bottomSheetDialog2.findViewById(R.id.btn_Online);
                            CardView btn_cancel = bottomSheetDialog2.findViewById(R.id.btn_cancel2);
                            CardView btn_Bayar = bottomSheetDialog2.findViewById(R.id.btn_Ya);
                            WebView webViewd = btn_Bayar.findViewById(R.id.bdana);
                            webViewd.getSettings().setJavaScriptEnabled(true);
                            webViewd.setWebViewClient(new WebViewClient());
                            WebView webViewl = btn_Bayar.findViewById(R.id.blangsung);
                            webViewl.getSettings().setJavaScriptEnabled(true);
                            webViewl.setWebViewClient(new WebViewClient());
                            CardView btn_cancelBayar = bottomSheetDialog2.findViewById(R.id.btn_cancelBayar);
                            bottom_sheet_process.setVisibility(View.VISIBLE);

                            btn_BayaraLangsung.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    bottom_sheet_process.setVisibility(View.GONE);
                                    bottom_sheet_validasi.setVisibility(View.VISIBLE);

                                    btn_Bayar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            webViewl.loadUrl(urllangsung);

                                            printPDF();

                                            bayarLangsung();

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
                                                    .getInstance(NomorMeja.getNamacabang())
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

                                                                NotaAdapter notaAdapter = new NotaAdapter(getApplicationContext(), notaModels);
                                                                nota.setAdapter(notaAdapter);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                            cartLoadListener.onCartLoadFailed(error.getMessage());
                                                        }
                                                    });
                                            bottomSheetDialog2.hide();
                                            bottomSheetDialogNota.show();
                                        }
                                    });

                                    btn_cancelBayar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bottom_sheet_validasi.setVisibility(View.GONE);
                                            bottom_sheet_process.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            });
                            btn_Dana.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    bottom_sheet_process.setVisibility(View.GONE);
                                    bottom_sheet_validasi.setVisibility(View.VISIBLE);

                                    btn_Bayar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            webViewd.loadUrl(urldana);

                                            printPDF();

                                            bayarLangsung();

                                            final BottomSheetDialog bottomSheetDialogNota = new BottomSheetDialog(CartActivity.this);
                                            bottomSheetDialogNota.setContentView(R.layout.nota);
                                            bottomSheetDialogNota.setCanceledOnTouchOutside(false);

                                            LinearLayout notadana = bottomSheetDialogNota.findViewById(R.id.notaDana);
                                            LinearLayout notalangsung = bottomSheetDialogNota.findViewById(R.id.notaLangsung);
                                            RecyclerView nota = bottomSheetDialogNota.findViewById(R.id.recyclerNotaDana);
                                            TextView totalBayarnota = bottomSheetDialogNota.findViewById(R.id.totalBayarNotaDana);
                                            CardView cardDana = bottomSheetDialogNota.findViewById(R.id.bayarDana);

                                            notadana.setVisibility(View.VISIBLE);
                                            notalangsung.setVisibility(View.GONE);

                                            RecyclerView.LayoutManager mManager;
                                            mManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
                                            nota.setLayoutManager(mManager);

                                            List<NotaModel> notaModels = new ArrayList<>();
                                            FirebaseDatabase
                                                    .getInstance(NomorMeja.getNamacabang())
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
                                                                cardDana.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        Intent i = new Intent(Intent.ACTION_VIEW, dana_);
                                                                        startActivity(i);
//                                                                            Toast.makeText(CartActivity.this, "DANA", Toast.LENGTH_SHORT).show();
                                                                        bottomSheetDialog2.hide();
                                                                    }
                                                                });

                                                                NotaAdapter notaAdapter = new NotaAdapter(getApplicationContext(), notaModels);
                                                                nota.setAdapter(notaAdapter);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                            cartLoadListener.onCartLoadFailed(error.getMessage());
                                                        }
                                                    });
                                            bottomSheetDialog2.hide();
                                            bottomSheetDialogNota.show();
                                        }
                                    });

                                    btn_cancelBayar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bottom_sheet_validasi.setVisibility(View.GONE);
                                            bottom_sheet_process.setVisibility(View.VISIBLE);
                                        }
                                    });

//
//
                                }
                            });

                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                        fungsi cancel bottom sheet
                                    bottomSheetDialog2.hide();
                                    sudahjalan = false;
//                    tx_numberOfRuns.setText("1");
                                }
                            });

                            bottomSheetDialog2.show();
                        }
                        if (!snapshot.exists()) {
                            cartLoadListener.onCartLoadFailed("Pesanan belum ada ...");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }
}