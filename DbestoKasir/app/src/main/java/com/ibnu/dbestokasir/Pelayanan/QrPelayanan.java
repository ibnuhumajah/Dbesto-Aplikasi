package com.ibnu.dbestokasir.Pelayanan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrPelayanan extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView mScanner;
    public String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScanner = new ZXingScannerView(this);
        setContentView(mScanner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScanner.setResultHandler(this);
        mScanner.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScanner.stopCamera();
    }

    @Override
    public void handleResult(final Result rawResult) {
        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Apakah data sudah benar?");
        builder.setMessage(rawResult.getText());
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(QrPelayanan.this, PelayananMain.class);
                intent.putExtra("hasil", rawResult.getText()+"");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                setResult(RESULT_FIRST_USER, intent);
                finish();
            }
        });

        final AlertDialog alert1 = builder.create();

        alert1.show();

        mScanner.resumeCameraPreview(this);
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
