package com.example.akshay.songify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    DBAdapter myDb;
    EditText titlemain;
    EditText artistmain;
    EditText albummain;
    EditText editsearch;
    ListViewAdapter adapter;
    ListView list;
    ArrayList<Songs> arraylist = null;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titlemain = (EditText) findViewById(R.id.editTexttitle);
        artistmain = (EditText) findViewById(R.id.editTextartist);
        albummain = (EditText) findViewById(R.id.editTextalbum);
        editsearch = (EditText) findViewById(R.id.editTextsearch);
        list = (ListView) findViewById(R.id.listView);


        openDB();
        if(myDb.getCount() > 0) {
            createlist();
        }
        listviewitemLongclick();
        loadSavedPreferences();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void openDB(){
        myDb = new DBAdapter(this);
        myDb.open();
    }

    public void createlist(){
    //Log.d("createlist", "called");
        cursor = myDb.getAllRows();
        //int idd1 = cursor.getInt(0);
        //Log.i("ID::",""+idd1);
        //Log.i("ID::",""+idd2);
        arraylist = new ArrayList<Songs>();
            do {
                Songs s = new Songs(cursor.getInt(0), cursor.getString(1), cursor.getString(2), (cursor.getString(3)));
                arraylist.add(s);
                //Log.d("hi", "" + cursor.getString(1));
                //Log.d("hi",""+cursor.getInt(0));
                adapter = new ListViewAdapter(this, arraylist);
                list.setAdapter(adapter);

                editsearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //Log.i("MainActivity", "here in aftertextchanged");
                        String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                        adapter.filter(text);
                    }
                });

            } while (cursor.moveToNext());

    }



    public void onClick_SaveSong(View v){

        if(!TextUtils.isEmpty(titlemain.getText().toString()) && !TextUtils.isEmpty(artistmain.getText().toString()) && !TextUtils.isEmpty(albummain.getText().toString())){
            myDb.insertRow(titlemain.getText().toString(),artistmain.getText().toString(),albummain.getText().toString());
        }

        else Toast.makeText(getApplicationContext(), "Please Enter all the Information!",
                Toast.LENGTH_LONG).show();

        //Log.d("before", "going in");
        if(myDb.getCount() > 0) {
            createlist();
        }
        //Log.d("after", "got out");
        albummain.setText("");
        artistmain.setText("");
        titlemain.setText("");
        //Log.d("after", "createlist in save");

    }

    private void listviewitemLongclick(){

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("Do you want to Delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.i("ID::", "" + id);

                        TextView tv = (TextView) view.findViewById(R.id.textViewitemid);
                        TextView tv1 = (TextView) findViewById(R.id.textViewitemid);

                        String primary_key = tv.getText().toString();

                        // Log.i("PK::", "" + primary_key);
                        // Cursor cursor = myDb.getAllRows();
                        // int idd1 = cursor.getInt(0);

                        boolean res = myDb.deleteRow(primary_key);
                        // Log.i("RESULT OF DELETE", "" + res);
                        createlist();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }

        });
    }

    private void loadSavedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String fortitle = sharedPreferences.getString("edittitle", "");
        String forartist = sharedPreferences.getString("editartist", "");
        String foralbum = sharedPreferences.getString("editalbum", "");
        titlemain.setText(fortitle);
        artistmain.setText(forartist);
        albummain.setText(foralbum);
    }

    private void savePreferences(String key, String value) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    @Override
    public void onBackPressed() {

        savePreferences("edittitle",titlemain.getText().toString());
        savePreferences("editartist",artistmain.getText().toString());
        savePreferences("editalbum",albummain.getText().toString());
        super.onBackPressed();
    }


}






