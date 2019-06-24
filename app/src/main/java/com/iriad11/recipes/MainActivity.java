package com.iriad11.recipes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.iriad11.recipes.db.DbHelper;
import com.iriad11.recipes.db.DisplayRecipe;
import com.iriad11.recipes.internet.Internet;
import com.iriad11.recipes.internet.Json;
import com.iriad11.recipes.internet.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    private DbHelper mydb;
    private boolean exist;
    private ArrayAdapter arrayAdapter;
    private ArrayList array_list;
    private Button ref;

    private int ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ref= findViewById(R.id.ref);
        obj = findViewById(R.id.listView1);

        mydb = new DbHelper(this);

        int a=mydb.numberOfRows();

        array_list = mydb.getAllRecipe();

        if(a>0){
            arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);
            //arrayAdapter=new ArrayAdapter(this,R.layout.listview, array_list);


        }
        else{
            if (Internet.checkConnection(getApplicationContext())) {
                mydb.upgrade();
                new GetDataTask().execute();
            } else {
                Toast.makeText(MainActivity.this,  "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this,  "Turn on internet connection and click refresh", Toast.LENGTH_SHORT).show();
                obj.setVisibility(View.INVISIBLE);
                ref.setVisibility(View.VISIBLE);
            }
        }
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Internet.checkConnection(getApplicationContext())) {
                    mydb.upgrade();
                    new GetDataTask().execute();
                } else {
                    Toast.makeText(MainActivity.this,  "Turn on internet connection and click refresh", Toast.LENGTH_SHORT).show();
                    obj.setVisibility(View.GONE);
                    ref.setVisibility(View.VISIBLE);
                }
            }
        });


        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                // TODO Auto-generated method stub
                int id_To_Search = arg2 + 1;

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(),DisplayRecipe.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.item:

                if (Internet.checkConnection(getApplicationContext())) {
                    mydb.upgrade();
                    new GetDataTask().execute();
                } else {
                    Toast.makeText(MainActivity.this, "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    class GetDataTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        int jIndex;
        int x;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Please Wait...");
            dialog.setMessage("Getting Recipes from internet");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            /**
             * Getting JSON Object from Web Using okHttp
             */
            JSONObject jsonObject = Json.getDataFromWeb();

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {
                    /**
                     * Check Length...
                     */
                    if(jsonObject.length() > 0) {
                        /**
                         * Getting Array named "contacts" From MAIN Json Object
                         */
                        JSONArray array = jsonObject.getJSONArray(Keys.KEY_RECIPES);

                        /**
                         * Check Length of Array...
                         */


                        int lenArray = array.length();
                        if(lenArray > 0) {
                            for( ; jIndex < lenArray; jIndex++) {

                                /**
                                 * Creating Every time New Object
                                 * and
                                 * Adding into List
                                 */

                                /**
                                 * Getting Inner Object from contacts array...
                                 * and
                                 * From that We will get Name of that Contact
                                 *
                                 */
                                JSONObject innerObject = array.getJSONObject(jIndex);
                                String name = innerObject.getString(Keys.KEY_NAME);
                                String ingrdnt = innerObject.getString(Keys.KEY_INGRDNT);
                                String process = innerObject.getString(Keys.KEY_PROCESS);
                                String link = innerObject.getString(Keys.KEY_LINK);

                                /**
                                 * Getting Object from Object gsheets
                                 */

                                mydb.insertrecipe(name,ingrdnt,process,link);

                            }
                        }
                    }
                } else {

                }
            } catch (JSONException je) {
                Log.i(Json.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

}