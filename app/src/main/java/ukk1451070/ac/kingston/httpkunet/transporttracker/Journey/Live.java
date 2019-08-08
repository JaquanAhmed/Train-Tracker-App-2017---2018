package ukk1451070.ac.kingston.httpkunet.transporttracker.Journey;

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
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;

public class Live extends AppCompatActivity {
    private String TAG = Live.class.getSimpleName();

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        new GetNear().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetNear extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Live.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras.getString("SC");
                extras.getString("SC2");
            }

            String url = "https://transportapi.com/v3/uk/train/station/"+extras.getString("SC")+"/live.json?app_id=30df19a0&app_key=d860f66065dfff6c84237c6836ae2e66&calling_at="+extras.getString("SC2")+"&darwin=false&train_status=passenger";
            url = url.replaceAll(" ", "%20");

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONObject departures = jsonObj.getJSONObject("departures");

                    JSONArray all = departures.getJSONArray("all");
                        JSONObject a = all.getJSONObject(0);

                    ArrayList<LatLng> full = (ArrayList<LatLng>) extras.get("full");
                    ArrayList<LatLng> train = (ArrayList<LatLng>) extras.get("train");
                    ArrayList<LatLng> foot = (ArrayList<LatLng>) extras.get("foot");
                    ArrayList<LatLng> tube = (ArrayList<LatLng>) extras.get("tube");
                    ArrayList<LatLng> bus = (ArrayList<LatLng>) extras.get("bus");
                    ArrayList<LatLng> other = (ArrayList<LatLng>) extras.get("other");

                        Intent intent = new Intent(Live.this, Position.class);
                        intent.putExtra("partNo", getIntent().getIntExtra("partNo", 0));
                        intent.putExtra("endNo", getIntent().getIntExtra("endNo", 0));
                        intent.putExtra("SC", getIntent().getStringExtra("SC"));
                        intent.putExtra("SC2", getIntent().getStringExtra("SC2"));
                        intent.putExtra("full", full);
                        intent.putExtra("train", train);
                        intent.putExtra("tube", tube);
                        intent.putExtra("foot", foot);
                        intent.putExtra("bus", bus);
                        intent.putExtra("other", other);
                        intent.putExtra("service", a.getInt("service"));
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
