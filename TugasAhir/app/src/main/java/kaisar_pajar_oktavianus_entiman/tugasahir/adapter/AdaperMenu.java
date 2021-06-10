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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kaisar_pajar_oktavianus_entiman.tugasahir.R;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.MenuModel;

public class AdaperMenu extends RecyclerView.Adapter<AdaperMenu.MyViewHolder> {

    Context context;

    ArrayList<MenuModel> list;

    public AdaperMenu(Context context, ArrayList<MenuModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_menu,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        MenuModel menuModel = list.get(position);
        Glide.with(context).load(list.get(position).getGambar()).into(holder.gambar);
        holder.namaMenu.setText(menuModel.getNama());
        holder.deskripsi.setText(menuModel.getDeskripsi());
        holder.harga.setText(menuModel.getHarga());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaMenu, deskripsi, harga;
        ImageView gambar;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.gambarmenu);
            namaMenu = itemView.findViewById(R.id.namaMenu);
            deskripsi = itemView.findViewById(R.id.deskripsi);
            harga = itemView.findViewById(R.id.harga);
        }
    }
}