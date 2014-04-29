package org.example.mislugares;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends FragmentActivity implements OnInfoWindowClickListener {

    private GoogleMap mapa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa)).getMap();
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.setMyLocationEnabled(true);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.getUiSettings().setCompassEnabled(true);   

        boolean primero = true;
        Cursor c = Lugares.listado();
        while (c.moveToNext()){
            GeoPunto p = new GeoPunto(c.getDouble(3), c.getDouble(4));
            if (p != null && p.getLatitud() != 0) {
                if (primero){
                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(p.getLatitud(), p.getLongitud()), 12));
                    primero = false;
                }
                BitmapDrawable iconoDrawable = (BitmapDrawable) getResources()
                        .getDrawable(TipoLugar.values()[c.getInt(5)].getRecurso());
                Bitmap iGrande = iconoDrawable.getBitmap();
                Bitmap icono = Bitmap.createScaledBitmap(iGrande,
                        iGrande.getWidth() / 7, iGrande.getHeight() / 7, false);
                mapa.addMarker(new MarkerOptions()
                        .position(new LatLng(p.getLatitud(), p.getLongitud()))
                        .title(c.getString(1)).snippet(c.getString(2))
                        .icon(BitmapDescriptorFactory.fromBitmap(icono)));
            }
        }       
        mapa.setOnInfoWindowClickListener(this);
    }

    @Override public void onInfoWindowClick(Marker marker) {
        int id = Lugares.buscarNombre(marker.getTitle());
        if (id!=-1){
            Intent intent = new Intent(this, VistaLugar.class);
            intent.putExtra("id", (long) id);
            startActivity(intent);       
        }
    }
}