package ukk1451070.ac.kingston.httpkunet.transporttracker.Old;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import ukk1451070.ac.kingston.httpkunet.transporttracker.Helpers.MyGooglePlaces;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Helpers.MyPlacesAdapter;
import ukk1451070.ac.kingston.httpkunet.transporttracker.R;

public class DestinationSearch extends AppCompatActivity {
    AutoCompleteTextView places;
    MyPlacesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destinationsearchform);

        places=(AutoCompleteTextView)findViewById(R.id.places);
        adapter=new MyPlacesAdapter(DestinationSearch.this);
        places.setAdapter(adapter);
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
        // handling click of autotextcompleteview items
        places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyGooglePlaces googlePlaces=(MyGooglePlaces)parent.getItemAtPosition(position);
                places.setText(googlePlaces.getName());
            }
        });
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
    public void sendMessage(View view)
    {
        String message = getIntent().getStringExtra("search");
        Intent intent = new Intent(DestinationSearch.this, OriginJSON.class);
        EditText editText = (EditText) findViewById(R.id.places);
        String message2 = editText.getText().toString();
        intent.putExtra("search",message);
        intent.putExtra("search2",message2);
        startActivity(intent);
    }
}
