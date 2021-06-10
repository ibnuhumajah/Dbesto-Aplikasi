package com.ibnu.dbestokasir.Penilaian.Terbaik;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ibnu.dbestokasir.R;
import com.ibnu.dbestokasir.Penilaian.Terbaik.Model;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewDataTerbaik extends RecyclerView.Adapter<RecyclerViewDataTerbaik.ViewHolder> {

    private ArrayList<Model> models;
    private Context context;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://dbesto-default-rtdb.firebaseio.com/").getReference();


    public RecyclerViewDataTerbaik(ArrayList<Model> models, Context mcontext) {
        this.models = models;
        this.context = mcontext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_data_terbaik_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tv_nama.setText(models.get(position).getNama());
        holder.tv_nbi.setText(models.get(position).getBulan());
        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle("Konfirmasi Hapus Karyawan Terbaik")
                        .setMessage("Apakah Anda ingin menghapus Karyawan Terbaik "+models.get(position).getNama()+" ?")
                        .setCancelable(true)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myDelJadwal(position);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                // membuat alert dialog dari builder
                AlertDialog alertDialog = alertDialogBuilder.create();

                // menampilkan alert dialog
                alertDialog.show();
            }
        });
    }

    private void myDelJadwal(final int position) {
        Model model = models.get(position);
        if(databaseReference!=null){databaseReference.child("Terbaik").child(model.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Data berhasil dihapus", Toast.LENGTH_LONG).show();
                models.remove(position);
                //notifyDataSetChanged();
                Intent intent = new Intent("reload");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("reload","reload");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                notifyDataSetChanged();
            }
        });

        }
    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    public void setFilter(List<Model> dataFilter) {
        if (dataFilter != null) {
            models = new ArrayList<>();
            models.addAll(dataFilter);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_hapus;
        TextView tv_nama, tv_nbi, terbaik,tv_qr, tv_masuk, tv_keluar;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_nama = itemView.findViewById(R.id.nama_terbaik_list);
            tv_nbi = itemView.findViewById(R.id.bulan_terbaik_list);
            btn_hapus = itemView.findViewById(R.id.terbaik_hapus);

        }
    }
}