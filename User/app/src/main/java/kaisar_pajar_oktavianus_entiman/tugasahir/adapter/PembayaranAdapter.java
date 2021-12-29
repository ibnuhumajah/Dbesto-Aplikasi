package kaisar_pajar_oktavianus_entiman.tugasahir.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kaisar_pajar_oktavianus_entiman.tugasahir.R;
import kaisar_pajar_oktavianus_entiman.tugasahir.Stringaddress;
import kaisar_pajar_oktavianus_entiman.tugasahir.eventbus.UpdateCartEvent;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.CartLoadListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.RecyclerViewListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.CartModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.MenuModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.NomorMeja;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.PembayaranModel;

public class PembayaranAdapter extends RecyclerView.Adapter<PembayaranAdapter.MyPembayaranViewHolder> {

    Context context;
    List<PembayaranModel> pembayaranModelList;
    CartLoadListener cartLoadListener;

    Stringaddress stringaddress;
    String nomormeja = NomorMeja.getNomormeja();

    public PembayaranAdapter(Context context, List<PembayaranModel> pembayaranModelList, CartLoadListener cartLoadListener) {
        this.context = context;
        this.pembayaranModelList = pembayaranModelList;
        this.cartLoadListener = cartLoadListener;
    }

    @NonNull
    @NotNull
    @Override
    public PembayaranAdapter.MyPembayaranViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new PembayaranAdapter.MyPembayaranViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pemesanan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PembayaranAdapter.MyPembayaranViewHolder holder, int position) {
        Glide.with(context).load(pembayaranModelList.get(position).getGambar()).into(holder.imgPemesanan);
        holder.txtNama.setText(new StringBuilder().append(pembayaranModelList.get(position).getNama()));
        holder.txtHarga.setText(new StringBuilder().append(pembayaranModelList.get(position).getHarga()));
        holder.txtQuantity.setText(new StringBuilder().append(pembayaranModelList.get(position).getQuantity()));
        holder.totalHarga.setText(new StringBuilder("Rp").append(pembayaranModelList.get(position).getTotalPrice()));

//        holder.setListener((view, adapterPosition) -> {
//            addToCart(menuModelList.get(position));
//        });
    }

//    private void addToCart(MenuModel menuModel) {
//        DatabaseReference useCart = FirebaseDatabase.
//                getInstance(stringaddress.firebaseDbesto).
//                getReference("cart").child(nomormeja);
//
//        useCart.child(menuModel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                if (snapshot.exists()) //jika sudah ada item di cart
//                {
//                    CartModel cartModel = snapshot.getValue(CartModel.class);
//                    cartModel.setQuantity(cartModel.getQuantity() + 1);
//                    Map<String, Object> updateData = new HashMap<>();
//                    updateData.put("quantity", cartModel.getQuantity());
//                    updateData.put("totalPrice", cartModel.getQuantity() * Float.parseFloat(cartModel.getHarga()));
//
//                    useCart.child(menuModel.getKey()).updateChildren(updateData).
//                            addOnSuccessListener(aVoid -> {
//                                cartLoadListener.onCartLoadFailed(cartModel.getNamaMakanan() + " ditambahkan ke keranjang");
//                            })
//                            .addOnFailureListener(e -> cartLoadListener.onCartLoadFailed(e.getMessage()));
//                } else //jika belum ada item dicart
//                {
//                    CartModel cartModel = new CartModel();
//                    cartModel.setNamaMakanan(menuModel.getNamaMakanan());
//                    cartModel.setDeskripsi(menuModel.getDeskripsi());
//                    cartModel.setHarga(menuModel.getHarga());
//                    cartModel.setGambar(menuModel.getGambar());
//                    cartModel.setKey(menuModel.getKey());
//                    cartModel.setQuantity(1);
//                    cartModel.setStok(menuModel.getStok());
//                    float a = cartModel.getQuantity() * Float.parseFloat(cartModel.getHarga());
//                    cartModel.setTotalPrice(a);
//
//                    useCart.child(menuModel.getKey())
//                            .setValue(cartModel)
//                            .addOnSuccessListener(aVoid -> cartLoadListener.onCartLoadFailed(cartModel.getNamaMakanan() + " ditambahkan ke keranjang"))
//                            .addOnFailureListener(e -> cartLoadListener.onCartLoadFailed(e.getMessage()));
//                }
//                EventBus.getDefault().postSticky(new UpdateCartEvent());
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//                cartLoadListener.onCartLoadFailed(error.getMessage());
//            }
//        });
//
//    }

    @Override
    public int getItemCount() {
        return pembayaranModelList.size();
    }

    public class MyPembayaranViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imgPemesanan)
        ImageView imgPemesanan;
        @BindView(R.id.txtNamaMenu)
        TextView txtNama;
        @BindView(R.id.txtHarga)
        TextView txtHarga;
        @BindView(R.id.txtQuantity)
        TextView txtQuantity;
        @BindView(R.id.totalHarga)
        TextView totalHarga;

        RecyclerViewListener listener;

        void setListener(RecyclerViewListener listener) {
            this.listener = listener;
        }

        Unbinder unbinder;

        public MyPembayaranViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRecyclerClick(view, getAdapterPosition());
        }
    }
}
