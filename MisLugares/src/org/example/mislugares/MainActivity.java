package org.example.mislugares;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements LocationListener {
	
	public BaseAdapter adaptador;
	private final static int RESULTADO_LIST = 1;
	private static final long DOS_MINUTOS = 2 * 60 * 1000;
	private MediaPlayer mp;
	private LocationManager manejador;
	private Location mejorLocaliz;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adaptador = new AdaptadorLugares(this);
		setListAdapter(adaptador);
		mp = MediaPlayer.create(this, R.raw.audio);
		mp.start();
		
		manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
		if(manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		}
		if(manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
		}
		
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
	
	private void activarProveedores() { 
		if(manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
			manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER,20 * 1000, 5, (LocationListener) this);
		}
		
		if(manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			manejador.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10 * 1000, 10, (LocationListener) this);
		}
	}
	
	
	@Override
	protected void onStart() {
	   super.onStart();
	}
	 
	@Override
	protected void onResume() {
	   super.onResume();
	   activarProveedores();
	   mp.start();
	}
	 
	@Override
	protected void onPause() {
	   super.onPause();
	   manejador.removeUpdates((LocationListener) this);
	}
	 
	@Override
	protected void onStop() {
	   super.onStop();
	   mp.pause();
	}
	 
	@Override
	protected void onRestart() {
	   super.onRestart();
	   mp.start();
	}
	 
	@Override
	protected void onDestroy() {
	   super.onDestroy();
	   mp.stop();
	}
	
	@Override public void onLocationChanged(Location location) {
	       Log.d(Lugares.TAG, "Nueva localización: "+location);
	       actualizaMejorLocaliz(location);
	}
	 

	@Override public void onProviderDisabled(String proveedor) {
	       Log.d(Lugares.TAG, "Se deshabilita: "+proveedor);
	       activarProveedores();
	}

	@Override    public void onProviderEnabled(String proveedor) {
	       Log.d(Lugares.TAG, "Se habilita: "+proveedor);
	       activarProveedores();
	}

	@Override
	public void onStatusChanged(String proveedor, int estado, Bundle extras) {
	       Log.d(Lugares.TAG, "Cambia estado: "+proveedor);
	       activarProveedores();
	}
	
	private void actualizaMejorLocaliz(Location localiz) {
	       if (mejorLocaliz == null
	                    || localiz.getAccuracy() < 2*mejorLocaliz.getAccuracy()
	                    || localiz.getTime() - mejorLocaliz.getTime() > DOS_MINUTOS) {
	             Log.d(Lugares.TAG, "Nueva mejor localización");
	             mejorLocaliz = localiz;
	             Lugares.posicionActual.setLatitud(localiz.getLatitude());
	             Lugares.posicionActual.setLongitud(localiz.getLongitude());
	       }
	}
}

