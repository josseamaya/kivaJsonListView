package com.example.joseamaya.kivajsonlistview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaPatrocinadores extends AppCompatActivity {
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_patrocinadores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext=this;
        String url="http://api.kivaws.org/v1/partners.json";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getKivaPatro("http://api.kivaws.org/v1/partners.json");
            }
        });

        getKivaPatro(url);

        ListView lv3=(ListView) findViewById(R.id.listViewPatrocinadores);
        lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(mContext, patrocinadores.class);
                intent.putExtra("numeroPatrocinador", Integer.toString(position + 1));
                startActivity(intent);

            }
        });


    }
    private void getKivaPatro(String url) {
        final Context context=this;
        JsonObjectRequest jor=new JsonObjectRequest(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray patro=response.getJSONArray("partners");

                            ArrayList<JSONObject> dataSourse=new ArrayList<JSONObject>();
                            for(int i=0;i<patro.length();i++)
                            {
                                dataSourse.add(patro.getJSONObject(i));

                            }
                            AdaptadorPatrocinadores adapter=new AdaptadorPatrocinadores(context,0,dataSourse);
                            ((ListView)findViewById(R.id.listViewPatrocinadores)).setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            TextView tv4=(TextView)findViewById(R.id.textViewPruebaPatro);
                            tv4.setText("errorCodigo");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TextView tv=(TextView)findViewById(R.id.textViewPruebaPatro);
                        tv.setText("errorResponse");
                    }
                }
        );
        MySingleton.getInstance(mContext).addToRequestQueue(jor);
    }



}
