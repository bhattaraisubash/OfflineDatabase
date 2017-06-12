package np.com.bsubash.offlinedatabase;

import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);

        DBHelper dbHelper = new DBHelper(this);
        try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            Log.e("subash-log", "MainActivity:createDatabase:" + e.toString());
        }
        try {
            List<String> strings = dbHelper.getUser();
            listView.setAdapter(new ListAdapter(this, strings));
        } catch (SQLException e) {
            Log.i("subash-log", "MainActivity::openDatabase:" + e.toString());
        }

    }

    private class ListAdapter extends ArrayAdapter<String> {
        private Context context;

        public ListAdapter(@NonNull Context context, List<String> strings) {
            super(context, 0, strings);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String string = getItem(position);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_view, null);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(string);
            return view;
        }
    }
}
