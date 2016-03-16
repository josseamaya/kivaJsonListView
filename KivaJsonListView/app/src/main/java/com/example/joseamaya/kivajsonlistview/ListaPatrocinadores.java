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
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListaPatrocinadores extends AppCompatActivity {
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_patrocinadores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Nuestros Socios");

        mContext=this;
        String url="http://api.kivaws.org/v1/partners.json";
        getKivaPatro(url);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Lista Actualizada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getKivaPatro("http://api.kivaws.org/v1/partners.json");
            }
        });

        ListView lv3=(ListView) findViewById(R.id.listViewPatrocinadores);
        lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) view.findViewById(R.id.textPatroId);
                final String patroId = (String) textView.getText();

                Intent intent = new Intent(mContext, patrocinadores.class);
                intent.putExtra("numeroPatrocinador", patroId);
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
                                if (i!=12)
                                {
                                    dataSourse.add(patro.getJSONObject(i));
                                }

                            }
                            AdaptadorPatrocinadores adapter=new AdaptadorPatrocinadores(context,0,dataSourse);
                            ((ListView)findViewById(R.id.listViewPatrocinadores)).setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();


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
