package org.example.mislugares;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AdaptadorCursorLugares extends CursorAdapter {
	   private LayoutInflater inflador; // Crea Layouts a partir del XML
	   TextView nombre, direccion, distancia;
	   ImageView foto;
	   RatingBar valoracion;

	   public AdaptadorCursorLugares(Context contexto, Cursor c) {
	      super(contexto, c, false);
	   }

	   @Override
	   public View newView(Context contexto, Cursor c, ViewGroup padre) {
	      inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      View vista = inflador.inflate(R.layout.elemento_lista, padre, false);
	      return vista;
	   }

	   @Override
	    public void bindView(View vista, Context contexto, Cursor c) {
	      nombre = (TextView) vista.findViewById(R.id.nombre);
	      direccion = (TextView) vista.findViewById(R.id.direccion);
	      foto = (ImageView) vista.findViewById(R.id.foto);
	      valoracion = (RatingBar) vista.findViewById(R.id.valoracion);
	      nombre.setText(c.getString(c.getColumnIndex("nombre")));
	      direccion.setText(c.getString(c.getColumnIndex("direccion")));
	      int tipo = c.getInt(c.getColumnIndex("tipo"));
	      foto.setImageResource(TipoLugar.values()[tipo].getRecurso());
	      foto.setScaleType(ImageView.ScaleType.FIT_END);
	      valoracion.setRating(c.getFloat(c.getColumnIndex("valoracion")));
	      distancia = (TextView) vista.findViewById(R.id.distancia);
	    GeoPunto posicion = new GeoPunto(
	        c.getDouble(c.getColumnIndex("longitud")),
	        c.getDouble(c.getColumnIndex("latitud")));
	      if (Lugares.posicionActual != null && posicion != null
	            && posicion.getLatitud() != 0) {
	         int d = (int) Lugares.posicionActual.distancia(posicion);
	         if(d < 2000) {
	            distancia.setText(d + " m");
	         } else {
	            distancia.setText(d / 1000 + "Km");
	         }
	      }
	   }

}