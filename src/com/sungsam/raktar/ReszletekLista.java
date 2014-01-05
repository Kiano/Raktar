package com.sungsam.raktar;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sungsam.raktar.AdatbazisAdapter.allapot;

public class ReszletekLista extends Activity {  //NOPMD
	
	private ArrayList<String> reszletek;  //NOPMD
	private ListView listaNezet;  //NOPMD
	private Cursor kurzor;
	private Cursor kurzorAsync;
	private int termekNevId;
	private boolean letezike;
	
	public void onCreate(final Bundle mentettAllas) {
        super.onCreate(mentettAllas);
        setContentView(R.layout.reszletek_lista);
        
        listaNezet = (ListView) findViewById(R.id.lista_reszletek);
        listaNezet.setLongClickable(true);
        
        final Intent bejovo=getIntent();
        if(bejovo.getIntExtra("termekNevId", -1)==-1){
        	Toast.makeText(ReszletekLista.this, getResources().getString(R.string.hiba_reszletek_extra), Toast.LENGTH_LONG).show();
        	finish();
        }else{
        	termekNevId=bejovo.getIntExtra("termekNevId", -1);
        	new ReszletekLekTask().execute();
        } 
        
        listaNezet.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> adapterNezet, final View nezet, final int pozicio, final long azonosito) {
				final String datum =(String) ((TextView) nezet.findViewById(R.id.tv_datum_reszlet)).getText();
				if(getResources().getString(R.string.uj_sor).equals(datum)){
					felugroHozzaadas();
				}
			}  
        });
        
        listaNezet.setOnItemLongClickListener(new OnItemLongClickListener() {
        	public boolean onItemLongClick(final AdapterView<?> adapterNezet, final View nezet, final int pozicio, final long azonosito) {
        		final String datum =(String) ((TextView) nezet.findViewById(R.id.tv_datum_reszlet)).getText();
        		final String netto =(String) ((TextView) nezet.findViewById(R.id.tv_nettoar_reszlet)).getText();
        		final String brutto =(String) ((TextView) nezet.findViewById(R.id.tv_bruttoar_reszlet)).getText();
        		final String mennyiseg =(String) ((TextView) nezet.findViewById(R.id.tv_mennyiseg_reszlet)).getText();
        		
        		if(!datum.equals(getResources().getString(R.string.uj_sor))){
        			kurzor=Adatbaziskezelo.getdbPeldany().rawQuery("SELECT * FROM "+ AdatbazisAdapter.getTablaReszletek()+
            				" WHERE "+AdatbazisAdapter.getAzonositoTermeknevId()+"='"+termekNevId+"'"+ 
        					" AND "+AdatbazisAdapter.getAzonositoDatum()+"='"+datum+"'"+  //NOPMD
        					" AND "+AdatbazisAdapter.getAzonositoNettoar()+"='"+netto+"'"+ 
        					" AND "+AdatbazisAdapter.getAzonositoBruttoar()+"='"+brutto+"'"+ 
        					" AND "+AdatbazisAdapter.getAzonositoMennyiseg()+"='"+mennyiseg+"'"
            				,null);
            		kurzor.moveToFirst();
            		final int reszletekId=kurzor.getInt(0);
            		
            		final int regiMennyiseg=Integer.parseInt(mennyiseg);
                    felugroTorlMod(reszletekId, regiMennyiseg);
                    
            		kurzor.close();
            		
        		}
        		
        		return true; 
            }
        }); 
        
        
	}
	
	public void felugroTorlMod(final int azonosito, final int mennyisegRegi){
		final Dialog felugroAblak=new Dialog(ReszletekLista.this);
		felugroAblak.setContentView(R.layout.reszletek_torol_mod_felugro);
		felugroAblak.setCancelable(true);
		felugroAblak.setTitle(getResources().getString(R.string.felugro_torol_mod_cim));
		
		final Button igen=(Button)felugroAblak.findViewById(R.id.bt_dialog_igen);
		igen.setOnClickListener(new OnClickListener() {
			public void onClick(final View view) {
				final Rekord rekord=new Rekord();
				rekord.onDeleteReszletek(azonosito);
				new ReszletekLekTask().execute();
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
				final EditText mennyiseg = (EditText) felugroAblak.findViewById(R.id.et_dialog_mennyiseg);
				
				if("".equals(mennyiseg.getText().toString())){
					Toast.makeText(ReszletekLista.this, getResources().getString(R.string.hiba_mennyiseg), Toast.LENGTH_LONG).show();
				}else{
					final Rekord rekord=new Rekord();
					rekord.onUpdateReszletekMennyiseg(Integer.parseInt(mennyiseg.getText().toString())+mennyisegRegi, azonosito);
					new ReszletekLekTask().execute();
					felugroAblak.cancel();
				}
				
			}
		});  
		
		
		
		felugroAblak.show();
		
		}
	
	public void felugroHozzaadas(){
		letezike=false;
		
		final Dialog felugroAblak=new Dialog(ReszletekLista.this);
		felugroAblak.setContentView(R.layout.reszletek_hozzaad_felugro);
		felugroAblak.setCancelable(true);
		felugroAblak.setTitle(getResources().getString(R.string.felugro_reszlhozza_cim));

		final EditText datum = (EditText) felugroAblak.findViewById(R.id.et_datum);
		final EditText netto = (EditText) felugroAblak.findViewById(R.id.et_netto);
		final EditText mennyiseg = (EditText) felugroAblak.findViewById(R.id.et_mennyiseg);

		final Button felvetel=(Button)felugroAblak.findViewById(R.id.bt_dialog_felvetel);
		felvetel.setOnClickListener(new OnClickListener() {
			public void onClick(final View view) {
				if("".equals(datum.getText().toString()) || "".equals(netto.getText().toString()) || "".equals(mennyiseg.getText().toString())){
					Toast.makeText(ReszletekLista.this, getResources().getString(R.string.hiba_reszlkitoltes), Toast.LENGTH_LONG).show();
				}else{
					kurzor=Adatbaziskezelo.getdbPeldany().rawQuery("SELECT * FROM "+ AdatbazisAdapter.getTablaReszletek()
							+" WHERE "+AdatbazisAdapter.getAzonositoDatum()+"='"+datum.getText().toString()+"'"
							+" AND "+AdatbazisAdapter.getAzonositoNettoar()+"="+netto.getText().toString()
							+" AND "+AdatbazisAdapter.getAzonositoMennyiseg()+"="+mennyiseg.getText().toString()
							+" AND "+AdatbazisAdapter.getAzonositoTermeknevId()+"="+termekNevId , null);

			    	while(kurzor.moveToNext()){
			    		letezike=true;
			    		Toast.makeText(ReszletekLista.this, getResources().getString(R.string.hiba_reszletek_felveve), Toast.LENGTH_LONG).show();
			    	}
			    	if(!letezike){
			    		final RadioGroup radioCsoport = (RadioGroup) felugroAblak.findViewById(R.id.rg_afa);
			    		final RadioButton kivalasztott = (RadioButton) felugroAblak.findViewById(radioCsoport.getCheckedRadioButtonId());
			    		final double bruttoar=Double.parseDouble(netto.getText().toString())*(1+(Double.parseDouble(kivalasztott.getText().toString())/100));
			    		final double bruttoarKerek=Math.round(bruttoar*100.0)/100.0;
			    		
			    		final Rekord rekord=new Rekord();
				    	rekord.onInsertReszletek(
				    					termekNevId,
				    					datum.getText().toString(), 
				    					Double.parseDouble(netto.getText().toString()), 
				    					bruttoarKerek, 
				    					Double.parseDouble(mennyiseg.getText().toString()),
				    					allapot.UJ.getAllapot());
				    	new ReszletekLekTask().execute();
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

	
	private class ReszletekLekTask extends AsyncTask<Void, Void, Void> {
		
		   private final String processMessage = getResources().getString(R.string.asynctask_lekeres);  //NOPMD
		   private ProgressDialog toltoAblak = null;
		   
		   
		   private void createProgressDialog() {
	            toltoAblak = new ProgressDialog(ReszletekLista.this);
	            toltoAblak.setMessage(processMessage);
	            toltoAblak.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            toltoAblak.setCancelable(true);
	            toltoAblak.show();
	        }
		   
			protected void onPreExecute() {
				createProgressDialog();
				reszletek=new ArrayList<String>();
				kurzorAsync=Adatbaziskezelo.getdbPeldany().rawQuery("SELECT * FROM "+ AdatbazisAdapter.getTablaReszletek()
			            +" WHERE "+AdatbazisAdapter.getAzonositoTermeknevId()+"="+termekNevId 
						+" ORDER BY "+ AdatbazisAdapter.getAzonositoDatum()+";", null);
	        }
			
			protected Void doInBackground(final Void... parameter) {
				
				final String elvalaszto=":::";  //NOPMD
				
				reszletek.add(getResources().getString(R.string.uj_sor)+elvalaszto+getResources().getString(R.string.uj_sor)+elvalaszto+getResources().getString(R.string.uj_sor)+elvalaszto+getResources().getString(R.string.uj_sor));
				
				while(kurzorAsync.moveToNext()){
					reszletek.add(kurzorAsync.getString(2)+elvalaszto+kurzorAsync.getDouble(3)+elvalaszto+kurzorAsync.getDouble(4)+elvalaszto+kurzorAsync.getDouble(5));
				}

				return null;
		     }
			
			protected void onPostExecute(final Void argumentumok2) {
				kurzorAsync.close();
				toltoAblak.dismiss();
				listaNezet.setAdapter(new ReszletekAdapter(ReszletekLista.this, reszletek));
		     }

		 }

}
