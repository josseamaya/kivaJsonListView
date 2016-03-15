package com.example.joseamaya.kivajsonlistview;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class patrocinadores extends AppCompatActivity {
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_patrocinadores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String posicionP=this.getIntent().getStringExtra("numeroPatrocinador");
        llenarInformacionPatrocinadores(posicionP);


    }
    private void llenarInformacionPatrocinadores(String p) {
        final Context context=this;

        JsonObjectRequest jor=new JsonObjectRequest(
                "http://api.kivaws.org/v1/partners/"+p+".json",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String patrocinadores=null;
                        try {
                            patrocinadores = response.getString("partners");
                            JSONArray arregloPersonas = new JSONArray(patrocinadores);

                            JSONObject persona = (JSONObject) arregloPersonas.get(0);

                            String nombre = persona.getString("name");
                            String estado = persona.getString("status");

                            JSONObject imagen = persona.getJSONObject("image");
                            String idImagen=imagen.getString("id");

                            String diaInicio=persona.getString("start_date");

                            String paises=persona.getString("countries");

                            JSONArray arregloPaises =new JSONArray(paises);
                            String listaPaises="";

                            JSONObject pais=null;

                            for (int a=0;a<=1;a++) {
                                pais = (JSONObject) arregloPaises.get(1);
                                listaPaises = pais.getString("region")+"\n";
                            }


                            String textoPatrocinadores="Nombre: "+nombre+"\n"+"Estado: "+estado+"\n"+"Inicio: "+diaInicio+"\n";

                            NetworkImageView avatar = (NetworkImageView) findViewById(R.id.networkImageViewPatrocinadores);
                            avatar.setImageUrl("https://www.kiva.org/img/512/" + idImagen + ".jpg", MySingleton.getInstance(context).getImageLoader());

                            TextView tv = (TextView) findViewById(R.id.textViewPatrocinadores);
                            tv.setText(textoPatrocinadores);

                            TextView tv2=(TextView) findViewById(R.id.textViewPaises);
                            tv2.setText(listaPaises);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            TextView tv = (TextView) findViewById(R.id.textViewPatrocinadores);
                            tv.setText("error");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        MySingleton.getInstance(mContext).addToRequestQueue(jor);

    }
}
