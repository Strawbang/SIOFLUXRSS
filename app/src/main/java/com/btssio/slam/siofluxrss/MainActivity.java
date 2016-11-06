package com.btssio.slam.siofluxrss;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.btssio.slam.siofluxrss.repository.FluxRepository;


public class MainActivity extends AppCompatActivity {

    private static final int STOPSPLASH = 0;
    private static final long SPLASHTIME = 3000;

    String leFlux;
    FluxRepository flxRepo;
    Intent IntentAbonnement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creation de l'objet Flux lie a l'utilisateur avec passage du contexte
        flxRepo = new FluxRepository(getApplicationContext());
        // Verification de l'existence d'un flux
        if (flxRepo.isFluxConfigured()) {
            leFlux = flxRepo.getFlux();
            Message msg = new Message();
            msg.what = STOPSPLASH;
            splashHandler.sendMessageDelayed(msg, SPLASHTIME);
        }
        //-> Lecture du fichier distant XML par SAXParser avec Thread et gestion d'erreurs
        // TO DO
        else {
            Intent intentChoix = new Intent(MainActivity.this, ChoixActivity.class);
            startActivityForResult(intentChoix,10);
        }
    }

    private Handler splashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPSPLASH:
                    lancementAbonnement();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == 20 ) {
            if (data.getStringExtra("url") != null) {
                leFlux = data.getStringExtra("url");
                flxRepo.setFlux(leFlux);
                lancementAbonnement();
            } else {
                this.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                getParent().finish();
            }
        }
        else {
            leFlux = data.getStringExtra("url");
            flxRepo.setFlux(leFlux);
            lancementAbonnement();
        }
    }

    public void lancementAbonnement() {
        IntentAbonnement = new Intent(this, Abonnement.class);
        IntentAbonnement.putExtra("url", leFlux);
        this.startActivityForResult(IntentAbonnement,20);
    }

}

