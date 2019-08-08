package ukk1451070.ac.kingston.httpkunet.transporttracker.Old;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ukk1451070.ac.kingston.httpkunet.transporttracker.Helpers.HttpHandler;
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;


public class OriginJSON extends AppCompatActivity {

    private String TAG = OriginJSON.class.getSimpleName();

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
            pDialog = new ProgressDialog(OriginJSON.this);
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

            String url = "https://transportapi.com/v3/uk/places.json?app_id=30df19a0&app_key=d860f66065dfff6c84237c6836ae2e66&query="+extras.getString("search");
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

                        String message = getIntent().getStringExtra("search");
                        String message2 = getIntent().getStringExtra("search2");

                        SharedPreferences apiData = getSharedPreferences("api_data", MODE_PRIVATE);
                        SharedPreferences.Editor edit = apiData.edit();
                        edit.putString("nameM", c.getString("name"));
                        edit.putLong("longitudeM", Double.doubleToRawLongBits(c.getDouble("longitude")));
                        edit.putLong("latitudeM", Double.doubleToRawLongBits(c.getDouble("latitude")));
                        edit.commit();

                        Intent intent = new Intent(OriginJSON.this, DestinationJSON.class);
                        intent.putExtra("longitudeM", c.getDouble("longitude"));
                        intent.putExtra("latitudeM", c.getDouble("latitude"));
                        intent.putExtra("search", message);
                        intent.putExtra("search2", message2);
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
