package com.sungsam.raktar;

import com.sungsam.raktar.AdatbazisAdapter.allapot;

import android.content.ContentValues;
import android.provider.BaseColumns;


public class Rekord extends AbsztraktRekord {  //NOPMD
	
	public void onInsertTermek(final String termek, final String allapot){
		final ContentValues termekErtek = new ContentValues();
		termekErtek.put(AdatbazisAdapter.getAzonositoTermeknev(), termek);
		termekErtek.put(AdatbazisAdapter.getAzonositoAllapot(), allapot);
		Adatbaziskezelo.getdbPeldany().insertOrThrow(AdatbazisAdapter.getTablaTermek(), null, termekErtek);
	}
	
	public void onInsertReszletek(final int termekNevId, final String datum, final double netto, final double brutto, final double mennyiseg, final String allapot){
		final ContentValues reszletekErtek = new ContentValues();
		reszletekErtek.put(AdatbazisAdapter.getAzonositoTermeknevId(), termekNevId);
		reszletekErtek.put(AdatbazisAdapter.getAzonositoDatum(), datum);
		reszletekErtek.put(AdatbazisAdapter.getAzonositoNettoar(), netto);
		reszletekErtek.put(AdatbazisAdapter.getAzonositoBruttoar(), brutto);
		reszletekErtek.put(AdatbazisAdapter.getAzonositoMennyiseg(), mennyiseg);
		reszletekErtek.put(AdatbazisAdapter.getAzonositoAllapot(), allapot);
		Adatbaziskezelo.getdbPeldany().insertOrThrow(AdatbazisAdapter.getTablaReszletek(), null, reszletekErtek);
	}
	
	public void onDeleteTermek(final int azonosito) {
		Adatbaziskezelo.getdbPeldany().delete(AdatbazisAdapter.getTablaTermek(),
				BaseColumns._ID+"="+azonosito,null);
	}

	public void onDeleteReszletek(final int azonosito) {
		Adatbaziskezelo.getdbPeldany().delete(AdatbazisAdapter.getTablaReszletek(),
				BaseColumns._ID+"="+azonosito,null);
	}
	
	public void onUpdateTermekAllapot(final String allapot, final int azonosito){
		final ContentValues allapotErtek = new ContentValues();
    	allapotErtek.put(AdatbazisAdapter.getAzonositoAllapot(), allapot);
    	Adatbaziskezelo.getdbPeldany().update(AdatbazisAdapter.getTablaTermek(), allapotErtek,
    			BaseColumns._ID+"="+azonosito,null);
	}
	
	public void onUpdateReszletekAllapot(final String allapot, final int azonosito){
		final ContentValues allapotErtek = new ContentValues();
    	allapotErtek.put(AdatbazisAdapter.getAzonositoAllapot(), allapot);
    	Adatbaziskezelo.getdbPeldany().update(AdatbazisAdapter.getTablaReszletek(), allapotErtek,
    			BaseColumns._ID+"="+azonosito,null);
	}
	
	public void onUpdateReszletekMennyiseg(final int mennyiseg, final int azonosito){
		final ContentValues mennyisegErtek = new ContentValues();
    	mennyisegErtek.put(AdatbazisAdapter.getAzonositoMennyiseg(), mennyiseg);
    	mennyisegErtek.put(AdatbazisAdapter.getAzonositoAllapot(), allapot.UJ.getAllapot());
    	Adatbaziskezelo.getdbPeldany().update(AdatbazisAdapter.getTablaReszletek(), mennyisegErtek,
    			BaseColumns._ID+"="+azonosito,null);
	}
	
	public void onUpdateTermekNev(final String nev, final int azonosito){
		final ContentValues nevErtek = new ContentValues();
		nevErtek.put(AdatbazisAdapter.getAzonositoTermeknev(), nev);
    	Adatbaziskezelo.getdbPeldany().update(AdatbazisAdapter.getTablaTermek(), nevErtek,
    			BaseColumns._ID+"="+azonosito,null);
	}
	

}
