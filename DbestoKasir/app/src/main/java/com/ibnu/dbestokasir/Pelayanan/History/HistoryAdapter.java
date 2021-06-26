package com.ibnu.dbestokasir.Pelayanan.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.CartLoadListener;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranAdapter;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.PembayaranModel;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.RecyclerViewListener;
import com.ibnu.dbestokasir.Pelayanan.Pemesanan.Stringaddress;
import com.ibnu.dbestokasir.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyPembayaranViewHolder> {
    Context context;
    List<HistoryModel> historyModelList;
    CartLoadListener cartLoadListener;

    Stringaddress stringaddress;


    public HistoryAdapter(Context context, List<HistoryModel> historyModelList, CartLoadListener cartLoadListener) {
        this.context = context;
        this.historyModelList = historyModelList;
        this.cartLoadListener = cartLoadListener;

    }

    @NonNull
    @Override
    public MyPembayaranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        MyPembayaranViewHolder historyAdapter = new MyPembayaranViewHolder(layout);
        return historyAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyPembayaranViewHolder holder, int position) {
        int a = 0;
        int b = 0;
        for (HistoryModel historyModel : historyModelList){
            a += historyModel.getQuantity();
            b += historyModel.getTotalPrice();
        }

        Glide.with(context).load(historyModelList.get(position).getGambar()).into(holder.imgHistory);
        holder.txtNamaHistory.setText(new StringBuilder().append(historyModelList.get(position).getNama()));
        holder.txtIdHistory.setText(new StringBuilder("Waktu Pemesanan : ").append(historyModelList.get(position).getKey()));
        holder.txtQuantity.setText(new StringBuilder("Banyak Item : ").append(historyModelList.get(position).getQuantity()));
        holder.totalHarga.setText(new StringBuilder("Rp.").append(historyModelList.get(position).getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return historyModelList.size();
    }

    public class MyPembayaranViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgHistory)
        ImageView imgHistory;
        @BindView(R.id.txtNamaHistory)
        TextView txtNamaHistory;
        @BindView(R.id.txtIdHistory)
        TextView txtIdHistory;
        @BindView(R.id.txtQuantity)
        TextView txtQuantity;
        @BindView(R.id.totalHarga)
        TextView totalHarga;

        Unbinder unbinder;

        public MyPembayaranViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
