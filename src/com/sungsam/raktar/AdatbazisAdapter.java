package com.sungsam.raktar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class AdatbazisAdapter extends SQLiteOpenHelper{

	    //Adatbázis verzió
		private static final int ADATBAZIS_VERZIO = 1;

		//Adatbázis neve
		private static final String ADATBAZIS_NEV = "raktar.db";

		//Táblák
		private static final String TABLA_TERMEK = "termek";
		private static final String TABLA_RESZLETEK = "reszletek";

		//Rekord azonosítók
		private static final String AZONOSITO_TERMEKNEV = "termeknev";  //NOPMD
		private static final String AZONOSITO_TERMEKNEV_ID = "termeknev_id";  //NOPMD
		private static final String AZONOSITO_DATUM = "datum";
		private static final String AZONOSITO_MENNYISEG = "mennyiseg";  //NOPMD
		private static final String AZONOSITO_NETTOAR = "nettoar";  //NOPMD
		private static final String AZONOSITO_BRUTTOAR = "bruttoar";  //NOPMD
		private static final String AZONOSITO_ALLAPOT = "allapot";  //NOPMD
		
		
		public enum allapot {
			UJ("uj"), TOROL("torol"), FRISSIT("frissit"), ZAROLVA("zarolva"), KESZ("kesz");
		 
			private String allapot;
		 
			private allapot(final String allapot) {
				this.allapot = allapot;
			}
		 
			public String getAllapot() {
				return allapot;
			}
		}
		
		public AdatbazisAdapter(final Context context, final String neve, final int verzio) {
			super(context, neve, null, verzio);
		}
	
		public void onCreate(final SQLiteDatabase adatbazis) {
			
			final String createTermek = "CREATE TABLE " + TABLA_TERMEK  
					+ " ( "+ BaseColumns._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ AZONOSITO_TERMEKNEV + " TEXT, "  
					+ AZONOSITO_ALLAPOT + " TEXT )";
			adatbazis.execSQL(createTermek);
			
			final String createReszletek = "CREATE TABLE " + TABLA_RESZLETEK
					+ " ( "+ BaseColumns._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ AZONOSITO_TERMEKNEV_ID + " INTEGER, "  //NOPMD
					+ AZONOSITO_DATUM + " TEXT, " 
					+ AZONOSITO_NETTOAR + " REAL, "
					+ AZONOSITO_BRUTTOAR + " REAL, "
					+ AZONOSITO_MENNYISEG + " REAL, " 
					+ AZONOSITO_ALLAPOT + " TEXT, " 
					+ "FOREIGN KEY ("+AZONOSITO_TERMEKNEV_ID+") REFERENCES "+TABLA_TERMEK+"("+BaseColumns._ID+") )";
			adatbazis.execSQL(createReszletek);			
					
		}
		
	public void onUpgrade(final SQLiteDatabase adatbazis, final int regiVerzio, final int ujVerzio) {  //NOPMD
	}

	public static int getAdatbazisVerzio() {
		return ADATBAZIS_VERZIO;
	}

	public static String getAdatbazisNev() {
		return ADATBAZIS_NEV;
	}

	public static String getTablaTermek() {
		return TABLA_TERMEK;
	}

	public static String getTablaReszletek() {
		return TABLA_RESZLETEK;
	}

	public static String getAzonositoTermeknev() {
		return AZONOSITO_TERMEKNEV;
	}

	public static String getAzonositoTermeknevId() {
		return AZONOSITO_TERMEKNEV_ID;
	}

	public static String getAzonositoDatum() {
		return AZONOSITO_DATUM;
	}

	public static String getAzonositoMennyiseg() {
		return AZONOSITO_MENNYISEG;
	}

	public static String getAzonositoAllapot() {
		return AZONOSITO_ALLAPOT;
	}

	public static String getAzonositoNettoar() {
		return AZONOSITO_NETTOAR;
	}

	public static String getAzonositoBruttoar() {
		return AZONOSITO_BRUTTOAR;
	}

	
}
