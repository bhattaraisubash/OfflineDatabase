package np.com.bsubash.offlinedatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * OfflineDatabase
 * Created on 6/5/2017.
 *
 * @author Subash Bhattarai
 */

public class DBHelper extends SQLiteOpenHelper {
    private String DB_PATH = null;
    private static String DB_NAME = "test_db";
    private SQLiteDatabase database;
    private final Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        Log.i("subash-log", "DB path:" + DB_PATH);
    }

    public void createDatabase() throws IOException {
        boolean dbExists = checkDatabase();
        if (dbExists) {

        } else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                Log.e("subash-log", "On copying database:" + e.toString());
            }
        }
    }

    private void copyDatabase() throws IOException {
        InputStream inputStream = context.getResources().openRawResource(R.raw.test_db);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream outputStream = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1080];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();

    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            Log.e("subash-log", "Database does not exist yet.");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    public void openDatabase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public List<String> getUser() {
        if (database == null) {
            openDatabase();
        }
        Cursor cursor = database.rawQuery("SELECT * FROM user;", null);
        if (cursor.getCount() > 0) {
            List<String> strList = new LinkedList<>();
            while (cursor.moveToNext()) {
                String string = "Name:" + cursor.getString(cursor.getColumnIndex("name")) + " Age:" +
                        cursor.getString(cursor.getColumnIndex("age"));
                strList.add(string);
            }
            cursor.close();
            database.close();
            return strList;
        }
        return null;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
