package ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.FailSafes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ukk1451070.ac.kingston.httpkunet.transporttracker.Helpers.HttpHandler;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Other.OJPartDestination;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Other.OJPartRoute;
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;

public class FailSafeOD extends AppCompatActivity {

    private String TAG = FailSafeOD.class.getSimpleName();

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        new GetLocations().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetLocations extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(FailSafeOD.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras.getInt("partNo");
                extras.getString("fpn");
                extras.getString("tpn");
            }

            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.509865,-0.118092&rankby=distance&type=train_station&keyword="+extras.getString("tpn")+"&key=AIzaSyAlyiUzp5hRbYOrN4O0HLmR4J387Gv1A7c";
            url = url.replaceAll(" ", "%20");

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray places = jsonObj.getJSONArray("results");

                    JSONObject p = places.getJSONObject(0);

                    p.getString("name");
                    p.getString("vicinity");
                    JSONObject g = p.getJSONObject("geometry");
                    JSONObject l = g.getJSONObject("location");
                    l.getDouble("lat");
                    l.getDouble("lng");

                    ArrayList<LatLng> full = (ArrayList<LatLng>) extras.get("full");

                    SharedPreferences apiData = getSharedPreferences("api_data", MODE_PRIVATE);
                    SharedPreferences.Editor edit = apiData.edit();
                    edit.putString("tpnN", p.getString("name"));
                    edit.putLong("TLon", Double.doubleToRawLongBits(l.getDouble("lng")));
                    edit.putLong("TLat", Double.doubleToRawLongBits(l.getDouble("lat")));
                    edit.commit();

                    Intent intent = new Intent(FailSafeOD.this, OJPartRoute.class);
                    intent.putExtra("partNo", getIntent().getIntExtra("partNo", 0));
                    intent.putExtra("endNo", getIntent().getIntExtra("endNo", 0));
                    intent.putExtra("FLon", getIntent().getDoubleExtra("FLon", 0));
                    intent.putExtra("FLat", getIntent().getDoubleExtra("FLat", 0));
                    intent.putExtra("TLon", l.getDouble("lng"));
                    intent.putExtra("TLat", l.getDouble("lat"));
                    intent.putExtra("full", full);
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
