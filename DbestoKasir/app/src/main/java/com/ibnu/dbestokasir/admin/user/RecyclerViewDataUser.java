package com.ibnu.dbestokasir.admin.user;

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
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ibnu.dbestokasir.R;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewDataUser extends RecyclerView.Adapter<RecyclerViewDataUser.ViewHolder> {

    private ArrayList<Model> models;
    private Context context;
    private Dialog myDialogDetail,myDialogEdit, myDialogQR;
    private EditText et_nama, et_nbi;
    private TextView tv_judul;
    private ImageView iv_qr;
    private Button btn_simpan, btn_simpan_qr_pdf;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://dbesto-default-rtdb.firebaseio.com/").getReference();

    public RecyclerViewDataUser(ArrayList<Model> models, Context mcontext) {
        this.models = models;
        this.context = mcontext;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_data_user_list, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_nama.setText(models.get(position).getNama());
        holder.tv_nbi.setText("NIK : "+models.get(position).getNik());
        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditUser(position);
            }
        });
        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle("Konfirmasi Hapus Karyawan")
                        .setMessage("Apakah Anda ingin menghapus Karyawan "+models.get(position).getNama()+" ?")
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

        holder.btn_gen_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQR(position);
            }
        });
    }

    private void showQR(final int position) {
        myDialogQR = new Dialog(context);
        myDialogQR.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialogQR.setContentView(R.layout.popup_qr_code);

        iv_qr = myDialogQR.findViewById(R.id.showQR);
        btn_simpan_qr_pdf = myDialogQR.findViewById(R.id.save_qr_pdf);

        final Bitmap myBitmap = QRCode.from(models.get(position).getNama()+"#"+models.get(position).getNik()).bitmap();
        iv_qr.setImageBitmap(myBitmap);

        btn_simpan_qr_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = null;
                try {
                    image = Image.getInstance(stream.toByteArray());
                    saveQRasPDF(position, image);
                } catch (BadElementException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        myDialogQR.setCancelable(true);
        myDialogQR.show();
        Window window = myDialogQR.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
    }

    private void saveQRasPDF(int position, Image myBitmap) {
        Document doc=new Document();
        String outpath= Environment.getExternalStorageDirectory()+"/qr_code_"+models.get(position).getNama()+".pdf";
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(outpath));
            doc.open();

            doc.setPageSize(PageSize.A6);

            Font normal =  new Font(Font.FontFamily.HELVETICA, 12,Font.NORMAL, BaseColor.BLACK);
            Font bold =  new Font(Font.FontFamily.HELVETICA, 14,Font.BOLD, BaseColor.BLACK);
            Font bold2 = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLACK);

            Paragraph title = new Paragraph("QRCode Karyawan \t" + models.get(position).getNama(), bold);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            doc.add(title);

            doc.add(new Paragraph("Nama : \t"+models.get(position).getNama(), normal));
            doc.add(new Paragraph("NIK : \t"+models.get(position).getNik()+"\n",normal));
            doc.add(new Paragraph("QR Code : \t",normal));

            doc.add(myBitmap);
//close the document
            doc.close();
            Toast.makeText(context, "PDF berhasil dibuat", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void myDelJadwal(final int position) {
        Model model = models.get(position);
        if(databaseReference!=null){databaseReference.child("User").child(model.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void myEditUser(final int position) {

        myDialogEdit = new Dialog(context);
        myDialogEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialogEdit.setContentView(R.layout.popup_tambah_user);

        tv_judul = myDialogEdit.findViewById(R.id.judul);
        et_nama = myDialogEdit.findViewById(R.id.nama);
        et_nbi = myDialogEdit.findViewById(R.id.nik_user);

        //setData
        tv_judul.setText("Edit User");
        et_nama.setText(models.get(position).getNama());
        et_nbi.setText(models.get(position).getNik());

        btn_simpan = myDialogEdit.findViewById(R.id.btnSimpan);


        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
                if (et_nama.getText().toString().equals("")){
                    et_nama.setError("Nama harus diisi");
                }else if (et_nbi.getText().toString().equals("")){
                    et_nbi.setError("NIK harus diisi");
                }else {
                    Model model = models.get(position);
                    model.setNama(et_nama.getText().toString());
                    model.setNik(et_nbi.getText().toString());

                    inputData(model,myDialogEdit);
                }



            }
        });

        myDialogEdit.setCancelable(true);
        myDialogEdit.show();
        Window window = myDialogEdit.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
    }

    private void inputData(Model model, final Dialog myDialogEdit) {
        databaseReference.child("User") //akses parent index, ibaratnya seperti nama tabel
                .child(model.getKey()) //select barang berdasarkan key
                .setValue(model) //set value barang yang baru
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Data berhasil di update", Toast.LENGTH_SHORT).show();

                        //notifyDataSetChanged();
                        Intent intent = new Intent("reload");
                        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                        intent.putExtra("reload","reload");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        myDialogEdit.dismiss();
                        notifyDataSetChanged();


                    }
                });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void setFilter(List<Model> dataFilter) {
        if (dataFilter != null){
            models = new ArrayList<>();
            models.addAll(dataFilter);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nama, tv_nbi, tv_qr, tv_masuk, tv_keluar;
        Button btn_hapus, btn_update, btn_gen_qr;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_nama = itemView.findViewById(R.id.nama_user_list);
            tv_nbi = itemView.findViewById(R.id.nbi_user_list);
            btn_hapus = itemView.findViewById(R.id.po_hapus);
            btn_update = itemView.findViewById(R.id.po_edit);
            btn_gen_qr = itemView.findViewById(R.id.po_qr);

        }
    }
}
