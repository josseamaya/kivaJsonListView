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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaPrestamistas extends AppCompatActivity {
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_prestamistas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Nuestros Prestamistas");

        mContext=this;
        String url="http://api.kivaws.org/v1/lenders/newest.json";
        getKivaPresta(url);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Lista Actualizada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getKivaPresta("http://api.kivaws.org/v1/lenders/newest.json");
            }
        });

    }
    private void getKivaPresta(String url) {
        final Context context=this;
        JsonObjectRequest jor=new JsonObjectRequest(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray presta=response.getJSONArray("lenders");

                            ArrayList<JSONObject> dataSourse=new ArrayList<JSONObject>();
                            for(int i=0;i<presta.length();i++)
                            {
                                dataSourse.add(presta.getJSONObject(i));

                            }
                            AdaptadorPrestamistas adapter=new AdaptadorPrestamistas(context,0,dataSourse);
                            ((ListView)findViewById(R.id.listViewPresta)).setAdapter(adapter);


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
