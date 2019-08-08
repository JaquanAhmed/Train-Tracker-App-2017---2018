package ukk1451070.ac.kingston.httpkunet.transporttracker.Old;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ukk1451070.ac.kingston.httpkunet.transporttracker.Helpers.HttpHandler;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Route;
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;

public class DestinationJSON extends AppCompatActivity {

    private String TAG = DestinationJSON.class.getSimpleName();

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        new DestinationJSON.GetStations().execute();
    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetStations extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(DestinationJSON.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras.getString("search");
                extras.getString("search2");
            }

            String url = "https://transportapi.com/v3/uk/places.json?app_id=30df19a0&app_key=d860f66065dfff6c84237c6836ae2e66&query="+extras.getString("search2");
            url = url.replaceAll(" ", "%20");;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray stations = jsonObj.getJSONArray("member");

                    // looping through All Contacts
                    for (int i = 0; i < stations.length(); i++) {
                        JSONObject c = stations.getJSONObject(i);

                        getIntent().getStringExtra("nameM");
                        getIntent().getStringExtra("SC");
                        getIntent().getDoubleExtra("longitudeM",0);
                        getIntent().getDoubleExtra("latitudeM",0);


                        int part = 0;

                        SharedPreferences apiData = getSharedPreferences("api_data", MODE_PRIVATE);
                        SharedPreferences.Editor edit = apiData.edit();
                        edit.putString("nameM2", c.getString("name"));
                        edit.putLong("longitudeM2", Double.doubleToRawLongBits(c.getDouble("longitude")));
                        edit.putLong("latitudeM2", Double.doubleToRawLongBits(c.getDouble("latitude")));
                        edit.commit();

                        Intent intent = new Intent(DestinationJSON.this, Route.class);
                        intent.putExtra("longitudeM2", c.getDouble("longitude"));
                        intent.putExtra("latitudeM2", c.getDouble("latitude"));
                        intent.putExtra("longitudeM", getIntent().getDoubleExtra("longitudeM",0));
                        intent.putExtra("latitudeM", getIntent().getDoubleExtra("latitudeM",0));
                        intent.putExtra("partNo", part);
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
