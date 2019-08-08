package ukk1451070.ac.kingston.httpkunet.transporttracker.Database;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ukk1451070.ac.kingston.httpkunet.transporttracker.Helpers.HttpHandler;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.FailSafes.FailSafeOD;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.MapsActivity;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Other.OJPartDestination;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Other.OJPartRoute;
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;

public class SaveRoute extends AppCompatActivity {

    private String TAG = SaveRoute.class.getSimpleName();

    private ProgressDialog pDialog;

    OfflineDatabase myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        myDb = new OfflineDatabase(this);
        new GetRoute().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetRoute extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SaveRoute.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            SharedPreferences apiData = getSharedPreferences("api_data", MODE_PRIVATE);
            Double longitudeM = Double.longBitsToDouble(apiData.getLong("longitudeM",0));
            Double longitudeM2 = Double.longBitsToDouble(apiData.getLong("longitudeM2",0));
            Double latitudeM = Double.longBitsToDouble(apiData.getLong("latitudeM",0));
            Double latitudeM2 = Double.longBitsToDouble(apiData.getLong("latitudeM2",0));
            String nameM = apiData.getString("nameM","name");
            String nameM2 = apiData.getString("nameM2","name");

            boolean insertData = myDb.addTable1Data(nameM, nameM2);

            if (insertData) {
                Log.d(TAG, "origin and destination added");
            } else {
                Log.d(TAG, "Something went wrong");
            }


            String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+Double.toString(latitudeM)+","+Double.toString(longitudeM)+"&destination="+Double.toString(latitudeM2)+","+Double.toString(longitudeM2)+"&mode=transit&key=AIzaSyBnNEJtu0w1k8l7jm3gtr-NB5pjS1mDssU";
            url = url.replaceAll(" ", "%20");;

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray routes = jsonObj.getJSONArray("routes");

                    JSONObject r = routes.getJSONObject(0);

                    JSONArray legs = r.getJSONArray("legs");

                    for (int i=0; i<legs.length(); i++){

                        JSONObject l = legs.getJSONObject(i);

                        JSONArray steps = l.getJSONArray("steps");

                        for (int i2=0; i2<steps.length(); i2++) {
                            JSONObject s = steps.getJSONObject(i2);
                            JSONObject d = s.getJSONObject("duration");
                            String duration = d.getString("text");
                            String instructions = Html.fromHtml(s.getString("html_instructions")).toString();

                            insertData = myDb.addTable2Data(instructions, duration);

                            if (insertData) {
                                Log.d(TAG, "instructions and duration added");
                            } else {
                                Log.d(TAG, "Something went wrong");
                            }
                        }
                    }




                    Bundle extras = getIntent().getExtras();
                    ArrayList<LatLng> full = (ArrayList<LatLng>) extras.get("full");
                    ArrayList<LatLng> train = (ArrayList<LatLng>) extras.get("train");
                    ArrayList<LatLng> foot = (ArrayList<LatLng>) extras.get("foot");
                    ArrayList<LatLng> tube = (ArrayList<LatLng>) extras.get("tube");
                    ArrayList<LatLng> bus = (ArrayList<LatLng>) extras.get("bus");
                    ArrayList<LatLng> other = (ArrayList<LatLng>) extras.get("other");
                    Intent intent = new Intent(SaveRoute.this, MapsActivity.class);
                    intent.putExtra("partNo", getIntent().getIntExtra("partNo", 0));
                    intent.putExtra("endNo", getIntent().getIntExtra("endNo", 0));
                    intent.putExtra("SC", getIntent().getStringExtra("SC"));
                    intent.putExtra("SC2", getIntent().getStringExtra("SC2"));
                    intent.putExtra("service", getIntent().getExtras().getInt("service"));
                    intent.putExtra("full", full);
                    intent.putExtra("train", train);
                    intent.putExtra("tube", tube);
                    intent.putExtra("foot", foot);
                    intent.putExtra("bus", bus);
                    intent.putExtra("other", other);
                    intent.putExtra("SName", getIntent().getStringExtra("SName"));
                    intent.putExtra("SLon", getIntent().getDoubleExtra("SLon", 0));
                    intent.putExtra("SLat", getIntent().getDoubleExtra("SLat", 0));
                    startActivity(intent);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

    }
}
