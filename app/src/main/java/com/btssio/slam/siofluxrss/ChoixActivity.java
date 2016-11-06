package com.btssio.slam.siofluxrss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.btssio.slam.siofluxrss.adapters.FluxAdapter;
import com.btssio.slam.siofluxrss.objects.Flux;
import com.btssio.slam.siofluxrss.repository.FluxRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

public class ChoixActivity extends AppCompatActivity {
    ListView lv;

    // Variables pour la lecture du flux Json
    private String jsonString;
    JSONObject jsonResponse;
    JSONArray arrayJson;
    ArrayList<Flux> items;


    private FluxRepository flxRepository;

    // Initialisation de l'activité et remplissage des champs existant si le compte est déjà configuré.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choix_main);

        lv = (ListView)findViewById(R.id.list);
        items = new ArrayList<Flux>();

        // Traitement du textView en autocomplétion à  partir de la source Json
        jsonString = loadJSONFromAsset();

        try {
            jsonResponse = new JSONObject(jsonString);
            // Création du tableau général à partir d'un JSONObject
            JSONArray jsonArray = jsonResponse.getJSONArray("sites");
            Flux currentFlux = null;

            // Pour chaque élément du tableau
            for (int i = 0; i < jsonArray.length(); i++) {
                currentFlux = new Flux();

                // Création d'un tableau élément à  partir d'un JSONObject
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                // Récupération à partir d'un JSONObject nommé
                //JSONObject fields  = jsonObj.getJSONObject("fields");

                // Récupération de l'item qui nous intéresse
                String nom = jsonObj.getString("nom");
                String adresse = jsonObj.getString("adresse");

                currentFlux.setNom(nom);
                currentFlux.setAdresse(adresse);

                // Ajout dans l'ArrayList
                items.add(currentFlux);
            }

            ArrayAdapter<Flux> objAdapter = new FluxAdapter(this,R.layout.row, items);
            lv.setAdapter(objAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                int choix = position+1;
                String url = items.get(position).getAdresse();

                Toast.makeText(getApplicationContext(),
                        "Vous avez choisi le flux numéro " + choix, Toast.LENGTH_LONG)
                        .show();

                Intent data = new Intent();
                data.putExtra("url",url);
                setResult(RESULT_OK,data);

                finish();
            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("flux.json");
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

}
