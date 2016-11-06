package com.btssio.slam.siofluxrss.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

// Classe intermediaire le flux déposé choisi par l'utilisateur et l'enregistrement Android
public class FluxRepository extends Repository {

	// Constructeur
	public FluxRepository(Context context) {
		super(context);
	}

	// Enregistre le flux dans les SharedPreferences
	public void setFlux(String flux) {
		SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(Repository.context);
		Editor prefsEditor = appSharedPrefs.edit();

		prefsEditor.putString("FLUX",flux);	
		prefsEditor.commit();
	}

	// Supprime le flux
	public void unsetFlux() {
		SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(Repository.context);
		Editor prefsEditor = appSharedPrefs.edit();

		prefsEditor.remove("FLUX");
		prefsEditor.commit();
	}

	// Indique si un flux est configure ou non
	public boolean isFluxConfigured() {
		FluxRepository flxRepo = new FluxRepository(Repository.context);
		String leFlux = flxRepo.getFlux();

		if (leFlux.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	// Recupere le flux de l'utilisateur
	public String getFlux()	{
		SharedPreferences app = PreferenceManager.getDefaultSharedPreferences(Repository.context);
		return app.getString("FLUX", "");
	}	
}
