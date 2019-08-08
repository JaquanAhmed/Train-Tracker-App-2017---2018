package ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Other;

import android.app.ProgressDialog;
import android.content.Intent;
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
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Live;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.MapsActivity;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Train.TJPartRoute;
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;

public class OJPartRoute extends AppCompatActivity {

    private String TAG = OJPartRoute.class.getSimpleName();

    private ProgressDialog pDialog;

    ArrayList<LatLng> train = new ArrayList<>();
    ArrayList<LatLng> foot = new ArrayList<>();
    ArrayList<LatLng> tube = new ArrayList<>();
    ArrayList<LatLng> bus = new ArrayList<>();
    ArrayList<LatLng> other = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        new GetRoutes().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetRoutes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(OJPartRoute.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras.getDouble("Flat");
                extras.getDouble("TLat");
                extras.getDouble("FLon");
                extras.getDouble("TLon");
            }

            String url = "https://transportapi.com/v3/uk/public/journey/from/lonlat:"+Double.toString(extras.getDouble("FLon"))+","+Double.toString(extras.getDouble("FLat"))+"/to/lonlat:"+Double.toString(extras.getDouble("TLon"))+","+Double.toString(extras.getDouble("TLat"))+".json?app_id=30df19a0&app_key=d860f66065dfff6c84237c6836ae2e66";
            url = url.replaceAll(" ", "%20");;

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            ArrayList<LatLng> full = (ArrayList<LatLng>) extras.get("full");

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray routes = jsonObj.getJSONArray("routes");

                    // looping through All Contacts
                    JSONObject r = routes.getJSONObject(0);

                    JSONArray route_parts = r.getJSONArray("route_parts");
                    for (int i = 0; i < route_parts.length(); i++) {

                        JSONObject rp = route_parts.getJSONObject(i);

                        JSONArray coordinates = rp.getJSONArray("coordinates");

                        for (int i2 = 0; i2 < coordinates.length(); i2++) {

                            JSONArray c = coordinates.getJSONArray(i2);
                            LatLng latLng = new LatLng(c.getDouble(1), c.getDouble(0));
                            if (rp.getString("mode").equals("foot")) {
                                foot.add(latLng);
                            }else if (rp.getString("mode").equals("bus")) {
                                bus.add(latLng);
                            }else if (rp.getString("mode").equals("tube")) {
                                tube.add(latLng);
                            }else {
                                other.add(latLng);
                            }
                        }
                    }
                    Intent intent = new Intent(OJPartRoute.this, MapsActivity.class);
                    intent.putExtra("partNo", getIntent().getIntExtra("partNo", 0));
                    intent.putExtra("endNo", getIntent().getIntExtra("endNo", 0));
                    intent.putExtra("full", full);
                    intent.putExtra("train", train);
                    intent.putExtra("tube", tube);
                    intent.putExtra("foot", foot);
                    intent.putExtra("bus", bus);
                    intent.putExtra("other", other);
                    startActivity(intent);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
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
