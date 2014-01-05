package com.sungsam.raktar;


import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Adatbaziskezelo extends Application{
	
	private static SQLiteDatabase adatbazisSQL;
    private static AdatbazisAdapter dbPeldany;
    private static final int ADATBAZIS_VERZIO = 1;
    private static final String ADATBAZIS_NEVE = "raktar.db";

    public void onCreate (){     
       super.onCreate();  
       inicializalas(getApplicationContext());
        
    } 

	public static synchronized void close() {  //NOPMD
        if (dbPeldany != null){
        	adatbazisSQL.close();
        }
    }
	
	public static SQLiteDatabase getdbPeldany() { 
		if (null == adatbazisSQL && null != dbPeldany) {    //NOPMD
			adatbazisSQL =  dbPeldany.getWritableDatabase();
		}
		return adatbazisSQL;
	}
    
    
    public static void inicializalas(final Context context) {
	    
		if (null == dbPeldany) {    //NOPMD
			dbPeldany =new AdatbazisAdapter(context , ADATBAZIS_NEVE, ADATBAZIS_VERZIO);
		}
	}
    
}
