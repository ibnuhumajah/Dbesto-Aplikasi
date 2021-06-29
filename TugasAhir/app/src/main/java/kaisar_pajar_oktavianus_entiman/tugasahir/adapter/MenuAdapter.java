package kaisar_pajar_oktavianus_entiman.tugasahir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kaisar_pajar_oktavianus_entiman.tugasahir.CartActivity;
import kaisar_pajar_oktavianus_entiman.tugasahir.R;
import kaisar_pajar_oktavianus_entiman.tugasahir.Stringaddress;
import kaisar_pajar_oktavianus_entiman.tugasahir.eventbus.UpdateCartEvent;
import kaisar_pajar_oktavianus_entiman.tugasahir.eventbus.UpdateCartEventonCart;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.CartLoadListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.RecyclerViewListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.CartModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.KategoriModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.MenuModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.NomorMeja;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.PembayaranModel;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyMenuViewHolder> {

    Context context;
    List<MenuModel> menuModelList;
    CartLoadListener cartLoadListener;

    Stringaddress stringaddress;
    String nomormeja = NomorMeja.getNomormeja();

    public MenuAdapter(Context context, List<MenuModel> menuModelList, CartLoadListener cartLoadListener) {
        this.context = context;
        this.menuModelList = menuModelList;
        this.cartLoadListener = cartLoadListener;
    }

    @NonNull
    @NotNull
    @Override
    public MyMenuViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MyMenuViewHolder(LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyMenuViewHolder holder, int position) {
        Glide.with(context).load(menuModelList.get(position).getGambar()).into(holder.imageView);
        holder.txtNama.setText(new StringBuilder().append(menuModelList.get(position).getNama()));
        holder.txtDeskripsi.setText(new StringBuilder().append(menuModelList.get(position).getDeskripsi()));
        if (menuModelList.get(position).getStok() == 0)
            holder.txtTersedia.setText(new StringBuilder("Tidak tersedia"));
        if (menuModelList.get(position).getStok() > 0)
            holder.txtTersedia.setText(new StringBuilder("Tersedia : ").append(menuModelList.get(position).getStok()));
        holder.txtHarga.setText(new StringBuilder("Rp").append(menuModelList.get(position).getHarga()));

        holder.setListener((view, adapterPosition) -> {
            if (menuModelList.get(position).getStok() == 0)
                cartLoadListener.onCartLoadFailed("Stok habis");

            if (menuModelList.get(position).getStok() > 0)
                addToCart(menuModelList.get(position), menuModelList.get(position).getStok());
        });
    }

    private void addToCart(MenuModel menuModel, int stok) {
        DatabaseReference useCart = FirebaseDatabase.
                getInstance(NomorMeja.getNamacabang()).
                getReference("cart").child(nomormeja);

        useCart.child(menuModel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) //jika sudah ada item di cart
                {
                    CartModel cartModel = snapshot.getValue(CartModel.class);
                    if (stok <=  cartModel.getQuantity()) {
                        cartLoadListener.onCartLoadFailed("Stok " + cartModel.getNama().toLowerCase() +" tidak memadai");
                    }
                    if (stok > cartModel.getQuantity()) {
                        cartModel.setQuantity(cartModel.getQuantity() + 1);
                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("quantity", cartModel.getQuantity());
                        updateData.put("totalPrice", cartModel.getQuantity() * Float.parseFloat(cartModel.getHarga()));

                        useCart.child(menuModel.getKey()).updateChildren(updateData).
                                addOnSuccessListener(aVoid -> {
                                    cartLoadListener.onCartLoadFailed(cartModel.getNama() + " ditambahkan ke keranjang");
                                })
                                .addOnFailureListener(e -> cartLoadListener.onCartLoadFailed(e.getMessage()));
                    }

                } else //jika belum ada item dicart
                {
                    CartModel cartModel = new CartModel();
                    cartModel.setNama(menuModel.getNama());
                    cartModel.setDeskripsi(menuModel.getDeskripsi());
                    cartModel.setHarga(menuModel.getHarga());
                    cartModel.setGambar(menuModel.getGambar());
                    cartModel.setKey(menuModel.getKey());
                    cartModel.setQuantity(1);
                    cartModel.setStok(menuModel.getStok());
                    float a = cartModel.getQuantity() * Float.parseFloat(cartModel.getHarga());
                    cartModel.setTotalPrice(a);

                    useCart.child(menuModel.getKey())
                            .setValue(cartModel)
                            .addOnSuccessListener(aVoid -> cartLoadListener.onCartLoadFailed(cartModel.getNama() + " ditambahkan ke keranjang"))
                            .addOnFailureListener(e -> cartLoadListener.onCartLoadFailed(e.getMessage()));

                }
                EventBus.getDefault().postSticky(new UpdateCartEvent());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                cartLoadListener.onCartLoadFailed(error.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuModelList.size();
    }

    public class MyMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imgMenu)
        ImageView imageView;
        @BindView(R.id.txtNamaMenu)
        TextView txtNama;
        @BindView(R.id.txtDeskripsi)
        TextView txtDeskripsi;
        @BindView(R.id.txtHarga)
        TextView txtHarga;
        @BindView(R.id.txtStok)
        TextView txtTersedia;

        RecyclerViewListener listener;

        void setListener(RecyclerViewListener listener) {
            this.listener = listener;
        }

        Unbinder unbinder;

        public MyMenuViewHolder(@NonNull @NotNull View itemView) {
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
