package com.sungsam.raktar;  //NOPMD

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sungsam.raktar.AdatbazisAdapter.allapot;

public class TermekLista extends Activity {  //NOPMD
    
	private ArrayList<String> termekek;  //NOPMD
	private FastSearchListView listaNezet;  //NOPMD
	private Cursor kurzor;
	private Cursor kurzorAsync;
	private boolean letezike;
	private EditText lvSearch;
	private TermekAdapter termekAdapter;
	
	public void onCreate(final Bundle mentettAllas) {
        super.onCreate(mentettAllas);
        setContentView(R.layout.termek_lista);
        
        listaNezet = (FastSearchListView) findViewById(R.id.lista_termek);
        
        listaNezet.setLongClickable(true);
        new TermekLekTask().execute();
        
        // Termék kereséshez edittextre szövegnézés
        lvSearch=(EditText) findViewById(R.id.listViewSearch);
		lvSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence cs, int start, int before, int count) {
				Log.d("RAKTAR","ITT VAGYOK, a cs: "+cs);
				 if (count < before) {
                     // We're deleting char so we need to reset the adapter data
                     termekAdapter.resetData();
				 }
				termekAdapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
        
        listaNezet.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> adapterNezet, final View nezet, final int pozicio, final long azonosito) {
				final String termekNev =(String) ((TextView) nezet.findViewById(R.id.tv_termekelem)).getText();
				
				if(getResources().getString(R.string.uj_termek).equals(termekNev)){
					felugroHozzaadas();
				}else{
					kurzor=Adatbaziskezelo.getdbPeldany().rawQuery("SELECT * FROM "+ AdatbazisAdapter.getTablaTermek()+
	        				" WHERE "+AdatbazisAdapter.getAzonositoTermeknev()+"='"+termekNev+"'", null);
	        		kurzor.moveToFirst();
	        		final int termekNevId=kurzor.getInt(0);
					
					final Intent reszletek= new Intent(TermekLista.this, ReszletekLista.class);
					reszletek.putExtra("termekNevId", termekNevId);
					startActivity(reszletek);
				}
				
			}  
        });
        
        listaNezet.setOnItemLongClickListener(new OnItemLongClickListener() {
        	public boolean onItemLongClick(final AdapterView<?> adapterNezet, final View nezet, final int pozicio, final long azonosito) {
        		final String termekNev =(String) ((TextView) nezet.findViewById(R.id.tv_termekelem)).getText();
        		
        		if(!termekNev.equals(getResources().getString(R.string.uj_termek))){
        			kurzor=Adatbaziskezelo.getdbPeldany().rawQuery("SELECT * FROM "+ AdatbazisAdapter.getTablaTermek()+
            				" WHERE "+AdatbazisAdapter.getAzonositoTermeknev()+"='"+termekNev+"'", null);
            		kurzor.moveToFirst();
            		final int termekNevId=kurzor.getInt(0);
            		
            		kurzor=Adatbaziskezelo.getdbPeldany().rawQuery("SELECT COUNT(*) FROM "+ AdatbazisAdapter.getTablaReszletek()+
            				" WHERE "+AdatbazisAdapter.getAzonositoTermeknevId()+"='"+termekNevId+"'", null);   
            		kurzor.moveToFirst();
                    if(kurzor.getInt(0)==0){
                    	felugroTorlMod(termekNevId, termekNev);
                    }else{
                    	felugroModositas(termekNevId, termekNev);
                    }
            		kurzor.close();
            		
        		}
        		
        		return true; 
            }
        }); 
        
    }
	
	public void felugroHozzaadas(){
		letezike=false;
		
		final Dialog felugroAblak=new Dialog(TermekLista.this);
		felugroAblak.setContentView(R.layout.termek_hozzaad_felugro);
		felugroAblak.setCancelable(true);
		felugroAblak.setTitle(getResources().getString(R.string.felugro_hozzaad_cim));

		final EditText beirt = (EditText) felugroAblak.findViewById(R.id.et_dialog_termek);

		final Button felvetel=(Button)felugroAblak.findViewById(R.id.bt_dialog_felvetel);
		felvetel.setOnClickListener(new OnClickListener() {
			public void onClick(final View view) {
				if("".equals(beirt.getText().toString())){
					Toast.makeText(TermekLista.this, getResources().getString(R.string.hiba_uresnev), Toast.LENGTH_LONG).show();
				}else{
					kurzor=Adatbaziskezelo.getdbPeldany().rawQuery("SELECT "+ AdatbazisAdapter.getAzonositoTermeknev()
				            +" FROM "+ AdatbazisAdapter.getTablaTermek(), null);
			    	
			    	final String beirtNagy=beirt.getText().toString().toUpperCase(Locale.getDefault());
			    	
			    	while(kurzor.moveToNext()){
			    		if(kurzor.getString(0).toUpperCase(Locale.getDefault()).equals(beirtNagy)){
			    			letezike=true;
			    			Toast.makeText(TermekLista.this, getResources().getString(R.string.hiba_felveve), Toast.LENGTH_LONG).show();
			    		}
			    	}
			    	if(!letezike){
			    		final Rekord rekord=new Rekord();
				    	rekord.onInsertTermek(beirt.getText().toString(), allapot.UJ.getAllapot());
				    	new TermekLekTask().execute();
			    	}
			    	kurzor.close();
			    	felugroAblak.cancel();
				}
			}
		});    
		
		final Button megse=(Button)felugroAblak.findViewById(R.id.bt_dialog_megse);
		megse.setOnClickListener(new OnClickListener() {
			public void onClick(final View view) {
				felugroAblak.cancel();
			}
		}); 
		
		felugroAblak.show();
		
	}
	
	//Törlés módosítás egyben, mivel nincs alá részletek felvéve
	public void felugroTorlMod(final int azonosito, final String termekNev){
		final Dialog felugroAblak=new Dialog(TermekLista.this);
		felugroAblak.setContentView(R.layout.termek_torol_mod_felugro);
		felugroAblak.setCancelable(true);
		felugroAblak.setTitle(getResources().getString(R.string.felugro_torol_mod_cim));
		
		final Button igen=(Button)felugroAblak.findViewById(R.id.bt_dialog_igen);
		igen.setOnClickListener(new OnClickListener() {
			public void onClick(final View view) {
				final Rekord rekord=new Rekord();
				rekord.onDeleteTermek(azonosito);
				new TermekLekTask().execute();
				felugroAblak.cancel();
			}
		});  
		
		final Button nem=(Button)felugroAblak.findViewById(R.id.bt_dialog_nem);
		nem.setOnClickListener(new OnClickListener() {
			public void onClick(final View view) {
				felugroAblak.cancel();
			}
		}); 
		
		final Button modosit=(Button)felugroAblak.findViewById(R.id.bt_dialog_modositas);
		modosit.setOnClickListener(new OnClickListener() {
			public void onClick(final View view) {
				String ujNev = ((EditText) felugroAblak.findViewById(R.id.et_dialog_ujnev)).getText().toString();
				
				if("".equals(ujNev)){
					Toast.makeText(TermekLista.this, getResources().getString(R.string.hiba_ujnev), Toast.LENGTH_LONG).show();
				}else{
					final Rekord rekord=new Rekord();
					rekord.onUpdateTermekNev(ujNev, azonosito);
					new TermekLekTask().execute();
					felugroAblak.cancel();
				}
				
			}
		});  
		
		felugroAblak.show();
		
		}
	
	/**
	 * Csak módosítás, mivel van alatta termék felvéve
	 */
	public void felugroModositas(final int azonosito, final String termekNev){
		final Dialog felugroAblak=new Dialog(TermekLista.this);
		felugroAblak.setContentView(R.layout.termek_csak_mod_felugro);
		felugroAblak.setCancelable(true);
		felugroAblak.setTitle(getResources().getString(R.string.felugro_csak_mod_cim));
		
		final Button modosit=(Button)felugroAblak.findViewById(R.id.bt_dialog_modositas);
		modosit.setOnClickListener(new OnClickListener() {
			public void onClick(final View view) {
				String ujNev = ((EditText) felugroAblak.findViewById(R.id.et_dialog_ujnev)).getText().toString();
				
				if("".equals(ujNev)){
					Toast.makeText(TermekLista.this, getResources().getString(R.string.hiba_ujnev), Toast.LENGTH_LONG).show();
				}else{
					final Rekord rekord=new Rekord();
					rekord.onUpdateTermekNev(ujNev, azonosito);
					new TermekLekTask().execute();
					felugroAblak.cancel();
				}
				
			}
		});  
		
		felugroAblak.show();
		
	}
	
	   private class TermekLekTask extends AsyncTask<Void, Void, Void> {
			
		   private final String processMessage = getResources().getString(R.string.asynctask_lekeres);  //NOPMD
		   private ProgressDialog toltoAblak = null;
		   
		   
		   private void createProgressDialog() {
	            toltoAblak = new ProgressDialog(TermekLista.this);
	            toltoAblak.setMessage(processMessage);
	            toltoAblak.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            toltoAblak.setCancelable(true);
	            toltoAblak.show();
	        }
		   
			protected void onPreExecute() {
				createProgressDialog();
				termekek=new ArrayList<String>();
				kurzorAsync=Adatbaziskezelo.getdbPeldany().rawQuery("SELECT "+ AdatbazisAdapter.getAzonositoTermeknev()
			            +" FROM "+ AdatbazisAdapter.getTablaTermek()
			            +" ORDER BY "+ AdatbazisAdapter.getAzonositoTermeknev() +" COLLATE NOCASE;", null);
	        }
			
			protected Void doInBackground(final Void... parameter) {
				
				termekek.add(getResources().getString(R.string.uj_termek));
				
				while(kurzorAsync.moveToNext()){
					termekek.add(kurzorAsync.getString(0));
				}

				return null;
		     }
			
			protected void onPostExecute(final Void argumentumok2) {
				kurzorAsync.close();
				toltoAblak.dismiss();
				
				termekAdapter=new TermekAdapter(TermekLista.this, termekek);
				listaNezet.setAdapter(termekAdapter);
		     }

		 }

}