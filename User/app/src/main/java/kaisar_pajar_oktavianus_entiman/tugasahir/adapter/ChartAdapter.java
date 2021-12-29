package kaisar_pajar_oktavianus_entiman.tugasahir.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kaisar_pajar_oktavianus_entiman.tugasahir.R;
import kaisar_pajar_oktavianus_entiman.tugasahir.Stringaddress;
import kaisar_pajar_oktavianus_entiman.tugasahir.eventbus.UpdateCartEvent;
import kaisar_pajar_oktavianus_entiman.tugasahir.eventbus.UpdateCartEventonCart;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.CartModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.NomorMeja;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.CartViewHolder> {

    Context context;
    List<CartModel> cartModelList;

    Stringaddress stringaddress;
    String nomormeja = NomorMeja.getNomormeja();

    public ChartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @NotNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartViewHolder holder, int position) {
        Glide.with(context).load(cartModelList.get(position).getGambar()).into(holder.imgCart);
        holder.txtNamaMenu.setText(new StringBuilder().append(cartModelList.get(position).getNama()));
        holder.txtHarga.setText(new StringBuilder("Rp").append(cartModelList.get(position).getHarga()));
        holder.txtQuantity.setText(new StringBuilder().append(cartModelList.get(position).getQuantity()));

        holder.btnMinus.setOnClickListener(v -> {
            minusCartItem(holder, cartModelList.get(position));
            minusCartItem1();

            if (cartModelList.get(position).getQuantity() == 0) {
                holder.btnMinus.setEnabled(false);
                notifyItemRemoved(position);
                deleteFromFirebase(cartModelList.get(position));
            }
        });
        holder.btnPlus.setOnClickListener(v -> {
            plusCartItem(holder, cartModelList.get(position));
        });

        holder.btnDelete.setOnClickListener(v -> {

            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Hapus item").setMessage("Yakin untuk menghapus item?")
                    .setNegativeButton("Cancel", (dialog1, i) -> dialog1.dismiss())
                    .setPositiveButton("Ok", (dialogInterface, i) -> {

                        notifyItemRemoved(position);

                        deleteFromFirebase(cartModelList.get(position));
                        dialogInterface.dismiss();
                    }).create();
            dialog.show();
        });
    }

    private void minusCartItem1() {
    }

    private void deleteFromFirebase(CartModel cartModel) {
        FirebaseDatabase.
                getInstance(NomorMeja.getNamacabang())
                .getReference("cart").child(nomormeja).child(cartModel.getKey())
                .removeValue().addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new UpdateCartEventonCart()));
    }

    private void plusCartItem(CartViewHolder holder, CartModel cartModel) {
        if (cartModel.getStok() == cartModel.getQuantity()){
            Toast.makeText(context, cartModel.getNama().toLowerCase() + " tidak dapat ditambah lagi", Toast.LENGTH_SHORT).show();
        }
        if (cartModel.getStok() > cartModel.getQuantity()){
            holder.btnPlus.setEnabled(true);

            cartModel.setQuantity(cartModel.getQuantity() + 1);
            cartModel.setTotalPrice(cartModel.getQuantity() * Float.parseFloat(cartModel.getHarga()));

            holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
            updateFirebase(cartModel);
        }
    }

    private void minusCartItem(CartViewHolder holder, CartModel cartModel) {
        cartModel.setQuantity(cartModel.getQuantity() - 1);
        cartModel.setTotalPrice(cartModel.getQuantity() * Float.parseFloat(cartModel.getHarga()));

        holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
        updateFirebase(cartModel);
    }

    private void updateFirebase(CartModel cartModel) {
        FirebaseDatabase.
                getInstance(NomorMeja.getNamacabang()).
                getReference("cart").child(nomormeja).child(cartModel.getKey()).
                setValue(cartModel).addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new UpdateCartEventonCart()));
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnMinus)
        ImageView btnMinus;
        @BindView(R.id.btnPlus)
        ImageView btnPlus;
        @BindView(R.id.btnDelete)
        ImageView btnDelete;
        @BindView(R.id.imgCart)
        ImageView imgCart;
        @BindView(R.id.txtNamaMenu)
        TextView txtNamaMenu;
        @BindView(R.id.txtHarga)
        TextView txtHarga;
        @BindView(R.id.txtQuantity)
        TextView txtQuantity;

        Unbinder unbinder;

        public CartViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
