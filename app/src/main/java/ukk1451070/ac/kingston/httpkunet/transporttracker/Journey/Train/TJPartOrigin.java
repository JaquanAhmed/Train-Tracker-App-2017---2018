package ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Train;

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
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.FailSafes.FailSafeOO;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.FailSafes.FailSafeTD;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.FailSafes.FailSafeTO;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Other.OJPartOrigin;
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;

public class TJPartOrigin extends AppCompatActivity {

    private String TAG = TJPartOrigin.class.getSimpleName();

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        new GetStations().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetStations extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(TJPartOrigin.this);
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

            String url = "http://transportapi.com/v3/uk/places.json?query="+extras.getString("fpn")+"&type=train_station&app_id=30df19a0&app_key=d860f66065dfff6c84237c6836ae2e66";
            url = url.replaceAll(" ", "%20");;

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray stations = jsonObj.getJSONArray("member");

                    for (int i = 0; i < stations.length(); i++) {
                        JSONObject c = stations.getJSONObject(i);

                        ArrayList<LatLng> full = (ArrayList<LatLng>) extras.get("full");

                        if (c.getDouble("latitude")<50||c.getDouble("latitude")>52){
                            Intent intent = new Intent(TJPartOrigin.this, FailSafeTO.class);
                            intent.putExtra("fpn", getIntent().getStringExtra("fpn"));
                            intent.putExtra("tpn", getIntent().getStringExtra("tpn"));
                            intent.putExtra("full", full);
                            intent.putExtra("partNo", getIntent().getIntExtra("partNo", 0));
                            intent.putExtra("endNo", getIntent().getIntExtra("endNo", 0));
                            startActivity(intent);
                        }

                        SharedPreferences apiData = getSharedPreferences("api_data", MODE_PRIVATE);
                        SharedPreferences.Editor edit = apiData.edit();
                        edit.putString("fpnN", c.getString("name"));
                        edit.putLong("FLon", Double.doubleToRawLongBits(c.getDouble("longitude")));
                        edit.putLong("FLat", Double.doubleToRawLongBits(c.getDouble("latitude")));
                        edit.commit();

                        Intent intent = new Intent(TJPartOrigin.this, TJPartDestination.class);
                        intent.putExtra("partNo", getIntent().getIntExtra("partNo", 0));
                        intent.putExtra("endNo", getIntent().getIntExtra("endNo", 0));
                        intent.putExtra("fpn", getIntent().getStringExtra("fpn"));
                        intent.putExtra("tpn", getIntent().getStringExtra("tpn"));
                        intent.putExtra("FLon", c.getDouble("longitude"));
                        intent.putExtra("FLat", c.getDouble("latitude"));
                        intent.putExtra("SC", c.getString("station_code"));
                        intent.putExtra("full", full);
                        startActivity(intent);
                    }
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
