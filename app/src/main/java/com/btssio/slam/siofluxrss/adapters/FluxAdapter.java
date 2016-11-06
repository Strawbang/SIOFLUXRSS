package com.btssio.slam.siofluxrss.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.btssio.slam.siofluxrss.R;
import com.btssio.slam.siofluxrss.objects.Flux;

public class FluxAdapter extends ArrayAdapter<Flux> {

	private Activity activity;
	private List<Flux> items;
	private Flux objBean;
	private int row;

	public FluxAdapter(Activity act, int resource, List<Flux> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.items = arrayList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		objBean = items.get(position);

		holder.nom = (TextView) view.findViewById(R.id.nom);
		holder.adresse = (TextView) view.findViewById(R.id.adresse);

		if (holder.nom != null && null != objBean.getNom()
				&& objBean.getNom().trim().length() > 0) {
			holder.nom.setText(Html.fromHtml(objBean.getNom()));
		}
		
		if (holder.adresse != null && null != objBean.getAdresse()
				&& objBean.getAdresse().trim().length() > 0) {
			holder.adresse.setText(Html.fromHtml(objBean.getAdresse()));
		}
				
		return view;
	}

	public class ViewHolder {
		public TextView nom, adresse;
	}
}