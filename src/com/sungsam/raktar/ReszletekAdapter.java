package com.sungsam.raktar;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReszletekAdapter extends BaseAdapter {
	
	private final ArrayList<String> reszletek; //NOPMD
	private final LayoutInflater elrendBetolto; //NOPMD
	private Context context; //NOPMD

	public ReszletekAdapter(final Context context, final ArrayList<String> reszletek) { //NOPMD
		this.context=context;
		this.reszletek = reszletek;
		elrendBetolto = LayoutInflater.from(context);
	}

	public int getCount() {
		return reszletek.size();
	}

	public Object getItem(final int pozicio) {
		return reszletek.get(pozicio);
	}

	public long getItemId(final int pozicio) {
		return pozicio;
	}

	public View getView(int pozicio, View hasznaltNezet, ViewGroup szuloNezet) {//NOPMD
		NezetHordozo nezetHordozo;
		
		if (hasznaltNezet == null) {
			hasznaltNezet = elrendBetolto.inflate(R.layout.reszletek_listaelem, null);
			nezetHordozo = new NezetHordozo();
		
			nezetHordozo.tvDatum = (TextView) hasznaltNezet.findViewById(R.id.tv_datum_reszlet);
			nezetHordozo.tvNetto = (TextView) hasznaltNezet.findViewById(R.id.tv_nettoar_reszlet);
			nezetHordozo.tvBrutto= (TextView) hasznaltNezet.findViewById(R.id.tv_bruttoar_reszlet);
			nezetHordozo.tvMennyiseg = (TextView) hasznaltNezet.findViewById(R.id.tv_mennyiseg_reszlet);

			hasznaltNezet.setTag(nezetHordozo);
		} else {
			nezetHordozo = (NezetHordozo) hasznaltNezet.getTag();
		}
		
		final String[] darabolva=reszletek.get(pozicio).split(":::");
		
		nezetHordozo.tvDatum.setText(darabolva[0]);
		
		if(darabolva[1].equals(context.getResources().getString(R.string.uj_sor))){
			nezetHordozo.tvNetto.setText(darabolva[1]);
			nezetHordozo.tvBrutto.setText(darabolva[2]);
			nezetHordozo.tvMennyiseg.setText(darabolva[3]);
		}else{
			if(Double.parseDouble(darabolva[1])%1==0 ){
				nezetHordozo.tvNetto.setText(String.valueOf((int)Double.parseDouble(darabolva[1])));
			}else{
				nezetHordozo.tvNetto.setText(darabolva[1]);
			}
			
			if(Double.parseDouble(darabolva[2])%1==0 ){
				nezetHordozo.tvBrutto.setText(String.valueOf((int)Double.parseDouble(darabolva[2])));
			}else{
				nezetHordozo.tvBrutto.setText(darabolva[2]);
			}
			
			if(Double.parseDouble(darabolva[3])%1==0 ){
				nezetHordozo.tvMennyiseg.setText(String.valueOf((int)Double.parseDouble(darabolva[3])));
			}else{
				nezetHordozo.tvMennyiseg.setText(darabolva[3]);
			}
		}

		return hasznaltNezet;
	}

	static class NezetHordozo {
		private TextView tvDatum;
		private TextView tvNetto;
		private TextView tvBrutto;
		private TextView tvMennyiseg;
	}
	
}
