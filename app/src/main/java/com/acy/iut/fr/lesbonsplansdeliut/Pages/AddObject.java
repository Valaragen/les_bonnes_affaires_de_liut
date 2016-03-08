package com.acy.iut.fr.lesbonsplansdeliut.Pages;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.acy.iut.fr.lesbonsplansdeliut.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AddObject extends Activity {
    private static final String FLAG_SUCCESS = "success";
    private static final String FLAG_MESSAGE = "message";
    private static final String URL = "http://rudyboinnard.esy.es/android/";
    private ArrayList<String> listCategories = new ArrayList<String>();
    private Spinner spinnerCategories;
    private EditText titreObjet;
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView photo1,photo2,photo3,imageX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);
        new LoadPage().execute();

        titreObjet = (EditText) findViewById(R.id.titreObjet);
        spinnerCategories = (Spinner) findViewById(R.id.spinnerCategories);
        photo1 = (ImageView) findViewById(R.id.photo1);
        photo2 = (ImageView) findViewById(R.id.photo2);
        photo3 = (ImageView) findViewById(R.id.photo3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_object, menu);
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

    public void ClickAddPhotoBtn(View v){
        if(v.getId() == R.id.btnAddPhoto){
            imageX = (ImageView)findViewById(R.id.photo1);
        }else if (v.getId() == R.id.btnAddPhoto2){
            imageX = (ImageView)findViewById(R.id.photo2);
        }else if (v.getId() == R.id.btnAddPhoto3){
            imageX = (ImageView)findViewById(R.id.photo3);
        }
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageX.setImageBitmap(BitmapFactory.decodeFile(picturePath));


            // String picturePath contains the path of selected Image
        }
    }


    class LoadPage extends AsyncTask<String, String, JSONObject> {

        //display loading and status
        protected void onPreExecute() {
        }

        //Get JSON data from the URL
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject json = null;
            try {
                Log.d("request", "starting GetCategories");
                URL url = null;
                HttpURLConnection connection = null;
                try {
                    //initialize connection
                    url = new URL(URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    String urlParameters = "method=LoadPageAddObject";
                    byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                    //write post data to URL
                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.write(postData);
                    //connect and get data
                    connection.connect();
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    json = new JSONObject(convertStreamToString(in));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //parse returned data
        protected void onPostExecute(JSONObject result) {
            int success = 0;

            try {


                JSONArray jArrayDept = (result.getJSONArray("nom_categorie"));
                JSONArray jArrayid = (result.getJSONArray("id_categorie"));
                if (jArrayDept != null) {
                    for (int i=0;i<jArrayDept.length();i++){
                        listCategories.add(jArrayDept.get(i).toString());
                        Log.d("DEBUG",listCategories.get(i));

                    }
                }
                fillSpinner(spinnerCategories,listCategories);
                //alert the user of the status of the connection
                success = result.getInt(FLAG_SUCCESS);
                //testText.setText(result.getString(FLAG_MESSAGE)+"");
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
                //e.printStackTrace();
            }
            //log the success status
            if (success == 1) {
                Log.d("OK", "OK");
            } else {
                Log.d("Error", "Error");
            }
        }
    }

    public String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void fillSpinner(Spinner spinner, List<String> list) {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        Log.d("Spinner", "Spinner adaptee");
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d("Spinner", "Spinner set");
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        Log.d("Spinner", "Spinner fini");
    }
}
