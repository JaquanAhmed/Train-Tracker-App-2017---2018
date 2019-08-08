package ukk1451070.ac.kingston.httpkunet.transporttracker.Journey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import ukk1451070.ac.kingston.httpkunet.transporttracker.Database.LoginActivity;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Database.Saved_Routes;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Helpers.MyGooglePlaces;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Helpers.MyPlacesAdapter;
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;
//http://coderzpassion.com/android-working-google-places-api/
public class Search extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    AutoCompleteTextView places;
    AutoCompleteTextView places2;
    AutoCompleteTextView lat;
    AutoCompleteTextView lon;
    AutoCompleteTextView lat2;
    AutoCompleteTextView lon2;
    MyPlacesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        places=(AutoCompleteTextView)findViewById(R.id.places);
        places2=(AutoCompleteTextView)findViewById(R.id.places2);
        lat=(AutoCompleteTextView)findViewById(R.id.lat);
        lon=(AutoCompleteTextView)findViewById(R.id.lon);
        lat2=(AutoCompleteTextView)findViewById(R.id.lat2);
        lon2=(AutoCompleteTextView)findViewById(R.id.lon2);
        adapter=new MyPlacesAdapter(Search.this);
        places.setAdapter(adapter);
        places2.setAdapter(adapter);
        // text changed listener to get results precisely according to our search
        places.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //calling getfilter to filter the results
                adapter.getFilter().filter(s);
                //notify the adapters after results changed
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        places2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //calling getfilter to filter the results
                adapter.getFilter().filter(s);
                //notify the adapters after results changed
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // handling click of autotextcompleteview items
        places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyGooglePlaces googlePlaces=(MyGooglePlaces)parent.getItemAtPosition(position);
                places.setText(googlePlaces.getName());
                String latitude = new Double(googlePlaces.getLatitude()).toString();
                String longitude = new Double(googlePlaces.getLongitude()).toString();
                lat.setText(latitude);
                lon.setText(longitude);
            }
        });
        places2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyGooglePlaces googlePlaces=(MyGooglePlaces)parent.getItemAtPosition(position);
                places2.setText(googlePlaces.getName());
                String latitude = new Double(googlePlaces.getLatitude()).toString();
                String longitude = new Double(googlePlaces.getLongitude()).toString();
                lat2.setText(latitude);
                lon2.setText(longitude);
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
        getMenuInflater().inflate(R.menu.menu_app, menu);
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
        switch (id){
            case R.id.get_directions:
                Intent gd = new Intent(Search.this, Search.class);
                startActivity(gd);
                break;
            case R.id.my_locations:
                Intent ml = new Intent(Search.this, Saved_Routes.class);
                startActivity(ml);
                break;
            case R.id.login:
                Intent l = new Intent(Search.this, LoginActivity.class);
                startActivity(l);
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Switch(View view)
    {
        EditText name = (EditText) findViewById(R.id.places);
        EditText lat = (EditText) findViewById(R.id.lat);
        EditText lon = (EditText) findViewById(R.id.lon);
        EditText name0 = (EditText) findViewById(R.id.places2);
        EditText lat2 = (EditText) findViewById(R.id.lat2);
        EditText lon2 = (EditText) findViewById(R.id.lon2);
        String ntext = name.getText().toString();
        String lattext = lat.getText().toString();
        String lontext = lon.getText().toString();
        String ntext2 = name0.getText().toString();
        String lattext2 = lat2.getText().toString();
        String lontext2 = lon2.getText().toString();
        places.setText(ntext2);
        lat.setText(lattext2);
        lon.setText(lontext2);
        places2.setText(ntext);
        lat2.setText(lattext);
        lon2.setText(lontext);
    }

    public void sendMessage(View view)
    {
        EditText name = (EditText) findViewById(R.id.places);
        EditText lon = (EditText) findViewById(R.id.lon);
        EditText lat = (EditText) findViewById(R.id.lat);
        EditText name2 = (EditText) findViewById(R.id.places2);
        EditText lat2 = (EditText) findViewById(R.id.lat2);
        EditText lon2 = (EditText) findViewById(R.id.lon2);
        String nameM = name.getText().toString();
        String nameM2 = name2.getText().toString();
        double longitudeM = Double.parseDouble(lon.getText().toString());
        double latitudeM = Double.parseDouble(lat.getText().toString());
        double longitudeM2 = Double.parseDouble(lon2.getText().toString());
        double latitudeM2 = Double.parseDouble(lat2.getText().toString());
        int part = 1;
        SharedPreferences apiData = getSharedPreferences("api_data", MODE_PRIVATE);
        SharedPreferences.Editor edit = apiData.edit();
        edit.putString("nameM", nameM);
        edit.putString("nameM2", nameM2);
        edit.putLong("longitudeM", Double.doubleToRawLongBits(longitudeM));
        edit.putLong("latitudeM", Double.doubleToRawLongBits(latitudeM));
        edit.putLong("longitudeM2", Double.doubleToRawLongBits(longitudeM2));
        edit.putLong("latitudeM2", Double.doubleToRawLongBits(latitudeM2));
        edit.commit();
        Intent intent = new Intent(Search.this, Route.class);
        intent.putExtra("latitudeM", latitudeM);
        intent.putExtra("longitudeM", longitudeM);
        intent.putExtra("latitudeM2", latitudeM2);
        intent.putExtra("longitudeM2", longitudeM2);
        intent.putExtra("partNo", part);
        startActivity(intent);
    }
}