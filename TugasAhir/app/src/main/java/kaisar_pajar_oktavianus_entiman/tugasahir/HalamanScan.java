package kaisar_pajar_oktavianus_entiman.tugasahir;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import kaisar_pajar_oktavianus_entiman.tugasahir.model.NomorMeja;

public class HalamanScan extends AppCompatActivity {
    CardView btnScan;
    TextView txtCabang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_scan);

        btnScan = findViewById(R.id.btnScan);
        txtCabang = findViewById(R.id.txtSelamatdatang);

        txtCabang.setText("Selamat datang di d'Besto");

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(HalamanScan.this);
                //set prompt text
                intentIntegrator.setPrompt("Arahkan pada QR Code yang tertera di meja\n\n\n\n\n\n\n\n");
                //set beep
                intentIntegrator.setBeepEnabled(false);
                //set lampu
                intentIntegrator.setTorchEnabled(false);
                //set locked orientation
                intentIntegrator.setOrientationLocked(true);
                //set capture activity
                intentIntegrator.setCaptureActivity(Capture.class);
                //initiate scan
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check condition beji ridwan rais
        if (intentResult.getContents() != null && (intentResult.getContents().equals("Beji ridwan rais meja 1") ||
                intentResult.getContents().equals("Beji ridwan rais meja 2") || intentResult.getContents().equals("Beji ridwan rais meja 3") ||
                intentResult.getContents().equals("Beji ridwan rais meja 4") || intentResult.getContents().equals("Beji ridwan rais meja 5") ||
                intentResult.getContents().equals("Beji ridwan rais meja 6"))) {
            NomorMeja.setNamacabang(Stringaddress.firebasedbestocabangA);
            String a = intentResult.getContents();
            char last = a.charAt(a.length() -1);
            NomorMeja.setNamacabangsel(a.substring(0, a.length() - 6));

            NomorMeja.setNomormeja(""+last);
            Intent utama = new Intent(this, Menu.class);
            startActivity(utama);
            finish();
        }
        //check condition nusantara
        if (intentResult.getContents() != null && (intentResult.getContents().equals("Nusantara meja 1") ||
                intentResult.getContents().equals("Nusantara meja 2") || intentResult.getContents().equals("Nusantara meja 3") ||
                intentResult.getContents().equals("Nusantara meja 4") || intentResult.getContents().equals("Nusantara meja 5") ||
                intentResult.getContents().equals("Nusantara meja 6"))) {
            NomorMeja.setNamacabang(Stringaddress.firebasedbestocabangB);
            String a = intentResult.getContents();
            char last = a.charAt(a.length() -1);
            NomorMeja.setNamacabangsel(a.substring(0, a.length() - 6));

            NomorMeja.setNomormeja(""+last);
            Intent utama = new Intent(this, Menu.class);
            startActivity(utama);
            finish();
        }
        //check condition auri
        if (intentResult.getContents() != null && (intentResult.getContents().equals("Auri meja 1") ||
                intentResult.getContents().equals("Auri meja 2") || intentResult.getContents().equals("Auri meja 3") ||
                intentResult.getContents().equals("Auri meja 4") || intentResult.getContents().equals("Auri meja 5") ||
                intentResult.getContents().equals("Auri meja 6"))) {
            NomorMeja.setNamacabang(Stringaddress.firebasedbestocabangC);
            String a = intentResult.getContents();
            char last = a.charAt(a.length() -1);
            NomorMeja.setNamacabangsel(a.substring(0, a.length() - 6));

            NomorMeja.setNomormeja(""+last);
            Intent utama = new Intent(this, Menu.class);
            startActivity(utama);
            finish();
        }
        //check condition pelni
        if (intentResult.getContents() != null && (intentResult.getContents().equals("Pelni meja 1") ||
                intentResult.getContents().equals("Pelni meja 2") || intentResult.getContents().equals("Pelni meja 3") ||
                intentResult.getContents().equals("Pelni meja 4") || intentResult.getContents().equals("Pelni meja 5") ||
                intentResult.getContents().equals("Pelni meja 6"))) {
            NomorMeja.setNamacabang(Stringaddress.firebasedbestocabangD);
            String a = intentResult.getContents();
            char last = a.charAt(a.length() -1);
            NomorMeja.setNamacabangsel(a.substring(0, a.length() - 6));

            NomorMeja.setNomormeja(""+last);
            Intent utama = new Intent(this, Menu.class);
            startActivity(utama);
            finish();
        }
        //check condition tegal parang
        if (intentResult.getContents() != null && (intentResult.getContents().equals("Tegal parang meja 1") ||
                intentResult.getContents().equals("Tegal parang meja 2") || intentResult.getContents().equals("Tegal parang meja 3") ||
                intentResult.getContents().equals("Tegal parang meja 4") || intentResult.getContents().equals("Tegal parang meja 5") ||
                intentResult.getContents().equals("Tegal parang meja 6"))) {
            NomorMeja.setNamacabang(Stringaddress.firebasedbestocabangE);
            String a = intentResult.getContents();
            char last = a.charAt(a.length() -1);
            NomorMeja.setNamacabangsel(a.substring(0, a.length() - 6));

            NomorMeja.setNomormeja(""+last);
            Intent utama = new Intent(this, Menu.class);
            startActivity(utama);
            finish();
        }
        if (intentResult.getContents() != null && !intentResult.getContents().equals("admin")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HalamanScan.this);
            //settittle
            builder.setTitle("Result");
            //setmessage
            builder.setMessage("Hasil tidak dikenali, mohon scan QRCode yang tersedia pada meja");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
        if (intentResult.getContents() == null) {
            Toast.makeText(this, "Scan ditunda", Toast.LENGTH_SHORT).show();
        }
    }
}