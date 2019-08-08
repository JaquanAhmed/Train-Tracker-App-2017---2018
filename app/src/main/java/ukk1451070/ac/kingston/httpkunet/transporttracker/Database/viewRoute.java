package ukk1451070.ac.kingston.httpkunet.transporttracker.Database;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ukk1451070.ac.kingston.httpkunet.transporttracker.R;
//these sites helped me to create this
//https://www.youtube.com/watch?v=aQAIMY-HzL8&t
//https://github.com/mitchtabian/SaveReadWriteDeleteSQLite/tree/master/SaveAndDisplaySQL/app/src/main
public class viewRoute extends AppCompatActivity {

    private static final String TAG = "viewRoutes";

    private Button btnDelete;

    OfflineDatabase MyDb;

    private ListView ListView;

    private TextView start;
    private TextView end;

    private String selectedstartAddress;
    private String endAddress;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_route);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        start = (TextView) findViewById(R.id.startAddress);
        end = (TextView) findViewById(R.id.endAddress);
        selectedID = getIntent().getIntExtra("id",-1); //NOTE: -1 is just the default value
        selectedstartAddress = getIntent().getStringExtra("startAddress");
        endAddress = getIntent().getStringExtra("endAddress");
        start.setText(selectedstartAddress);
        end.setText(endAddress);
        ListView = (ListView) findViewById(R.id.listView2);
        MyDb = new OfflineDatabase(this);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDb.deleteRoute(selectedID,selectedstartAddress);
                start.setText("");
            }
        });
        Results();
    }

    private void Results() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");


        Cursor data = MyDb.getData2(selectedID);
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){

            listData.add(data.getString(1));
            listData.add("Duration: " + data.getString(2));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        ListView.setAdapter(adapter);
    }


}
