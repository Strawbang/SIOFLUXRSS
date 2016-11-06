package com.btssio.slam.siofluxrss;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.btssio.slam.siofluxrss.objects.Article;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Abonnement extends ListActivity {
    public static final int MSG_ERR = 0;
    public static final int MSG_CNF = 1;
    public static final int MSG_IND = 2;
    ArrayList<Article> articles;
    ProgressDialog mProgressDialog;
    private Abonnement mContext;
    String leFlux;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonnement);
        leFlux = this.getIntent().getExtras().getString("url");
        //-> Lecture du fichier distant XML par SAXParser avec
        //-> Thread et gestion d'erreurs
        mContext = this;
        mProgressDialog = ProgressDialog.show(this, "Veuillez patienter",
                "Initialisation...", true);

        final Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                String text = null;
                switch (msg.what) {
                    case MSG_IND:
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.setMessage(((String) msg.obj));
                        }
                        break;
                    case MSG_ERR:
                        text = (String) msg.obj;
                        Toast.makeText(mContext, "Erreur: " + text, Toast.LENGTH_LONG).show();
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        break;
                    case MSG_CNF:
                        text = (String) msg.obj;
                        Toast.makeText(mContext, "" + text, Toast.LENGTH_LONG).show();
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        // Positionnement de la vue liste des articles car tout est ok
                        setListAdapter(new ArrayAdapter<Article>(mContext, R.layout.list_item, articles));
                        ListView lv = getListView();
                        lv.setTextFilterEnabled(true);
                        mProgressDialog.dismiss();
                        break;
                }
            }
        };

        new Thread((new Runnable() {
            @Override
            public void run() {
                Message msg = null;
                String progressBarData = "Récupération des articles...";

                // Positionnement du message
                msg = mHandler.obtainMessage(MSG_IND, (Object) progressBarData);
                // Envoi du message au handler
                mHandler.sendMessage(msg);

                // On défini l'url du fichier XML
                URL url = null;

                // On défini l'url
                try {
                    url = new URL(leFlux);
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }

                articles = ContainerData.getArticles(url);

                msg = mHandler.obtainMessage(MSG_CNF, "Récupération terminée !");
                mHandler.sendMessage(msg);
            }
        })).start();
    }
}