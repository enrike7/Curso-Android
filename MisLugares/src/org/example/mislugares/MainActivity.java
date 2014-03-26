package org.example.mislugares;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
	public BaseAdapter adaptador;
	private final static int RESULTADO_LIST = 1;
	private MediaPlayer mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adaptador = new AdaptadorLugares(this);
		setListAdapter(adaptador);
		Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
		mp = MediaPlayer.create(this, R.raw.audio);
		mp.start();
	}
	
	@Override
	 protected void onSaveInstanceState(Bundle estadoGuardado){
		super.onSaveInstanceState(estadoGuardado);
		if (mp != null) {
			int pos = mp.getCurrentPosition();
			estadoGuardado.putInt("posicion", pos);   
		}
	 }
	 
	 @Override
	 protected void onRestoreInstanceState(Bundle estadoGuardado){
		 super.onRestoreInstanceState(estadoGuardado);
		 if (estadoGuardado != null && mp != null) {
			 int pos = estadoGuardado.getInt("posicion");
			 mp.seekTo(pos);
		 }
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	public void lanzarAcercaDe(View view) {
		Intent i = new Intent(this,AcercaDe.class);
		startActivity(i);
	}
	
	public void salir(View view) {
		finish();
	}
	
	public void lanzarPreferencias(View view) {
		Intent i = new Intent(this,Preferencias.class);
		startActivity(i);
	}
	
	public void mostrarPreferencias(View view) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String s = "notificaciones: " + pref.getBoolean("notificaciones", true) + ", distancia mínima: " 
				+ pref.getString("distancia", "?");
		
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}
	
/*	public void lanzarVistaLugar(View view) {
		 final EditText entrada = new EditText(this);
	        entrada.setText("0");
	        new AlertDialog.Builder(this)
	           .setTitle("Selección de lugar")
	           .setMessage("indica su id:")
	           .setView(entrada)
	           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int whichButton) {
	                   long id = Long.parseLong(entrada.getText().toString());
	                      Intent i = new Intent(MainActivity.this, VistaLugar.class);
	                      i.putExtra("id", id);
	                      startActivity(i);            
	               }})
	           .setNegativeButton("Cancelar", null)
	           .show();          
	}*/
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.acercaDe:
				lanzarAcercaDe(null);
				break;
			case R.id.config:
				lanzarPreferencias(null);
				break;
			default:
				
		}
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView listView, View vista, int posicion, long id) {
		super.onListItemClick(listView, vista, posicion, id);
		Intent intent = new Intent(this, VistaLugar.class);
		intent.putExtra("id", id);
		startActivityForResult(intent, RESULTADO_LIST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULTADO_LIST) {
			setListAdapter(adaptador);
		}
	}
	
	@Override
	protected void onStart() {
	   super.onStart();
	   Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
	}
	 
	@Override
	protected void onResume() {
	   super.onResume();
	   Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
	   mp.start();
	}
	 
	@Override
	protected void onPause() {
	   Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
	   super.onPause();
	}
	 
	@Override
	protected void onStop() {
	   super.onStop();
	   Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
	   mp.stop();
	}
	 
	@Override
	protected void onRestart() {
	   super.onRestart();
	   Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
	   mp.start();
	}
	 
	@Override
	protected void onDestroy() {
	   super.onDestroy();
	   Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();     
	}
}

