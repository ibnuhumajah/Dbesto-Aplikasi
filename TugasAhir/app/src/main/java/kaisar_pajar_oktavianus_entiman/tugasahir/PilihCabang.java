package kaisar_pajar_oktavianus_entiman.tugasahir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_cabang);

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

        btn_cabangA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NomorMeja.setNamacabang(Stringaddress.firebaseTugasahir);
                NomorMeja.setNamacabangsel(txtCabangA.getText().toString());
                Intent cabangA = new Intent(PilihCabang.this, HalamanScan.class);
                startActivity(cabangA);
            }
        });

        btn_cabangB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NomorMeja.setNamacabang(Stringaddress.firebaseDbesto);
                NomorMeja.setNamacabangsel(txtCabangB.getText().toString());
                Intent cabangB = new Intent(PilihCabang.this, HalamanScan.class);
                startActivity(cabangB);
            }
        });

        btn_cabangC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NomorMeja.setNamacabang(Stringaddress.firebaseDbesto);
                NomorMeja.setNamacabangsel(txtCabangC.getText().toString());
                Intent cabangC = new Intent(PilihCabang.this, HalamanScan.class);
                startActivity(cabangC);
            }
        });

        btn_cabangD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NomorMeja.setNamacabang(Stringaddress.firebaseDbesto);
                NomorMeja.setNamacabangsel(txtCabangD.getText().toString());
                Intent cabangD = new Intent(PilihCabang.this, HalamanScan.class);
                startActivity(cabangD);
            }
        });

        btn_cabangE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                notifikasi();
//                NomorMeja.setNamacabang(Stringaddress.firebaseDbesto);
//                NomorMeja.setNamacabangsel(txtCabangE.getText().toString());
//                Intent cabangE = new Intent(PilihCabang.this, HalamanScan.class);
//                startActivity(cabangE);
            }
        });
    }

    void notifikasi() {
        String message = "this is a notification";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                PilihCabang.this
        )
                .setSmallIcon(R.drawable.dbesto)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.dbesto))
                .setContentTitle("New Notification ")
                .setAutoCancel(true)
                .setSound(customuri)
                .setContentText(message);

        Intent intenta = new Intent(PilihCabang.this, PilihCabang.class);
        intenta.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(PilihCabang.this, 0,
                intenta, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE
        );
        notificationManager.notify(0, builder.build());
    }

    }

//    void sendOnChanel(){
//        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.logo_footer_white)
//                .setContentTitle("Hi World")
//                .setContentText("Hello world")
//                .setPriority(PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .build();
//
//        notificationManager.notify(0, notification);
//    }

