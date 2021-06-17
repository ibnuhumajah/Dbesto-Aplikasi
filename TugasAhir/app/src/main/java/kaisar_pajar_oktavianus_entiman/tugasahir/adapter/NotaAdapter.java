package kaisar_pajar_oktavianus_entiman.tugasahir.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import kaisar_pajar_oktavianus_entiman.tugasahir.model.CartModel;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.NomorMeja;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.NotaModel;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.HolderData> {
    private List<NotaModel> mItems;
    private Context context;

    public NotaAdapter (Context context, List<NotaModel> items) {
        this.mItems = items;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        NotaModel md = mItems.get(position);
        holder.namaMenuNota.setText(md.getNama());
        holder.hargaNota.setText("Rp" + md.getHarga());
        holder.quantityNota.setText("X" + (md.getQuantity()));
        holder.totalHargaNota.setText("Rp" + md.getTotalPrice() +"00");
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class HolderData extends RecyclerView.ViewHolder {
        TextView namaMenuNota, hargaNota, quantityNota, totalHargaNota;

        public HolderData (View view) {
            super(view);

            namaMenuNota = view.findViewById(R.id.namaMenuNota);
            hargaNota = view.findViewById(R.id.hargaNota);
            quantityNota = view.findViewById(R.id.QuantityNota);
            totalHargaNota = view.findViewById(R.id.totalHargaNota);
        }
    }
}
