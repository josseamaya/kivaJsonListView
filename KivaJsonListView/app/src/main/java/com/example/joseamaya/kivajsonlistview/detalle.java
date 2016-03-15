package com.example.joseamaya.kivajsonlistview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class detalle extends AppCompatActivity {
    public static Context mContext;
    JSONObject jsonKivaP;
    String idPatrocinador="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        mContext=this;
        String tempkiva=this.getIntent().getStringExtra("kiva");
        Integer posicion=this.getIntent().getIntExtra("numero", 0);

        try {
              jsonKivaP= new JSONObject(tempkiva);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        llenarInformacion(posicion);

    }

    private void llenarInformacion(Integer p){
        String loans=null;

        try {
            loans = jsonKivaP.getString("loans");
            JSONArray arregloPersonas = new JSONArray(loans);

            JSONObject persona = (JSONObject) arregloPersonas.get(p);

            String nombre = persona.getString("name");
            String monto = persona.getString("loan_amount");

            JSONObject imagen = persona.getJSONObject("image");
            String idImagen=imagen.getString("id");

            String activiad=persona.getString("activity");
            String uso=persona.getString("use");
            String sector=persona.getString("sector");
            idPatrocinador= persona.getString("partner_id");

            NetworkImageView avatar = (NetworkImageView) findViewById(R.id.networkImageViewFoto);
            avatar.setImageUrl("https://www.kiva.org/img/512/" + idImagen + ".jpg", MySingleton.getInstance(mContext).getImageLoader());

            TextView tv = (TextView) findViewById(R.id.textViewNombre2);
            tv.setText(nombre);
            TextView tv2 = (TextView) findViewById(R.id.textViewActividad2);
            tv2.setText(activiad);
            TextView tv3 = (TextView) findViewById(R.id.textViewSector);
            tv3.setText(sector);
            TextView tv4 = (TextView) findViewById(R.id.textViewUso);
            tv4.setText(uso);
            TextView tv5 = (TextView) findViewById(R.id.textViewMonto);
            tv5.setText(monto);


        } catch (JSONException e) {
            e.printStackTrace();
            TextView tvJoseNombre = (TextView) findViewById(R.id.textViewNombre2);
            tvJoseNombre.setText("error");

        }
    }
    public void onClickPatrocinadores(View v){
        Intent intent=new Intent (mContext,patrocinadores.class);
        intent.putExtra("numeroPatrocinador", idPatrocinador);
        startActivity(intent);
    }
}
