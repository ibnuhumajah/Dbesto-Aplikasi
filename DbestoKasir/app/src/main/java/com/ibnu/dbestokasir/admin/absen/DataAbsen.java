package com.ibnu.dbestokasir.admin.absen;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibnu.dbestokasir.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Phaser;

import static android.text.TextUtils.split;


public class DataAbsen extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private RecyclerViewDataAdmin adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Model> models;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final String[] date = new String[1];
    private List<Model> dataFilter;
    private String status ="all";
    private String nama = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_data_absen, container,false);
        models = new ArrayList<>();
        setHasOptionsMenu(true);
        recyclerView = rootView.findViewById(R.id.recyclerview_data_admin);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_700);

        databaseReference = FirebaseDatabase.getInstance("https://dbesto-default-rtdb.firebaseio.com/").getReference();
        initData();
        return rootView;
    }

    private void initData(){
        mSwipeRefreshLayout.setRefreshing(true);

        status = "all";
        nama = "";
        databaseReference.child("Absen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()){
                    Model model = noteDataSnapshot.getValue(Model.class);
                    model.setKey(noteDataSnapshot.getKey());

                    models.add(model);
                }
                setRvAdapter(models);

                Log.d("data", models.toString());

                mSwipeRefreshLayout.setRefreshing(false);

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.println(databaseError.getDetails() + " " + databaseError.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setRvAdapter(ArrayList<Model> models) {
        adapter = new RecyclerViewDataAdmin(models, getActivity());
        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        inflater.inflate(R.menu.filter_menu, menu);
        inflater.inflate(R.menu.save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(getActivity());

        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.search_hint) + "</font>"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                nextText = nextText.toLowerCase();
                Log.d("next", nextText.toString());
                dataFilter= new ArrayList<>();
                for (Model data : models) {
                    String nama = data.getNama().toLowerCase();
                    String nik = data.getNik().toLowerCase();
                    String masuk = data.getMasuk().toLowerCase();

                    if (nama.contains(nextText)) {
                        dataFilter.add(data);

                    }

                }
                if (adapter != null) {
                    adapter.setFilter(dataFilter);
                    status ="nama";
                    nama = nextText;
                } else {
                    return false;
                }
                return true;

            }
        });
        searchItem.setActionView(searchView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_filter:
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                if (dayOfMonth <= 9) {
                                    date[0] = "0"+dayOfMonth + "-0"
                                            + (monthOfYear + 1) + "-" + year;
                                } else {
                                    date[0] = dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year;
                                }

                                String nextText = date[0].toLowerCase();
                                dataFilter = new ArrayList<>();
                                for (Model data : models) {
                                    String masuk = data.getMasuk().toLowerCase();
                                    if (masuk.contains(nextText)) {
                                        dataFilter.add(data);
                                    }
                                }
                                if (adapter != null) {
                                    adapter.setFilter(dataFilter);
                                }

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
                status = "tanggal";
                nama ="";


                return true;
            case R.id.action_save:
                createPDF(nama);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        if (adapter != null) {
            models.clear();
            adapter.notifyDataSetChanged();
            initData();
        } else {
            initData();
        }
    }

    public void createPDF(String nama) {

        Document doc = new Document();
        String outpath = "";
        if (status.equals("all")){
            outpath = Environment.getExternalStorageDirectory() + "/laporan_semua_absensi.pdf";
        }else if (status.equals("tanggal")){
            outpath = Environment.getExternalStorageDirectory() + "/laporan_absensi_" + date[0] + ".pdf";
        }else if (status.equals("nama")){
            outpath = Environment.getExternalStorageDirectory() + "/laporan_absensi_"+nama+".pdf";
        }

        try {
            PdfWriter.getInstance(doc, new FileOutputStream(outpath));
            doc.open();
            doc.setPageSize(PageSize.A4);

            Font normal =  new Font(Font.FontFamily.HELVETICA, 12,Font.NORMAL, BaseColor.BLACK);
            Font bold =  new Font(Font.FontFamily.HELVETICA, 14,Font.BOLD, BaseColor.BLACK);
            Font bold2 = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLACK);

            Paragraph title = new Paragraph("Data Absensi Karyawan Dbesto", bold2);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1,4,3,4});

            PdfPCell cell;
            cell = new PdfPCell(new Phrase("No.", bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Nama", bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("NIK", bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Waktu Absen", bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            for (int i = 0; i < models.size(); i++) {
                String[] waktumasuk = split(models.get(i).getMasuk(), " ");
                String waktuFilter = waktumasuk[0].toString();

                //Log.d("ddd", waktumasuk[0] + " " + date[0] + ", " + waktuFilter.trim().equals(dataWaktu.trim()));
                if (status.equals("all")){

                    cell = new PdfPCell( new Phrase(String.valueOf(i+1), normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell( new Phrase(models.get(i).getNama(), normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell( new Phrase(models.get(i).getNik(), normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell( new Phrase(models.get(i).getMasuk(), normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                }else if (status.equals("tanggal")){
                    String dataWaktu = date[0].toString();
                    if (waktuFilter.trim().contains(dataWaktu.trim())) {

                        cell = new PdfPCell( new Phrase(String.valueOf(i+1), normal));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell( new Phrase(models.get(i).getNama(), normal));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell( new Phrase(models.get(i).getNik(), normal));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell( new Phrase(models.get(i).getMasuk(), normal));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }
                }
            }

            if (status.equals("nama")){
                for (int i =0 ; i<dataFilter.size();i++){

                    cell = new PdfPCell( new Phrase(String.valueOf(i+1), normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell( new Phrase(models.get(i).getNama(), normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell( new Phrase(models.get(i).getNik(), normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell( new Phrase(models.get(i).getMasuk(), normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }

//            doc.add(new Paragraph(date[0] + "wwww.dbesto.net"));
            doc.add(title);
            doc.add(table);
            doc.close();
            Toast.makeText(getActivity(), "Laporan berhasil dibuat", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}