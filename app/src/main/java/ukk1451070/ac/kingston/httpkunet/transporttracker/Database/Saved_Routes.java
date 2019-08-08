package ukk1451070.ac.kingston.httpkunet.transporttracker.Database;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ukk1451070.ac.kingston.httpkunet.transporttracker.R;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Search;
//these sites helped me to create this
//https://www.youtube.com/watch?v=aQAIMY-HzL8&t
//https://github.com/mitchtabian/SaveReadWriteDeleteSQLite/tree/master/SaveAndDisplaySQL/app/src/main
public class Saved_Routes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Saved_Routes";

    OfflineDatabase MyDb;

    private ListView ListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView = (ListView) findViewById(R.id.listView);
        MyDb = new OfflineDatabase(this);

        populateListView();
    }
    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = MyDb.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1) + " to " + data.getString(2));
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        ListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String addresses = adapterView.getItemAtPosition(i).toString();
                String[] parts = addresses.split(" to ");
                String startAddress = parts[0];
                String endAddress = parts[1];

                Log.d(TAG, "onItemClick: You Clicked on " + startAddress);

                Cursor data = MyDb.getRouteID(startAddress); //get the id associated with that name
                int RouteID = -1;
                while(data.moveToNext()){
                    RouteID = data.getInt(0);
                }
                if(RouteID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + RouteID);
                    Intent Intent = new Intent(Saved_Routes.this, viewRoute.class);
                    Intent.putExtra("id",RouteID);
                    Intent.putExtra("startAddress",startAddress);
                    Intent.putExtra("endAddress",endAddress);
                    MyDb.setForeignKey(RouteID);
                    startActivity(Intent);
                }
                else{
                    toastMessage("No ID associated with that name");
                }
            }
        });
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
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
            Intent gd = new Intent(Saved_Routes.this, Search.class);
            startActivity(gd);
            break;
            case R.id.my_locations:
            Intent ml = new Intent(Saved_Routes.this, Saved_Routes.class);
            startActivity(ml);
            break;
            case R.id.login:
            Intent l = new Intent(Saved_Routes.this, LoginActivity.class);
            startActivity(l);
            break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
