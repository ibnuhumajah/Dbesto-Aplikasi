package kaisar_pajar_oktavianus_entiman.tugasahir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends AppCompatActivity {

    int durasiSplash = 3000;
    private static final String CHANNEL_ID ="102" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        createNotificationChannel();
        new Handler().postDelayed(() -> {
            Intent splash = new Intent(Splashscreen.this,
                    PilihCabang.class);
            startActivity(splash);
            finish();
        }, durasiSplash);

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "firebaseNotifChannelA";
            String description = "Receve Firebase notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}