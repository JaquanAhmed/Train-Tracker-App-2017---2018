package ukk1451070.ac.kingston.httpkunet.transporttracker.Journey;

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
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Other.OJPartOrigin;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Train.TJPartOrigin;
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;

public class Route extends AppCompatActivity {

    private String TAG = Route.class.getSimpleName();

    private ProgressDialog pDialog;

    ArrayList<LatLng> full = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        new Route.GetRoutes().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetRoutes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Route.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras.getDouble("longitudeM");
                extras.getDouble("latitudeM");
                extras.getDouble("longitudeM2");
                extras.getDouble("latitudeM2");
            }

            String url = "https://transportapi.com/v3/uk/public/journey/from/lonlat:"+Double.toString(extras.getDouble("longitudeM"))+","+Double.toString(extras.getDouble("latitudeM"))+"/to/lonlat:"+Double.toString(extras.getDouble("longitudeM2"))+","+Double.toString(extras.getDouble("latitudeM2"))+".json?app_id=30df19a0&app_key=d860f66065dfff6c84237c6836ae2e66&modes=train";
            url = url.replaceAll(" ", "%20");;

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    JSONArray routes = jsonObj.getJSONArray("routes");

                        JSONObject r = routes.getJSONObject(0);

                        JSONArray route_parts = r.getJSONArray("route_parts");

                        JSONObject prp = route_parts.getJSONObject(getIntent().getExtras().getInt("partNo"));

                        for (int i = 0; i < route_parts.length(); i++) {

                            JSONObject rp = route_parts.getJSONObject(i);

                            JSONArray coordinates = rp.getJSONArray("coordinates");

                            for (int i2 = 0; i2 < coordinates.length(); i2++) {

                                JSONArray c = coordinates.getJSONArray(i2);
                                LatLng latLng = new LatLng(c.getDouble(1), c.getDouble(0));
                                full.add(latLng);

                            }

                        }
                        String fpn = prp.getString("from_point_name");
                        String tpn = prp.getString("to_point_name");
                        if (fpn.contains(",")){
                            fpn = fpn.substring(0, fpn.indexOf(","));
                        }
                        if (tpn.contains(",")){
                            tpn = tpn.substring(0, tpn.indexOf(","));
                        }
                            int End = route_parts.length() - 1;

                            if(prp.getString("mode").equals("train")) {
                                Intent intent = new Intent(Route.this, TJPartOrigin.class);
                                intent.putExtra("fpn", fpn);
                                intent.putExtra("tpn", tpn);
                                intent.putExtra("full", full);
                                intent.putExtra("partNo", getIntent().getIntExtra("partNo", 0));
                                intent.putExtra("endNo", End);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(Route.this, OJPartOrigin.class);
                                intent.putExtra("fpn", prp.getString("from_point_name"));
                                intent.putExtra("tpn", prp.getString("to_point_name"));
                                intent.putExtra("full", full);
                                intent.putExtra("partNo", getIntent().getIntExtra("partNo", 0));
                                intent.putExtra("endNo", End);
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
