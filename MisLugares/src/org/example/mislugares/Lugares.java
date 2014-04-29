package org.example.mislugares;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Lugares {
	static final String TAG = "MyActivity";
	protected static GeoPunto posicionActual = new GeoPunto(0,0);
	private static LugaresBD lugaresBD;

	static Lugar elemento(int id){
		Lugar lugar = null;
	    SQLiteDatabase bd = lugaresBD.getReadableDatabase();
	    Cursor cursor = bd.rawQuery("SELECT * FROM lugares WHERE _id = " + id, null);
	    if (cursor.moveToNext()){
	        lugar = new Lugar();
	        lugar.setNombre(cursor.getString(1));
	        lugar.setDireccion(cursor.getString(2));
	        lugar.setPosicion(new GeoPunto(cursor.getDouble(3), cursor.getDouble(4)));
	        lugar.setTipo(TipoLugar.values()[cursor.getInt(5)]);
	        lugar.setFoto(cursor.getString(6));
	        lugar.setTelefono(cursor.getInt(7));
	        lugar.setUrl(cursor.getString(8));
	        lugar.setComentario(cursor.getString(9));
	        lugar.setFecha(cursor.getLong(10));
	        lugar.setValoracion(cursor.getFloat(11));
	    }
	    cursor.close();
	    bd.close();
	    return lugar;
	}
	
	public static void actualizaLugar(int id, Lugar lugar){
	    SQLiteDatabase bd = lugaresBD.getWritableDatabase();
	    bd.execSQL("UPDATE lugares SET nombre = '"+ lugar.getNombre() +
	        "', direccion = '" + lugar.getDireccion() +
	        "', longitud = " + lugar.getPosicion().getLongitud()  +
	        " , latitud = " + lugar.getPosicion().getLatitud()  +
	        " , tipo = " + lugar.getTipo().ordinal() +
	        " , foto = '" + lugar.getFoto()  +
	        "', telefono = " + lugar.getTelefono()  +
	        " , url = '" + lugar.getUrl()  +
	        "', comentario = '" + lugar.getComentario()  +
	        "', fecha = " + lugar.getFecha()  +
	        " , valoracion = " + lugar.getValoracion()  +
	        " WHERE _id = "+ id);
	        bd.close();
	}
	
	static int nuevo(){
		int id = -1;
	    Lugar lugar = new Lugar();
	    SQLiteDatabase bd = lugaresBD.getWritableDatabase();
	    bd.execSQL("INSERT INTO lugares (longitud, latitud, tipo, fecha) VALUES ( "+
	       lugar.getPosicion().getLongitud()+", "+lugar.getPosicion().getLatitud()+", "+
	       lugar.getTipo().ordinal()+", "+lugar.getFecha()+")");
	    Cursor c = bd.rawQuery("SELECT _id FROM lugares WHERE fecha = " +
	                                lugar.getFecha(), null);
	    if (c.moveToNext()){
	        id = c.getInt(0);
	    }
	    c.close();
	    bd.close();
	    return id;
	}
	
	static void borrar(int id){
		SQLiteDatabase bd = lugaresBD.getWritableDatabase();
	    bd.execSQL("DELETE FROM lugares WHERE _id = " + id );
	    bd.close();
	}
	
	public static int buscarNombre(String nombre) {
	    int id = -1;
	    SQLiteDatabase bd = lugaresBD.getReadableDatabase();
	    Cursor c = bd.rawQuery("SELECT * FROM lugares WHERE nombre = '" + nombre + "'", null);
	    if (c.moveToNext()){
	        id = c.getInt(0);
	    }
	    c.close();
	    bd.close();
	    return id;
	}

	public static void indicializaBD(Context contexto){
		lugaresBD = new LugaresBD(contexto);   
	}

	public static Cursor listado() {
		SQLiteDatabase bd = lugaresBD.getReadableDatabase();
		return bd.rawQuery("SELECT * FROM lugares", null);
	}
}