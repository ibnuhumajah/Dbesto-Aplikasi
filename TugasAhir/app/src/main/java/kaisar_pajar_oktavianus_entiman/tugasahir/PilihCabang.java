package kaisar_pajar_oktavianus_entiman.tugasahir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import kaisar_pajar_oktavianus_entiman.tugasahir.model.NomorMeja;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

public class PilihCabang extends AppCompatActivity {
    //    NotificationManagerCompat notificationManager;
    CardView btn_cabangA, btn_cabangB, btn_cabangC, btn_cabangD, btn_cabangE;
    TextView txtCabangA, txtCabangB, txtCabangC, txtCabangD, txtCabangE;
    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    Uri customuri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://kaisar_pajar_oktavianus_entiman.tugasahir/raw/pristine");
    private static final String TAG = "PushNotificationa";
    private static final String CHANNEL_ID = "102";
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_cabang);

       checkInternet();

//        notificationManager = NotificationManagerCompat.from(this);

        btn_cabangA = findViewById(R.id.cabangA);
        btn_cabangB = findViewById(R.id.cabangB);
        btn_cabangC = findViewById(R.id.cabangC);
        btn_cabangD = findViewById(R.id.cabangD);
        btn_cabangE = findViewById(R.id.cabangE);
        txtCabangA = findViewById(R.id.txtcabangA);
        txtCabangB = findViewById(R.id.txtcabangB);
        txtCabangC = findViewById(R.id.txtcabangC);
        txtCabangD = findViewById(R.id.txtcabangD);
        txtCabangE = findViewById(R.id.txtcabangE);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        btn_cabangA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NomorMeja.setNamacabang(Stringaddress.firebasedbestocabangA);
                NomorMeja.setNamacabangsel(txtCabangA.getText().toString());
                Intent cabangA = new Intent(PilihCabang.this, HalamanScan.class);
                startActivity(cabangA);
            }
        });

        btn_cabangB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NomorMeja.setNamacabang(Stringaddress.firebasedbestocabangB);
                NomorMeja.setNamacabangsel(txtCabangB.getText().toString());
                Intent cabangB = new Intent(PilihCabang.this, HalamanScan.class);
                startActivity(cabangB);
            }
        });

        btn_cabangC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NomorMeja.setNamacabang(Stringaddress.firebasedbestocabangC);
                NomorMeja.setNamacabangsel(txtCabangC.getText().toString());
                Intent cabangC = new Intent(PilihCabang.this, HalamanScan.class);
                startActivity(cabangC);
            }
        });

        btn_cabangD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NomorMeja.setNamacabang(Stringaddress.firebasedbestocabangD);
                NomorMeja.setNamacabangsel(txtCabangD.getText().toString());
                Intent cabangD = new Intent(PilihCabang.this, HalamanScan.class);
                startActivity(cabangD);
            }
        });

        btn_cabangE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NomorMeja.setNamacabang(Stringaddress.firebasedbestocabangE);
                NomorMeja.setNamacabangsel(txtCabangE.getText().toString());
                Intent cabangE = new Intent(PilihCabang.this, HalamanScan.class);
                startActivity(cabangE);
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                //If task is failed then
                if (!task.isSuccessful()) {
                    Log.e(TAG, "onComplete: Failed to get the Token");
                }

                //Token
                String token = task.getResult();
                Log.e(TAG, "onComplete: " + token);
            }
        });
    }

    void checkInternet() {
        if (isNetworkConnected())
            getToken();
        if (!isNetworkConnected())
            Toast.makeText(this, "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
    }
}