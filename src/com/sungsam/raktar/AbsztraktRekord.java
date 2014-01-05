package com.sungsam.raktar;

import android.provider.BaseColumns;


public abstract class AbsztraktRekord implements BaseColumns{  //NOPMD
	
	public static final String ID=AbsztraktRekord._ID;  //NOPMD
	
	public static final String  _COUNT = "_count";
	
	abstract public void onInsertTermek(final String termek, final String allapot);
	
	abstract public void onInsertReszletek(final int termekNevId, final String datum, final double netto, final double brutto, final double mennyiseg, final String allapot);
	
	abstract public void onDeleteTermek(final int azonosito);
	
	abstract public void onDeleteReszletek(final int azonosito);
	
	abstract public void onUpdateTermekAllapot(final String allapot, final int azonosito);
	
	abstract public void onUpdateReszletekAllapot(final String allapot, final int azonosito);
	
	abstract public void onUpdateReszletekMennyiseg(final int mennyiseg, final int azonosito);
	
	abstract public void onUpdateTermekNev(final String nev, final int azonosito);
	
}
