package com.example.joseamaya.kivajsonlistview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Context mContext;
    JSONObject kivaJason=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        setTitle("Loans Kiva");


        mContext=this;
        String url="http://api.kivaws.org/v1/loans/newest.json";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Lista Actualizada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getKivaLoans("http://api.kivaws.org/v1/loans/newest.json");


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getKivaLoans(url);

        ListView lv2=(ListView) findViewById(R.id.listViewLoans);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent (mContext,detalle.class);
                intent.putExtra("kiva", kivaJason.toString());
                intent.putExtra("numero",position);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent intent=new Intent (mContext,ListaPatrocinadores.class);
            startActivity(intent);
            /*int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "presiono galaria", duration);
            toast.show();*/

        } else if (id == R.id.nav_slideshow) {
            Intent intent=new Intent (mContext,ListaPrestamistas.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent=new Intent (mContext,graficos.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "josseamaya@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Nueva App");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Me gusta esta aplicacion deberias de probarla para ayudar a mas personas");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getKivaLoans(String url) {
        final Context context=this;
        JsonObjectRequest jor=new JsonObjectRequest(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            kivaJason=response;
                            JSONArray loans=response.getJSONArray("loans");

                            ArrayList<JSONObject> dataSourse=new ArrayList<JSONObject>();
                            for(int i=0;i<loans.length();i++)
                            {
                                dataSourse.add(loans.getJSONObject(i));

                            }
                            CeldaComplejaAdapter adapter=new CeldaComplejaAdapter(context,0,dataSourse);
                            ((ListView)findViewById(R.id.listViewLoans)).setAdapter(adapter);
                            adapter.notifyDataSetChanged();


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
