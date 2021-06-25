package kaisar_pajar_oktavianus_entiman.tugasahir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends AppCompatActivity {

    int durasiSplash = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(() -> {
            Intent splash = new Intent(Splashscreen.this,
                    PilihCabang.class);
            startActivity(splash);
            finish();
        }, durasiSplash);

    }
}