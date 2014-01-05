package com.sungsam.raktar;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class TermekAdapter extends BaseAdapter implements Filterable, SectionIndexer{
	
	private ArrayList<String> termekek; //NOPMD
	private ArrayList<String> origTermekek; //NOPMD
	private final LayoutInflater elrendBetolto; //NOPMD
	private static String sections = "abcdefghilmnopqrstuvz";

	public TermekAdapter(final Context context, final ArrayList<String> termekek) { //NOPMD
		this.termekek = termekek;
		this.origTermekek=termekek;
		elrendBetolto = LayoutInflater.from(context);
	}

	public int getCount() {
		return termekek.size();
	}

	public Object getItem(final int pozicio) {
		return termekek.get(pozicio);
	}

	public long getItemId(final int pozicio) {
		return pozicio;
	}

	public View getView(int pozicio, View hasznaltNezet, ViewGroup szuloNezet) {//NOPMD
		NezetHordozo nezetHordozo;
		
		if (hasznaltNezet == null) {
			hasznaltNezet = elrendBetolto.inflate(R.layout.termek_listaelem, null);
			nezetHordozo = new NezetHordozo();
		
			nezetHordozo.tvTermek = (TextView) hasznaltNezet.findViewById(R.id.tv_termekelem);

			hasznaltNezet.setTag(nezetHordozo);
		} else {
			nezetHordozo = (NezetHordozo) hasznaltNezet.getTag();
		}
		
		nezetHordozo.tvTermek.setText(termekek.get(pozicio));

		return hasznaltNezet;
	}

	static class NezetHordozo {
		private TextView tvTermek;
	}

	 public void resetData() {
         termekek = origTermekek;
	 }
	
	@Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
            	Log.d("TERMEKADAPTER","publish");
                termekek = (ArrayList<String>) results.values;
                TermekAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
            	Log.d("TERMEKADAPTER","perform, constraints: "+constraint);
            	FilterResults results = new FilterResults();
                // We implement here the filter logic
                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                	Log.d("TERMEKADAPTER","perform, null vagy nulla");
                    results.values = termekek;
                    results.count = termekek.size();
                    for(String s:termekek){
                    	Log.d("TERMEKADAPTER","perform, null vagy nulla, az elemek: "+s);
                    }
                    
                }
                else {
                	Log.d("TERMEKADAPTER","perform, NEM null vagy nulla");
                    // We perform filtering operation
                    List<String> termekList = new ArrayList<String>();
                     
                    for (String s : termekek) {
                        if (s.toUpperCase().startsWith(constraint.toString().toUpperCase())){
                            termekList.add(s);
                            Log.d("TERMEKADAPTER","perform, NEM null vagy nulla, szûrés közben az elem: "+s);
                        }
                    }
                     
                    results.values = termekList;
                    results.count = termekList.size();
             
                    for(String s:termekList){
                    	Log.d("TERMEKADAPTER","perform, NEM null vagy nulla, az elemek: "+s);
                    }
                    
                }
                return results;
            }
        };
    }

	@Override
    public int getPositionForSection(int section) {
            Log.d("ListView", "Get position for section");
            for (int i=0; i < this.getCount(); i++) {
                    String item = ((String) this.getItem(i)).toLowerCase();
                    if (item.charAt(0) == sections.charAt(section))
                            return i;
            }
            return 0;
    }

    @Override
    public int getSectionForPosition(int arg0) {
            Log.d("ListView", "Get section");
            return 0;
    }

    @Override
    public Object[] getSections() {
            Log.d("ListView", "Get sections");
            String[] sectionsArr = new String[sections.length()];
            for (int i=0; i < sections.length(); i++)
                    sectionsArr[i] = "" + sections.charAt(i);
            
            return sectionsArr;
            
    }
	
}
